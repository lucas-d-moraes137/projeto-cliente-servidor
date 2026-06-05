package com.grupoAura.projeto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoAura.projeto.dto.PacienteRequestDTO;
import com.grupoAura.projeto.dto.PacienteResponseDTO;
import com.grupoAura.projeto.exception.GlobalExceptionHandler;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@Import(GlobalExceptionHandler.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarPacientes() throws Exception {
        when(pacienteService.listarTodos()).thenReturn(List.of(
                new PacienteResponseDTO(1L, "João", "joao@email.com", "12345678901", "81999990000")
        ));

        mvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void deveRetornar404QuandoPacienteNaoExistir() throws Exception {
        when(pacienteService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Paciente", 99L));

        mvc.perform(get("/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar201AoCriarPaciente() throws Exception {
        PacienteRequestDTO req = new PacienteRequestDTO(
                "João Silva", "joao@email.com", "senha123", "12345678901", "81999990000");
        PacienteResponseDTO resp = new PacienteResponseDTO(
                1L, "João Silva", "joao@email.com", "12345678901", "81999990000");

        when(pacienteService.salvarPaciente(any())).thenReturn(resp);

        mvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        // nome em branco deve falhar na validação
        String jsonInvalido = """
                {"nome": "", "email": "joao@email.com", "senha": "senha123",
                 "cpf": "12345678901", "telefone": "81999990000"}
                """;

        mvc.perform(post("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar204AoDeletarPaciente() throws Exception {
        mvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent());
    }
}
