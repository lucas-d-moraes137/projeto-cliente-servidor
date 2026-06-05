package com.grupoAura.projeto.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConsultaRequestDTO(

        @NotNull(message = "Data da consulta é obrigatória")
        @Future(message = "A data da consulta deve ser no futuro")
        LocalDateTime dataConsulta,

        @NotBlank(message = "Status é obrigatório")
        @Size(max = 30, message = "Status deve ter no máximo 30 caracteres")
        String status,

        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "ID do profissional é obrigatório")
        Long profissionalId
) {}
