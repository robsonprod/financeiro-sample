package com.financeiro.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conta_corrente")
public class ContaCorrente {
    @Id
    @GeneratedValue(generator = "seq_conta_corrente", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_conta_corrente", sequenceName = "seq_conta_corrente", allocationSize = 1)
    private Long id;

    @Column(name = "saldo", precision = 12, scale = 4)
    @JsonProperty("saldo")
    private BigDecimal saldo;

    public ContaCorrente(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
