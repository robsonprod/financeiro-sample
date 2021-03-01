package com.financeiro.sample.service;

import com.financeiro.sample.model.Funcionario;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface FuncionarioService extends CrudRepository<Funcionario, Long> {
    void deposito(Long funcionarioId, BigDecimal saldo);
}
