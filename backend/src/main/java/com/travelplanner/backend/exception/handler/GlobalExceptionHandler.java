package com.travelplanner.backend.exception.handler;

import com.travelplanner.backend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // =========================================================================
  // HTTP STATUS 400 - BAD REQUEST
  // =========================================================================

  @ExceptionHandler({
      InvalidTripDatesException.class,
      InvalidAccommodationDatesException.class
  })
  public ProblemDetail handleBadRequestException(RuntimeException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        ex.getMessage()
    );

    problemDetail.setTitle("Bad Request");
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        "One or more fields have invalid data."
    );

    problemDetail.setTitle("Validation Failure");

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error ->
            errors
                .put(error.getField(), error.getDefaultMessage())
        );

    problemDetail.setProperty("invalid_fields", errors);

    return problemDetail;
  }

  // =========================================================================
  // HTTP STATUS 401 - UNAUTHORIZED
  // =========================================================================

  @ExceptionHandler({
      BadCredentialsException.class,
      UsernameNotFoundException.class
  })
  public ProblemDetail handleAuthenticationException(RuntimeException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.UNAUTHORIZED,
        "Invalid email or password."
    );

    problemDetail.setTitle("Authentication Failure");

    return problemDetail;
  }

  // =========================================================================
  // HTTP STATUS 404 - NOT FOUND
  // =========================================================================

  @ExceptionHandler({
      UserNotFoundException.class,
      TripNotFoundException.class,
      AccommodationNotFoundException.class
  })
  public ProblemDetail handleResourceNotFoundException(RuntimeException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        ex.getMessage()
    );
    problemDetail.setTitle("Resource Not Found");
    return problemDetail;
  }

  // =========================================================================
  // HTTP STATUS 409 - CONFLICT
  // =========================================================================

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.CONFLICT,
        ex.getMessage()
    );
    problemDetail.setTitle("Data Conflict");
    return problemDetail;
  }

  // =========================================================================
  // HTTP STATUS 500 - INTERNAL SERVER ERROR
  // =========================================================================

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpectedException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred."
    );

    problemDetail.setTitle("Internal Server Error");

    return problemDetail;
  }

}
