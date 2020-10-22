package com.br.helpdesk.service.impl;

import com.br.helpdesk.entity.Usuario;
import com.br.helpdesk.repository.UsuarioRepository;
import com.br.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public Usuario findByEmail(String email) {
        return null;
    }

    @Override
    public Usuario createOrUpdate(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario findById(String id) {
        return null;
    }

    @Override
    public Page<Usuario> findAll(int page, int count) {
        return null;
    }
}
