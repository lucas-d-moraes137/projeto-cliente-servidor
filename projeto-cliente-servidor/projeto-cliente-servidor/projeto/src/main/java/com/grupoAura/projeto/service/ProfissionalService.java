package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.ProfissionalRequestDTO;
import com.grupoAura.projeto.dto.ProfissionalResponseDTO;
import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.exception.BusinessConflictException;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> listarTodos() {
        return profissionalRepository.findAll()
                .stream()
                .map(ProfissionalResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfissionalResponseDTO buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .map(ProfissionalResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));
    }

    @Transactional
    public ProfissionalResponseDTO salvarProfissional(ProfissionalRequestDTO dto) {
        if (profissionalRepository.existsByCpf(dto.cpf())) {
            throw new BusinessConflictException("Já existe um profissional cadastrado com este CPF!");
        }
        if (profissionalRepository.existsByCrm(dto.crm())) {
            throw new BusinessConflictException("Já existe um profissional cadastrado com este CRM!");
        }

        Profissional p = new Profissional();
        // Campos herdados de Usuario
        p.setNome(dto.nome());
        p.setEmail(dto.email());
        p.setSenha(dto.senha());
        p.setCpf(dto.cpf());
        // Campos próprios de Profissional
        p.setEspecialidade(dto.especialidade());
        p.setCrm(dto.crm());

        return ProfissionalResponseDTO.from(profissionalRepository.save(p));
    }

    @Transactional
    public ProfissionalResponseDTO atualizar(Long id, ProfissionalRequestDTO dto) {
        Profissional atual = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", id));

        // Campos herdados de Usuario (atualizáveis)
        atual.setNome(dto.nome());
        atual.setEmail(dto.email());
        atual.setSenha(dto.senha());
        // CPF e CRM não são atualizados — campos imutáveis por regra de negócio
        // Campo próprio de Profissional
        atual.setEspecialidade(dto.especialidade());

        return ProfissionalResponseDTO.from(profissionalRepository.save(atual));
    }

    @Transactional
    public void deletar(Long id) {
        if (!profissionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profissional", id);
        }
        profissionalRepository.deleteById(id);
    }
}
