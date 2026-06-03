package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.AccommodationRequest;
import com.travelplanner.backend.dto.AccommodationResponse;
import com.travelplanner.backend.entity.Accommodation;
import com.travelplanner.backend.entity.Trip;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.exception.AccommodationNotFoundException;
import com.travelplanner.backend.exception.InvalidAccommodationDatesException;
import com.travelplanner.backend.mapper.AccommodationMapper;
import com.travelplanner.backend.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;
  private final AccommodationMapper accommodationMapper;
  private final AuthService authService;
  private final TripService tripService;

  public AccommodationResponse create(Long tripId, AccommodationRequest request) {
    Trip trip = getValidatedTrip(tripId);

    validateAccommodationDates(request.checkInDate(), request.checkOutDate());

    Accommodation newAccommodation = accommodationMapper.toEntity(request);
    newAccommodation.setTrip(trip);
    Accommodation savedAccommodation = accommodationRepository.save(newAccommodation);

    return accommodationMapper.toResponse(savedAccommodation);
  }

  public List<AccommodationResponse> findAllByTrip(Long tripId) {
    Trip trip = getValidatedTrip(tripId);

    return accommodationRepository.findAllByTrip(trip)
        .stream()
        .map(accommodationMapper::toResponse)
        .toList();
  }

  public AccommodationResponse findById(Long tripId, Long id) {
    Trip trip = getValidatedTrip(tripId);
    Accommodation accommodation = findByIdAndTripOrThrow(id, trip);

    return accommodationMapper.toResponse(accommodation);
  }

  public AccommodationResponse update(Long tripId, Long id, AccommodationRequest request) {
    Trip trip = getValidatedTrip(tripId);

    Accommodation accommodation = findByIdAndTripOrThrow(id, trip);

    validateAccommodationDates(request.checkInDate(), request.checkOutDate());

    accommodationMapper.updateEntityFromRequest(accommodation, request);

    Accommodation savedAccommodation = accommodationRepository.save(accommodation);
    return accommodationMapper.toResponse(savedAccommodation);
  }

  public void delete(Long tripId, Long id) {
    Trip trip = getValidatedTrip(tripId);

    Accommodation accommodation = findByIdAndTripOrThrow(id, trip);

    accommodationRepository.delete(accommodation);
  }

  private Trip getValidatedTrip(Long tripId) {
    User loggedUser = authService.getAuthenticatedUser();
    return tripService.findByIdAndUserOrThrow(tripId, loggedUser);
  }

  private Accommodation findByIdAndTripOrThrow(Long id, Trip trip) {
    return accommodationRepository.findByIdAndTrip(id, trip)
        .orElseThrow(
            () -> new AccommodationNotFoundException(id)
        );
  }

  private void validateAccommodationDates(LocalDate checkIn, LocalDate checkOut) {
    if (checkIn != null && checkOut != null && checkOut.isBefore(checkIn)) {
      throw new InvalidAccommodationDatesException();
    }
  }

}
