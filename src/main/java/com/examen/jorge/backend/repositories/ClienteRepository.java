package com.examen.jorge.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.jorge.backend.data.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}