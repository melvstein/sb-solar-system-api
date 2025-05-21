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
@Table(name = "atmospheres")
@ToString(exclude = "planet")
public class Atmosphere implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
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

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;
}
