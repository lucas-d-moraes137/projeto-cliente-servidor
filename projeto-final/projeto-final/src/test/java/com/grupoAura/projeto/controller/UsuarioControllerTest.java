package com.grupoAura.projeto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoAura.projeto.dto.UsuarioRequestDTO;
import com.grupoAura.projeto.dto.UsuarioResponseDTO;
import com.grupoAura.projeto.exception.BusinessConflictException;
import com.grupoAura.projeto.exception.GlobalExceptionHandler;
import com.grupoAura.projeto.exception.UnauthorizedException;
import com.grupoAura.projeto.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarUsuarios() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(
                List.of(new UsuarioResponseDTO(1L, "Maria", "maria@email.com")));

        mvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria"))
                // senha nunca deve aparecer no JSON
                .andExpect(jsonPath("$[0].senha").doesNotExist());
    }

    @Test
    void deveRetornar201AoCriarUsuario() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO("Maria", "maria@email.com", "senha123");
        UsuarioResponseDTO resp = new UsuarioResponseDTO(1L, "Maria", "maria@email.com");

        when(usuarioService.salvarUsuario(any())).thenReturn(resp);

        mvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        // email inválido
        String jsonInvalido = """
                {"nome": "Maria", "email": "nao-e-email", "senha": "senha123"}
                """;

        mvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoEmailDuplicado() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO("Maria", "maria@email.com", "senha123");

        when(usuarioService.salvarUsuario(any()))
                .thenThrow(new BusinessConflictException("Este e-mail já está sendo usado por outro usuário!"));

        mvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deveRetornar200AoFazerLoginComSucesso() throws Exception {
        UsuarioResponseDTO resp = new UsuarioResponseDTO(1L, "Maria", "maria@email.com");
        when(usuarioService.fazerLogin("maria@email.com", "senha123")).thenReturn(resp);

        mvc.perform(post("/usuarios/login")
                .param("email", "maria@email.com")
                .param("senha", "senha123"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Maria")));
    }

    @Test
    void deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        when(usuarioService.fazerLogin("maria@email.com", "errada"))
                .thenThrow(new UnauthorizedException("Senha incorreta!"));

        mvc.perform(post("/usuarios/login")
                .param("email", "maria@email.com")
                .param("senha", "errada"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }
}
