package com.travelplanner.backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TripRequest(
    @NotBlank(message = "Title is required")
    String title,

    String description,

    LocalDate startDate,
    LocalDate endDate

) {
}
