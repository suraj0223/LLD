package com.splitwise;

import java.util.List;
import java.util.Map;
import lombok.Getter;

public class Expense {
    @Getter private String expenseId;
    private String description;
    @Getter private int amount;
    @Getter private User paidBy;
    private SplitType splitType;
    private SplitStrategy splitStrategy;
    @Getter private List<Split> borrowers;
    private ExpenseType expenseType;

    public Expense(String expenseId, String description, int amount, User paidBy,
                   SplitType splitType, SplitStrategy splitStrategy,
                   List<User> borrowerUsers, Map<String, Integer> splitParams,
                   ExpenseType expenseType) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.splitStrategy = splitStrategy;
        this.expenseType = expenseType;
        this.borrowers = splitStrategy.split(amount, borrowerUsers, splitParams);
    }

    @Override
    public String toString() {
        return description + " (" + amount + " paid by " + paidBy + ", " + splitType + ")";
    }
}
