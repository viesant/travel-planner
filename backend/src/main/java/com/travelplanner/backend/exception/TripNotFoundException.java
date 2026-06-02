package com.travelplanner.backend.exception;

public class TripNotFoundException extends RuntimeException {
  public TripNotFoundException(Long id) {
    super("Trip with ID " + id + " was not found.");

  }

}
