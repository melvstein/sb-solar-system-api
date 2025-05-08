package com.melvstein.solar_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "moons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moon_name",
                        columnNames = "name"
                )
        }
)
@Entity
public class Moon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;
}
