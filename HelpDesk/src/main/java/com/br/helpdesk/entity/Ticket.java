package com.br.helpdesk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
public class Ticket {

    @Id
    private String id;

    private String titulo;

    @DBRef(lazy = true)
    private Usuario usuario;

    private Date data;

    private Integer number;

    private StatusEnum status;

    private PriorityEnum priority;

    @DBRef(lazy = true)
    private Usuario assigneredUser;

    private String description;

    private String image;

    private List<ChangeStatus> changeStatus;
}
