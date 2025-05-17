package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.RingDto;
import com.melvstein.solar_system.model.Ring;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RingMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "innerRadius", target = "innerRadius"),
            @Mapping(source = "outerRadius", target = "outerRadius"),
            @Mapping(source = "tilt", target = "tilt"),
            @Mapping(source = "opacity", target = "opacity"),
    })
    RingDto toDto(Ring ring);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "innerRadius", target = "innerRadius"),
            @Mapping(source = "outerRadius", target = "outerRadius"),
            @Mapping(source = "tilt", target = "tilt"),
            @Mapping(source = "opacity", target = "opacity"),
            @Mapping(target = "planet", ignore = true),
    })
    Ring toEntity(RingDto dto);
}
