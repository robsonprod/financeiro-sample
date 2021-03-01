package com.financeiro.sample.service;

import com.financeiro.sample.model.Empresa;
import com.financeiro.sample.model.Funcionario;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface EmpresaService extends CrudRepository<Empresa, Long> {
    Funcionario pagarFuncionario(Long idEmpresa, Long idFuncionario, BigDecimal pagamento);

    Empresa deposito(Long idEmpresa, BigDecimal body);
}
