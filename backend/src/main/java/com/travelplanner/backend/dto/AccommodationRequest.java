package com.travelplanner.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccommodationRequest(
    @NotBlank(message = "Name is required")
    String name,

    String location,

    String address,

    String bookingNumber,

    @NotNull(message = "Check-in date is required")
    LocalDate checkInDate,

    @NotNull(message = "Check-out date is required")
    LocalDate checkOutDate,

    BigDecimal price,

    String notes
) {
}
