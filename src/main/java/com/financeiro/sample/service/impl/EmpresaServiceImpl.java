package com.financeiro.sample.service.impl;

import com.financeiro.sample.model.ContaCorrente;
import com.financeiro.sample.model.Empresa;
import com.financeiro.sample.model.Funcionario;
import com.financeiro.sample.repository.EmpresaRepository;
import com.financeiro.sample.repository.FuncionarioRepository;
import com.financeiro.sample.service.EmpresaService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public Funcionario pagarFuncionario(Long idEmpresa, Long idFuncionario, @NotNull BigDecimal pagamento) {
        Assert.notNull(pagamento, "Valor do pagamento não pode ser");

        return funcionarioRepository.findByIdAndEmpresaId(idFuncionario, idEmpresa)
                .or(() -> funcionarioRepository.findById(idFuncionario).map(funcionario -> {
                        // TODO: Fix - caso o funcionario não seja da empresa
                        funcionario.setEmpresa(findById(idEmpresa)
                                .orElseThrow(() -> new IllegalArgumentException("Empresa não existe")));

                        return funcionario;
                    }).map(funcionarioRepository::saveAndFlush)
                )
                .map(funcionario -> {
                    if (Objects.isNull(funcionario.getContaCorrente())) {
                        funcionario.setContaCorrente(new ContaCorrente());
                    }

                    val pagamentoAtualizado = pagamento
                            .subtract((BigDecimal.valueOf(0.38).divide(BigDecimal.valueOf(100)))
                                    .multiply(pagamento));

                    val saldoEmpresa = funcionario.getEmpresa().getContaCorrente().getSaldo();
                    val saldoFuncionario = funcionario.getContaCorrente().getSaldo();

                    if (saldoEmpresa.compareTo(pagamentoAtualizado) == -1)
                        throw new IllegalArgumentException("Saldo da empresa insuficiente");

                    val saldoFuncionarioAtuaizado = Optional.ofNullable(saldoFuncionario)
                            .orElse(BigDecimal.ZERO).add(pagamentoAtualizado);

                    funcionario.getContaCorrente().setSaldo(saldoFuncionarioAtuaizado);
                    funcionario.getEmpresa().getContaCorrente().setSaldo(saldoEmpresa.subtract(pagamentoAtualizado));

                    return funcionario;
                })
                .map(funcionarioRepository::saveAndFlush)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não localizado"));
    }

    @Override
    public Empresa deposito(Long idEmpresa, BigDecimal saldo) {

        return empresaRepository.findById(idEmpresa)
                .map(empresa -> {
                    if (Objects.isNull(empresa.getContaCorrente())) empresa.setContaCorrente(new ContaCorrente());

                    if (Objects.isNull(empresa.getContaCorrente().getSaldo())){
                        val novoSaldo = empresa.getContaCorrente().getSaldo().add(saldo);
                        empresa.getContaCorrente().setSaldo(novoSaldo);
                    } else {
                        empresa.getContaCorrente().setSaldo(saldo);
                    }

                    return empresa;
                })
                .map(empresaRepository::saveAndFlush)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

    }

    @Override
    public Empresa save(Empresa entity) {
        entity.setContaCorrente(new ContaCorrente(BigDecimal.ZERO));
        return empresaRepository.save(entity);
    }

    @Override
    public <S extends Empresa> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Empresa> findAll() {
        return null;
    }

    @Override
    public Iterable<Empresa> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }

    @Override
    public void delete(Empresa entity) {
    }

    @Override
    public void deleteAll(Iterable<? extends Empresa> entities) {
    }

    @Override
    public void deleteAll() {
    }

}
