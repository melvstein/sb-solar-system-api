package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.mapper.mapstruct.AtmosphereMapper;
import com.melvstein.solar_system.repository.AtmosphereRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtmosphereService {
    private final AtmosphereRepository atmosphereRepository;
    private final AtmosphereMapper atmosphereMapper;

    public AtmosphereService(AtmosphereRepository atmosphereRepository, AtmosphereMapper atmosphereMapper) {
        this.atmosphereRepository = atmosphereRepository;
        this.atmosphereMapper = atmosphereMapper;
    }

    public Optional<AtmosphereDto> getAtmosphereById(Long id) {
        return atmosphereRepository.findById(id).map(atmosphereMapper::toDto);
    }
}
