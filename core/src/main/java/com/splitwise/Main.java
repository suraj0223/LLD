package com.splitwise;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ExpenseService service = new ExpenseService();

        // --- Create Users ---
        User alice   = new User("u1", "Alice");
        User bob     = new User("u2", "Bob");
        User charlie = new User("u3", "Charlie");
        User dave = new User("u4", "Dave");

        // --- Create Group ---
        Group trip = new Group("g1", "Goa Trip");
        trip.addMember(alice);
        trip.addMember(bob);
        trip.addMember(charlie);
        trip.addMember(dave);
        service.addGroup(trip);

        List<User> goaTripMembers = List.of(alice, bob, charlie, dave);

        // ========== Demo 1: Equal Split ==========
        // Alice paid 1000 for dinner, split equally among all 3 (333 each)
        Expense dinner = new Expense("e1", "Dinner", 1000, alice,
                SplitType.EQUAL, new EqualSplitStrategy(),
                goaTripMembers, null,
                ExpenseType.FOOD);
        service.addExpense("g1", dinner);

        System.out.println("=== After Dinner (Equal Split: Alice paid 1000) ===");
        printBalances(service, "g1");

        // ========== Demo 4: Manual Settle-Up ==========
        // Bob pays Alice 150
        System.out.println("=== Bob pays Alice 150 (settle up) ===");
        boolean settled = service.settleUp("g1", bob, alice, 150);
        System.out.println("Settlement successful: " + settled);
        printBalances(service, "g1");

        // ========== Demo 5: Edit Expense ==========
        // Edit dinner from 1000 to 1200
        Expense dinnerEdited = new Expense("e1", "Dinner (edited)", 1200, alice,
                SplitType.EQUAL, new EqualSplitStrategy(),
                goaTripMembers, null,
                ExpenseType.FOOD);
        service.editExpense("g1", dinner, dinnerEdited);

        System.out.println("=== After Editing Dinner to 1200 ===");
        printBalances(service, "g1");

        // ========== Demo 6: Optimal Group Settlement ==========
        System.out.println("=== Optimal Settlement (Minimum Transactions) ===");

        /**
         * Most important call -
         */
        List<String> transactions = service.settleUpGroup("g1");

        for (String t : transactions) {
            System.out.println("  " + t);
        }
    }

    private static void printBalances(ExpenseService service, String groupId) {
        Map<String, Map<String, Integer>> balances = service.getBalances(groupId);
        // Map userId to name for readable output
        Map<String, String> names = Map.of("u1", "Alice", "u2", "Bob", "u3", "Charlie", "u4", "Dave");

        boolean printed = false;
        for (Map.Entry<String, Map<String, Integer>> entry : balances.entrySet()) {
            String creditor = names.getOrDefault(entry.getKey(), entry.getKey());
            for (Map.Entry<String, Integer> inner : entry.getValue().entrySet()) {
                if (inner.getValue() > 0) {
                    String borrower = names.getOrDefault(inner.getKey(), inner.getKey());
                    System.out.println("  " + borrower + " owes " + creditor + ": " + inner.getValue());
                    printed = true;
                }
            }
        }
        if (!printed) {
            System.out.println("  All settled!");
        }
        System.out.println();
    }
}
