package com.melvstein.solar_system.repository;

import com.melvstein.solar_system.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    public Planet findByName(String name);
}
