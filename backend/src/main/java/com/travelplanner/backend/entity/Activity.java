package com.travelplanner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String location;

  @Column(columnDefinition = "TEXT")
  private String address;

  @Column(nullable = false)
  private LocalDateTime startDateTime;
  @Column(nullable = false)
  private LocalDateTime endDateTime;

  private String bookingNumber;

  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

}
