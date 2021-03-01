package com.financeiro.sample.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonProperty("empresa_id")
    private Long empresaId;

    @NotNull
    @JsonProperty("funcionario_id")
    private Long funcionarioId;

    @NotNull
    @JsonProperty("valor")
    private BigDecimal valor;
}
