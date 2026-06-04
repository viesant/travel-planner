package com.travelplanner.backend.exception;

public class ActivityNotFoundException extends RuntimeException {
  public ActivityNotFoundException(Long id) {
    super("Activity with ID " + id + " was not found.");
  }

}
