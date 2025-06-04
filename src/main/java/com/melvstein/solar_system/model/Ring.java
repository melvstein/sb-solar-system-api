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
@Table(name = "rings")
@ToString(exclude = "planet")
public class Ring implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
