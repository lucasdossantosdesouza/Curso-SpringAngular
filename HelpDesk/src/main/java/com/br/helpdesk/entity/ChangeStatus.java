package com.br.helpdesk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class ChangeStatus {

    @Id
    private String id;

    @DBRef
    private Ticket ticket;

    @DBRef
    private Usuario usuario;

    private Date data;

    private StatusEnum statusEnum;

}
