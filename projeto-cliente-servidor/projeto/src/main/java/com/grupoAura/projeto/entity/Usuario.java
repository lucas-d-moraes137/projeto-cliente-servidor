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
@Table(name = "usuarios")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            nullable = false,
            length = 100)
    private String nome;


    @Column(
            nullable = false,
            unique = true,
            length = 100)
    private String email;


    @Column(
            nullable = false,
            length = 255)
    private String senha;

}