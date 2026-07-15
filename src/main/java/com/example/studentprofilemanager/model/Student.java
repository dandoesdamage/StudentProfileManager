package com.example.studentprofilemanager.model;

public class Student extends User {

    private String studentId;
    private String course;
    private int yearLevel;
    private String section;
    private double gpa;

    /* -----------------------------------------------------------------
       Additional profile fields (additive — used by the management,
       add and update screens). Kept nullable so the existing
       constructor and any future JDBC mapping remain compatible.
       ----------------------------------------------------------------- */
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String contactNumber;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Convenience display name built from the profile parts.
     * Falls back to the inherited fullName when the parts are empty,
     * so existing data keeps working. Used by the TableView columns.
     */
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.isBlank()) {
            sb.append(lastName).append(", ");
        }
        if (firstName != null && !firstName.isBlank()) {
            sb.append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            sb.append(" ").append(middleName.charAt(0)).append(".");
        }
        String composed = sb.toString().trim();
        if (composed.isEmpty() || composed.equals(",")) {
            return fullName != null ? fullName : "";
        }
        return composed;
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