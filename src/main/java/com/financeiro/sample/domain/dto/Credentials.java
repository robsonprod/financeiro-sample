package com.financeiro.sample.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credentials implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Email
    @JsonProperty("username")
    private String username;

    @NotNull
    @JsonProperty("password")
    private String password;
}
