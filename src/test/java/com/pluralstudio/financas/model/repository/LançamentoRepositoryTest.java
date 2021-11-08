package com.pluralstudio.financas.model.repository;

import com.pluralstudio.financas.model.entities.Lancamento;
import com.pluralstudio.financas.model.enuns.StatusLancamento;
import com.pluralstudio.financas.model.enuns.TipoLancamento;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LançamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamento = deveCriarUmLancamento();
        lancamento = repository.save(lancamento);

        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento(){

        Lancamento lancamento = deveCriarUmLancamento();
        entityManager.persist(lancamento);

        lancamento = entityManager.find(Lancamento.class,lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class,lancamento.getId());

        assertThat(lancamentoInexistente).isNull();

    }

    @Test
    public void deveAtualizarUmLancamento(){
        Lancamento lancamento = criarEPersistirLancamento();

        lancamento.setAno(2018);
        lancamento.setDescricao("Teste atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        repository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class,lancamento.getId());

        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizar");
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

    }

    @Test
    public void deveBuscarUmLancamentoPorId(){
        Lancamento lancamento = criarEPersistirLancamento();

        Optional<Lancamento> lancamentoEncntrado = repository.findById(lancamento.getId());

        assertThat(lancamentoEncntrado.isPresent()).isTrue();
    }

    private Lancamento deveCriarUmLancamento(){
        return Lancamento.builder()
                .ano(2019)
                .descricao("lancamento qualquer")
                .mes(1)
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENDENTE)
                .dataCadastro(LocalDate.now())
                .build();

    }

    private Lancamento criarEPersistirLancamento(){
        Lancamento lancamento = deveCriarUmLancamento();
        entityManager.persist(lancamento);
        return lancamento;

    }
}
