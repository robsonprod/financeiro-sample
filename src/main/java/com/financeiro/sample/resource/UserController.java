package com.financeiro.sample.resource;

import com.financeiro.sample.domain.UserCreateDTO;
import com.financeiro.sample.repository.GroupRepository;
import com.financeiro.sample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class UserController implements Serializable {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;

    @ResponseBody
    @PostMapping("signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserCreateDTO body){

        return Optional.ofNullable(body.toUser())
                .map(user -> {
                    user.setRoles(new HashSet<>());
                    groupRepository.findByRole("USER").ifPresent(user.getRoles()::add);
                    return user;
                })
                .map(userService::save)
                .map(UserCreateDTO::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Tivemos um problema ao salvar o usuário"));
    }

    @ResponseBody
    @PostMapping("teste")
    public ResponseEntity<?> teste(@Valid @RequestBody UserCreateDTO body){

        val user = body.toUser();

        if (Objects.nonNull(body.getGroups())) {
            val groups =  body.getGroups().stream()
                    .map(groupRepository::findByRole)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            user.getRoles().addAll(groups);
            userService.save(user);
        }

        return ResponseEntity.ok(user);
    }

}
