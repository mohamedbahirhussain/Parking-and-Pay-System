package com.parkingsystem.exception;

public class VehicleExitException extends RuntimeException {
    public VehicleExitException(String message) {
        super(message);
    }
}