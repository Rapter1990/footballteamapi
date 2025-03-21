package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.dto.request.RegisterRequest;
import com.example.footballteamapi.auth.application.port.out.UserRepository;
import com.example.footballteamapi.auth.domain.enums.UserType;
import com.example.footballteamapi.auth.domain.exception.UserAlreadyExistException;
import com.example.footballteamapi.auth.domain.model.User;
import com.example.footballteamapi.auth.infrastructure.mapper.RegisterRequestToUserEntityMapper;
import com.example.footballteamapi.auth.infrastructure.mapper.UserEntityToUserMapper;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import com.example.footballteamapi.base.AbstractBaseServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link RegisterServiceImpl}.
 * This test class verifies the functionality of the registration service,
 * which handles user registration, including password encoding and saving user data.
 * It mocks the {@link UserRepository} and {@link PasswordEncoder} to test the registration logic
 * independently of external systems.
 */
class RegisterServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper =
            RegisterRequestToUserEntityMapper.initialize();

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    @Test
    void givenAdminRegisterRequest_whenRegisterAdmin_thenReturnAdmin() {

        // Given
        final RegisterRequest request = RegisterRequest.builder()
                .email("usertest@example.com")
                .password("password123")
                .firstName("User FirstName")
                .lastName("User LastName")
                .userType(UserType.USER)
                .phoneNumber("1234567890")
                .build();

        final String encodedPassword = "encodedPassword";

        final UserEntity userEntity = registerRequestToUserEntityMapper.mapForSaving(request);

        final User expected = userEntityToUserMapper.map(userEntity);

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Then
        User result = registerService.registerUser(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());

        // Verify
        verify(userRepository).save(any(UserEntity.class));

    }

    @Test
    void givenAdminRegisterRequest_whenEmailAlreadyExists_thenThrowAdminAlreadyExistException() {

        // Given
        final RegisterRequest request = RegisterRequest.builder()
                .email("usertest@example.com")
                .password("password123")
                .firstName("User FirstName")
                .lastName("User LastName")
                .userType(UserType.USER)
                .phoneNumber("1234567890")
                .build();

        // When
        when(userRepository.existsUserEntityByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(UserAlreadyExistException.class, () -> registerService.registerUser(request));

        // Verify
        verify(userRepository, never()).save(any(UserEntity.class));

    }

}