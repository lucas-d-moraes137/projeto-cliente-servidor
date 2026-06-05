package com.grupoAura.projeto.dto;

import com.grupoAura.projeto.entity.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email
        // senha NUNCA é exposta na resposta
) {
    public static UsuarioResponseDTO from(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail());
    }
}
