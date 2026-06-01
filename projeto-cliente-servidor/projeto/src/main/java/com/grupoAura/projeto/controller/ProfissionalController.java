package com.grupoAura.projeto.controller;

import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import com.grupoAura.projeto.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    // 1. Aqui nós "injetamos" o Service que criamos para validar as regras
    @Autowired
    private ProfissionalService profissionalService;

    // Essa rota continua igual, listando todo mundo
    @GetMapping
    public List<Profissional> listarTodos() {
        List<Profissional> lista = new ArrayList<>();
        profissionalRepository.findAll().forEach(lista::add);
        return lista;
    }

    // 2. Mudamos essa rota! Agora ela usa o try-catch para pegar o erro de CPF/CRM duplicado
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Profissional profissional) {
        try {
            // Em vez de salvar direto, chama o Service para validar
            Profissional novoProfissional = profissionalService.salvarProfissional(profissional);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProfissional);
        } catch (RuntimeException e) {
            // Se o Service jogar o erro de duplicado, o Controller devolve o texto do erro pro usuário
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}