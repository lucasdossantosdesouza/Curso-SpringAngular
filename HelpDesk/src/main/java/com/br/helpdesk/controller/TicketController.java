package com.br.helpdesk.controller;

import com.br.helpdesk.api.response.Response;
import com.br.helpdesk.api.dto.Summary;
import com.br.helpdesk.entity.*;
import com.br.helpdesk.security.jwt.JwtTokenUtil;
import com.br.helpdesk.service.TicketService;
import com.br.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket,
                                                   BindingResult result){
        Response<Ticket> ticketResponse = new Response<>();
        try {
            validateCreateTicket(ticket, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        ticketResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(ticketResponse);
            }
            ticket.setStatus(StatusEnum.getStatus("New"));
            ticket.setData(new Date());
            ticket.setNumber(ticketService.generatedNumber());
            ticket.setUsuario(userFromRequest(request));
            Ticket ticketPersist = ticketService.createOrUpdate(ticket);
            ticketResponse.setData(ticketPersist);
        }catch (DuplicateKeyException de){
            ticketResponse.getErrors().add("Ticket already registred");
            return ResponseEntity.badRequest().body(ticketResponse);
        }catch (Exception e){
            ticketResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(ticketResponse);
        }

        return ResponseEntity.ok(ticketResponse);
    }

    private Usuario userFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUserNameFromToken(token);
        return usuarioService.findByEmail(email);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket,
                                                    BindingResult result){
        Response<Ticket> ticketResponse = new Response<>();
        try {
            validateUpdateTicket(ticket, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        ticketResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(ticketResponse);
            }
            Optional<Ticket> ticketFind = ticketService.findById(ticket.getId());
            if (ticketFind != null){
                ticket.setStatus(ticketFind.get().getStatus());
                ticket.setData(ticketFind.get().getData());
                ticket.setNumber(ticketFind.get().getNumber());
                ticket.setUsuario(ticketFind.get().getUsuario());
                if(ticketFind.get().getAssigneredUser() != null){
                    ticket.setAssigneredUser(ticketFind.get().getAssigneredUser());
                }
                Ticket ticketPersist = ticketService.createOrUpdate(ticket);
                ticketResponse.setData(ticketPersist);
            }
        }catch (Exception e){
            ticketResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(ticketResponse);
        }

        return ResponseEntity.ok(ticketResponse);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
        Response<Ticket> ticketResponse = new Response<>();
        Optional<Ticket> ticketFind = ticketService.findById(id);
        if(ticketFind == null || ticketFind.get() == null){
            ticketResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(ticketResponse);
        }
        Iterable<ChangeStatus> changeStatuses = ticketService.listChangeStatus(ticketFind.get().getId());
        changeStatuses.forEach(changeStatus -> {
            changeStatus.setTicket(null);
            ticketFind.get().getChangeStatus().add(changeStatus);
        });

        ticketResponse.setData(ticketFind.get());
        return ResponseEntity.ok(ticketResponse);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
        Response<String> ticketResponse = new Response<>();
        Optional<Ticket> ticketFind = ticketService.findById(id);
        if(ticketFind == null || ticketFind.get() == null){
            ticketResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(ticketResponse);
        }
        ticketService.delete(ticketFind.get().getId());
        return ResponseEntity.ok(new Response<String>());
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Iterable<Ticket>>> findAll() {
        Response<Iterable<Ticket>> ticketsResponse = new Response<>();
        Iterable<Ticket> tickets = ticketService.findAll();
        ticketsResponse.setData(tickets);
        return ResponseEntity.ok(ticketsResponse);
    }

    @GetMapping(value = "/{page}/{count}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Page<Ticket>>> listTicket(HttpServletRequest request,@PathVariable("page") int page,
                                                             @PathVariable("count") int count) {
        Response<Page<Ticket>> ticketResponse = new Response<>();
        Usuario usuario = userFromRequest(request);
        Page<Ticket> tickets = null;
        if(ProfileEnum.ROLE_TECHNICIAN == usuario.getProfile()){
            tickets = ticketService.listTicket(page, count);
        }else if(ProfileEnum.ROLE_CUSTOMER == usuario.getProfile()){
            tickets = ticketService.findByCurrentUser(page, count,usuario.getId());
        }
        ticketResponse.setData(tickets);
        return ResponseEntity.ok(ticketResponse);
    }

    @GetMapping(value = "/{page}/{count}/{titulo}/{status}/{priority}/{number}/{assigned}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Page<Ticket>>> findByParams(HttpServletRequest request,
                                                               @PathVariable("page") int page,
                                                               @PathVariable("count") int count,
                                                               @PathVariable("titulo") String titulo,
                                                               @PathVariable("status") String status,
                                                               @PathVariable("priority") String priority,
                                                               @PathVariable("number") Integer number,
                                                               @PathVariable("assigned") boolean assigned) {
        Response<Page<Ticket>> ticketResponse = new Response<>();
        Usuario usuario = userFromRequest(request);

        Page<Ticket> tickets = null;
        titulo = titulo.equals("uninformed") ? "" :titulo;
        priority = priority.equals("uninformed") ? "" :priority;
        status = status.equals("uninformed") ? "" :status;

        if(number > 0){
            tickets = ticketService.findByNumber(page, count, number);
        }else {
             if (ProfileEnum.ROLE_TECHNICIAN.equals(usuario.getProfile())) {
                 if(assigned) {
                     tickets = ticketService.findByParametersAndAssignedUser(page, count, titulo, status, priority, usuario.getId());
                 }else{
                     tickets = ticketService.findByParameters(page, count, titulo, status, priority);
                 }
            } else if (ProfileEnum.ROLE_CUSTOMER.equals(usuario.getProfile())) {
                tickets = ticketService.findByParametersAndCurrentUser(page, count, titulo, status, priority, usuario.getId());
            }
        }
        ticketResponse.setData(tickets);
        return ResponseEntity.ok(ticketResponse);
    }

    @PutMapping("/{id}/{status}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Ticket>> changeStatus(HttpServletRequest request,
                                                         @PathVariable("status") String status,
                                                         @PathVariable("id") String id,
                                                         @RequestBody Ticket ticket,
                                                         BindingResult result){
        Response<Ticket> ticketResponse = new Response<>();
        try {
            validateChangeStatus(id , status, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        ticketResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(ticketResponse);
            }
            Optional<Ticket> ticketFind = ticketService.findById(ticket.getId());

            if(ticketFind != null) {
                ticketFind.get().setStatus(StatusEnum.getStatus(status));
                if(status.equals("Assigned")){
                    ticketFind.get().setAssigneredUser(userFromRequest(request));
                }
                Ticket ticketPersist = ticketService.createOrUpdate(ticket);
                ChangeStatus changeStatus = new ChangeStatus();
                changeStatus.setUsuario(userFromRequest(request));
                changeStatus.setData(new Date());
                changeStatus.setStatusEnum(StatusEnum.getStatus(status));
                changeStatus.setTicket(ticketPersist);
                ticketService.createCahngesStatus(changeStatus);
                ticketResponse.setData(ticketPersist);
            }
        }catch (Exception e){
            ticketResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(ticketResponse);
        }

        return ResponseEntity.ok(ticketResponse);
    }
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Summary>> findSummary(){
        AtomicReference<Integer> amountNew = new AtomicReference<>(0);
        AtomicReference<Integer> amountAssigned = new AtomicReference<>(0);
        AtomicReference<Integer> amountResolved = new AtomicReference<>(0);
        AtomicReference<Integer> amountAproved = new AtomicReference<>(0);
        AtomicReference<Integer> amountDisaproved = new AtomicReference<>(0);
        AtomicReference<Integer> amountClosed = new AtomicReference<>(0);

        Response<Summary> summaryResponse = new Response<>();
        Summary summary = new Summary();

        Iterable<Ticket> tickets = ticketService.findAll();
        tickets.forEach(ticket -> {
            if(StatusEnum.New.equals(ticket.getStatus())){
                amountNew.getAndSet(amountNew.get() + 1);
            }
            if(StatusEnum.Resolved.equals(ticket.getStatus())){
                amountResolved.getAndSet(amountResolved.get() + 1);
            }
            if(StatusEnum.Aproved.equals(ticket.getStatus())){
                amountAproved.getAndSet(amountAproved.get() + 1);
            }
            if(StatusEnum.Disaproved.equals(ticket.getStatus())){
                amountDisaproved.getAndSet(amountDisaproved.get() + 1);
            }
            if(StatusEnum.Assigned.equals(ticket.getStatus())){
                amountAssigned.getAndSet(amountAssigned.get() + 1);
            }
            if(StatusEnum.Closed.equals(ticket.getStatus())){
                amountClosed.getAndSet(amountClosed.get() + 1);
            }
        });
        summary.setAmountNew(amountNew.get());
        summary.setAmountResolved(amountResolved.get());
        summary.setAmountAproved(amountAproved.get());
        summary.setAmountDisaproved(amountDisaproved.get());
        summary.setAmountAssigned(amountAssigned.get());
        summary.setAmountClosed(amountClosed.get());
        summaryResponse.setData(summary);
        return ResponseEntity.ok(summaryResponse);
    }

    private void validateChangeStatus(String id, String status, BindingResult result) {
        if(id == null || id.equals("")){
            result.addError(new ObjectError("User", "Id no Information"));
            return;
        }
        if(status == null || status.equals("")){
            result.addError(new ObjectError("User", "status no Information"));
        }
    }

    private void validateCreateTicket(Ticket ticket, BindingResult result){
        if(ticket.getTitulo() == null){
            result.addError(new ObjectError("User", "titulo no Information"));
        }
    }

    private void validateUpdateTicket(Ticket usuario, BindingResult result){
        if(usuario.getId() == null){
            result.addError(new ObjectError("User", "Id no Information"));
        }
        if(usuario.getTitulo() == null){
            result.addError(new ObjectError("User", "Titulo no Information"));
        }
    }

}
