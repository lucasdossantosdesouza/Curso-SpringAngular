package com.br.helpdesk.repository;

import com.br.helpdesk.entity.ChangeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {
}
