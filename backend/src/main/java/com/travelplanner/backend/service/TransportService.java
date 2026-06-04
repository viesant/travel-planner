package com.travelplanner.backend.service;

import com.travelplanner.backend.dto.TransportRequest;
import com.travelplanner.backend.dto.TransportResponse;
import com.travelplanner.backend.entity.Transport;
import com.travelplanner.backend.entity.Trip;
import com.travelplanner.backend.exception.InvalidTransportDatesException;
import com.travelplanner.backend.exception.TransportNotFoundException;
import com.travelplanner.backend.mapper.TransportMapper;
import com.travelplanner.backend.repository.TransportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportService {

  private final TransportRepository transportRepository;
  private final TransportMapper transportMapper;
  private final TripService tripService;

  public TransportResponse create(Long tripId, TransportRequest request) {
    Trip trip = tripService.getValidatedTrip(tripId);
    validateTransportDates(request.departureDateTime(), request.arrivalDateTime());

    Transport newTransport = transportMapper.toEntity(request);
    newTransport.setTrip(trip);
    Transport savedTransport = transportRepository.save(newTransport);
    return transportMapper.toResponse(savedTransport);
  }

  public List<TransportResponse> findAllByTrip(Long tripId) {
    Trip trip = tripService.getValidatedTrip(tripId);

    return transportRepository.findAllByTrip(trip)
        .stream()
        .map(transportMapper::toResponse)
        .toList();
  }

  public TransportResponse findById(Long tripId, Long id) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Transport transport = findByIdAndTripOrThrow(id, trip);

    return transportMapper.toResponse(transport);
  }

  public TransportResponse update(Long tripId, Long id, TransportRequest request) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Transport transport = findByIdAndTripOrThrow(id, trip);

    validateTransportDates(request.departureDateTime(), request.arrivalDateTime());

    transportMapper.updateEntityFromRequest(transport, request);

    Transport savedTransport = transportRepository.save(transport);
    return transportMapper.toResponse(savedTransport);
  }

  public void delete(Long tripId, Long id) {
    Trip trip = tripService.getValidatedTrip(tripId);
    Transport transport = findByIdAndTripOrThrow(id, trip);

    transportRepository.delete(transport);
  }

  private Transport findByIdAndTripOrThrow(Long id, Trip trip) {
    return transportRepository.findByIdAndTrip(id, trip)
        .orElseThrow(
            () -> new TransportNotFoundException(id)
        );
  }

  private void validateTransportDates(LocalDateTime departure, LocalDateTime arrival) {
    if (departure != null && arrival != null && arrival.isBefore(departure)) {
      throw new InvalidTransportDatesException();
    }
  }

}
