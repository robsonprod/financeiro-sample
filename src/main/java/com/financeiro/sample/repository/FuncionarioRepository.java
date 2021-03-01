package com.financeiro.sample.repository;

import com.financeiro.sample.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByIdAndEmpresaId(Long id, Long empresa_id);
}
