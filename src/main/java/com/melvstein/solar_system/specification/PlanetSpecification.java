package com.melvstein.solar_system.specification;

import com.melvstein.solar_system.model.Planet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PlanetSpecification {
    public static Specification<Planet> hasAtmosphere() {
        return (root, query, builder) -> {
            return builder.isNotNull(root.get("atmosphere"));
        };
    }

    public static Specification<Planet> hasNoAtmosphere() {
        return (root, query, builder) -> {
            return builder.isNull(root.get("atmosphere"));
        };
    }

    public static Specification<Planet> hasMoon() {
        return (root, query, builder) -> {
            return builder.isNotEmpty(root.get("moons"));
        };
    }

    public static Specification<Planet> hasNoMoon() {
        return (root, query, builder) -> {
            return builder.isEmpty(root.get("moons"));
        };
    }

    public static Specification<Planet> hasRing() {
        return (root, query, builder) -> {
            return builder.isNotNull(root.get("ring"));
        };
    }

    public static Specification<Planet> hasNoRing() {
        return (root, query, builder) -> {
            return builder.isNull(root.get("ring"));
        };
    }
}
