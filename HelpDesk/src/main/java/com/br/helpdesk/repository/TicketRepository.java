package com.br.helpdesk.repository;

import com.br.helpdesk.entity.Ticket;
import com.br.helpdesk.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket,String> {

    Page<Ticket> findByUsuarioIdOrderByDataDesc(Pageable pageable, String userId);

    Page<Ticket> findByTituloIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingOrderByDataDesc(
            String titulo, String status, String priority, Pageable pageable);
}
