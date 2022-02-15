package com.justpickup.userservice.domain.user.repository;

import com.justpickup.userservice.domain.user.entity.AuthType;
import com.justpickup.userservice.domain.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
}
