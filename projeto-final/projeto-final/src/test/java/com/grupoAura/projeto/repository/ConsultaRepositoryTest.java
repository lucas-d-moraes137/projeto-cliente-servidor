package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Consulta;
import com.grupoAura.projeto.entity.Paciente;
import com.grupoAura.projeto.entity.Profissional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConsultaRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ConsultaRepository consultaRepository;

    private Paciente paciente;
    private Profissional profissional;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setNome("João Silva");
        paciente.setEmail("joao@email.com");
        paciente.setSenha("senha123");
        paciente.setCpf("12345678901");
        paciente.setTelefone("81999990000");
        em.persist(paciente);

        profissional = new Profissional();
        profissional.setNome("Dr. Carlos");
        profissional.setEmail("carlos@med.com");
        profissional.setSenha("senha123");
        profissional.setCpf("11122233344");
        profissional.setEspecialidade("Cardiologia");
        profissional.setCrm("CRM12345");
        em.persist(profissional);

        em.flush();
    }

    private Consulta novaConsulta(String status, LocalDateTime data) {
        Consulta c = new Consulta();
        c.setStatus(status);
        c.setDataConsulta(data);
        c.setPaciente(paciente);
        c.setProfissional(profissional);
        return c;
    }

    @Test
    void deveBuscarConsultasPorStatus() {
        em.persist(novaConsulta("AGENDADA", LocalDateTime.now().plusDays(1)));
        em.persist(novaConsulta("CANCELADA", LocalDateTime.now().plusDays(2)));
        em.flush();

        List<Consulta> resultado = consultaRepository.findByStatus("AGENDADA");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStatus()).isEqualTo("AGENDADA");
    }

    @Test
    void deveBuscarConsultasPorPacienteId() {
        em.persist(novaConsulta("AGENDADA", LocalDateTime.now().plusDays(1)));
        em.persist(novaConsulta("AGENDADA", LocalDateTime.now().plusDays(3)));
        em.flush();

        List<Consulta> resultado = consultaRepository.findByPacienteId(paciente.getId());

        assertThat(resultado).hasSize(2);
        resultado.forEach(c -> assertThat(c.getPaciente().getId()).isEqualTo(paciente.getId()));
    }

    @Test
    void deveRetornarListaVaziaQuandoStatusNaoExistir() {
        em.persist(novaConsulta("AGENDADA", LocalDateTime.now().plusDays(1)));
        em.flush();

        List<Consulta> resultado = consultaRepository.findByStatus("CONCLUIDA");

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveSalvarERecuperarConsulta() {
        Consulta consulta = em.persist(novaConsulta("AGENDADA", LocalDateTime.now().plusDays(5)));
        em.flush();

        Consulta encontrada = consultaRepository.findById(consulta.getId()).orElse(null);

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getStatus()).isEqualTo("AGENDADA");
        assertThat(encontrada.getPaciente().getNome()).isEqualTo("João Silva");
    }
}
