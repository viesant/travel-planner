package com.travelplanner.backend.dto;

import com.travelplanner.backend.enums.TransportType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportResponse(
    Long id,
    TransportType type,
    String carrier,
    String departureLocation,
    String departureAddress,
    String arrivalLocation,
    String arrivalAddress,
    LocalDateTime departureDateTime,
    LocalDateTime arrivalDateTime,
    String bookingNumber,
    BigDecimal price,
    String notes,
    Long tripId
) {
}
