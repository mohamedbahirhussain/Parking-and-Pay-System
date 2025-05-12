package com.parkingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "VehicleEntry")
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numberPlate;

    private LocalDateTime inTime;
    private LocalDateTime outTime;

    private Duration totalDuration;
    private Double totalAmount;

    private String pictureUrl;
    // Added for payment status
    private String paymentStatus; // "Paid" or "Not Paid"


}
