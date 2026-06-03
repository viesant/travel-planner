package com.travelplanner.backend.dto;

import com.travelplanner.backend.enums.TransportType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportRequest(

    @NotBlank(message = "Transport type is required")
    TransportType type,

    String carrier,

    @NotBlank(message = "Departure location is required")
    String departureLocation,
    String departureAddress,

    @NotBlank(message = "Arrival location is required")
    String arrivalLocation,
    String arrivalAddress,

    @NotBlank(message = "Departure date/time is required")
    LocalDateTime departureDateTime,
    @NotBlank(message = "Arrival date/time is required")
    LocalDateTime arrivalDateTime,

    String bookingNumber,

    BigDecimal price,

    String notes

) {
}
