package com.grupoAura.projeto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataConsulta;

    @Column(nullable = false, length = 30)
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    public Consulta() {}

    public Long getId()                      { return id; }
    public LocalDateTime getDataConsulta()   { return dataConsulta; }
    public String getStatus()                { return status; }
    public Paciente getPaciente()            { return paciente; }
    public Profissional getProfissional()    { return profissional; }

    public void setDataConsulta(LocalDateTime dataConsulta) { this.dataConsulta = dataConsulta; }
    public void setStatus(String status)                    { this.status = status; }
    public void setPaciente(Paciente paciente)              { this.paciente = paciente; }
    public void setProfissional(Profissional profissional)  { this.profissional = profissional; }
}
