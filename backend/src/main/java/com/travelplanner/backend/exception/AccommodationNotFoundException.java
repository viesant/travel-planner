package com.travelplanner.backend.exception;

public class AccommodationNotFoundException extends RuntimeException {
  public AccommodationNotFoundException(Long id) {
    super("Accommodation with ID " + id + " was not found.");
  }

}
