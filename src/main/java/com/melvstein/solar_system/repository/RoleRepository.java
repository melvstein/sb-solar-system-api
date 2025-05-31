package com.melvstein.solar_system.repository;

import com.melvstein.solar_system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
