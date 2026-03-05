package com.atlassian_cost_explorer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PricingPlan {

  private String planId;
  private double monthlyCost;
}