package com.grupoAura.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultaDTO {

    @NotNull(message = "O ID do paciente é obrigatório.")
    private Long pacienteId;

    @NotNull(message = "O ID do profissional é obrigatório.")
    private Long profissionalId; // Corrigido para dois 'ss'

    @NotNull(message = "A data e hora da consulta são obrigatórias.")
    @Future(message = "A consulta deve ser agendada para uma data futura.")
    private LocalDateTime dataHora;
}