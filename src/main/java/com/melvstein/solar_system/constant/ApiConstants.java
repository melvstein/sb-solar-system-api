package com.melvstein.solar_system.constant;

import java.util.Map;

public class ApiConstants {
    public static final Map<String, String> RESPONSE_SUCCESS = Map.of(
            "code", "0",
            "message", "Success"
    );

    public static final Map<String, String> RESPONSE_ERROR = Map.of(
            "code", "1",
            "message", "Error"
    );

    private ApiConstants() {}
}
