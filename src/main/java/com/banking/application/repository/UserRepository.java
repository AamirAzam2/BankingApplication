package com.banking.application.repository;

import com.banking.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    Optional<User> findByAccountNumber(String accountNumber);

    Optional<User> findByEmail(String email);
}
