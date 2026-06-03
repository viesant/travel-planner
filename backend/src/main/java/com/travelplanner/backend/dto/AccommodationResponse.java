package com.travelplanner.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccommodationResponse(
    Long id,
    String name,
    String location,
    String address,
    String bookingNumber,
    LocalDate checkInDate,
    LocalDate checkOutDate,
    BigDecimal price,
    String notes,
    Long tripId
) {
}
