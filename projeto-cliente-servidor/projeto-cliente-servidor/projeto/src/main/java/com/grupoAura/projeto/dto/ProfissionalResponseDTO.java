package com.grupoAura.projeto.dto;

import com.grupoAura.projeto.entity.Profissional;

public record ProfissionalResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String especialidade,
        String crm
) {
    public static ProfissionalResponseDTO from(Profissional p) {
        return new ProfissionalResponseDTO(
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getCpf(),
                p.getEspecialidade(),
                p.getCrm()
        );
    }
}
