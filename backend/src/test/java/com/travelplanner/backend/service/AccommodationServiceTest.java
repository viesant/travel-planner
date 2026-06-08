package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.AccommodationRequest;
import com.travelplanner.backend.dto.AccommodationResponse;
import com.travelplanner.backend.entity.Accommodation;
import com.travelplanner.backend.entity.Trip;
import com.travelplanner.backend.exception.InvalidDatesException;
import com.travelplanner.backend.exception.ResourceNotFoundException;
import com.travelplanner.backend.mapper.AccommodationMapper;
import com.travelplanner.backend.repository.AccommodationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccommodationServiceTest {

  private final Long tripId = 1L;
  private final Long accommodationId = 10L;
  private Trip trip;
  private Accommodation accommodation;
  private AccommodationRequest validRequest;
  private AccommodationResponse expectedResponse;

  @Mock
  private AccommodationRepository accommodationRepository;
  @Mock
  private AccommodationMapper accommodationMapper;
  @Mock
  private TripService tripService;
  @InjectMocks
  private AccommodationService accommodationService;

  @BeforeEach
  void setUp() {
    trip = new Trip();
    accommodation = new Accommodation();

    validRequest = new AccommodationRequest(
        "Hotel Paris",
        "Paris",
        "Address Lalala, 123",
        "BOOK12345678",
        LocalDate.of(2026, 8, 1),
        LocalDate.of(2026, 8, 15),
        BigDecimal.valueOf(100),
        "notes lalalalalalalalalalalala"
    );

    expectedResponse = new AccommodationResponse(
        accommodationId, "Hotel Paris",
        "Paris",
        "Address Lalala, 123",
        "BOOK12345678",
        LocalDate.of(2026, 8, 1),
        LocalDate.of(2026, 8, 15),
        BigDecimal.valueOf(100),
        "notes lalalalalalalalalalalala",
        tripId
    );
  }

  @Test
  void create_WhenDataIsValid_ShouldReturnAccommodation() {

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationMapper.toEntity(validRequest)).thenReturn(accommodation);
    when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
    when(accommodationMapper.toResponse(accommodation)).thenReturn(expectedResponse);

    AccommodationResponse result = accommodationService.create(tripId, validRequest);

    assertNotNull(result);
    assertEquals(accommodationId, result.id());
    assertEquals("Hotel Paris", result.name());
  }

  @Test
  void create_WhenCheckoutBeforeCheckin_ShouldThrowException() {

    AccommodationRequest invalidRequest = createInvalidDatesRequest();

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);

    InvalidDatesException exception = assertThrows(
        InvalidDatesException.class, () -> accommodationService.create(tripId, invalidRequest));

    assertEquals("Check-out date cannot be before check-in date.", exception.getMessage());

    verify(accommodationRepository, never()).save(any());
  }

  @Test
  void update_WhenDataIsValid_ShouldReturnUpdatedAccommodation() {

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(accommodationId, trip)).thenReturn(Optional.of(accommodation));
    when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
    when(accommodationMapper.toResponse(accommodation)).thenReturn(expectedResponse);

    AccommodationResponse result = accommodationService.update(tripId, accommodationId, validRequest);

    assertNotNull(result);
    assertEquals(accommodationId, result.id());
    assertEquals("Hotel Paris", result.name());
    verify(accommodationMapper).updateEntityFromRequest(accommodation, validRequest);
  }

  @Test
  void update_WhenCheckoutBeforeCheckin_ShouldThrowException() {

    AccommodationRequest invalidRequest = createInvalidDatesRequest();

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(accommodationId, trip)).thenReturn(Optional.of(accommodation));

    InvalidDatesException exception = assertThrows(
        InvalidDatesException.class, () -> accommodationService.update(tripId, accommodationId, invalidRequest));

    assertEquals("Check-out date cannot be before check-in date.", exception.getMessage());
    verify(accommodationRepository, never()).save(any());
    verify(accommodationMapper, never()).toResponse(any());
  }

  @Test
  void findById_WhenAccommodationExists_ShouldReturnAccommodation() {
    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(accommodationId, trip)).thenReturn(Optional.of(accommodation));
    when(accommodationMapper.toResponse(accommodation)).thenReturn(expectedResponse);

    AccommodationResponse result = accommodationService.findById(tripId, accommodationId);

    assertNotNull(result);
    assertEquals(accommodationId, result.id());
    verify(accommodationRepository).findByIdAndTrip(accommodationId, trip);
  }

  @Test
  void findById_WhenAccommodationDoesNotExist_ShouldThrowException() {
    Long invalidId = 99L;
    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(invalidId, trip)).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> accommodationService.findById(tripId, invalidId));

    assertEquals("Accommodation not found with id: " + invalidId, exception.getMessage());
  }

  @Test
  void delete_WhenAccommodationExists_ShouldDeleteAccommodation() {

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(accommodationId, trip)).thenReturn(Optional.of(accommodation));

    accommodationService.delete(tripId, accommodationId);

    verify(accommodationRepository).delete(accommodation);
  }

  @Test
  void delete_WhenAccommodationDoesNotExist_ShouldThrowException() {

    Long invalidId = 99L;

    when(tripService.getValidatedTrip(tripId)).thenReturn(trip);
    when(accommodationRepository.findByIdAndTrip(invalidId, trip))
        .thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () ->
        accommodationService.delete(tripId, invalidId)
    );

    verify(accommodationRepository, never()).delete(any());
  }

  private AccommodationRequest createInvalidDatesRequest() {
    return new AccommodationRequest(
        "Hotel Paris", "Paris", "Address Lalala, 123", "BOOK12345678",
        LocalDate.of(2026, 7, 12), LocalDate.of(2026, 7, 10), // Invertidas
        BigDecimal.valueOf(100), "notes"
    );
  }

}
