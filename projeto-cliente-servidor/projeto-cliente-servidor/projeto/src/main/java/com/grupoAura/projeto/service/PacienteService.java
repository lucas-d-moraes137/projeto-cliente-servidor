package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.PacienteRequestDTO;
import com.grupoAura.projeto.dto.PacienteResponseDTO;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.exception.BusinessConflictException;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(PacienteResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    @Transactional
    public PacienteResponseDTO salvarPaciente(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new BusinessConflictException("Já existe um paciente cadastrado com este CPF!");
        }

        Paciente paciente = new Paciente();
        // Campos herdados de Usuario
        paciente.setNome(dto.nome());
        paciente.setEmail(dto.email());
        paciente.setSenha(dto.senha());
        paciente.setCpf(dto.cpf());
        // Campo próprio de Paciente
        paciente.setTelefone(dto.telefone());

        return PacienteResponseDTO.from(pacienteRepository.save(paciente));
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente atual = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        // Campos herdados de Usuario (atualizáveis)
        atual.setNome(dto.nome());
        atual.setEmail(dto.email());
        atual.setSenha(dto.senha());
        // CPF não é atualizado — campo imutável por regra de negócio
        // Campo próprio de Paciente
        atual.setTelefone(dto.telefone());

        return PacienteResponseDTO.from(pacienteRepository.save(atual));
    }

    @Transactional
    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        pacienteRepository.deleteById(id);
    }
}
