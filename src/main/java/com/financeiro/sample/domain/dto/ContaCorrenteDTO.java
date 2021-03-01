package com.financeiro.sample.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrenteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("saldo")
    private BigDecimal saldo;
}
