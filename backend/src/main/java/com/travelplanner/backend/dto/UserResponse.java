package com.travelplanner.backend.dto;

public record UserResponse(
    Long id,
    String name,
    String email
) {
}
