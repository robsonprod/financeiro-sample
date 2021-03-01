package com.financeiro.sample.resource;

import com.financeiro.sample.domain.dto.ContaCorrenteDTO;
import com.financeiro.sample.domain.dto.FuncionarioDTO;
import com.financeiro.sample.model.Empresa;
import com.financeiro.sample.model.Funcionario;
import com.financeiro.sample.service.EmpresaService;
import com.financeiro.sample.service.FuncionarioService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "funcionario", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<?> save(@Valid @RequestBody FuncionarioDTO body){

        val empresa = empresaService.findById(body.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não existe"));

        return body.toFuncionario()
                .map(funcionario -> {
                    funcionario.setEmpresa(empresa);
                    return funcionario;
                })
                .map(funcionarioService::save)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Error ao salvar"));
    }

    @GetMapping("saldo/{id}")
    public ResponseEntity<?> saldo(@PathVariable Long id){

        val func = funcionarioService.findById(id);

        return ResponseEntity.ok().body(func.get().getContaCorrente().getSaldo());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        funcionarioService.deleteById(id);
    }

    @GetMapping("{id}")
    public Funcionario getById(@PathVariable Long id){

        return funcionarioService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado."));
    }
}
