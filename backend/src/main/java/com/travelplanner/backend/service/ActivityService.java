package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.ActivityRequest;
import com.travelplanner.backend.dto.ActivityResponse;
import com.travelplanner.backend.entity.Activity;
import com.travelplanner.backend.entity.Trip;
import com.travelplanner.backend.exception.ActivityNotFoundException;
import com.travelplanner.backend.exception.InvalidActivityDatesException;
import com.travelplanner.backend.mapper.ActivityMapper;
import com.travelplanner.backend.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;
  private final ActivityMapper activityMapper;
  private final TripService tripService;

  public ActivityResponse create(Long tripId, ActivityRequest request) {
    Trip trip = tripService.getValidatedTrip(tripId);
    validateActivityDates(request.startDateTime(), request.endDateTime());

    Activity newActivity = activityMapper.toEntity(request);
    newActivity.setTrip(trip);
    Activity savedActivity = activityRepository.save(newActivity);
    return activityMapper.toResponse(savedActivity);
  }

  public List<ActivityResponse> findAllByTrip(Long tripId) {
    Trip trip = tripService.getValidatedTrip(tripId);

    return activityRepository.findAllByTrip(trip)
        .stream()
        .map(activityMapper::toResponse)
        .toList();
  }

  public ActivityResponse findById(Long tripId, Long id) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Activity activity = findByIdAndTripOrThrow(id, trip);

    return activityMapper.toResponse(activity);
  }

  public ActivityResponse update(Long tripId, Long id, ActivityRequest request) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Activity activity = findByIdAndTripOrThrow(id, trip);

    validateActivityDates(request.startDateTime(), request.endDateTime());

    activityMapper.updateEntityFromRequest(activity, request);

    Activity savedActivity = activityRepository.save(activity);
    return activityMapper.toResponse(savedActivity);
  }

  public void delete(Long tripId, Long id) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Activity activity = findByIdAndTripOrThrow(id, trip);

    activityRepository.delete(activity);
  }

  private Activity findByIdAndTripOrThrow(Long id, Trip trip) {
    return activityRepository.findByIdAndTrip(id, trip)
        .orElseThrow(
            () -> new ActivityNotFoundException(id)
        );
  }

  private void validateActivityDates(LocalDateTime start, LocalDateTime end) {
    if (start != null && end != null && end.isBefore(start)) {
      throw new InvalidActivityDatesException();
    }
  }

}
