package com.travelplanner.backend.dto;

public record LoginResponse(
    String accessToken,
    Long expiresIn
) {
}
