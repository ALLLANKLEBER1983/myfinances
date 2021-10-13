package com.pluralstudio.financas.model.repository;

import com.pluralstudio.financas.model.entities.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {
}
