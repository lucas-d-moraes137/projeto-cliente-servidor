package com.grupoAura.projeto.controller;

import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.repository.PacienteRepository;
import com.grupoAura.projeto.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        pacienteRepository.findAll().forEach(lista::add);
        return lista;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Paciente paciente) {
        try {
            // Chama o serviço para validar antes de salvar
            Paciente novoPaciente = pacienteService.salvarPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (RuntimeException e) {
            // Se o CPF for repetido, devolve o erro com texto explicativo
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}