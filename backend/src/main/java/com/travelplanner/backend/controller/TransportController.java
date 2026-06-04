package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.TransportRequest;
import com.travelplanner.backend.dto.TransportResponse;
import com.travelplanner.backend.service.TransportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/transports")
@RequiredArgsConstructor
public class TransportController {

  private final TransportService transportService;

  @PostMapping
  public ResponseEntity<TransportResponse> create(@PathVariable Long tripId,
                                                  @Valid @RequestBody TransportRequest request) {

    TransportResponse response = transportService.create(tripId, request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping
  public ResponseEntity<List<TransportResponse>> findAllByTrip(@PathVariable Long tripId) {
    List<TransportResponse> response = transportService.findAllByTrip(tripId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransportResponse> findById(@PathVariable Long tripId,
                                                    @PathVariable Long id) {
    TransportResponse response = transportService.findById(tripId, id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TransportResponse> update(@PathVariable Long tripId,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody TransportRequest request) {
    TransportResponse response = transportService.update(tripId, id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long tripId,
                                     @PathVariable Long id) {
    transportService.delete(tripId, id);
    return ResponseEntity.noContent().build();
  }

}
