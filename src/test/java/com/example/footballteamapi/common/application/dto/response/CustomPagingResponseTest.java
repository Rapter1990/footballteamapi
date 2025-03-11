package com.example.footballteamapi.common.application.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import com.example.footballteamapi.common.domain.model.CustomPage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPagingResponseTest {

    @Test
    void testOfMethodWithContent() {

        // Given
        CustomPage<String> dummyPage = CustomPage.<String>builder()
                .pageNumber(3)               // Expected response page number: 3
                .pageSize(10)                // Expected response page size: 10
                .totalElementCount(50L)      // Expected total element count: 50
                .totalPageCount(5)           // Expected total page count: 5
                .content(Arrays.asList("a", "b", "c"))
                .build();

        // When
        CustomPagingResponse<String> response = CustomPagingResponse.<String>builder()
                .of(dummyPage)
                .content(dummyPage.getContent())
                .build();

        // Then
        assertEquals(3, response.getPageNumber(), "Page number should match dummyPage value.");
        assertEquals(10, response.getPageSize(), "Page size should match dummyPage value.");
        assertEquals(50L, response.getTotalElementCount(), "Total element count should match dummyPage value.");
        assertEquals(5, response.getTotalPageCount(), "Total page count should match dummyPage value.");
        assertEquals(Arrays.asList("a", "b", "c"), response.getContent(), "Content should match dummyPage content.");

    }

    @Test
    void testOfMethodWithEmptyContent() {

        // Given
        CustomPage<String> dummyPage = CustomPage.<String>builder()
                .pageNumber(1)
                .pageSize(20)
                .totalElementCount(0L)
                .totalPageCount(0)
                .content(List.of())
                .build();

        // When
        CustomPagingResponse<String> response = CustomPagingResponse.<String>builder()
                .of(dummyPage)
                .content(dummyPage.getContent())
                .build();

        // Then
        assertEquals(1, response.getPageNumber(), "Page number should match dummyPage value.");
        assertEquals(20, response.getPageSize(), "Page size should match dummyPage value.");
        assertEquals(0L, response.getTotalElementCount(), "Total element count should be 0.");
        assertEquals(0, response.getTotalPageCount(), "Total page count should be 0.");
        assertTrue(response.getContent().isEmpty(), "Content should be empty.");

    }

}