package com.grupoAura.projeto.service;

import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente salvarPaciente(Paciente paciente) {
        // REGRA DE NEGÓCIO: Se o CPF já existir, impede o cadastro
        if (pacienteRepository.existsByCpf(paciente.getCpf())) {
            throw new RuntimeException("Já existe um paciente cadastrado com este CPF!");
        }
        
        return pacienteRepository.save(paciente);
    }
}