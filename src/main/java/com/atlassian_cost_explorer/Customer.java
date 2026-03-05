package com.atlassian_cost_explorer;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Customer {

  private String customerId;
  private List<Product> products;
}