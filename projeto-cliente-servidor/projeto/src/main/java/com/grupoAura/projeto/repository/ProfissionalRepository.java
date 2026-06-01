package com.grupoAura.projeto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.grupoAura.projeto.entity.Profissional;

public interface ProfissionalRepository extends CrudRepository<Profissional, Long> {

    // O seu código original que busca por especialidade (MANTIDO!)
    List<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade);

    // Os novos métodos para a nossa validação do Service:
    boolean existsByCpf(String cpf);
    boolean existsByCrm(String crm);
    
}