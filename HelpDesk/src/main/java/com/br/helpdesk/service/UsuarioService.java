package com.br.helpdesk.service;

import com.br.helpdesk.entity.Usuario;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UsuarioService {
    Usuario findByEmail(String email);

    Usuario createOrUpdate(Usuario usuario);

    Optional<Usuario> findById(String id);

    void delete(String id);

    Page<Usuario> findAll(int page, int count);
}
