package com.travelplanner.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActivityRequest(
    @NotBlank(message = "Activity name is required")
    String name,

    String location,

    String address,

    @NotNull(message = "Start date/time is required")
    LocalDateTime startDateTime,

    @NotNull(message = "End date/time is required")
    LocalDateTime endDateTime,

    String bookingNumber,

    BigDecimal price,

    String notes

) {
}
