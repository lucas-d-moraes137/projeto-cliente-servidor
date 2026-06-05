package com.grupoAura.projeto.dto;

import com.grupoAura.projeto.entity.Paciente;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String telefone
) {
    public static PacienteResponseDTO from(Paciente p) {
        return new PacienteResponseDTO(
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getCpf(),
                p.getTelefone()
        );
    }
}
