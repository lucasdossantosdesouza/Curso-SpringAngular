package com.helptask.service.impl;

import com.helptask.entity.Comentario;
import com.helptask.repository.ComentarioRepositoy;
import com.helptask.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepositoy comentarioRepositoy;

    @Override
    public Comentario createOrUpdate(Comentario comentario) {
        return comentarioRepositoy.save(comentario);
    }

    @Override
    public Optional<Comentario> findById(String id) {
        return comentarioRepositoy.findById(id);
    }

    @Override
    public void delete(String id) {
        comentarioRepositoy.deleteById(id);
    }

    @Override
    public Iterable<Comentario> findByTask( String idTask) {
         return comentarioRepositoy.findByTask(idTask);
    }

    @Override
    public Comentario buildComentarioUpdate(Optional<Comentario> comentarioFind, Comentario comentario) {
        AtomicReference<Comentario> comentarioPersist = new AtomicReference<>();
        comentarioFind.ifPresent(comentario1 -> {
            comentario.setUsuario(comentario1.getUsuario());
            comentario.setTask(comentario1.getTask());
            comentarioPersist.set(createOrUpdate(comentario));
        });
        return comentarioPersist.get();
    }

}
