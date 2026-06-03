package com.travelplanner.backend.entity;

import com.travelplanner.backend.enums.TransportType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransportType type;

  private String carrier;

  @Column(nullable = false)
  private String departureLocation;

  @Column(columnDefinition = "TEXT")
  private String departureAddress;

  @Column(nullable = false)
  private String arrivalLocation;
  @Column(columnDefinition = "TEXT")
  private String arrivalAddress;

  @Column(nullable = false)
  private LocalDateTime departureDateTime;
  @Column(nullable = false)
  private LocalDateTime arrivalDateTime;

  private String bookingNumber;

  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

}
