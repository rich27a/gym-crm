package com.example.gym.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME = TimeUnit.MINUTES.toMillis(5);

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = attempts.getOrDefault(username, new LoginAttempt(0, 0));
        attempt.increment();
        attempt.setLastAttempt(System.currentTimeMillis());
        attempts.put(username, attempt);
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        if (attempt == null) return false;

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            long timeSinceLastAttempt = System.currentTimeMillis() - attempt.getLastAttempt();
            if (timeSinceLastAttempt < LOCK_TIME) {
                return true;
            } else {
                attempts.remove(username);
            }
        }
        return false;
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    private static class LoginAttempt {
        private int attempts;
        private long lastAttempt;

        public LoginAttempt(int attempts, long lastAttempt) {
            this.attempts = attempts;
            this.lastAttempt = lastAttempt;
        }

        public int getAttempts() {
            return attempts;
        }

        public void increment() {
            this.attempts++;
        }

        public long getLastAttempt() {
            return lastAttempt;
        }

        public void setLastAttempt(long lastAttempt) {
            this.lastAttempt = lastAttempt;
        }
    }
}

