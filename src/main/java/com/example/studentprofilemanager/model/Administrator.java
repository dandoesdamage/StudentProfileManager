package com.example.studentprofilemanager.model;

public class Administrator extends User {

    public Administrator() {
    }

    public Administrator(int userId, String username,
                         String password, String fullName,
                         String email) {

        super(userId, username, password, fullName, email);
    }

    @Override
    public void login() {
        System.out.println("Administrator Logged In");
    }

    @Override
    public void logout() {
        System.out.println("Administrator Logged Out");
    }
}