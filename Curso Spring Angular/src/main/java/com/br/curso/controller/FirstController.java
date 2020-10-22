package com.br.curso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class FirstController {

    @GetMapping(value = "/show")
    public ResponseEntity<String> showText(){
        return new ResponseEntity<String>("Hello Word!", HttpStatus.CREATED);
    }
}
