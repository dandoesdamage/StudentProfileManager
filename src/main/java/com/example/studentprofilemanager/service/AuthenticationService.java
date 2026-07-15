package com.example.studentprofilemanager.service;

public class AuthenticationService {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";

    public boolean authenticate(String username, String password) {

        return USERNAME.equals(username)
                && PASSWORD.equals(password);

    }

}