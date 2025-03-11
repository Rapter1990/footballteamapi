package com.example.footballteamapi.common.application.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import com.example.footballteamapi.common.domain.model.CustomPaging;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

class CustomPagingRequestTest {

    @Test
    public void testToPageable_WithValidPageNumberAndPageSize() {
        // Given a CustomPaging with pageNumber=1 and pageSize=10.
        // getPageNumber() returns 1 - 1 = 0.
        CustomPaging paging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        CustomPagingRequest request = CustomPagingRequest.builder()
                .pagination(paging)
                .build();

        // When converting to Pageable
        Pageable pageable = request.toPageable();

        // Then the page number should be 0 and page size 10.
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    public void testToPageable_WithPageNumberGreaterThanOne() {
        // Given a CustomPaging with pageNumber=5 and pageSize=20.
        // getPageNumber() returns 5 - 1 = 4.
        CustomPaging paging = CustomPaging.builder()
                .pageNumber(5)
                .pageSize(20)
                .build();
        CustomPagingRequest request = CustomPagingRequest.builder()
                .pagination(paging)
                .build();

        Pageable pageable = request.toPageable();

        assertNotNull(pageable);
        assertEquals(4, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
    }

    @Test
    public void testToPageable_WithNullPagination_ShouldThrowException() {
        // Given a CustomPagingRequest with null pagination,
        // calling toPageable() should throw a NullPointerException.
        CustomPagingRequest request = CustomPagingRequest.builder()
                .pagination(null)
                .build();

        assertThrows(NullPointerException.class, request::toPageable);
    }

    @Test
    public void testCustomPagingGetPageNumberLogic() {
        // Even though validation should normally prevent invalid values,
        // test the getter logic directly.
        CustomPaging paging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(15)
                .build();
        // For pageNumber 1, getter returns 0.
        assertEquals(0, paging.getPageNumber());

        // For pageNumber 10, getter returns 9.
        paging.setPageNumber(10);
        assertEquals(9, paging.getPageNumber());
    }

}