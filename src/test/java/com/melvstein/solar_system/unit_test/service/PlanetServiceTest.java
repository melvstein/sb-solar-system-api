package com.melvstein.solar_system.unit_test.service;

import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @Mock
    private PlanetRepository planetRepository;

    @InjectMocks
    private PlanetService planetService;

    @Test
    public void testGetPlanet() {
    }
}
