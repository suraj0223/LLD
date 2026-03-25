package com.design_patterns.factory;

public class Main {

  public static void main(String[] args) {
    LaptopFactory laptopFactory = new LaptopFactory();
    Laptop macLaptop = laptopFactory.createLaptop(LaptopType.MAC);

    macLaptop.start();
  }
}
