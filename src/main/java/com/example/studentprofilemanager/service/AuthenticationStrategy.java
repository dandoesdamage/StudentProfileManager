package com.example.studentprofilemanager.service;

public interface AuthenticationStrategy {
    AuthResult authenticate(String username, String password);
}