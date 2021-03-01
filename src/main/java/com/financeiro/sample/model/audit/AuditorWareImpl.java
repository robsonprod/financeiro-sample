package com.financeiro.sample.model.audit;

import com.financeiro.sample.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorWareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        try {
            User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return Optional.ofNullable(String.format("%d:%s", user.getId(), user.getName().toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}

