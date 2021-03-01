package com.financeiro.sample.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.financeiro.sample.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreateDTO implements Serializable {

    private final static long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @NotBlank
    @JsonProperty("password")
    private String password;

    private Set<String> groups;

    public UserCreateDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public User toUser() {
        return new User(this.name, this.email, this.password);
    }
}
