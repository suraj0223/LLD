package com.design_patterns.singleton;

public class Main {

  public static void main(String[] args) {
    TicketManager ticketManager = TicketManager.getTicketManagerInstance();

    ticketManager.bookTicket();
  }
}
