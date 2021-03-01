package com.financeiro.sample.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends GenericModel<String> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "seq_users", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_users", sequenceName = "seq_users", allocationSize = 1)
    private Long id;

    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Column(length = 200)
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_groups", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<UserGroup> roles;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
