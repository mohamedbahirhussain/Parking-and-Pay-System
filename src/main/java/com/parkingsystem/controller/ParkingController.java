package com.parkingsystem.controller;

import com.parkingsystem.model.ParkingRecord;
import com.parkingsystem.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService service;

    @PostMapping("/entry")
    public ResponseEntity<ParkingRecord> entry(@RequestParam String numberPlate, @RequestParam(required = false) String pictureUrl) {
        return ResponseEntity.ok(service.vehicleEntry(numberPlate, pictureUrl));
    }

    @PostMapping("/exit")
    public ResponseEntity<ParkingRecord> exit(@RequestParam String numberPlate) {
        return ResponseEntity.ok(service.vehicleExit(numberPlate));
    }

    @PostMapping("/pay")
    public ResponseEntity<ParkingRecord> pay(@RequestParam String numberPlate) {
        return ResponseEntity.ok(service.markAsPaid(numberPlate));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ParkingRecord>> getAll() {
        return ResponseEntity.ok(service.getAllRecords());
    }
}
