package com.travelplanner.backend.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthEmailNotFoundException extends UsernameNotFoundException {
  public AuthEmailNotFoundException(String email) {
    super("Authentication failed: User with email '" + email + "' was not found.");
  }

}
