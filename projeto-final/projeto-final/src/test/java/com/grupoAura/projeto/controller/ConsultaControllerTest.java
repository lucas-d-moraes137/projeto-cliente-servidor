package com.grupoAura.projeto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoAura.projeto.dto.ConsultaRequestDTO;
import com.grupoAura.projeto.dto.ConsultaResponseDTO;
import com.grupoAura.projeto.exception.GlobalExceptionHandler;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.service.ConsultaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultaController.class)
@Import(GlobalExceptionHandler.class)
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConsultaService consultaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ConsultaResponseDTO fakeResponse() {
        return new ConsultaResponseDTO(
                1L, LocalDateTime.now().plusDays(1), "AGENDADA",
                1L, "João Silva", 1L, "Dr. Carlos");
    }

    @Test
    void deveListarTodasAsConsultas() throws Exception {
        when(consultaService.listarTodas()).thenReturn(List.of(fakeResponse()));

        mvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AGENDADA"))
                .andExpect(jsonPath("$[0].pacienteNome").value("João Silva"));
    }

    @Test
    void deveBuscarConsultaPorId() throws Exception {
        when(consultaService.buscarPorId(1L)).thenReturn(fakeResponse());

        mvc.perform(get("/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.profissionalNome").value("Dr. Carlos"));
    }

    @Test
    void deveRetornar404QuandoConsultaNaoExistir() throws Exception {
        when(consultaService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Consulta", 99L));

        mvc.perform(get("/consultas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar201AoAgendarConsulta() throws Exception {
        ConsultaRequestDTO req = new ConsultaRequestDTO(
                LocalDateTime.now().plusDays(1), "AGENDADA", 1L, 1L);

        when(consultaService.agendarConsulta(any())).thenReturn(fakeResponse());

        mvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidosAoAgendar() throws Exception {
        // status em branco deve falhar na validação
        String jsonInvalido = """
                {"dataConsulta": "2099-12-31T10:00:00", "status": "",
                 "pacienteId": 1, "profissionalId": 1}
                """;

        mvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar204AoCancelarConsulta() throws Exception {
        doNothing().when(consultaService).cancelarConsulta(1L);

        mvc.perform(delete("/consultas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoCancelarConsultaInexistente() throws Exception {
        doThrow(new ResourceNotFoundException("Consulta", 99L))
                .when(consultaService).cancelarConsulta(99L);

        mvc.perform(delete("/consultas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
