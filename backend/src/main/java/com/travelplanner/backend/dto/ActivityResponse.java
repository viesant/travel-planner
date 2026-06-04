package com.travelplanner.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActivityResponse(
    Long id,
    String name,
    String location,
    String address,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    String bookingNumber,
    BigDecimal price,
    String notes,
    Long tripId
) {
}
