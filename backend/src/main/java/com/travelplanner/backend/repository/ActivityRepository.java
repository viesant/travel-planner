package com.travelplanner.backend.repository;

import com.travelplanner.backend.entity.Activity;
import com.travelplanner.backend.entity.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository {

  List<Activity> findAllByTrip(Trip trip);

  Optional<Activity> findByIdAndTrip(Long id, Trip trip);
}
