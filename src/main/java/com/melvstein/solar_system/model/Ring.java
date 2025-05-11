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
@Table(name = "rings")
public class Ring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    private String color;
    private Double innerRadius;
    private Double outerRadius;
    private Double tilt;
    private Double opacity;

    @OneToOne()
    @JoinColumn(name = "planet_id")
    @JsonBackReference
    private Planet planet;
}
