package com.grupoAura.projeto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    @Column(nullable = false, length = 20)
    private String telefone;

    public Paciente() {}

    public Paciente(Long id, String nome, String email, String senha, String cpf, String telefone) {
        super(id, nome, email, senha, cpf);
        this.telefone = telefone;
    }

    public String getTelefone()              { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
