package com.travelplanner.backend.exception;

public class InvalidAccommodationDatesException extends RuntimeException {
  public InvalidAccommodationDatesException() {
    super("Check-out date cannot be before check-in date.");
  }

}
