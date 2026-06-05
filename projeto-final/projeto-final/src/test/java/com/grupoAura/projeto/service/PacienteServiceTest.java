package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.PacienteRequestDTO;
import com.grupoAura.projeto.dto.PacienteResponseDTO;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.PacienteRepository;
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
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente buildPaciente(Long id, String nome, String email, String cpf, String telefone) {
        Paciente p = new Paciente();
        p.setNome(nome);
        p.setEmail(email);
        p.setSenha("senha123");
        p.setCpf(cpf);
        p.setTelefone(telefone);
        // Reflexão não necessária: id é setado via construtor all-args herdado
        // Usamos o construtor completo disponível
        return new Paciente(id, nome, email, "senha123", cpf, telefone);
    }

    @Test
    void deveSalvarPacienteComSucesso() {
        PacienteRequestDTO dto = new PacienteRequestDTO(
                "João Silva", "joao@email.com", "senha123", "12345678901", "81999990000");
        Paciente pacienteSalvo = buildPaciente(1L, "João Silva", "joao@email.com", "12345678901", "81999990000");

        when(pacienteRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteSalvo);

        PacienteResponseDTO resultado = pacienteService.salvarPaciente(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João Silva");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfDuplicado() {
        PacienteRequestDTO dto = new PacienteRequestDTO(
                "João Silva", "joao@email.com", "senha123", "12345678901", "81999990000");

        when(pacienteRepository.existsByCpf(dto.cpf())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> pacienteService.salvarPaciente(dto));
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void deveLancar404QuandoPacienteNaoEncontrado() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(99L));
    }

    @Test
    void deveBuscarPacientePorId() {
        Paciente paciente = buildPaciente(1L, "Ana Lima", "ana@email.com", "11122233300", "81977770000");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteResponseDTO resultado = pacienteService.buscarPorId(1L);

        assertThat(resultado.nome()).isEqualTo("Ana Lima");
    }
}
