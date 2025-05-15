package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.dto.RingDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Moon;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.model.Ring;
import com.melvstein.solar_system.repository.PlanetRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {
    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private PlanetMapper planetMapper;

    @InjectMocks
    private PlanetService planetService;

    public Planet createPlanetEntity() {
        Atmosphere atmosphere = Atmosphere.builder()
                .id(1L)
                .radius(2.3)
                .color("#ffe0b2")
                .opacity(0.25)
                .emissive("#ffd180")
                .emissiveIntensity(0.5)
                .build();

        List<Moon> moons = List.of(
                Moon.builder()
                        .id(1L)
                        .name("Titan")
                        .radius(0.25)
                        .distance(3.5)
                        .speed(0.025)
                        .build(),
                Moon.builder()
                        .id(2L)
                        .name("Enceladus")
                        .radius(0.1)
                        .distance(2.8)
                        .speed(0.03)
                        .build()
        );

        Ring ring = Ring.builder()
                .id(1L)
                .color("#b1976b")
                .innerRadius(2.2)
                .outerRadius(3.5)
                .tilt(1.1047963267948965)
                .opacity(0.6)
                .build();

        return Planet.builder()
                .id(1L)
                .name("Saturn")
                .radius(2.0)
                .distance(65.0)
                .speed(0.005)
                .atmosphere(atmosphere)
                .moons(moons)
                .ring(ring)
                .build();
    }

    public PlanetDto createPlanetDto(Planet planet) {
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

    public List<Planet> createPlanetEntities() {
        Atmosphere atmosphere1 = Atmosphere.builder()
                .id(2L)
                .radius(2.3)
                .color("#ffe0b2")
                .opacity(0.25)
                .emissive("#ffd180")
                .emissiveIntensity(0.5)
                .build();

        List<Moon> moons1 = List.of(
                Moon.builder().id(2L).name("Titan1").radius(0.25).distance(3.5).speed(0.025).build(),
                Moon.builder().id(3L).name("Enceladus1").radius(0.1).distance(2.8).speed(0.03).build()
        );

        Ring ring1 = Ring.builder()
                .id(2L)
                .color("#b1976b")
                .innerRadius(2.2)
                .outerRadius(3.5)
                .tilt(1.1047963267948965)
                .opacity(0.6)
                .build();

        Planet planet1 = Planet.builder()
                .id(3L)
                .name("Saturn1")
                .radius(2.0)
                .distance(65.0)
                .speed(0.005)
                .atmosphere(atmosphere1)
                .moons(moons1)
                .ring(ring1)
                .build();

        Atmosphere atmosphere2 = Atmosphere.builder()
                .id(3L)
                .radius(2.1)
                .color("#ffe0b2")
                .opacity(0.2)
                .emissive("#ffd180")
                .emissiveIntensity(0.4)
                .build();

        List<Moon> moons2 = List.of(
                Moon.builder().id(4L).name("Europa").radius(0.22).distance(4.1).speed(0.02).build(),
                Moon.builder().id(5L).name("Io").radius(0.15).distance(3.9).speed(0.018).build()
        );

        Ring ring2 = Ring.builder()
                .id(3L)
                .color("#a0a0a0")
                .innerRadius(1.8)
                .outerRadius(3.2)
                .tilt(0.9)
                .opacity(0.5)
                .build();

        Planet planet2 = Planet.builder()
                .id(4L)
                .name("Jupiter1")
                .radius(2.2)
                .distance(60.0)
                .speed(0.006)
                .atmosphere(atmosphere2)
                .moons(moons2)
                .ring(ring2)
                .build();

        return List.of(planet1, planet2);
    }

    @Tag("supported")
    @Test
    public void shouldReturnAllPlanets_whenNoFilterIsApplied() {
        // Arrange
        Planet planet = createPlanetEntity();
        when(planetRepository.findAll(ArgumentMatchers.<Specification<Planet>>any())).thenReturn(List.of(planet));

        // Act
        List<PlanetDto> planets = planetService.getAll(null);

        // Assert
        assertNotNull(planets);
    }

    @Disabled("Not needed for now")
    @Test
    public void getAllWithPageable() {

    }

    @Tag("supported")
    @Test
    public void getById_shouldReturnPlanet_whenIdIsValid() {
        // Arrange
        Planet planet = createPlanetEntity();
        PlanetDto planetDto = createPlanetDto(planet);

        when(planetRepository.findById(1L)).thenReturn(Optional.of(planet));
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        Optional<PlanetDto> result = planetService.getById(1L);

        // Assert
        System.out.println(result);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Tag("supported")
    @Test
    public void save_shouldReturnPlanet_whenSaveSuccessfully() {
        // Arrange
        Planet planet = createPlanetEntity();
        PlanetDto planetDto = createPlanetDto(planet);

        when(planetRepository.save(planet)).thenReturn(planet);
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        PlanetDto savedPlanet = planetService.save(planet);

        // Assert
        assertNotNull(savedPlanet);
        assertEquals(planet.getName(), savedPlanet.name());
    }

    @Tag("supported")
    @Test
    public void saveAll_shouldReturnListOfPlanets_whenSaveSuccessfully() {
        // Arrange
        List<Planet> planets = createPlanetEntities();
        List<PlanetDto> planetDtos = planets.stream().map(this::createPlanetDto).toList();

        when(planetRepository.saveAll(planets)).thenReturn(planets);
        when(planetMapper.toDtos(planets)).thenReturn(planetDtos);

        // Action
        List<PlanetDto> savePlanets = planetService.saveAll(planets);

        // Assert
        assertFalse(savePlanets.isEmpty());
    }

    @Tag("supported")
    @Test
    public void deleteById() {
        // Arrange
        Planet planet = createPlanetEntity();
        PlanetDto planetDto = createPlanetDto(planet);

        when(planetRepository.save(planet)).thenReturn(planet);
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        PlanetDto savedPlanet = planetService.save(planet);

        // Action
        planetService.deleteById(1L);

        // Assert
    }

    @Disabled("Not needed for now")
    @Test
    public void getByName() {
    }

    @Disabled("Not needed for now")
    @Test
    public void existsById() {
    }
}