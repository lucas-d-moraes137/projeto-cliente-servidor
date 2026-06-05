package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.ProfissionalRequestDTO;
import com.grupoAura.projeto.dto.ProfissionalResponseDTO;
import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.ProfissionalRepository;
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
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional buildProfissional(Long id, String nome, String cpf,
                                           String especialidade, String crm) {
        return new Profissional(id, nome, "prof@email.com", "senha123", cpf, especialidade, crm);
    }

    @Test
    void deveSalvarProfissionalComSucesso() {
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");
        Profissional salvo = buildProfissional(1L, "Dr. Carlos", "11122233344", "Cardiologia", "CRM12345");

        when(profissionalRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(profissionalRepository.existsByCrm(dto.crm())).thenReturn(false);
        when(profissionalRepository.save(any())).thenReturn(salvo);

        ProfissionalResponseDTO resultado = profissionalService.salvarProfissional(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.crm()).isEqualTo("CRM12345");
    }

    @Test
    void deveLancarExcecaoQuandoCpfDuplicado() {
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");
        when(profissionalRepository.existsByCpf(dto.cpf())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> profissionalService.salvarProfissional(dto));
        verify(profissionalRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCrmDuplicado() {
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                "Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345");
        when(profissionalRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(profissionalRepository.existsByCrm(dto.crm())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> profissionalService.salvarProfissional(dto));
        verify(profissionalRepository, never()).save(any());
    }

    @Test
    void deveLancar404QuandoProfissionalNaoEncontrado() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.buscarPorId(99L));
    }
}
