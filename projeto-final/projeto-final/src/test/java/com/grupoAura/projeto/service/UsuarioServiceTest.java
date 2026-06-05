package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.UsuarioRequestDTO;
import com.grupoAura.projeto.dto.UsuarioResponseDTO;
import com.grupoAura.projeto.entity.Usuario;
import com.grupoAura.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    // Usuario(Long id, String nome, String email, String senha, String cpf)
    private Usuario buildUsuario(Long id, String nome, String email, String senha) {
        return new Usuario(id, nome, email, senha, "00000000000");
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Maria", "maria@email.com", "senha123");
        Usuario salvo = buildUsuario(1L, "Maria", "maria@email.com", "senha123");

        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(salvo);

        UsuarioResponseDTO resultado = usuarioService.salvarUsuario(dto);

        assertThat(resultado.email()).isEqualTo("maria@email.com");
        // senha NÃO deve aparecer no DTO de resposta
        verify(usuarioRepository).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Maria", "maria@email.com", "senha123");
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.salvarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveFazerLoginComSucesso() {
        Usuario usuario = buildUsuario(1L, "Maria", "maria@email.com", "senha123");
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.fazerLogin("maria@email.com", "senha123");

        assertThat(resultado.nome()).isEqualTo("Maria");
    }

    @Test
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        Usuario usuario = buildUsuario(1L, "Maria", "maria@email.com", "senha123");
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () -> usuarioService.fazerLogin("maria@email.com", "errada"));
    }
}
