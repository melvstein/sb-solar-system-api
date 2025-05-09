package com.melvstein.solar_system.repository;

import com.melvstein.solar_system.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long>, JpaSpecificationExecutor<Planet> {
    public Planet findByName(String name);
}
