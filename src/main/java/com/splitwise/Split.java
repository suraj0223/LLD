package com.splitwise;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Split {
    private User user;
    private int amount;

    public Split(User user, int amount) {
        this.user = user;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return user.name() + ": " + amount;
    }
}
