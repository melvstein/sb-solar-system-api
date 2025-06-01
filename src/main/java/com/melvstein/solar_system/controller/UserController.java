package com.melvstein.solar_system.controller;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.service.UserService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import com.melvstein.solar_system.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('admin, 'user')")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        try {
            boolean exists = userService.existsById(id);

            if (!exists) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "User not found", null));
            }

            log.info("{} - id={}", Utils.currentMethod(), id);

            userService.deleteById(id);

            return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Deleted Successfully", null));
        } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
