package com.amazonlocker;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccessToken {
    private final String code;
    private final LocalDateTime expiration;
    private final Compartment compartment;
    private final Package pkg;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }
}
