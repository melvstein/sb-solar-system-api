package com.melvstein.solar_system.controller;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.dto.builder.auth.LoginRequest;
import com.melvstein.solar_system.mapper.mapstruct.UserMapper;
import com.melvstein.solar_system.model.User;
import com.melvstein.solar_system.repository.UserRepository;
import com.melvstein.solar_system.service.JwtService;
import com.melvstein.solar_system.service.UserService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import com.melvstein.solar_system.util.Utils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserRepository userRepository, UserMapper userMapper, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        UserDto userDto = userMapper.toDto(user);

        if (userService.isAuthenticated(userDto)) {
            String jwtToken = jwtService.generateToken(request.username(), null);

            Map<String, String> data = new HashMap<>();
            data.put("jwtToken", jwtToken);

            return ResponseEntity.ok(ApiResponseUtils.success(data));
        }

        return ResponseEntity.internalServerError().body(ApiResponseUtils.error("Invalid credentials!"));
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody UserDto request) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(request.username());

            if (existingUser.isPresent()) {
                log.info("{} existingUser={}", Utils.currentMethod(), existingUser);
                return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "User already exists", userMapper.toDto(existingUser.get())));
            }

            UserDto user = userService.register(request);

            log.info("{} User={}", Utils.currentMethod(), user);

            if (user == null) {
                return ResponseEntity.ok(ApiResponseUtils.success(null));
            }

            return ResponseEntity.ok(ApiResponseUtils.success(user));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
