package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.util.Utils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {
    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private PlanetMapper planetMapper;

    @InjectMocks
    private PlanetService planetService;

    @Tag("supported")
    @Test
    public void test_getAll_shouldReturnAllPlanets_whenNoFilterIsApplied() {
        // Arrange
        List<Planet> planets = Utils.createPlanetEntities();
        List<PlanetDto> PlanetDtos = planets.stream()
                .map(Utils::createPlanetDto)
                .toList();

        Specification<Planet> spec = Specification.where(null);
        when(planetRepository.findAll(spec)).thenReturn(planets);
        when(planetMapper.toDtos(planets)).thenReturn(PlanetDtos);

        // Act
        List<PlanetDto> result = planetService.getAll(null);

        System.out.println(result);

        // Assertion
        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(planetRepository, times(1)).findAll(ArgumentMatchers.<Specification<Planet>>any());
    }

    @Tag("supported")
    @Test
    public void test_getAllWithPageable_shouldReturnPageable_whenSuccess() {
        // Arrange
        List<Planet> planets = Utils.createPlanetEntities();
        List<PlanetDto> planetDtos = planets.stream()
                .map(Utils::createPlanetDto)
                .toList();

        Specification<Planet> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(0, 5);

        Page<Planet> planetPage = new PageImpl<>(planets, pageable, planets.size());

        when(planetRepository.findAll(spec, pageable)).thenReturn(planetPage);
        // Stub planetMapper.toDto for each planet individually
        for (int i = 0; i < planets.size(); i++) {
            when(planetMapper.toDto(planets.get(i))).thenReturn(planetDtos.get(i));
        }

        // Action
        Page<PlanetDto> result = planetService.getAllWithPageable(null, pageable);

        result.getContent().forEach(System.out::println);

        // Assertion
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.hasContent());

        verify(planetRepository, times(1)).findAll(spec, pageable);
    }

    @Tag("supported")
    @Test
    public void test_getById_shouldReturnPlanet_whenIdIsValid() {
        // Arrange
        Planet planet = Utils.createPlanetEntity();
        PlanetDto planetDto = Utils.createPlanetDto(planet);

        when(planetRepository.findById(planet.getId())).thenReturn(Optional.of(planet));
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        Optional<PlanetDto> result = planetService.getById(planet.getId());

        System.out.println(result);

        // Assertion
        System.out.println(result);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(planetRepository, times(1)).findById(planet.getId());
    }

    @Tag("supported")
    @Test
    public void test_save_shouldReturnPlanet_whenSaveSuccessfully() {
        // Arrange
        Planet planet = Utils.createPlanetEntity();
        PlanetDto planetDto = Utils.createPlanetDto(planet);

        when(planetRepository.save(planet)).thenReturn(planet);
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        PlanetDto result = planetService.save(planet);

        System.out.println(result);

        // Assertion
        assertNotNull(result);
        assertEquals(planet.getName(), result.name());

        verify(planetRepository, times(1)).save(planet);
    }

    @Tag("supported")
    @Test
    public void test_saveAll_shouldReturnListOfPlanets_whenSaveSuccessfully() {
        // Arrange
        List<Planet> planets = Utils.createPlanetEntities();
        List<PlanetDto> planetDtos = planets.stream()
                .map(Utils::createPlanetDto)
                .toList();

        when(planetRepository.saveAll(planets)).thenReturn(planets);
        when(planetMapper.toDtos(planets)).thenReturn(planetDtos);

        // Action
        List<PlanetDto> result = planetService.saveAll(planets);

        result.forEach(System.out::println);

        // Assertion
        assertFalse(result.isEmpty());

        verify(planetRepository, times(1)).saveAll(planets);
    }

    @Tag("supported")
    @Test
    public void test_deleteById_shouldDelete_whenRecordExists() {
        // Arrange
        // Planet planet = Utils.createPlanetEntity();
        // PlanetDto planetDto = Utils.createPlanetDto(planet);

        // Action
        planetService.deleteById(0L);

        verify(planetRepository, times(1)).deleteById(0L);
    }

    @Tag("supported")
    @Test
    public void test_getByName_shouldReturnPlanetByName_whenExists() {
        // Arrange
        Planet planet = Utils.createPlanetEntity();
        PlanetDto planetDto = Utils.createPlanetDto(planet);

        when(planetRepository.findByName(planet.getName())).thenReturn(planet);
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        PlanetDto result = planetService.getByName(planet.getName());

        System.out.println(result);

        // Assertion
        assertNotNull(result);
        assertEquals(planet.getName(), result.name());

        verify(planetRepository, times(1)).findByName(planet.getName());
    }

    @Tag("supported")
    @Test
    public void test_existsById_shouldReturnPlanetById_whenExists() {
        // Arrange
        Planet planet = Utils.createPlanetEntity();
        PlanetDto planetDto = Utils.createPlanetDto(planet);

        when(planetRepository.findById(planet.getId())).thenReturn(Optional.of(planet));
        when(planetMapper.toDto(planet)).thenReturn(planetDto);

        // Action
        Optional<PlanetDto> result = planetService.getById(planet.getId());

        System.out.println(result);

        // Assertion
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(planet.getName(), result.get().name());
    }
}