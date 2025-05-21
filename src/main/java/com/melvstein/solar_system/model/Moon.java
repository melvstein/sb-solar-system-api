package com.melvstein.solar_system.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "moons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moon_name",
                        columnNames = "name"
                )
        }
)
@ToString(exclude = "planet")
public class Moon implements Serializable {
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;
}
