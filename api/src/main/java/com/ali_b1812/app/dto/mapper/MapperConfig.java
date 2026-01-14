package com.ali_b1812.app.dto.mapper;

import org.mapstruct.*;

@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface MapperConfig {
}