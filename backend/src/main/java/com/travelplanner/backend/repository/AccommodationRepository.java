package com.travelplanner.backend.repository;

import com.travelplanner.backend.entity.Accommodation;
import com.travelplanner.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
  List<Accommodation> findAllByTrip(Trip trip);

  Optional<Accommodation> findByIdAndTrip(Long id, Trip trip);

}
