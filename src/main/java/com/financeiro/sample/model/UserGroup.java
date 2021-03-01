package com.financeiro.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class UserGroup {

    //insert into groups (id, role) values (nextval('seq_groups'), 'USER');
    //insert into groups (id, role) values (nextval('seq_groups'), 'ADMIN');

    @Id
    @GeneratedValue(generator = "seq_groups", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_groups", sequenceName = "seq_groups", allocationSize = 1)
    private Long id;

    @Column(unique = true, updatable = false)
    private String role;
}
