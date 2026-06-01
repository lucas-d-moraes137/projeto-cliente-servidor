package com.grupoAura.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome não pode passar de 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Por favor, insira um e-mail válido.")
    @Size(max = 100, message = "O e-mail não pode passar de 100 caracteres.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, max = 255, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;
}