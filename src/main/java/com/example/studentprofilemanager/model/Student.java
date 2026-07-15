package com.example.studentprofilemanager.model;

public class Student extends User {

    private String studentId;
    private String course;
    private int yearLevel;
    private String section;
    private double gpa;

    public Student() {
    }

    public Student(int userId,
                   String username,
                   String password,
                   String fullName,
                   String email,
                   String studentId,
                   String course,
                   int yearLevel,
                   String section,
                   double gpa) {

        super(userId, username, password, fullName, email);

        this.studentId = studentId;
        this.course = course;
        this.yearLevel = yearLevel;
        this.section = section;
        this.gpa = gpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public void login() {
        System.out.println("Student Logged In");
    }

    @Override
    public void logout() {
        System.out.println("Student Logged Out");
    }
}