package com.splitwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Group {
    private String groupId;
    private String groupName;
    private List<User> members;
    private List<Expense> expenses;
    private Map<String, Map<String, Integer>> balanceSheet; // userId -> (userId -> amount)

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.balanceSheet = new HashMap<>();
    }

    public void addMember(User user) {
        members.add(user);
    }

    public boolean addExpense(Expense expense) {
        expenses.add(expense);
        updateBalanceSheet(expense);
        return true;
    }

    public boolean editExpense(Expense oldExpense, Expense newExpense) {
        reverseBalanceSheet(oldExpense);
        expenses.remove(oldExpense);
        expenses.add(newExpense);
        updateBalanceSheet(newExpense);
        return true;
    }

    public Map<String, Integer> getBalance(String userId) {
        return balanceSheet.getOrDefault(userId, new HashMap<>());
    }

    // balanceSheet.get(payerId).get(borrowerId) > 0 means borrower owes payer that amount
    // balanceSheet.get(borrowerId).get(payerId) < 0 means borrower owes payer (symmetric mirror)
    private void updateBalanceSheet(Expense expense) {
        String payerId = expense.getPaidBy().userId();

        for (Split split : expense.getBorrowers()) {
            String borrowerId = split.getUser().userId();
            if (borrowerId.equals(payerId)) continue; // payer's own share

            int amount = split.getAmount();

            // payer is owed by borrower
            balanceSheet.computeIfAbsent(payerId, k -> new HashMap<>());
            balanceSheet.get(payerId).merge(borrowerId, amount, Integer::sum);

            // borrower owes payer (symmetric, negative)
            balanceSheet.computeIfAbsent(borrowerId, k -> new HashMap<>());
            balanceSheet.get(borrowerId).merge(payerId, -amount, Integer::sum);
        }
    }

    private void reverseBalanceSheet(Expense expense) {
        String payerId = expense.getPaidBy().userId();

        for (Split split : expense.getBorrowers()) {
            String borrowerId = split.getUser().userId();
            if (borrowerId.equals(payerId)) continue;

            int amount = split.getAmount();

            // Reverse: subtract what was added
            balanceSheet.get(payerId).merge(borrowerId, -amount, Integer::sum);
            balanceSheet.get(borrowerId).merge(payerId, amount, Integer::sum);
        }
    }
}
