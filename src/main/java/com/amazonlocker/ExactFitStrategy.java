package com.amazonlocker;

import java.util.List;

public class ExactFitStrategy implements AllocationStrategy {

    @Override
    public Compartment allocate(List<Compartment> compartments, Size size) {
        for (Compartment c : compartments) {
            if (c.getSize() == size && !c.isOccupied()) {
                return c;
            }
        }
        return null;
    }
}
