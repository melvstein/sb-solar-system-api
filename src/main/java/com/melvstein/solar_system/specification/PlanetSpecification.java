package com.melvstein.solar_system.specification;

import com.melvstein.solar_system.model.Planet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PlanetSpecification {
    public static Specification<Planet> hasMoon() {
        return (root, query, builder) -> {
            Join<Object, Object> moonsJoin = root.join("moons");

            return builder.isNotNull(moonsJoin.get("id"));
        };
    }
}
