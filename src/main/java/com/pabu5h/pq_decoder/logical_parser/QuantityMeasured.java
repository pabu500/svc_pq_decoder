package com.pabu5h.pq_decoder.logical_parser;

public enum QuantityMeasured {
    // None or not applicable
    NONE(0),
    // Voltage
    VOLTAGE(1),
    // Current
    CURRENT(2),
    // Power - includes all data for a quantity or characteristic derived from multiplying voltage and current components.
    POWER(3),
    // Energy - includes all data from an integration of a quantity or characteristic derived from multiplying voltage and current components together.
    ENERGY(4),
    // Temperature
    TEMPERATURE(5),
    // Pressure
    PRESSURE(6),
    // Charge
    CHARGE(7),
    // Electrical field
    ELECTRICAL_FIELD(8),
    // Magnetic field
    MAGNETIC_FIELD(9),
    // Velocity
    VELOCITY(10),
    // Compass bearing
    BEARING(11),
    // Applied force, electrical, mechanical etc.
    FORCE(12),
    // Torque
    TORQUE(13),
    // Spatial position
    POSITION(14),
    // Flux linkage Weber Turns
    FLUX_LINKAGE(15),
    // Magnetic field density
    FLUX_DENSITY(16),
    // Status data
    STATUS(17);

    private final int value;

    QuantityMeasured(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

