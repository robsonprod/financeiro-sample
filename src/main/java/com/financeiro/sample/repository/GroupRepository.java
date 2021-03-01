package com.financeiro.sample.repository;

import com.financeiro.sample.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByRole(String role);
}
