package com.amazonlocker;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Compartment {
    private final String id;
    private final Size size;
    @Setter
    private boolean occupied;

    public Compartment(String id, Size size) {
        this.id = id;
        this.size = size;
        this.occupied = false;
    }

    public void markOccupied() {
        this.occupied = true;
    }

    public void markFree() {
        this.occupied = false;
    }
}
