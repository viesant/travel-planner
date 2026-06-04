package com.travelplanner.backend.dto;

import com.travelplanner.backend.enums.TransportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportRequest(

    @NotNull(message = "Transport type is required")
    TransportType type,

    String carrier,

    @NotBlank(message = "Departure location is required")
    String departureLocation,
    String departureAddress,

    @NotBlank(message = "Arrival location is required")
    String arrivalLocation,
    String arrivalAddress,

    @NotNull(message = "Departure date/time is required")
    LocalDateTime departureDateTime,
    @NotNull(message = "Arrival date/time is required")
    LocalDateTime arrivalDateTime,

    String bookingNumber,

    BigDecimal price,

    String notes

) {
}
