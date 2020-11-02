package com.helptask.service;

import com.helptask.entity.Comentario;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ComentarioService {
    Comentario createOrUpdate(Comentario comentario);
    Optional<Comentario> findById(String id);
    void delete(String id);
    Iterable<Comentario> findByTask(String idTask);

    Comentario buildComentarioUpdate(Optional<Comentario> comentarioFind, Comentario comentario);
}
