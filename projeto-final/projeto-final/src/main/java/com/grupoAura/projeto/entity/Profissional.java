package com.grupoAura.projeto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profissionais")
@DiscriminatorValue("PROFISSIONAL")
public class Profissional extends Usuario {

    @Column(nullable = false, length = 80)
    private String especialidade;

    @Column(nullable = false, unique = true, length = 20)
    private String crm;

    public Profissional() {}

    public Profissional(Long id, String nome, String email, String senha, String cpf,
                        String especialidade, String crm) {
        super(id, nome, email, senha, cpf);
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public String getEspecialidade()                     { return especialidade; }
    public String getCrm()                               { return crm; }
    public void setEspecialidade(String especialidade)   { this.especialidade = especialidade; }
    public void setCrm(String crm)                       { this.crm = crm; }
}
