package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.AccommodationRequest;
import com.travelplanner.backend.dto.AccommodationResponse;
import com.travelplanner.backend.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

  private final AccommodationService accommodationService;

  @PostMapping
  public ResponseEntity<AccommodationResponse> create(@PathVariable Long tripId,
                                                      @Valid @RequestBody AccommodationRequest request) {

    AccommodationResponse response = accommodationService.create(tripId, request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping
  public ResponseEntity<List<AccommodationResponse>> findAllByTrip(@PathVariable Long tripId) {
    List<AccommodationResponse> response = accommodationService.findAllByTrip(tripId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccommodationResponse> findById(@PathVariable Long tripId,
                                                        @PathVariable Long id) {
    AccommodationResponse response = accommodationService.findById(tripId, id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AccommodationResponse> update(@PathVariable Long tripId,
                                                      @PathVariable Long id,
                                                      @Valid @RequestBody AccommodationRequest request) {
    AccommodationResponse response = accommodationService.update(tripId, id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long tripId,
                                     @PathVariable Long id) {
    accommodationService.delete(tripId, id);
    return ResponseEntity.noContent().build();
  }

}
