package com.grupoAura.projeto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="profissionais")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Profissional {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            nullable=false,
            length=150)
    private String nome;


    @Column(
            nullable=false,
            length=80)
    private String especialidade;


    @Column(
            nullable=false,
            length=20)
    private String crm;

    @Column(nullable=false, length=11, unique=true)
    private String cpf;

}