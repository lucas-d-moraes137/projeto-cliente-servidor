package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade);

    boolean existsByCpf(String cpf);

    boolean existsByCrm(String crm);
}
