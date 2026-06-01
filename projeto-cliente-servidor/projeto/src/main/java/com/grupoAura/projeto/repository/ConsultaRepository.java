package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Consulta;

import org.springframework.data.repository.
CrudRepository;

import java.util.List;

public interface ConsultaRepository
extends CrudRepository<
        Consulta,
        Long> {


    List<Consulta>
    findByStatus(
            String status
    );


    List<Consulta>
    findByPacienteId(
            Long pacienteId
    );

}