package com.melvstein.solar_system.seed;

import com.melvstein.solar_system.model.Role;
import com.melvstein.solar_system.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                    .role("admin")
                    .build();

            Role userRole = Role.builder()
                    .role("user")
                    .build();

            List<Role> roles = List.of(adminRole, userRole);

            roleRepository.saveAll(roles);
        }
    }
}
