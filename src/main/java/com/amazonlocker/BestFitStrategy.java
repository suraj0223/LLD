package com.amazonlocker;

import java.util.List;

public class BestFitStrategy implements AllocationStrategy {

    @Override
    public Compartment allocate(List<Compartment> compartments, Size size) {
        Size[] candidates;
        switch (size) {
            case SMALL:
                candidates = new Size[]{Size.SMALL, Size.MEDIUM, Size.LARGE};
                break;
            case MEDIUM:
                candidates = new Size[]{Size.MEDIUM, Size.LARGE};
                break;
            case LARGE:
                candidates = new Size[]{Size.LARGE};
                break;
            default:
                return null;
        }

        for (Size candidate : candidates) {
            for (Compartment c : compartments) {
                if (c.getSize() == candidate && !c.isOccupied()) {
                    return c;
                }
            }
        }
        return null;
    }
}
