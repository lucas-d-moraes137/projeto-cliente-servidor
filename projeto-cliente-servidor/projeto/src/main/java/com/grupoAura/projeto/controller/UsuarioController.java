package com.grupoAura.projeto.controller;

import com.grupoAura.projeto.entity.Usuario;
import com.grupoAura.projeto.repository.UsuarioRepository;
import com.grupoAura.projeto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        usuarioRepository.findAll().forEach(lista::add);
        return lista;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RN 5: Rota que o Swagger vai usar para testar o Login recebendo email e senha
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        try {
            Usuario usuarioLogado = usuarioService.fazerLogin(email, senha);
            return ResponseEntity.ok().body("Login realizado com sucesso! Bem-vindo, " + usuarioLogado.getNome());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}