package com.melvstein.solar_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-h2")
class SolarSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
