package com.dititalinnovation.buymore.repository;

import com.dititalinnovation.buymore.entity.Electronic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectronicRepository extends JpaRepository<Electronic, Long> {

    Optional<Electronic> findByName(String name);
}
