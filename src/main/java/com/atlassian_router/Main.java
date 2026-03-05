package com.atlassian_router;

public class Main {

  public static void main(String[] args) {
    Router r = new Router();

    r.addRoute("/users/:id", "User Profile");
    r.addRoute("/users/:id/orders/:orderId", "Order Detail");
    r.addRoute("/files/*", "Static File");

    System.out.println(r.callRoute("/users/321"));
    System.out.println(r.callRoute("/users/99/orders/5001"));
    System.out.println(r.callRoute("/files/docs/a.pdf"));
    System.out.println(r.callRoute("/no/match"));
  }

}
