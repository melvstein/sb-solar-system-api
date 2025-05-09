package com.melvstein.solar_system.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
}
