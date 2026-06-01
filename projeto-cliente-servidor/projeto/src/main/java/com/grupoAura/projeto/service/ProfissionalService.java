package com.grupoAura.projeto.service;

import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    // Garanta que o nome do método seja EXATAMENTE salvarProfissional
    public Profissional salvarProfissional(Profissional profissional) {
        
        if (profissionalRepository.existsByCpf(profissional.getCpf())) {
            throw new RuntimeException("Já existe um profissional cadastrado com este CPF!");
        }

        if (profissionalRepository.existsByCrm(profissional.getCrm())) {
            throw new RuntimeException("Já existe um profissional cadastrado com este CRM!");
        }

        return profissionalRepository.save(profissional);
    }
}