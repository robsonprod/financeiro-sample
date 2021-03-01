package com.financeiro.sample.resource;

import com.financeiro.sample.domain.dto.Credentials;
import com.financeiro.sample.security.JwtTokenUtil;
import com.financeiro.sample.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "authenticate", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class AuthenticateController implements Serializable {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtToken;

    public AuthenticateController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtTokenUtil jwtToken) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtToken = jwtToken;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> auth(@RequestBody @Valid Credentials user) {
        val credentials = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        try {
            authenticationManager.authenticate(credentials);
        } catch (BadCredentialsException | DisabledException e) {
            throw new BadCredentialsException("BadCredentialsException", e);
        }

        val token = jwtToken.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));

        return ResponseEntity.ok(new HashMap<>() {{ put("token", token); }});
    }

}
