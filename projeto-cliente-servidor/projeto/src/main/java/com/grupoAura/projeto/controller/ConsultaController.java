package com.grupoAura.projeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupoAura.projeto.entity.Consulta;
import com.grupoAura.projeto.repository.ConsultaRepository;
import com.grupoAura.projeto.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ConsultaService consultaService;

    // RF 5: Listagem de consultas agendadas
    @GetMapping
    public List<Consulta> listarTodas() {
        List<Consulta> lista = new ArrayList<>();
        consultaRepository.findAll().forEach(lista::add);
        return lista;
    }

    // RF 3: Agendamento de consultas (usando o Service)
    @PostMapping
    public ResponseEntity<?> agendar(@RequestBody Consulta consulta) {
        try {
            Consulta novaConsulta = consultaService.agendarConsulta(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF 4: Cancelamento de consultas (Adicionando a rota DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            consultaService.cancelarConsulta(id);
            return ResponseEntity.ok().body("Consulta cancelada com sucesso!");
        } catch (RuntimeException e) {
            // Se o ID não existir, o Service joga o erro e cai aqui
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}