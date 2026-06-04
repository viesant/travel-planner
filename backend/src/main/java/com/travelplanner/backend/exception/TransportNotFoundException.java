package com.travelplanner.backend.exception;

public class TransportNotFoundException extends RuntimeException {
  public TransportNotFoundException(Long id) {
    super("Transport with ID " + id + " was not found.");
  }

}
