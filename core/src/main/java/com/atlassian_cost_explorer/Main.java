package com.atlassian_cost_explorer;

import java.util.List;

/*
{
  "customerId": "c1",
  "products": [
    {
      "name": "Jira",
      "subscription": {
        "planId": "BASIC",
        "startDate": "2021-03-10",
        "trialPeriodMonths": 4
      }
    },
    {
      "name": "Confluence",
      "subscription": {
        "planId": "STANDARD",
        "startDate": "2021-06-15"
      }
    }
  ]
}

Note: "trialPeriodMonths" is optional. If omitted, defaults to 0 (no trial period).

 */
public class Main {

  public static void main(String[] args) {

    // Pricing Plans

    // Customer Data with multiple products
    // Jira subscription with 2-month trial period
    Subscription jiraSubscription = new Subscription("BASIC", "2021-03-10", 4);
    Product jiraProduct = new Product("Jira", jiraSubscription);

    // Confluence subscription without trial period
    Subscription confluenceSubscription = new Subscription("STANDARD", "2021-06-15");
    Product confluenceProduct = new Product("Confluence", confluenceSubscription);

    Customer customer = new Customer("c1", List.of(jiraProduct, confluenceProduct));

    // Cost Explorer
    CostExplorer explorer = new CostExplorer();

    // API 1: Monthly cost list
    List<Double> monthlyCosts = explorer.monthlyCostList(customer);
    System.out.println("Monthly Costs: " + monthlyCosts);

    // API 2: Annual cost
    double totalAnnual = explorer.annualCost(customer);
    System.out.println("Annual Cost: " + totalAnnual);
  }
}
