package com.design_patterns.observer;

public class WeatherStation extends Subject {

    public void setTemperature(String temperature) {
        // Notifying all the observers.
        notifyObservers(temperature);
    }

}

