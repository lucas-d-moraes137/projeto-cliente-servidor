package com.grupoAura.projeto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf; // CPF movido para cá por ser comum a Paciente e Profissional

    public Usuario() {}

    public Usuario(Long id, String nome, String email, String senha, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
    }

    public Long getId()       { return id; }
    public String getNome()   { return nome; }
    public String getEmail()  { return email; }
    public String getSenha()  { return senha; }
    public String getCpf()    { return cpf; }

    public void setNome(String nome)    { this.nome = nome; }
    public void setEmail(String email)  { this.email = email; }
    public void setSenha(String senha)  { this.senha = senha; }
    public void setCpf(String cpf)      { this.cpf = cpf; }
}
