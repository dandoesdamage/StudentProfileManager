package com.example.studentprofilemanager.service;

public final class DashboardStats {

    private final int totalStudents;
    private final int activeCourses;
    private final int totalUsers;
    private final double averageGpa;

    public DashboardStats(int totalStudents, int activeCourses, int totalUsers, double averageGpa) {
        this.totalStudents = totalStudents;
        this.activeCourses = activeCourses;
        this.totalUsers = totalUsers;
        this.averageGpa = averageGpa;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public int getActiveCourses() {
        return activeCourses;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public double getAverageGpa() {
        return averageGpa;
    }
}