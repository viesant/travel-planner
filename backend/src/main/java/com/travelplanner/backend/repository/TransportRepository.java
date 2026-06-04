package com.travelplanner.backend.repository;

import com.travelplanner.backend.entity.Transport;
import com.travelplanner.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {

  List<Transport> findAllByTrip(Trip trip);

  Optional<Transport> findByIdAndTrip(Long id, Trip trip);

}
