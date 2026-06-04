package com.travelplanner.backend.exception;

public class InvalidActivityDatesException extends RuntimeException {
  public InvalidActivityDatesException() {
    super("End date/time cannot be before start date/time");
  }

}
