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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupoAura.dto.UsuarioDTO;
import com.grupoAura.projeto.entity.Usuario;
import com.grupoAura.projeto.repository.UsuarioRepository;
import com.grupoAura.projeto.service.UsuarioService;

import jakarta.validation.Valid;

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
    public ResponseEntity<?> criar(@RequestBody @Valid UsuarioDTO dto) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(dto.getSenha());

            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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