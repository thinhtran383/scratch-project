package com.studentmanagement.services;

import com.studentmanagement.models.Student;
import com.studentmanagement.utils.DbConnect;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private final DbConnect dbConnect;

    public StudentService() {
        this.dbConnect = DbConnect.getInstance();
    }

    public boolean isExistStudent(Student student) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (ResultSet rs = dbConnect.executeQuery(sql, student.getId())) {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getAllStudent(){
        List<Student> result = new ArrayList<>(); // Create a list to store students
        String sql = "SELECT * FROM students";
        try (ResultSet rs = dbConnect.executeQuery(sql)) { // Execute query
            while (rs.next()) { // Loop through result set


                String studentId = rs.getString("student_id");
                String fullName = rs.getString("full_name");
                String gender = rs.getString("gender");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                Student student = new Student(studentId, fullName, gender, dateOfBirth, phoneNumber, address); // Create a new student
                result.add(student); // Add student to list

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addStudent(Student student) throws IllegalArgumentException {
        if (isExistStudent(student)) {
            throw new IllegalArgumentException("Student is already exist");
        }


        String sql = "INSERT INTO students (student_id, full_name, gender, date_of_birth, phone_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dbConnect.executeUpdate(sql, student.getId(), student.getName(), student.getGender(), student.getDob(), student.getPhone(), student.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteStudent(Student student) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try {
            dbConnect.executeUpdate(sql, student.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateStudent(Student student, String newStudentId) throws IllegalArgumentException {

        // Check if the new student ID exists and is different from the current student ID
        if (!student.getId().equals(newStudentId) && isExistStudent(new Student(newStudentId, "", "", null, "", ""))) {
            throw new IllegalArgumentException("New student ID already exists");
        }

        String sql = "UPDATE students SET student_id = ?, full_name = ?, gender = ?, date_of_birth = ?, phone_number = ?, address = ? WHERE student_id = ?";
        try {
            dbConnect.executeUpdate(sql, newStudentId, student.getName(), student.getGender(), student.getDob(), student.getPhone(), student.getAddress(), student.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
