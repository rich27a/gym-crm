package com.example.gym.utils;

import java.util.UUID;

public class TransactionContext {
    private static final ThreadLocal<String> transactionId = new ThreadLocal<>();

    public static String getTransactionId() {
        return transactionId.get();
    }

    public static void setTransactionId(String id) {
        transactionId.set(id);
    }

    public static void clear() {
        transactionId.remove();
    }

    public static String generateTransactionId() {
        String id = UUID.randomUUID().toString();
        setTransactionId(id);
        return id;
    }

}
