package com.grupoAura.projeto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoAura.projeto.dto.ProfissionalRequestDTO;
import com.grupoAura.projeto.dto.ProfissionalResponseDTO;
import com.grupoAura.projeto.exception.BusinessConflictException;
import com.grupoAura.projeto.exception.GlobalExceptionHandler;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.service.ProfissionalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfissionalController.class)
@Import(GlobalExceptionHandler.class)
class ProfissionalControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProfissionalService profissionalService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfissionalResponseDTO fakeResponse() {
        return new ProfissionalResponseDTO(
                1L, "Dr. Carlos", "carlos@med.com", "11122233344", "Cardiologia", "CRM12345");
    }

    @Test
    void deveListarProfissionais() throws Exception {
        when(profissionalService.listarTodos()).thenReturn(List.of(fakeResponse()));

        mvc.perform(get("/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Dr. Carlos"))
                .andExpect(jsonPath("$[0].crm").value("CRM12345"));
    }

    @Test
    void deveBuscarProfissionalPorId() throws Exception {
        when(profissionalService.buscarPorId(1L)).thenReturn(fakeResponse());

        mvc.perform(get("/profissionais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especialidade").value("Cardiologia"));
    }

    @Test
    void deveRetornar404QuandoProfissionalNaoExistir() throws Exception {
        when(profissionalService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Profissional", 99L));

        mvc.perform(get("/profissionais/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar201AoCriarProfissional() throws Exception {
        ProfissionalRequestDTO req = new ProfissionalRequestDTO(
                "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");

        when(profissionalService.salvarProfissional(any())).thenReturn(fakeResponse());

        mvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.crm").value("CRM12345"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        String jsonInvalido = """
                {"nome": "", "email": "carlos@med.com", "senha": "senha123",
                 "cpf": "11122233344", "especialidade": "Cardiologia", "crm": "CRM12345"}
                """;

        mvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoCrmDuplicado() throws Exception {
        ProfissionalRequestDTO req = new ProfissionalRequestDTO(
                "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");

        when(profissionalService.salvarProfissional(any()))
                .thenThrow(new BusinessConflictException("Já existe um profissional cadastrado com este CRM!"));

        mvc.perform(post("/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deveRetornar200AoAtualizar() throws Exception {
        ProfissionalRequestDTO req = new ProfissionalRequestDTO(
                "Dr. Carlos Updated", "carlos@med.com", "senha123", "11122233344", "Neurologia", "CRM12345");
        ProfissionalResponseDTO resp = new ProfissionalResponseDTO(
                1L, "Dr. Carlos Updated", "carlos@med.com", "11122233344", "Neurologia", "CRM12345");

        when(profissionalService.atualizar(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/profissionais/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especialidade").value("Neurologia"));
    }

    @Test
    void deveRetornar204AoDeletar() throws Exception {
        doNothing().when(profissionalService).deletar(1L);

        mvc.perform(delete("/profissionais/1"))
                .andExpect(status().isNoContent());
    }
}
