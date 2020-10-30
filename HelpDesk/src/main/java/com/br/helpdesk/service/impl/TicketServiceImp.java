package com.br.helpdesk.service.impl;

import com.br.helpdesk.entity.ChangeStatus;
import com.br.helpdesk.entity.Ticket;
import com.br.helpdesk.repository.ChangeStatusRepository;
import com.br.helpdesk.repository.TicketRepository;
import com.br.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class TicketServiceImp implements TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ChangeStatusRepository changeStatusRepository;

    @Override
    public Ticket createOrUpdate(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Integer generatedNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }

    @Override
    public Optional<Ticket> findById(String id) {
        return ticketRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Page<Ticket> listTicket(int page, int count) {
        Pageable pageable = PageRequest.of(page,count);
        return ticketRepository.findAll(pageable);
    }

    @Override
    public ChangeStatus createCahngesStatus(ChangeStatus changeStatus) {
        return changeStatusRepository.save(changeStatus);
    }

    @Override
    public Iterable<ChangeStatus> listChangeStatus(String idTicket) {
        return changeStatusRepository.findByTicketIdOrderByDataDesc(idTicket);
    }

    @Override
    public Page<Ticket> findByCurrentUser(int page, int count, String idUsuario) {
        Pageable pageable= PageRequest.of(page,count);
        return ticketRepository.findByUsuarioIdOrderByDataDesc(idUsuario, pageable);
    }

    @Override
    public Page<Ticket> findByParameters(int page, int count, String titulo, String status, String priority) {
        Pageable pageable = PageRequest.of(page,count);
        return ticketRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingOrderByDataDesc(titulo, status, priority, pageable);
    }

    @Override
    public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String priority, String idUsuario) {
        Pageable pageable = PageRequest.of(page,count);
        return ticketRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndUsuarioIdOrderByDataDesc(titulo, status, priority, pageable, idUsuario);
    }

    @Override
    public Page<Ticket> findByNumber(int page, int count, Integer number) {
        Pageable pageable = PageRequest.of(page,count);
        return ticketRepository.findByNumber(number,pageable);
    }

    @Override
    public Iterable<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Page<Ticket> findByParametersAndAssignedUser(int page, int count, String titulo, String status, String priority, String assignedUser) {
        Pageable pageable = PageRequest.of(page,count);
        return ticketRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndAssigneredUserIdOrderByDataDesc(titulo, status, priority, pageable,assignedUser);
    }
}
