package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.mapper.mapstruct.UserMapper;
import com.melvstein.solar_system.model.User;
import com.melvstein.solar_system.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtos(users);
    }

    public UserDto register(UserDto user) {
        User toRegisterUser = userMapper.toEntity(user);

        toRegisterUser.setRole(toRegisterUser.getRole().toLowerCase(Locale.ROOT));
        toRegisterUser.setPassword(encoder.encode(toRegisterUser.getPassword()));

        User registeredUser = userRepository.save(toRegisterUser);

        return userMapper.toDto(registeredUser);
    }

    public boolean isAuthenticated(UserDto user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        return authentication.isAuthenticated();
    }
}
