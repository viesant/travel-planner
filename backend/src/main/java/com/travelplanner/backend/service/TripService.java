package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.TripRequest;
import com.travelplanner.backend.dto.TripResponse;
import com.travelplanner.backend.entity.Trip;
import com.travelplanner.backend.entity.User;
import com.travelplanner.backend.exception.InvalidTripDatesException;
import com.travelplanner.backend.exception.TripNotFoundException;
import com.travelplanner.backend.mapper.TripMapper;
import com.travelplanner.backend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

  private final TripRepository tripRepository;
  private final AuthService authService;
  private final TripMapper tripMapper;

  public TripResponse create(TripRequest request) {
    User loggedUser = authService.getAuthenticatedUser();

    validateTripDates(request.startDate(), request.endDate());

    Trip newTrip = tripMapper.toEntity(request);

    newTrip.setUser(loggedUser);

    Trip savedTrip = tripRepository.save(newTrip);

    return tripMapper.toResponse(savedTrip);
  }

  public List<TripResponse> findAllByUser() {
    User loggedUser = authService.getAuthenticatedUser();

    return tripRepository.findAllByUser(loggedUser)
        .stream()
        .map(tripMapper::toResponse)
        .toList();
  }

  public TripResponse findById(Long id) {
    Trip trip = getValidatedTrip(id);

    return tripMapper.toResponse(trip);
  }

  public TripResponse update(Long id, TripRequest request) {
    Trip trip = getValidatedTrip(id);

    validateTripDates(request.startDate(), request.endDate());

    trip.setTitle(request.title());
    trip.setDescription(request.description());
    trip.setStartDate(request.startDate());
    trip.setEndDate(request.endDate());

    Trip savedTrip = tripRepository.save(trip);
    return tripMapper.toResponse(savedTrip);
  }

  public void delete(Long id) {

    Trip trip = getValidatedTrip(id);
    tripRepository.delete(trip);
  }

  private Trip findByIdAndUserOrThrow(Long id, User user) {
    return tripRepository.findByIdAndUser(id, user)
        .orElseThrow(
            () -> new TripNotFoundException(id)
        );
  }

  private void validateTripDates(LocalDate start, LocalDate end) {
    if (start != null && end != null && end.isBefore(start)) {
      throw new InvalidTripDatesException();
    }
  }

  protected Trip getValidatedTrip(Long tripId) {
    User loggedUser = authService.getAuthenticatedUser();
    return findByIdAndUserOrThrow(tripId, loggedUser);
  }

}
