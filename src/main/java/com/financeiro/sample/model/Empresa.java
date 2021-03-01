package com.financeiro.sample.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empresas")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Empresa extends GenericModel<String> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "seq_empresas", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_empresas", sequenceName = "seq_empresas", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "nome_fantasia")
    @JsonProperty("nome_fantasia")
    private String nomeFantasia;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_corrente_id")
    private ContaCorrente contaCorrente;
}
