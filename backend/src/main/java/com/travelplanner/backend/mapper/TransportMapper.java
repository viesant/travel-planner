package com.travelplanner.backend.mapper;

import com.travelplanner.backend.dto.TransportRequest;
import com.travelplanner.backend.dto.TransportResponse;
import com.travelplanner.backend.entity.Transport;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper {

  public Transport toEntity(TransportRequest request) {
    return Transport.builder()
        .type(request.type())
        .carrier(request.carrier())
        .departureLocation(request.departureLocation())
        .departureAddress(request.departureAddress())
        .arrivalLocation(request.arrivalLocation())
        .arrivalAddress(request.arrivalAddress())
        .departureDateTime(request.departureDateTime())
        .arrivalDateTime(request.arrivalDateTime())
        .bookingNumber(request.bookingNumber())
        .price(request.price())
        .notes(request.notes())
        .build();
  }

  public TransportResponse toResponse(Transport transport) {
    return new TransportResponse(
        transport.getId(),
        transport.getType(),
        transport.getCarrier(),
        transport.getDepartureLocation(),
        transport.getDepartureAddress(),
        transport.getArrivalLocation(),
        transport.getArrivalAddress(),
        transport.getDepartureDateTime(),
        transport.getArrivalDateTime(),
        transport.getBookingNumber(),
        transport.getPrice(),
        transport.getNotes(),
        transport.getTrip().getId()
    );
  }

  public void updateEntityFromRequest(Transport transport, TransportRequest request) {
    transport.setType(request.type());
    transport.setDepartureLocation(request.departureLocation());
    transport.setDepartureAddress(request.departureAddress());
    transport.setArrivalLocation(request.arrivalLocation());
    transport.setArrivalAddress(request.arrivalAddress());
    transport.setDepartureDateTime(request.departureDateTime());
    transport.setArrivalDateTime(request.arrivalDateTime());
    transport.setBookingNumber(request.bookingNumber());
    transport.setPrice(request.price());
    transport.setNotes(request.notes());
  }

}
