package com.melvstein.solar_system.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor()
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "planets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_planet_name",
                        columnNames = "name"
                )
        }
)
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(
            length = 32,
            nullable = false
    )
    private String name;

    @Column(nullable = false)
    private Double radius;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Double speed;

    @OneToOne(
            mappedBy = "planet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Atmosphere atmosphere;

    @OneToMany(
            mappedBy = "planet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Moon> moons = new ArrayList<>();

    @OneToOne(
            mappedBy = "planet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Ring ring;

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;

        if (atmosphere != null) {
            atmosphere.setPlanet(this);
        }
    }

    public void setMoons(List<Moon> moons) {
        this.moons = moons;

        if (moons != null) {
            moons.forEach((moon) -> {
                moon.setPlanet(this);
            });
        }
    }

    public void setRing(Ring ring) {
        this.ring = ring;

        if (ring != null) {
            ring.setPlanet(this);
        }
    }
}
