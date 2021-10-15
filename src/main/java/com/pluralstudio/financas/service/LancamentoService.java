package com.pluralstudio.financas.service;

import com.pluralstudio.financas.model.entities.Lancamento;
import com.pluralstudio.financas.model.enuns.StatusLancamento;

import java.util.List;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);
    void validar(Lancamento lancamento);
}
