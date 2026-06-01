package com.grupoAura.projeto.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Indica que esta classe captura erros globalmente nos Controllers
public class GlobalExceptionHandler {

    // 1. Captura o erro de Recurso Não Encontrado e devolve HTTP 404 (Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", HttpStatus.NOT_FOUND.value());
        corpo.put("erro", "Recurso não encontrado");
        corpo.put("mensagem", ex.getMessage());

        return new ResponseEntity<>(corpo, HttpStatus.NOT_FOUND);
    }

    // 2. Captura erros de validação do @Valid (DTOs) e devolve HTTP 400 (Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errosValidacao = new HashMap<>();
        
        // Mapeia qual campo falhou e qual foi a mensagem de erro dele
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nomeCampo = ((FieldError) error).getField();
            String mensagemErro = error.getDefaultMessage();
            errosValidacao.put(nomeCampo, mensagemErro);
        });

        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("erro", "Erro de validação de dados");
        corpo.put("campos", errosValidacao);

        return new ResponseEntity<>(corpo, HttpStatus.BAD_REQUEST);
    }

    // 3. Captura qualquer outro erro genérico (RuntimeException) e devolve HTTP 400 ou 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("erro", "Erro na requisição");
        corpo.put("mensagem", ex.getMessage());

        return new ResponseEntity<>(corpo, HttpStatus.BAD_REQUEST);
    }
}