package com.atlassian_cost_explorer;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription {

  private final String planId;
  private final LocalDate startDate;
  private final int trialPeriodMonths; // Number of months for free trial (default 0)

  Subscription(String planId, String startDate) {
    this(planId, startDate, 0);
  }

  Subscription(String planId, String startDate, int trialPeriodMonths) {
    this.planId = planId;
    this.startDate = LocalDate.parse(startDate); // yyyy-MM-dd
    this.trialPeriodMonths = trialPeriodMonths;
  }
}