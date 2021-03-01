package com.financeiro.sample.service.impl;

import com.financeiro.sample.model.ContaCorrente;
import com.financeiro.sample.model.Funcionario;
import com.financeiro.sample.repository.FuncionarioRepository;
import com.financeiro.sample.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public Funcionario save(Funcionario entity) {
        entity.setContaCorrente(new ContaCorrente(BigDecimal.ZERO));
        return funcionarioRepository.save(entity);
    }

    @Override
    public void deposito(Long funcionarioId, BigDecimal saldo) {

    }

    @Override
    public <S extends Funcionario> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Funcionario> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Funcionario> findAll() {
        return null;
    }

    @Override
    public Iterable<Funcionario> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Funcionario entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Funcionario> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
