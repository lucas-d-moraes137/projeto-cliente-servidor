package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    // Mantém o seu método original que você já tinha criado
    boolean existsByEmail(String email);

    // Adiciona a busca por e-mail que o Service precisa para o login
    Optional<Usuario> findByEmail(String email);
}