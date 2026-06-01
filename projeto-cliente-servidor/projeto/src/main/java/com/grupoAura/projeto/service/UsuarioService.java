package com.grupoAura.projeto.service;

import com.grupoAura.projeto.entity.Usuario;
import com.grupoAura.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Cadastra o usuário e barra se o e-mail já existir
    public Usuario salvarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Este e-mail já está sendo usado por outro usuário!");
        }
        return usuarioRepository.save(usuario);
    }

    // RN 5: Regra de negócio do Login buscando pelo seu campo 'email'
    public Usuario fazerLogin(String email, String senha) {
        // 1. Busca o usuário no banco pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este e-mail!"));

        // 2. Compara a senha enviada com a do banco
        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta!");
        }

        return usuario;
    }
}