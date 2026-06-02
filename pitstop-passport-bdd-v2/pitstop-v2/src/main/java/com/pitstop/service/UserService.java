package com.pitstop.service;

import com.pitstop.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<String, User> userDb = new HashMap<>();
    private int nextId = 1;

    public User register(String username, String email, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Imeto ne mozhe da e prazno.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Nevaliden email adres.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Parolata tryabva da e pone 6 simvola.");
        }
        if (userDb.containsKey(email)) {
            throw new IllegalArgumentException("Email adresat veche e registriran.");
        }
        String passwordHash = "hashed_" + password;
        User user = new User(nextId++, username, email, passwordHash);
        userDb.put(email, user);
        return user;
    }

    public String login(String email, String password) {
        if (!userDb.containsKey(email)) {
            throw new IllegalArgumentException("Potrebitelat ne e nameren.");
        }
        User user = userDb.get(email);
        String expectedHash = "hashed_" + password;
        if (!user.getPasswordHash().equals(expectedHash)) {
            throw new IllegalArgumentException("Nevalidna parola.");
        }
        return "TOKEN_" + user.getId() + "_" + System.currentTimeMillis();
    }

    public User findByEmail(String email) {
        return userDb.get(email);
    }

    public void reset() {
        userDb.clear();
        nextId = 1;
    }
}
