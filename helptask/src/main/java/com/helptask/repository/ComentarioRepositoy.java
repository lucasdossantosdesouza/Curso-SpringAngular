package com.helptask.repository;

import com.helptask.entity.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepositoy extends MongoRepository<Comentario, String> {

    Iterable<Comentario> findByTask(String taskId);

}
