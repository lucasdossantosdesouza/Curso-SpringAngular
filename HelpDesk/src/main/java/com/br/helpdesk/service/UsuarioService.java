package com.br.helpdesk.service;

import com.br.helpdesk.entity.Usuario;
import org.springframework.data.domain.Page;

public interface UsuarioService {
    Usuario findByEmail(String email);

    Usuario createOrUpdate(Usuario usuario);

    Usuario findById(String id);

    Page<Usuario> findAll(int page, int count);
}
