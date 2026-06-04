package com.travelplanner.backend.mapper;

import com.travelplanner.backend.dto.ActivityRequest;
import com.travelplanner.backend.dto.ActivityResponse;
import com.travelplanner.backend.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
  public Activity toEntity(ActivityRequest request) {
    return Activity.builder()
        .name(request.name())
        .location(request.location())
        .address(request.address())
        .startDateTime(request.startDateTime())
        .endDateTime(request.endDateTime())
        .bookingNumber(request.bookingNumber())
        .price(request.price())
        .notes(request.notes())
        .build();
  }

  public ActivityResponse toResponse(Activity activity) {
    return new ActivityResponse(
        activity.getId(),
        activity.getName(),
        activity.getLocation(),
        activity.getAddress(),
        activity.getStartDateTime(),
        activity.getEndDateTime(),
        activity.getBookingNumber(),
        activity.getPrice(),
        activity.getNotes(),
        activity.getTrip().getId()
    );
  }

  public void updateEntityFromRequest(Activity activity, ActivityRequest request) {
    activity.setName(request.name());
    activity.setLocation(request.location());
    activity.setAddress(request.address());
    activity.setStartDateTime(request.startDateTime());
    activity.setEndDateTime(request.endDateTime());
    activity.setBookingNumber(request.bookingNumber());
    activity.setPrice(request.price());
    activity.setNotes(request.notes());
  }

}
