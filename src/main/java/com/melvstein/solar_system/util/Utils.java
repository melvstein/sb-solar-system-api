package com.melvstein.solar_system.util;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.dto.RingDto;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Moon;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.model.Ring;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static String currentMethod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // [0] is getStackTrace, [1] is currentMethod, [2] is the caller
        if (stackTrace.length > 2) {
            StackTraceElement element = stackTrace[2];
            return element.getClassName() + "::" + element.getMethodName();
        }

        return "UnknownMethod";
    }

    public static Planet createPlanetEntity() {
        Atmosphere atmosphere = Atmosphere.builder()
                .radius(2.3)
                .color("#ffe0b2")
                .opacity(0.25)
                .emissive("#ffd180")
                .emissiveIntensity(0.5)
                .build();

        List<Moon> moons = List.of(
                Moon.builder()
                        .name("Titan")
                        .radius(0.25)
                        .distance(3.5)
                        .speed(0.025)
                        .build(),
                Moon.builder()
                        .name("Enceladus")
                        .radius(0.1)
                        .distance(2.8)
                        .speed(0.03)
                        .build()
        );

        Ring ring = Ring.builder()
                .color("#b1976b")
                .innerRadius(2.2)
                .outerRadius(3.5)
                .tilt(1.1047963267948965)
                .opacity(0.6)
                .build();

        Planet planet = Planet.builder()
                .name("Saturn")
                .radius(2.0)
                .distance(65.0)
                .speed(0.5)
                .atmosphere(atmosphere)
                .moons(moons)
                .ring(ring)
                .build();

        atmosphere.setPlanet(planet);
        moons.forEach(moon -> moon.setPlanet(planet));
        ring.setPlanet(planet);

        return planet;
    }

    public static PlanetDto createPlanetDto(Planet planet) {
        AtmosphereDto atmosphereDto = new AtmosphereDto(
                planet.getAtmosphere().getId(),
                planet.getAtmosphere().getRadius(),
                planet.getAtmosphere().getColor(),
                planet.getAtmosphere().getOpacity(),
                planet.getAtmosphere().getEmissive(),
                planet.getAtmosphere().getEmissiveIntensity()
        );

        List<MoonDto> moons = planet.getMoons().stream().map((moon) -> {
            return new MoonDto(
                    moon.getId(),
                    moon.getName(),
                    moon.getRadius(),
                    moon.getDistance(),
                    moon.getSpeed()
            );
        }).toList();

        RingDto ringDto = new RingDto(
                planet.getRing().getId(),
                planet.getRing().getColor(),
                planet.getRing().getInnerRadius(),
                planet.getRing().getOuterRadius(),
                planet.getRing().getTilt(),
                planet.getRing().getOpacity()
        );

        return new PlanetDto(
                planet.getId(),
                planet.getName(),
                planet.getRadius(),
                planet.getDistance(),
                planet.getSpeed(),
                atmosphereDto,
                moons,
                ringDto
        );
    }

    public static List<Planet> createPlanetEntities() {
        Atmosphere atmosphere1 = Atmosphere.builder()
                .radius(2.3)
                .color("#ffe0b2")
                .opacity(0.25)
                .emissive("#ffd180")
                .emissiveIntensity(0.5)
                .build();

        List<Moon> moons1 = List.of(
                Moon.builder().name("Titan1").radius(0.25).distance(3.5).speed(0.025).build(),
                Moon.builder().name("Enceladus1").radius(0.1).distance(2.8).speed(0.03).build()
        );

        Ring ring1 = Ring.builder()
                .color("#b1976b")
                .innerRadius(2.2)
                .outerRadius(3.5)
                .tilt(1.1047963267948965)
                .opacity(0.6)
                .build();

        Planet planet1 = Planet.builder()
                .name("Saturn1")
                .radius(2.0)
                .distance(65.0)
                .speed(0.5)
                .atmosphere(atmosphere1)
                .moons(moons1)
                .ring(ring1)
                .build();

        atmosphere1.setPlanet(planet1);
        moons1.forEach(moon -> moon.setPlanet(planet1));
        ring1.setPlanet(planet1);

        Atmosphere atmosphere2 = Atmosphere.builder()
                .radius(2.1)
                .color("#ffe0b2")
                .opacity(0.2)
                .emissive("#ffd180")
                .emissiveIntensity(0.4)
                .build();

        List<Moon> moons2 = List.of(
                Moon.builder().name("Europa").radius(0.22).distance(4.1).speed(0.02).build(),
                Moon.builder().name("Io").radius(0.15).distance(3.9).speed(0.018).build()
        );

        Ring ring2 = Ring.builder()
                .color("#a0a0a0")
                .innerRadius(1.8)
                .outerRadius(3.2)
                .tilt(0.9)
                .opacity(0.5)
                .build();

        Planet planet2 = Planet.builder()
                .name("Jupiter1")
                .radius(2.2)
                .distance(60.0)
                .speed(0.006)
                .atmosphere(atmosphere2)
                .moons(moons2)
                .ring(ring2)
                .build();

        atmosphere2.setPlanet(planet2);
        moons2.forEach(moon -> moon.setPlanet(planet2));
        ring2.setPlanet(planet2);

        return List.of(planet1, planet2);
    }

    public static String generateCacheKeyFromParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "noParams";
        }

        return params.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("-"));
    }
}
