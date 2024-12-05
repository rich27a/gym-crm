package com.example.gym.utils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Profile {
    private static final Map<String, Integer> usernameMap = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateUsername(String firstName, String lastName){
        return firstName + "." + lastName;
//        if (usernameMap.containsKey(username)) {
//            int count = usernameMap.get(username) + 1;
//            usernameMap.put(username, count);
//            return username + count;
//        } else {
//            usernameMap.put(username, 1);
//            return username;
//        }
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return password.toString();
    }
}
