package com.melvstein.solar_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "atmospheres")
public class Atmosphere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double radius;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Double opacity;

    @Column(nullable = false)
    private String emissive;

    @Column(nullable = false)
    private Double emissiveIntensity;

    @OneToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;
}
