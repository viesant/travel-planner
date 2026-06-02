package com.travelplanner.backend.exception;

public class InvalidTripDatesException extends RuntimeException {
  public InvalidTripDatesException() {
    super("End date cannot be before start date.");
  }

}
