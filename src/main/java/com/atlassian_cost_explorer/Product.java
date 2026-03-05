package com.atlassian_cost_explorer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Product {

  // fields start here
  public String name;
  public Subscription subscription;
}
