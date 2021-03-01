package com.financeiro.sample.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.financeiro.sample.model.ContaCorrente;
import com.financeiro.sample.model.Funcionario;
import com.sun.istack.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonProperty("nome")
    private String nome;

    @NotNull
    @JsonProperty("empresa_id")
    private Long empresaId;

    @JsonProperty("conta_corrente_id")
    private Long contaCorrenteId;

    @JsonProperty("cargo")
    private String cargo;

    public Optional<Funcionario> toFuncionario() {
        val funcionario = new Funcionario();
        funcionario.setCargo(this.cargo);
        funcionario.setNome(this.nome);

        return Optional.of(funcionario);
    }
}
