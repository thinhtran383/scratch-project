package com.studentmanagement.services;

import com.studentmanagement.models.Course;
import com.studentmanagement.utils.DbConnect;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private final DbConnect dbConnect;

    public CourseService() {
        this.dbConnect = DbConnect.getInstance();
    }

    public List<Course> getAllCourses() {
        List<Course> result = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (ResultSet rs = dbConnect.executeQuery(sql)) {
            while (rs.next()) {
                String courseId = rs.getString("course_code");
                String courseName = rs.getString("course_name");
                int credit = rs.getInt("credits");

                Course course = new Course(courseId, courseName, credit);
                result.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;

    }

    private boolean isExistCourse(Course course) {
        String sql = "SELECT * FROM courses WHERE course_code = ?";
        try (ResultSet rs = dbConnect.executeQuery(sql, course.getId())) {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addCourse(Course course) throws IllegalArgumentException {
        if (isExistCourse(course)) {
            throw new IllegalArgumentException("Course already exists");
        }
        String sql = "INSERT INTO courses (course_code, course_name, credits) VALUES (?, ?, ?)";
        try {
            dbConnect.executeUpdate(sql, course.getId(), course.getName(), course.getCredit());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Add course failed");
        }
    }

    public void deleteCourse(Course course) throws IllegalArgumentException {
        if (!isExistCourse(course)) {
            throw new IllegalArgumentException("Course does not exist");
        }
        String sql = "DELETE FROM courses WHERE course_code = ?";
        try {
            dbConnect.executeUpdate(sql, course.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Delete course failed");
        }
    }

    public void updateCourse(Course course, String newCourseCode) throws IllegalArgumentException {
        if (!isExistCourse(course)) {
            throw new IllegalArgumentException("Course does not exist");
        }

        System.out.println(course.getCredit());
        if (!course.getId().equals(newCourseCode) && isExistCourse(new Course(newCourseCode, "", 0))) {
            throw new IllegalArgumentException("Course code already exists");
        }

        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ? WHERE course_code = ?";
        try {
            dbConnect.executeUpdate(sql, newCourseCode, course.getName(), course.getCredit(), course.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Update course failed");
        }
    }


}
