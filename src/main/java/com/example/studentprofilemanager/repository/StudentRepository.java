package com.example.studentprofilemanager.repository;

import com.example.studentprofilemanager.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private final List<Student> studentList = new ArrayList<>();

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public List<Student> getAllStudents() {
        return studentList;
    }

    public void deleteStudent(Student student) {
        studentList.remove(student);
    }

    public void updateStudent(int index, Student student) {
        studentList.set(index, student);
    }

    public Student findStudentById(String studentId) {

        for (Student student : studentList) {

            if (student.getStudentId().equals(studentId)) {

                return student;

            }

        }

        return null;

    }
}