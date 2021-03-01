package com.financeiro.sample.resource;

import com.financeiro.sample.domain.dto.ContaCorrenteDTO;
import com.financeiro.sample.domain.dto.PagamentoDTO;
import com.financeiro.sample.model.Empresa;
import com.financeiro.sample.model.User;
import com.financeiro.sample.service.EmpresaService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "empresa", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<?> save(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody Empresa empresa) {

        log.info("{}", user.getName());

        return Optional.ofNullable(empresaService.save(empresa))
                .map(empresaService::save)
                .map(result -> {
                    result.setId(null);
                    return result;
                })
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Error ao salva empresa"));
    }

    @PostMapping("depositar/{empresaId}")
    @ResponseBody
    public ResponseEntity<?> depositar(@Valid @RequestBody ContaCorrenteDTO body,
                                       @PathVariable Long empresaId){
        empresaService.deposito(empresaId, body.getSaldo());

        return ResponseEntity.ok().body("Valor depositado");
    }

    @GetMapping("saldo/{id}")
    public ResponseEntity<?> saldo(@PathVariable Long id){

        val empresa = empresaService.findById(id);
        return new ResponseEntity<>(empresa.get().getContaCorrente().getSaldo(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        empresaService.deleteById(id);
    }

    @GetMapping("{id}")
    public Empresa getById(@PathVariable Long id){

        return empresaService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada."));
    }

    @PostMapping("pagar-funcionario")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<?> pagarFuncionario(@Valid @RequestBody PagamentoDTO body){

        empresaService.pagarFuncionario(body.getEmpresaId(), body.getFuncionarioId(), body.getValor());

        return ResponseEntity.ok().body("Funcionário pago");
    }

}
