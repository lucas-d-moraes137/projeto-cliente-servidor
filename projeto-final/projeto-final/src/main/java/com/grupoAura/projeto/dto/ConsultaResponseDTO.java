package com.grupoAura.projeto.dto;

import java.time.LocalDateTime;

import com.grupoAura.projeto.entity.Consulta;

public record ConsultaResponseDTO(
        Long id,
        LocalDateTime dataConsulta,
        String status,
        Long pacienteId,
        String pacienteNome,
        Long profissionalId,
        String profissionalNome
) {
    public static ConsultaResponseDTO from(Consulta c) {
        return new ConsultaResponseDTO(
                c.getId(),
                c.getDataConsulta(),
                c.getStatus(),
                c.getPaciente() != null ? c.getPaciente().getId() : null,
                c.getPaciente() != null ? c.getPaciente().getNome() : null,
                c.getProfissional() != null ? c.getProfissional().getId() : null,
                c.getProfissional() != null ? c.getProfissional().getNome() : null
        );
    }
}
