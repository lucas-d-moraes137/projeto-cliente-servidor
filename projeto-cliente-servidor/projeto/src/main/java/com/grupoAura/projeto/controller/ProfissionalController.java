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

import com.grupoAura.dto.ProfissionalDTO;
import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import com.grupoAura.projeto.service.ProfissionalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ProfissionalService profesionalService; 

    @GetMapping
    public List<Profissional> listarTodos() {
        List<Profissional> lista = new ArrayList<>();
        profissionalRepository.findAll().forEach(lista::add);
        return lista;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid ProfissionalDTO dto) {
        try {
            Profissional profissional = new Profissional();
            profissional.setNome(dto.getNome());
            profissional.setEspecialidade(dto.getEspecialidade());
            Profissional novoProfissional = profesionalService.salvarProfissional(profissional);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}