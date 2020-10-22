package com.br.data.repository;

import com.br.data.entity.Alunos;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface AlunosRepository extends MongoRepository<Alunos,String> {

    List<Alunos> findByNameLikeIgnoreCase(String name);
}
