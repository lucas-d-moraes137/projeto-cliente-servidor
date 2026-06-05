package com.grupoAura.projeto.controller;

import com.grupoAura.projeto.dto.ProfissionalRequestDTO;
import com.grupoAura.projeto.dto.ProfissionalResponseDTO;
import com.grupoAura.projeto.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@Tag(name = "Profissionais", description = "Operações de gerenciamento de profissionais de saúde")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os profissionais")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ProfissionalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca profissional por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profissional encontrado"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<ProfissionalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo profissional")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profissional criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou CRM já cadastrado")
    })
    public ResponseEntity<ProfissionalResponseDTO> criar(@Valid @RequestBody ProfissionalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.salvarProfissional(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza dados de um profissional")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profissional atualizado"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<ProfissionalResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfissionalRequestDTO dto) {
        return ResponseEntity.ok(profissionalService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um profissional")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profissional removido"),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        profissionalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
