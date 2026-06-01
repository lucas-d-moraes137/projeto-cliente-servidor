package com.grupoAura.projeto.service;

import com.grupoAura.projeto.entity.Consulta;
import com.grupoAura.projeto.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("null") // Isso desliga os avisos chatos de "Null type safety" do VS Code
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    // Lógica para agendar consulta (RF 3)
    public Consulta agendarConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    // Lógica para cancelamento de consulta (RF 4)
    public void cancelarConsulta(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new RuntimeException("Não foi possível cancelar: Consulta não encontrada com o ID " + id);
        }
        consultaRepository.deleteById(id);
    }
}