package com.design_patterns.observer;

public class Main {

  public static void main(String[] args) {
    WeatherStation station = new WeatherStation();

    AppIcon appIcon = new AppIcon("App_Icon");
    HomeScreen homeScreen = new HomeScreen("Home_Screen");

    station.addObserver(appIcon);
    station.addObserver(homeScreen);

    station.setTemperature("30°C");  // both get notified

    station.removeObserver(appIcon);

    station.setTemperature("25°C");  // only Home Screen temp got updated
  }
}
