package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.UsuarioRequestDTO;
import com.grupoAura.projeto.dto.UsuarioResponseDTO;
import com.grupoAura.projeto.entity.Usuario;
import com.grupoAura.projeto.exception.BusinessConflictException;
import com.grupoAura.projeto.exception.UnauthorizedException;
import com.grupoAura.projeto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::from)
                .toList();
    }

    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessConflictException("Este e-mail já está sendo usado por outro usuário!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    // RN 5: Login — retorna DTO sem expor senha
    @Transactional(readOnly = true)
    public UsuarioResponseDTO fazerLogin(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado com este e-mail!"));
        if (!usuario.getSenha().equals(senha)) {
            throw new UnauthorizedException("Senha incorreta!");
        }
        return UsuarioResponseDTO.from(usuario);
    }
}
