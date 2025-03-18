package com.pabu5h.pq_decoder.logical_parser;

public enum Phase {
    // Phase is not applicable
    NONE(0),
    // A-to-neutral
    AN(1),
    // B-to-neutral
    BN(2),
    // C-to-neutral
    CN(3),
    // Neutral-to-ground
    NG(4),
    // A-to-B
    AB(5),
    // B-to-C
    BC(6),
    // C-to-A
    CA(7),
    // Residual - the vector or point-on-wave sum of Phases A, B, and C.
    RESIDUAL(8),
    // Net - the vector or point-on-wave sum of Phases A, B, C and the Neutral phase.
    NET(9),
    // Positive sequence
    POSITIVE_SEQUENCE(10),
    // Negative sequence
    NEGATIVE_SEQUENCE(11),
    // Zero sequence
    ZERO_SEQUENCE(12),
    // The value representing a total or other summarizing value in a multi-phase system.
    TOTAL(13),
    // The value representing average of 3 line-neutral values.
    LINE_TO_NEUTRAL_AVERAGE(14),
    // The value representing average of 3 line-line values.
    LINE_TO_LINE_AVERAGE(15),
    // The value representing the "worst" of the 3 phases.
    WORST(16),
    // DC Positive
    PLUS(17),
    // DC Negative
    MINUS(18),
    // Generic Phase 1
    GENERAL1(19),
    // Generic Phase 2
    GENERAL2(20),
    // Generic Phase 3
    GENERAL3(21),
    // Generic Phase 4
    GENERAL4(22),
    // Generic Phase 5
    GENERAL5(23),
    // Generic Phase 6
    GENERAL6(24),
    // Generic Phase 7
    GENERAL7(25),
    // Generic Phase 8
    GENERAL8(26),
    // Generic Phase 9
    GENERAL9(27),
    // Generic Phase 10
    GENERAL10(28),
    // Generic Phase 11
    GENERAL11(29),
    // Generic Phase 12
    GENERAL12(30),
    // Generic Phase 13
    GENERAL13(31),
    // Generic Phase 14
    GENERAL14(32),
    // Generic Phase 15
    GENERAL15(33),
    // Generic Phase 16
    GENERAL16(34);

    private final int value;

    Phase(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

