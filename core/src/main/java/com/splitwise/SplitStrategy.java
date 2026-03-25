package com.splitwise;

import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    List<Split> split(int totalAmount, List<User> borrowers, Map<String, Integer> params);
}
