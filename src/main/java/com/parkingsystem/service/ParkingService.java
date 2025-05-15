package com.parkingsystem.service;

import com.parkingsystem.exception.VehicleExitException;
import com.parkingsystem.model.ParkingRecord;
import com.parkingsystem.repository.ParkingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRecordRepository repository;

    private static final double RATE_PER_HOUR = 100.0;

    // Vehicle Entry
    public ParkingRecord vehicleEntry(String numberPlate, String pictureUrl) {
        ParkingRecord record = new ParkingRecord();
        record.setNumberPlate(numberPlate);
        record.setInTime(LocalDateTime.now());
        record.setPictureUrl(pictureUrl);
        record.setPaymentStatus("Not Paid");  // Default status is "Not Paid"
        return repository.save(record);
    }

    // Vehicle Exit
    public ParkingRecord vehicleExit(String numberPlate) {
        ParkingRecord record = repository.findByNumberPlate(numberPlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!"Paid".equals(record.getPaymentStatus())) {
            throw new VehicleExitException("Payment not completed. Vehicle cannot exit.");
        }

        if (record.getOutTime() == null) {
            record.setOutTime(LocalDateTime.now());
            return repository.save(record);
        }

        return record; // Already exited
    }
    // Mark as Paid
    public ParkingRecord markAsPaid(String numberPlate) {
        ParkingRecord record = repository.findByNumberPlate(numberPlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!record.getPaymentStatus().equals("Paid")) {
            // Don't set outTime here, just calculate duration till now
            Duration duration = Duration.between(record.getInTime(), LocalDateTime.now());
            record.setTotalDuration(duration);
            double hours = Math.ceil((double) duration.toMinutes() / 60);
            record.setTotalAmount(hours * RATE_PER_HOUR);
            record.setPaymentStatus("Paid");
            return repository.save(record);
        }

        return record;
    }

    // Get All Records
    public List<ParkingRecord> getAllRecords() {
        return repository.findAll();
    }
}