package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.TripRequest;
import com.travelplanner.backend.dto.TripResponse;
import com.travelplanner.backend.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService tripService;

  @PostMapping
  public ResponseEntity<TripResponse> create(@Valid @RequestBody TripRequest request) {
    TripResponse response = tripService.create(request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping
  public ResponseEntity<List<TripResponse>> findAllByUser() {
    List<TripResponse> response = tripService.findAllByUser();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TripResponse> findById(@PathVariable Long id) {
    TripResponse response = tripService.findById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TripResponse> update(@PathVariable Long id, @Valid @RequestBody TripRequest request) {
    TripResponse response = tripService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    tripService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
