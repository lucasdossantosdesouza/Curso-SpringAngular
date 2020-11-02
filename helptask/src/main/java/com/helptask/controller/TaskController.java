package com.helptask.controller;

import com.helptask.api.dto.Summary;
import com.helptask.api.response.Response;
import com.helptask.entity.*;
import com.helptask.security.jwt.JwtTokenUtil;
import com.helptask.service.TaskService;
import com.helptask.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @Operation(summary = "create ticket", security = @SecurityRequirement(name = "Authorization"))
    public ResponseEntity<Response<Task>> create(HttpServletRequest request, @RequestBody Task task,
                                                 BindingResult result){
        Response<Task> taskResponse = new Response<>();
        try {
            validateCreateTicket(task, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        taskResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(taskResponse);
            }
            //ticket.setStatus(StatusEnum.getStatus("New"));
            task.setData(new Date());
            task.setNumber(taskService.generatedNumber());
            task.setUsuario(userFromRequest(request));
            task.setTitulo(task.getTitulo().toUpperCase());
            Task taskPersist = taskService.createOrUpdate(task);
            taskResponse.setData(taskPersist);
        }catch (DuplicateKeyException de){
            taskResponse.getErrors().add("Ticket already registred");
            return ResponseEntity.badRequest().body(taskResponse);
        }catch (Exception e){
            taskResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(taskResponse);
        }

        return ResponseEntity.ok(taskResponse);
    }

    private Usuario userFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUserNameFromToken(token);
        return usuarioService.findByEmail(email);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<Task>> update(HttpServletRequest request, @RequestBody Task task,
                                                 BindingResult result){
        Response<Task> taskResponse = new Response<>();
        try {
            validateUpdateTicket(task, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        taskResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(taskResponse);
            }
            Optional<Task> ticketFind = taskService.findById(task.getId());
            if (ticketFind != null){
                task.setStatus(ticketFind.get().getStatus());
                task.setData(ticketFind.get().getData());
                task.setNumber(ticketFind.get().getNumber());
                task.setUsuario(ticketFind.get().getUsuario());
                task.setTitulo(task.getTitulo().toUpperCase());
                if(ticketFind.get().getAssigneredUser() != null){
                    task.setAssigneredUser(ticketFind.get().getAssigneredUser());
                }
                Task taskPersist = taskService.createOrUpdate(task);
                taskResponse.setData(taskPersist);
            }
        }catch (Exception e){
            taskResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(taskResponse);
        }

        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Task>> findById(@PathVariable("id") String id) {
        Response<Task> taskResponse = new Response<>();
        Optional<Task> taskFind = taskService.findById(id);
        if(taskFind == null || taskFind.get() == null){
            taskResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(taskResponse);
        }
        Iterable<ChangeStatus> changeStatuses = taskService.listChangeStatus(taskFind.get().getId());
        taskFind.get().setChangeStatus(new ArrayList<>());
        changeStatuses.forEach(changeStatus -> {
            changeStatus.setTicket(null);

            taskFind.get().getChangeStatus().add(changeStatus);
        });

        taskResponse.setData(taskFind.get());
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
        Response<String> taskResponse = new Response<>();
        Optional<Task> ticketFind = taskService.findById(id);
        if(ticketFind == null || ticketFind.get() == null){
            taskResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(taskResponse);
        }
        taskService.delete(ticketFind.get().getId());
        return ResponseEntity.ok(new Response<String>());
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    @Operation(summary = "mostrar todos tickets", security = @SecurityRequirement(name = "Authorization"))
    public ResponseEntity<Response<Iterable<Task>>> findAll() {
        Response<Iterable<Task>> taksResponse = new Response<>();
        Iterable<Task> tasks = taskService.findAll();
        taksResponse.setData(tasks);
        return ResponseEntity.ok(taksResponse);
    }

    @GetMapping(value = "/{page}/{count}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Page<Task>>> listTasks(HttpServletRequest request, @PathVariable("page") int page,
                                                           @PathVariable("count") int count) {
        Response<Page<Task>> tasksResponse = new Response<>();
        Usuario usuario = userFromRequest(request);
        Page<Task> tasks = null;
        if(ProfileEnum.ROLE_TECHNICIAN == usuario.getProfile()){
            tasks = taskService.listTasks(page, count);
        }else if(ProfileEnum.ROLE_CUSTOMER == usuario.getProfile()){
            tasks = taskService.findByCurrentUser(page, count,usuario.getId());
        }
        tasksResponse.setData(tasks);
        return ResponseEntity.ok(tasksResponse);
    }

    @GetMapping(value = "/{page}/{count}/{titulo}/{status}/{priority}/{number}/{assigned}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Page<Task>>> findByParams(HttpServletRequest request,
                                                             @PathVariable("page") int page,
                                                             @PathVariable("count") int count,
                                                             @PathVariable("titulo") String titulo,
                                                             @PathVariable("status") String status,
                                                             @PathVariable("priority") String priority,
                                                             @PathVariable("number") Integer number,
                                                             @PathVariable("assigned") boolean assigned) {
        Response<Page<Task>> taskResponse = new Response<>();
        Usuario usuario = userFromRequest(request);

        Page<Task> tasks = null;
        titulo = titulo.equals("uninformed") ? "" :titulo.toUpperCase();
        priority = priority.equals("uninformed") ? "" :priority;
        status = status.equals("uninformed") ? "" :status;

        if(number > 0){
            tasks = taskService.findByNumber(page, count, number);
        }else {
             if (ProfileEnum.ROLE_TECHNICIAN.equals(usuario.getProfile())) {
                 if(assigned) {
                     tasks = taskService.findByParametersAndAssignedUser(page, count, titulo, status, priority, usuario.getId());
                 }else{
                     tasks = taskService.findByParameters(page, count, titulo, status, priority);
                 }
            } else if (ProfileEnum.ROLE_CUSTOMER.equals(usuario.getProfile())) {
                tasks = taskService.findByParametersAndCurrentUser(page, count, titulo, status, priority, usuario.getId());
            }
        }
        taskResponse.setData(tasks);
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{id}/{status}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<Response<Task>> changeStatus(HttpServletRequest request,
                                                       @PathVariable("status") String status,
                                                       @PathVariable("id") String id,
                                                       @RequestBody Task task,
                                                       BindingResult result){
        Response<Task> taskResponse = new Response<>();
        try {
            validateChangeStatus(id , status, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        taskResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(taskResponse);
            }
            Optional<Task> taskFind = taskService.findById(task.getId());

            if(taskFind != null) {
                taskFind.get().setStatus(StatusEnum.getStatus(status));
                if(status.equals("Assigned")){
                    taskFind.get().setAssigneredUser(new Usuario());
                    taskFind.get().setAssigneredUser(userFromRequest(request));
                }
                Task taskPersist = taskService.createOrUpdate(taskFind.get());
                ChangeStatus changeStatus = new ChangeStatus();
                changeStatus.setUsuario(userFromRequest(request));
                changeStatus.setData(new Date());
                changeStatus.setStatusEnum(StatusEnum.getStatus(status));
                changeStatus.setTicket(taskPersist);
                taskService.createCahngesStatus(changeStatus);
                taskResponse.setData(taskPersist);
            }
        }catch (Exception e){
            taskResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(taskResponse);
        }

        return ResponseEntity.ok(taskResponse);
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

        Iterable<Task> tasks = taskService.findAll();
        tasks.forEach(task -> {
            if(StatusEnum.New.equals(task.getStatus())){
                amountNew.getAndSet(amountNew.get() + 1);
            }
            if(StatusEnum.Resolved.equals(task.getStatus())){
                amountResolved.getAndSet(amountResolved.get() + 1);
            }
            if(StatusEnum.Aproved.equals(task.getStatus())){
                amountAproved.getAndSet(amountAproved.get() + 1);
            }
            if(StatusEnum.Disaproved.equals(task.getStatus())){
                amountDisaproved.getAndSet(amountDisaproved.get() + 1);
            }
            if(StatusEnum.Assigned.equals(task.getStatus())){
                amountAssigned.getAndSet(amountAssigned.get() + 1);
            }
            if(StatusEnum.Closed.equals(task.getStatus())){
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

    private void validateCreateTicket(Task task, BindingResult result){
        if(task.getTitulo() == null){
            result.addError(new ObjectError("User", "titulo no Information"));
        }
    }

    private void validateUpdateTicket(Task usuario, BindingResult result){
        if(usuario.getId() == null){
            result.addError(new ObjectError("User", "Id no Information"));
        }
        if(usuario.getTitulo() == null){
            result.addError(new ObjectError("User", "Titulo no Information"));
        }
    }

}
