package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.ConsultaRequestDTO;
import com.grupoAura.projeto.dto.ConsultaResponseDTO;
import com.grupoAura.projeto.entity.Consulta;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.ConsultaRepository;
import com.grupoAura.projeto.repository.PacienteRepository;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private ConsultaService consultaService;

    private Paciente buildPaciente(Long id) {
        return new Paciente(id, "João Silva", "joao@email.com", "senha123", "12345678901", "81999990000");
    }

    private Profissional buildProfissional(Long id) {
        return new Profissional(id, "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");
    }

    private Consulta buildConsulta(Long id, Paciente p, Profissional prof) {
        Consulta c = new Consulta();
        c.setDataConsulta(LocalDateTime.now().plusDays(1));
        c.setStatus("AGENDADA");
        c.setPaciente(p);
        c.setProfissional(prof);
        return c;
    }

    @Test
    void deveAgendarConsultaComSucesso() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                LocalDateTime.now().plusDays(1), "AGENDADA", 1L, 1L);
        Paciente paciente = buildPaciente(1L);
        Profissional profissional = buildProfissional(1L);
        Consulta salva = buildConsulta(1L, paciente, profissional);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(consultaRepository.save(any())).thenReturn(salva);

        ConsultaResponseDTO resultado = consultaService.agendarConsulta(dto);

        assertThat(resultado.status()).isEqualTo("AGENDADA");
        assertThat(resultado.pacienteNome()).isEqualTo("João Silva");
        assertThat(resultado.profissionalNome()).isEqualTo("Dr. Carlos");
        verify(consultaRepository).save(any());
    }

    @Test
    void deveLancar404QuandoPacienteNaoEncontradoAoAgendar() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                LocalDateTime.now().plusDays(1), "AGENDADA", 99L, 1L);

        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> consultaService.agendarConsulta(dto));
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancar404QuandoProfissionalNaoEncontradoAoAgendar() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                LocalDateTime.now().plusDays(1), "AGENDADA", 1L, 99L);
        Paciente paciente = buildPaciente(1L);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> consultaService.agendarConsulta(dto));
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveBuscarConsultaPorId() {
        Paciente paciente = buildPaciente(1L);
        Profissional profissional = buildProfissional(1L);
        Consulta consulta = buildConsulta(1L, paciente, profissional);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        ConsultaResponseDTO resultado = consultaService.buscarPorId(1L);

        assertThat(resultado.status()).isEqualTo("AGENDADA");
    }

    @Test
    void deveLancar404QuandoConsultaNaoEncontrada() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> consultaService.buscarPorId(99L));
    }

    @Test
    void deveCancelarConsultaComSucesso() {
        when(consultaRepository.existsById(1L)).thenReturn(true);

        consultaService.cancelarConsulta(1L);

        verify(consultaRepository).deleteById(1L);
    }

    @Test
    void deveLancar404AoCancelarConsultaInexistente() {
        when(consultaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> consultaService.cancelarConsulta(99L));
        verify(consultaRepository, never()).deleteById(any());
    }

    @Test
    void deveListarTodasAsConsultas() {
        Paciente paciente = buildPaciente(1L);
        Profissional profissional = buildProfissional(1L);
        when(consultaRepository.findAll()).thenReturn(
                List.of(buildConsulta(1L, paciente, profissional)));

        List<ConsultaResponseDTO> resultado = consultaService.listarTodas();

        assertThat(resultado).hasSize(1);
    }
}
