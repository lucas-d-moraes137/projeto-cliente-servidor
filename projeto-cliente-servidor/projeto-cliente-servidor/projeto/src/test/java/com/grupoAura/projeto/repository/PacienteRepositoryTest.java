package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PacienteRepository pacienteRepository;

    private Paciente novoPaciente(String nome, String email, String senha, String cpf, String telefone) {
        Paciente p = new Paciente();
        p.setNome(nome);
        p.setEmail(email);
        p.setSenha(senha);
        p.setCpf(cpf);
        p.setTelefone(telefone);
        return p;
    }

    @Test
    void deveBuscarPorNomeContendo() {
        em.persist(novoPaciente("João Silva",  "joao@email.com",  "senha123", "12345678901", "81999990000"));
        em.persist(novoPaciente("Maria Souza", "maria@email.com", "senha123", "98765432100", "81988880000"));
        em.flush();

        List<Paciente> resultado = pacienteRepository.findByNomeContainingIgnoreCase("silva");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");
    }

    @Test
    void deveRetornarTrueQuandoCpfExistir() {
        em.persist(novoPaciente("João Silva", "joao@email.com", "senha123", "12345678901", "81999990000"));
        em.flush();

        boolean existe = pacienteRepository.existsByCpf("12345678901");

        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoCpfNaoExistir() {
        boolean existe = pacienteRepository.existsByCpf("00000000000");

        assertThat(existe).isFalse();
    }

    @Test
    void deveSalvarERecuperarPaciente() {
        Paciente paciente = em.persist(novoPaciente("Ana Lima", "ana@email.com", "senha123", "11122233300", "81977770000"));
        em.flush();

        Paciente encontrado = pacienteRepository.findById(paciente.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo("Ana Lima");
    }
}
