package com.inmemory_key_value;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        InMemoryCache<String, String> cache = new InMemoryCache<>(1000);
        
        cache.put("user1", "Mr. Kumar", 2000);
        System.out.println(cache.get("user1")); // Output: Mr. Kumar

        Thread.sleep(2500);
        System.out.println(cache.get("user1")); // Output: null (expired)
    }
}
