package com.melvstein.solar_system.repository;

import com.melvstein.solar_system.model.Moon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoonRepository extends JpaRepository<Moon, Long> {
}
