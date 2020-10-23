package com.br.helpdesk.controller;

import com.br.helpdesk.api.response.Response;
import com.br.helpdesk.entity.StatusEnum;
import com.br.helpdesk.entity.Ticket;
import com.br.helpdesk.entity.Usuario;
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
    public ResponseEntity<Response<Ticket>> findById(@PathVariable String id) {
        Response<Ticket> ticketResponse = new Response<>();
        Optional<Ticket> ticketFind = ticketService.findById(id);
        if(ticketFind == null || ticketFind.get() == null){
            ticketResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(ticketResponse);
        }
        ticketResponse.setData(ticketFind.get());
        return ResponseEntity.ok(ticketResponse);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<String>> delete(@PathVariable String id) {
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
    public ResponseEntity<Response<Page<Ticket>>> listTicket(@PathVariable int page, @PathVariable int count) {
        Response<Page<Ticket>> ticketResponse = new Response<>();
        Page<Ticket> tickets = ticketService.listTicket(page, count);
        ticketResponse.setData(tickets);
        return ResponseEntity.ok(ticketResponse);
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
