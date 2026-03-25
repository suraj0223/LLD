package com.design_patterns.factory;

public class LaptopFactory {
  public Laptop createLaptop(LaptopType laptopType) {
    return switch (laptopType) {
      case MAC -> new MacLaptop();
      case WINDOWS -> new WindowsLaptop();
    };
  }
}
