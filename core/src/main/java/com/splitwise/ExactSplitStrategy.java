package com.splitwise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(int totalAmount, List<User> borrowers, Map<String, Integer> params) {
        List<Split> splits = new ArrayList<>();
        int sum = 0;

        for (User borrower : borrowers) {
            int amount = params.getOrDefault(borrower.userId(), 0);
            splits.add(new Split(borrower, amount));
            sum += amount;
        }

        if (sum != totalAmount) {
            throw new IllegalArgumentException(
                "Exact split amounts (" + sum + ") do not add up to total (" + totalAmount + ")");
        }
        return splits;
    }
}
