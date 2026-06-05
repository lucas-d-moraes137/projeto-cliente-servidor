package com.grupoAura.projeto.repository;

import com.grupoAura.projeto.entity.Profissional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfissionalRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    private Profissional novoProfissional(String nome, String email, String senha, String cpf,
                                          String especialidade, String crm) {
        Profissional p = new Profissional();
        p.setNome(nome);
        p.setEmail(email);
        p.setSenha(senha);
        p.setCpf(cpf);
        p.setEspecialidade(especialidade);
        p.setCrm(crm);
        return p;
    }

    @Test
    void deveBuscarPorEspecialidade() {
        em.persist(novoProfissional("Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345"));
        em.persist(novoProfissional("Dra. Lena",  "lena@med.com",   "senha123", "55566677788", "Pediatria",   "CRM99999"));
        em.flush();

        List<Profissional> resultado = profissionalRepository
                .findByEspecialidadeContainingIgnoreCase("cardio");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Dr. Carlos");
    }

    @Test
    void deveRetornarTrueQuandoCpfExistir() {
        em.persist(novoProfissional("Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345"));
        em.flush();

        assertThat(profissionalRepository.existsByCpf("11122233344")).isTrue();
    }

    @Test
    void deveRetornarTrueQuandoCrmExistir() {
        em.persist(novoProfissional("Dr. Carlos", "carlos@med.com", "senha123", "11122233344", "Cardiologia", "CRM12345"));
        em.flush();

        assertThat(profissionalRepository.existsByCrm("CRM12345")).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoCrmNaoExistir() {
        assertThat(profissionalRepository.existsByCrm("CRM00000")).isFalse();
    }
}
