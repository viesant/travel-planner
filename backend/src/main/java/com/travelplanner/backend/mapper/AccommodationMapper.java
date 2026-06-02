package com.travelplanner.backend.mapper;

import com.travelplanner.backend.dto.AccommodationRequest;
import com.travelplanner.backend.dto.AccommodationResponse;
import com.travelplanner.backend.entity.Accommodation;
import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

  public Accommodation toEntity(AccommodationRequest request){
    return Accommodation.builder()
        .name(request.name())
        .location(request.location())
        .address(request.address())
        .bookingNumber(request.bookingNumber())
        .checkInDate(request.checkInDate())
        .checkOutDate(request.checkOutDate())
        .price(request.price())
        .notes(request.notes())
        .build();
  }

  public AccommodationResponse toResponse(Accommodation accommodation){
    return new AccommodationResponse(
        accommodation.getName(),
        accommodation.getLocation(),
        accommodation.getAddress(),
        accommodation.getBookingNumber(),
        accommodation.getCheckInDate(),
        accommodation.getCheckOutDate(),
        accommodation.getPrice(),
        accommodation.getNotes(),
        accommodation.getTrip().getId()
    );
  }
}
