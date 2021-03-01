package com.financeiro.sample.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "funcionarios")
public class Funcionario extends GenericModel<String> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "seq_funcionarios", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_funcionarios", sequenceName = "seq_funcionarios", allocationSize = 1)
    private Long id;

    @Column
    private String nome;

    @Column
    private String cargo;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_corrente_id")
    private ContaCorrente contaCorrente;

}
