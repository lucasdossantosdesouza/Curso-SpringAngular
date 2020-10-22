package com.br.data.controller;

import com.br.data.entity.Alunos;
import com.br.data.repository.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AlunosController {

    @Autowired
    AlunosRepository alunosRepository;

    @GetMapping(value = "/alunos")
    public List<Alunos> listaAlunos(){
        return alunosRepository.findAll();
    }

    @GetMapping(value = "/alunos/{id}")
    public Optional<Alunos> findId(@PathVariable String id){
        return alunosRepository.findById(id);
    }

    @GetMapping(value = "/alunos/{name}/name")
    public List<Alunos> findNameIgnorecase(@PathVariable String name){
        return alunosRepository.findByNameLikeIgnoreCase(name);
    }

    @PostMapping(value = "/")
    public Alunos saveAlunos(@RequestBody Alunos alunos){
        return alunosRepository.save(alunos);
    }
}
