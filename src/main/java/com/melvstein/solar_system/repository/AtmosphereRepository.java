package com.melvstein.solar_system.repository;

import com.melvstein.solar_system.model.Atmosphere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtmosphereRepository extends JpaRepository<Atmosphere, Long> {
}
