package com.parkingsystem.service;

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

    // Vehicle Exit (Calculate Duration, Fee, and Set Payment Status)
    public ParkingRecord vehicleExit(String numberPlate) {
        ParkingRecord record = repository.findByNumberPlate(numberPlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!record.getPaymentStatus().equals("Paid")) {
            record.setOutTime(LocalDateTime.now());
            Duration duration = Duration.between(record.getInTime(), record.getOutTime());
            record.setTotalDuration(duration);
            double hours = Math.ceil((double) duration.toMinutes() / 60);
            record.setTotalAmount(hours * RATE_PER_HOUR);

            // If the user has not paid, the status will remain "Not Paid"
            return repository.save(record);
        }

        return record; // Already paid, no update
    }

    // Mark as Paid
    public ParkingRecord markAsPaid(String numberPlate) {
        ParkingRecord record = repository.findByNumberPlate(numberPlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!record.getPaymentStatus().equals("Paid")) {
            if (record.getOutTime() == null) {
                record.setOutTime(LocalDateTime.now());
            }
            Duration duration = Duration.between(record.getInTime(), record.getOutTime());
            record.setTotalDuration(duration);
            double hours = Math.ceil((double) duration.toMinutes() / 60);
            record.setTotalAmount(hours * RATE_PER_HOUR);
            record.setPaymentStatus("Paid");  // Set to "Paid" when payment is made
            return repository.save(record);
        }

        return record;
    }

    // Get All Records
    public List<ParkingRecord> getAllRecords() {
        return repository.findAll();
    }
}