package com.travelplanner.backend.exception;

public class InvalidTransportDatesException extends RuntimeException {
  public InvalidTransportDatesException() {
    super("Arrivel date/time cannot be before departure date/time");
  }

}
