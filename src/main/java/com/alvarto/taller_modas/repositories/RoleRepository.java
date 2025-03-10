package com.alvarto.taller_modas.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alvarto.taller_modas.Enums.RoleList;
import com.alvarto.taller_modas.models.Role;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleList name);
}