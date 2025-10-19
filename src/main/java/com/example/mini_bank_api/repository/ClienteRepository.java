package com.example.mini_bank_api.repository;

import com.example.mini_bank_api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNumeroConta(String numeroConta);
    boolean existsByNumeroConta(String numeroConta);
}
