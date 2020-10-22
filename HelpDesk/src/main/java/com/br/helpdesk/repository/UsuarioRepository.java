package com.br.helpdesk.repository;

import com.br.helpdesk.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario,String> {
}
