## Design SplitWise

### Requirements
- Users can add expenses.
- Users can edit expenses.
- Expenses can be split: equal, exact, or by percentage.
- Users can settle up with each other.
- Allow users to make groups and add, edit and settle expenses in the group.

### Core Entities
- User
- Group
- Expense
- Split
- SplitType enum {EQUAL, EXACT, PERCENTAGE}
- ExpenseType enum {FOOD, TRANSPORT, TICKET, OTHER}
- ExpenseService

### Class Design

``` txt
User
- userId: String
- name: String

-----------------------------------------
Group
- groupId: String
- groupName: String
- members: List<User>
- expenses: List<Expense>
- balanceSheet: Map<String, Map<String, Int>>  // userId -> (userId -> amount)

+ addExpense(expense: Expense): boolean
+ editExpense(expense: Expense): boolean
+ getBalance(userId: String): Map<String, Int>

-----------------------------------------
Expense
- expenseId: String
- description: String
- amount: Int
- paidBy: User
- splitType: SplitType
- borrowers: List<Split>
- expenseType: ExpenseType

-----------------------------------------
Split
- user: User
- amount: Int

-----------------------------------------
ExpenseService
- groups: List<Group>

+ addGroup(group: Group): boolean
+ getGroup(name: String): Group
+ addExpense(groupId: String, expense: Expense): boolean
+ editExpense(groupId: String, expense: Expense): boolean
+ settleUp(groupId: String, from: User, to: User, amount: Int): boolean
+ getBalances(groupId: String): Map<String, Map<String, Int>>
```

