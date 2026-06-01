package com.grupoAura.projeto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Paciente {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false,
            length = 150)
    private String nome;


    @Column(nullable = false,
            length = 14)
    private String cpf;


    @Column(nullable = false,
            length = 20)
    private String telefone;

}