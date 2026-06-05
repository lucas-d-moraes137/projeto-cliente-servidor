package com.grupoAura.projeto.service;

import com.grupoAura.projeto.dto.ConsultaRequestDTO;
import com.grupoAura.projeto.dto.ConsultaResponseDTO;
import com.grupoAura.projeto.entity.Consulta;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.entity.Profissional;
import com.grupoAura.projeto.exception.ResourceNotFoundException;
import com.grupoAura.projeto.repository.ConsultaRepository;
import com.grupoAura.projeto.repository.PacienteRepository;
import com.grupoAura.projeto.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           ProfissionalRepository profissionalRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(ConsultaResponseDTO::from)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
    }

    // RF 3: Agendamento de consulta
    @Transactional
    public ConsultaResponseDTO agendarConsulta(ConsultaRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", dto.pacienteId()));
        Profissional profissional = profissionalRepository.findById(dto.profissionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", dto.profissionalId()));

        Consulta consulta = new Consulta();
        consulta.setDataConsulta(dto.dataConsulta());
        consulta.setStatus(dto.status());
        consulta.setPaciente(paciente);
        consulta.setProfissional(profissional);

        return ConsultaResponseDTO.from(consultaRepository.save(consulta));
    }

    // RF 4: Cancelamento de consulta
    @Transactional
    public void cancelarConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta", id);
        }
        consultaRepository.deleteById(id);
    }
}