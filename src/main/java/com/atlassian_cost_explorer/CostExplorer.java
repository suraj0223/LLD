package com.atlassian_cost_explorer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostExplorer {

  private final Map<String, PricingPlan> pricingPlanMap;

  public CostExplorer() {
    pricingPlanMap = new HashMap<>();
    pricingPlanMap.put("BASIC", new PricingPlan("BASIC", 9.99));
    pricingPlanMap.put("STANDARD", new PricingPlan("STANDARD", 49.99));
    pricingPlanMap.put("PREMIUM", new PricingPlan("PREMIUM", 249.99));
  }

  /* API 1: Returns 12 size list of monthly cost (Jan - Dec) */
  public List<Double> monthlyCostList(Customer customer) {

    Double[] monthlyCost = new Double[12];

    Arrays.fill(monthlyCost, 0.0);

    // Aggregate costs across all products
    for (Product product : customer.getProducts()) {
      Subscription sub = product.getSubscription();
      String planId = sub.getPlanId();

      PricingPlan plan = pricingPlanMap.get(planId);
      if (plan == null) {
        throw new IllegalArgumentException("Invalid Plan ID: " + planId);
      }

      double monthlyAmount = plan.getMonthlyCost();
      int startMonth = sub.getStartDate().getMonthValue(); // 1-12
      int trialPeriodMonths = sub.getTrialPeriodMonths();

      // Calculate the first billing month (after trial period ends)
      // If trial is 2 months, and subscription starts in March, billing starts in May
      int firstBillingMonth = startMonth + trialPeriodMonths;

      // If trial extends beyond December, no charges for this year
      if (firstBillingMonth > 12) {
        continue;
      }

      // since full-month charge even if subscribed mid-month
      // Start billing from firstBillingMonth (which accounts for trial period)
      for (int i = firstBillingMonth - 1; i < 12; i++) {
        monthlyCost[i] = monthlyCost[i] + monthlyAmount;
      }
    }

    return Arrays.asList(monthlyCost);
  }

  /* API 2: Returns total annual cost */
  public double annualCost(Customer customer) {
    List<Double> monthlyCosts = monthlyCostList(customer);
    double total = 0.0;

    for (double cost : monthlyCosts) {
      total += cost;
    }

    return total;
  }
}
