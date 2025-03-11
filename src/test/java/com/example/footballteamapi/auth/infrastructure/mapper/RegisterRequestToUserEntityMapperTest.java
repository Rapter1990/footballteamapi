package com.example.footballteamapi.auth.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.footballteamapi.auth.application.dto.request.RegisterRequest;
import com.example.footballteamapi.auth.domain.enums.UserType;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestToUserEntityMapperTest {

    private final RegisterRequestToUserEntityMapper mapper = RegisterRequestToUserEntityMapper.initialize();

    @Test
    void testMapRegisterRequestNull() {

        UserEntity result = mapper.map((RegisterRequest) null);

        assertNull(result);

    }

    @Test
    void testMapRegisterRequestCollectionNull() {

        List<UserEntity> result = mapper.map((Collection<RegisterRequest>) null);

        assertNull(result);

    }

    @Test
    void testMapRegisterRequestListEmpty() {

        List<UserEntity> result = mapper.map(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void testMapRegisterRequestListWithNullElements() {

        List<RegisterRequest> requests = Arrays.asList(
                RegisterRequest.builder()
                        .email("test1@example.com")
                        .password("password1")
                        .firstName("UserFirstName")
                        .lastName("UserLastName")
                        .phoneNumber("01234567890") // 11 characters minimum
                        .userType(UserType.ADMIN)
                        .build(),
                null
        );

        List<UserEntity> result = mapper.map(requests);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNull(result.get(1));

    }

    @Test
    void testMapSingleRegisterRequest() {

        RegisterRequest request = RegisterRequest.builder()
                .email("test1@example.com")
                .password("password1")
                .firstName("UserFirstName")
                .lastName("UserLastName")
                .phoneNumber("01234567890")
                .userType(UserType.ADMIN)
                .build();

        UserEntity result = mapper.map(request);

        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getPassword(), result.getPassword());
        assertEquals(request.getFirstName(), result.getFirstName());
        assertEquals(request.getLastName(), result.getLastName());
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(request.getUserType(), result.getUserType());

    }

}