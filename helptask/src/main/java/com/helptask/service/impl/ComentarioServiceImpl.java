package com.helptask.service.impl;

import com.helptask.entity.Comentario;
import com.helptask.repository.ComentarioRepositoy;
import com.helptask.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    public Page<Comentario> findByTask(int page, int count, String idTask) {
        Pageable pageable= PageRequest.of(page,count);
        return comentarioRepositoy.findByTask(pageable,idTask);
    }

}
