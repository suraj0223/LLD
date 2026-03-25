package com.amazonlocker;

import java.util.List;

public interface AllocationStrategy {
    Compartment allocate(List<Compartment> compartments, Size size);
}
