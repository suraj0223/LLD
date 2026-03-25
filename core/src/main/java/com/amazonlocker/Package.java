package com.amazonlocker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Package {
    private final String orderId;
    private final Size size;
    private final String receiverContact;
}
