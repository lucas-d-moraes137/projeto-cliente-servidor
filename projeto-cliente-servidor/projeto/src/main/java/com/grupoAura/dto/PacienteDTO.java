package com.grupoAura.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PacienteDTO {

    @NotBlank(message = "O nome do paciente é obrigatório.")
    @Size(max = 100, message = "O nome não pode passar de 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "Por favor, insira um CPF válido.")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId; 
}