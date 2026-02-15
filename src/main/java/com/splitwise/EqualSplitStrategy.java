package com.splitwise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(int totalAmount, List<User> borrowers, Map<String, Integer> params) {
        List<Split> splits = new ArrayList<>();
        int n = borrowers.size();
        int perPerson = totalAmount / n;
        int remainder = totalAmount % n;

        for (int i = 0; i < n; i++) {
            int share = perPerson + (i < remainder ? 1 : 0);
            splits.add(new Split(borrowers.get(i), share));
        }
        return Collections.unmodifiableList(splits); // so that no one can modify the splits.
    }
}
