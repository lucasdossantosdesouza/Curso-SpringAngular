package com.br.helpdesk.service;

import com.br.helpdesk.entity.ChangeStatus;
import com.br.helpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface TicketService {
    Ticket createOrUpdate(Ticket ticket);
    Optional<Ticket> findById(String id);
    void delete(String id);
    Page<Ticket> listTicket(int page, int count);
    ChangeStatus createCahngesStatus(ChangeStatus changeStatus);
    Iterable<ChangeStatus> listChangeStatus(String idTicket);
    Page<Ticket> findByCurrentUser(int page, int count, String idUsuario);
    Page<Ticket> findByParameters(int page, int count,String titulo, String status, String priority);
    Page<Ticket> findByParametersAndCurrentUser(int page, int count,String titulo, String status, String priority,String idUsuario);
    Page<Ticket> findByNumber(int page, int count, Integer number);
    Iterable<Ticket> findAll();
    Page<Ticket> findByParametersAndAssignedUser(int page, int count,String titulo, String status, String priority, String assignedUser);

    Integer generatedNumber();
}
