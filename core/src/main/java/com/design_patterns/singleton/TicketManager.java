package com.design_patterns.singleton;

public class TicketManager {
  private static TicketManager ticketManagerInstance;

  private TicketManager() {
    // prevented to instantiate this class.
  }

  public static synchronized TicketManager getTicketManagerInstance() {
    if (ticketManagerInstance == null) {
      ticketManagerInstance = new TicketManager();
    }
    return ticketManagerInstance;
  }

  public void bookTicket() {
    System.out.println("Ticket booking done...");
  }
}
