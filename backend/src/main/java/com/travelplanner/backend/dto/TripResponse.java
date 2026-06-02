package com.travelplanner.backend.dto;

import java.time.LocalDate;

public record TripResponse(
    Long id,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate
) {
}
