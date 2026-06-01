package com.grupoAura.projeto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="consultas")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Consulta {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable=false)
    private LocalDateTime dataConsulta;


    @Column(
            nullable=false,
            length=30)
    private String status;



    @ManyToOne
    @JoinColumn(
            name="paciente_id")
    private Paciente paciente;



    @ManyToOne
    @JoinColumn(
            name="profissional_id")
    private Profissional profissional;

}