package com.example.studentprofilemanager.service;

/**
 * Strategy pattern: one shared contract for authenticating a user,
 * regardless of role. AuthenticationService depends on this interface
 * rather than on the concrete admin/student logic directly.
 */
public interface AuthenticationStrategy {
    AuthResult authenticate(String username, String password);
}