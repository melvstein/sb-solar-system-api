package com.melvstein.solar_system.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "planets")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private Double radius;
    private Double distance;
    private Double speed;
}
