package com.travelplanner.backend.mapper;

import com.travelplanner.backend.dto.TripRequest;
import com.travelplanner.backend.dto.TripResponse;
import com.travelplanner.backend.entity.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

  public Trip toEntity(TripRequest request) {
    return new Trip(
        request.title(),
        request.description(),
        request.startDate(),
        request.endDate()
    );
  }

  public TripResponse toResponse(Trip trip) {
    return new TripResponse(
        trip.getId(),
        trip.getTitle(),
        trip.getDescription(),
        trip.getStartDate(),
        trip.getEndDate()
    );
  }

  public void updateEntityFromRequest(Trip trip, TripRequest request) {
    trip.setTitle(request.title());
    trip.setDescription(request.description());
    trip.setStartDate(request.startDate());
    trip.setEndDate(request.endDate());
  }

}
