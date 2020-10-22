package com.br.helpdesk.repository;

import com.br.helpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket,String> {

    Page<Ticket> findByUsuarioIdOrderByDataDesc(String userId, Pageable paginas);

    Page<Ticket> findByTituloIgnoreCaseContainingAndStatusAndPriorityOrderByDataDesc(
            String titulo, String status, String priority, Pageable paginas);

    Page<Ticket> findByTituloIgnoreCaseContainingAndStatusAndPriorityAndUsuarioIdOrderByDataDesc(
            String titulo, String status, String priority, Pageable paginas);

    Page<Ticket> findByTituloIgnoreCaseContainingAndStatusAndPriorityAndAssigneredUserIdOrderByDataDesc(
            String titulo, String status, String priority, Pageable paginas);

    Page<Ticket> findByNumber(Integer numero, Pageable paginas);

}
