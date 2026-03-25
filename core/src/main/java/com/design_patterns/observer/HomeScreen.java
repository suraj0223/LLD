package com.design_patterns.observer;

public class HomeScreen implements Observer {

  private final String homeScreen;
  public HomeScreen(String homeScreen) {
    this.homeScreen = homeScreen;
  }

  @Override
  public void update(String message) {
    System.out.println(homeScreen + " updated with temp: " + message);
  }
}
