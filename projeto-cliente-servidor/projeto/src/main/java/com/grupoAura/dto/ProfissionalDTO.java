package com.grupoAura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfissionalDTO {

    @NotBlank(message = "O nome do profissional é obrigatório.")
    @Size(max = 100, message = "O nome não pode passar de 100 caracteres.")
    private String nome;

    @NotBlank(message = "A especialidade é obrigatória.")
    private String especialidade;

    @NotBlank(message = "O registro profissional (CRM/CRP) é obrigatório.")
    private String registroProfissional;
}