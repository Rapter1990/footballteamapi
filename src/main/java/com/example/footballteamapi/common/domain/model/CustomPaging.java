package com.example.footballteamapi.common.domain.model;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CustomPaging {

    @Min(value = 1, message = "Page number must be bigger than 0")
    private Integer pageNumber;

    @Min(value = 1, message = "Page size must be bigger than 0")
    private Integer pageSize;

    public Integer getPageNumber() {
        return pageNumber - 1;
    }

}
