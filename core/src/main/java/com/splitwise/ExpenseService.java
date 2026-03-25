package com.splitwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ExpenseService {
    private List<Group> groups;

    public ExpenseService() {
        this.groups = new ArrayList<>();
    }

    public boolean addGroup(Group group) {
        groups.add(group);
        return true;
    }

    public Group getGroup(String groupId) {
        return groups.stream()
                .filter(g -> g.getGroupId().equals(groupId))
                .findFirst()
                .orElse(null);
    }

    public boolean addExpense(String groupId, Expense expense) {
        Group group = getGroup(groupId);
        if (group == null) return false;
        return group.addExpense(expense);
    }

    public boolean editExpense(String groupId, Expense oldExpense, Expense newExpense) {
        Group group = getGroup(groupId);
        if (group == null) return false;
        return group.editExpense(oldExpense, newExpense);
    }

    // Manual settle-up: from pays to a specific amount
    public boolean settleUp(String groupId, User from, User to, int amount) {
        Group group = getGroup(groupId);
        if (group == null || amount <= 0) return false;

        Map<String, Map<String, Integer>> sheet = group.getBalanceSheet();

        // "from" owes "to", so "to" is the creditor
        // sheet[to][from] > 0 means from owes to
        int currentDebt = sheet
                .getOrDefault(to.userId(), new HashMap<>())
                .getOrDefault(from.userId(), 0);

        if (currentDebt <= 0) return false;       // from doesn't owe to
        if (amount > currentDebt) return false;    // can't overpay

        // Reduce from's debt to "to"
        sheet.get(to.userId()).put(from.userId(), currentDebt - amount);
        sheet.get(from.userId()).put(to.userId(), -(currentDebt - amount));

        // Clean up zero entries
        if (currentDebt - amount == 0) {
            sheet.get(to.userId()).remove(from.userId());
            sheet.get(from.userId()).remove(to.userId());
        }

        return true;
    }

    public Map<String, Map<String, Integer>> getBalances(String groupId) {
        Group group = getGroup(groupId);
        if (group == null) return new HashMap<>();
        return group.getBalanceSheet();
    }

    // Optimal group settlement: minimum transactions using two max-heaps
    public List<String> settleUpGroup(String groupId) {
        Group group = getGroup(groupId);
        if (group == null) return List.of();

        Map<String, Map<String, Integer>> sheet = group.getBalanceSheet();

        // Step 1: Compute net balance per user
        // net > 0 means others owe them (creditor)
        // net < 0 means they owe others (debtor)
        Map<String, Integer> netBalance = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : sheet.entrySet()) {
            String userId = entry.getKey();
            int net = 0;
            for (int amount : entry.getValue().values()) {
                net += amount;
            }
            if (net != 0) {
                netBalance.put(userId, net);
            }
        }

        // We need userId -> userName for readable output
        Map<String, String> userNames = new HashMap<>();
        for (User member : group.getMembers()) {
            userNames.put(member.userId(), member.name());
        }

        // Step 2: Separate into max-heaps by absolute amount
        // Creditors: net > 0 (they are owed money)
        PriorityQueue<String[]> creditors = new PriorityQueue<>(
                (a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
        // Debtors: net < 0 (they owe money), store absolute value
        PriorityQueue<String[]> debtors = new PriorityQueue<>(
                (a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

        for (Map.Entry<String, Integer> entry : netBalance.entrySet()) {
            int net = entry.getValue();
            String name = userNames.getOrDefault(entry.getKey(), entry.getKey());
            if (net > 0) {
                creditors.offer(new String[]{name, String.valueOf(net)});
            } else {
                debtors.offer(new String[]{name, String.valueOf(-net)});
            }
        }

        // Step 3: Greedily match largest debtor with largest creditor
        List<String> transactions = new ArrayList<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            String[] debtor = debtors.poll();
            String[] creditor = creditors.poll();

            int debtAmt = Integer.parseInt(debtor[1]);
            int credAmt = Integer.parseInt(creditor[1]);
            int settled = Math.min(debtAmt, credAmt);

            transactions.add(debtor[0] + " pays " + creditor[0] + ": " + settled);

            int remainingDebt = debtAmt - settled;
            int remainingCred = credAmt - settled;

            if (remainingDebt > 0) {
                debtors.offer(new String[]{debtor[0], String.valueOf(remainingDebt)});
            }
            if (remainingCred > 0) {
                creditors.offer(new String[]{creditor[0], String.valueOf(remainingCred)});
            }
        }

        return transactions;
    }
}
