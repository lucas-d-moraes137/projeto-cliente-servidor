package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Paciente;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PacienteRepository extends CrudRepository<Paciente, Long> {

    // Procura pacientes pelo nome (ignorando maiúsculas/minúsculas)
    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    // Verifica se o CPF já existe no banco de dados
    boolean existsByCpf(String cpf);
    
}