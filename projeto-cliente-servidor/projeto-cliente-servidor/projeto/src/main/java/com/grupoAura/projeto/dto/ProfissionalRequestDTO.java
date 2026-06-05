package com.grupoAura.projeto.dto;

import jakarta.validation.constraints.*;

public record ProfissionalRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 255, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                 message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Especialidade é obrigatória")
        @Size(max = 80, message = "Especialidade deve ter no máximo 80 caracteres")
        String especialidade,

        @NotBlank(message = "CRM é obrigatório")
        @Size(max = 20, message = "CRM deve ter no máximo 20 caracteres")
        String crm

) {}
