package com.example.footballteamapi.logging.application.service.impl;

import com.example.footballteamapi.base.AbstractBaseServiceTest;
import com.example.footballteamapi.logging.domain.model.LogEntity;
import com.example.footballteamapi.logging.infrastructure.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LogServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LogServiceImpl logService;

    @Mock
    private LogRepository logRepository;

    @Test
    public void testSaveLogToDatabase() {

        // Given
        LogEntity logEntity = LogEntity.builder()
                .message("Test message")
                .endpoint("/test-endpoint")
                .method("GET")
                .status(HttpStatus.OK)
                .userInfo("user123")
                .errorType("None")
                .response("Test response")
                .operation("CREATE")
                .build();

        // When
        when(logRepository.save(any(LogEntity.class))).thenReturn(logEntity);

        // Then
        logService.saveLogToDatabase(logEntity);

        // Assert that the time field is set by the service
        assertNotNull(logEntity.getTime(), "The log time should be set");

        // Verify
        verify(logRepository, times(1)).save(logEntity);

    }


}