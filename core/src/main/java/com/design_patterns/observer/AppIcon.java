package com.design_patterns.observer;

public class AppIcon implements Observer {

    private final String name;

    public AppIcon(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
      System.out.println(name + " updated with temp: " + message);
    }
}

