package com.grupoAura.projeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupoAura.projeto.entity.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByStatus(String status);

    List<Consulta> findByPacienteId(Long pacienteId);
}