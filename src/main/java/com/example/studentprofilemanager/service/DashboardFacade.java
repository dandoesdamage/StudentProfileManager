package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.repository.CourseRepository;
import com.example.studentprofilemanager.repository.StudentRepository;
import com.example.studentprofilemanager.repository.UserRepository;

/**
 * Facade pattern: hides the fact that dashboard statistics require
 * querying three different repositories, exposing one simple call
 * instead. DashboardController depends only on this class, not on
 * StudentRepository / CourseRepository / UserRepository directly.
 */
public final class DashboardFacade {

    public DashboardStats getDashboardStats() {
        int totalStudents = StudentRepository.getInstance().countStudents();
        double averageGpa = StudentRepository.getInstance().getAverageGpa();
        int activeCourses = CourseRepository.getInstance().countCourses();
        int totalUsers = UserRepository.getInstance().countUsers();

        return new DashboardStats(totalStudents, activeCourses, totalUsers, averageGpa);
    }
}