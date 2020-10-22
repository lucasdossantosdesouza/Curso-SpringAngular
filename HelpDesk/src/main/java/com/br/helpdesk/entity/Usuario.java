package com.br.helpdesk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document
@Data
public class Usuario {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Email Obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Password Obrigatório")
    @Size(min = 6 )
    private String password;

    private ProfileEnum profile;


}
