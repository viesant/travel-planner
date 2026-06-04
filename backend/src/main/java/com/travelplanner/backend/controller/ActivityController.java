package com.travelplanner.backend.controller;

import com.travelplanner.backend.dto.ActivityRequest;
import com.travelplanner.backend.dto.ActivityResponse;
import com.travelplanner.backend.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/activities")
@RequiredArgsConstructor
public class ActivityController {

  private final ActivityService activityService;

  @PostMapping
  public ResponseEntity<ActivityResponse> create(@PathVariable Long tripId,
                                                 @Valid @RequestBody ActivityRequest request) {

    ActivityResponse response = activityService.create(tripId, request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping
  public ResponseEntity<List<ActivityResponse>> findAllByTrip(@PathVariable Long tripId) {
    List<ActivityResponse> response = activityService.findAllByTrip(tripId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityResponse> findById(@PathVariable Long tripId,
                                                   @PathVariable Long id) {

    ActivityResponse response = activityService.findById(tripId, id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ActivityResponse> update(@PathVariable Long tripId,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody ActivityRequest request) {

    ActivityResponse response = activityService.update(tripId, id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long tripId,
                                     @PathVariable Long id) {
    activityService.delete(tripId, id);
    return ResponseEntity.noContent().build();
  }

}
