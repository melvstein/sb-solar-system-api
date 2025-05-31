package com.melvstein.solar_system.controller;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.mapper.mapstruct.UserMapper;
import com.melvstein.solar_system.model.User;
import com.melvstein.solar_system.repository.UserRepository;
import com.melvstein.solar_system.service.JwtService;
import com.melvstein.solar_system.service.UserService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import com.melvstein.solar_system.util.Utils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers() {
        try {
            List<UserDto> users = userService.getAll();

            log.info("{} - Users={}", Utils.currentMethod(), users);

            if (users == null) {
                return ResponseEntity.ok(ApiResponseUtils.success(null));
            }

            return ResponseEntity.ok(ApiResponseUtils.success(users));
        } catch (Exception e) {
            log.error("{} - error={}", Utils.currentMethod(), e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
