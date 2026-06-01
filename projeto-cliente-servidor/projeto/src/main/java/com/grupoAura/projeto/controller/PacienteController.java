package com.grupoAura.projeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupoAura.dto.PacienteDTO;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.repository.PacienteRepository;
import com.grupoAura.projeto.service.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService; // Injetando o Service

    @GetMapping
    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        pacienteRepository.findAll().forEach(lista::add);
        return lista;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid PacienteDTO dto) {
        try {
            Paciente paciente = new Paciente();
            paciente.setNome(dto.getNome());
            paciente.setCpf(dto.getCpf());
            paciente.setTelefone(dto.getTelefone());

            // CHAMANDO O SERVICE para rodar a validação de CPF duplicado!
            Paciente novoPaciente = pacienteService.salvarPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}