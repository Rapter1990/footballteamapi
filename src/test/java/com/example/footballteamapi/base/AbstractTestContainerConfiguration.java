package com.example.footballteamapi.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
public abstract class AbstractTestContainerConfiguration {

    private static final String POSTGRES_IMAGE = "postgres:15-alpine";

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
            .withDatabaseName("footballteamdatabase")
            .withUsername("postgres")
            .withPassword("111111");

    @BeforeAll
    static void startContainer() {
        log.info("Starting PostgreSQL Testcontainer...");
        POSTGRES_CONTAINER.start();
        log.info("PostgreSQL Testcontainer started on port: {}", POSTGRES_CONTAINER.getMappedPort(5432));
    }

    @AfterAll
    static void stopContainer() {
        log.info("Stopping PostgreSQL Testcontainer...");
        POSTGRES_CONTAINER.stop();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // Dynamically override Spring Boot properties to use Testcontainers PostgreSQL
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

}
