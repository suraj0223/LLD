package com.splitwise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(int totalAmount, List<User> borrowers, Map<String, Integer> params) {
        List<Split> splits = new ArrayList<>();
        int percentSum = 0;

        for (User borrower : borrowers) {
            percentSum += params.getOrDefault(borrower.userId(), 0);
        }

        if (percentSum != 100) {
            throw new IllegalArgumentException(
                "Percentages (" + percentSum + ") do not add up to 100");
        }

        int amountSum = 0;
        for (int i = 0; i < borrowers.size(); i++) {
            User borrower = borrowers.get(i);
            int percent = params.getOrDefault(borrower.userId(), 0);
            int amount;

            if (i == borrowers.size() - 1) {
                amount = totalAmount - amountSum; // absorb rounding error
            } else {
                amount = (totalAmount * percent) / 100;
            }

            amountSum += amount;
            splits.add(new Split(borrower, amount));
        }
        return splits;
    }
}
