package com.studentmanagement.services;

import com.studentmanagement.models.Score;
import com.studentmanagement.utils.DbConnect;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ScoreService {
    private final DbConnect dbConnect;

    public ScoreService() {
        this.dbConnect = DbConnect.getInstance();
    }

    public List<String> getStudentIds() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT student_id FROM students";
        try(ResultSet rs = dbConnect.executeQuery(sql)) {
            while(rs.next()) {
                String studentId = rs.getString("student_id");
                result.add(studentId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getCourseIds() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT course_code FROM courses";
        try (ResultSet rs = dbConnect.executeQuery(sql)) {
            while (rs.next()) {
                String courseId = rs.getString("course_code");
                result.add(courseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Score> getScores() {
        List<Score> result = new ArrayList<>();
        String sql = "SELECT * FROM scores";
        try (ResultSet rs = dbConnect.executeQuery(sql)) {
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String courseId = rs.getString("course_code");
                double attendance = rs.getDouble("attendance_score");
                double midTerm = rs.getDouble("midterm_score");
                double finalTerm = rs.getDouble("final_score");
                double total = rs.getDouble("total_score");
                String description = rs.getString("description");

                Score score = new Score(studentId, courseId, attendance, midTerm, finalTerm, total, description);
                result.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    public void addScore(Score score) throws IllegalArgumentException {
        if (isExistScore(score)) {
            throw new IllegalArgumentException("Score already exists");
        }
        String sql = "INSERT INTO scores (student_id, course_code, attendance_score, midterm_score, final_score, description) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dbConnect.executeUpdate(sql, score.getStudentId(), score.getCourseId(), score.getAttendance(), score.getMidTerm(), score.getFinalTerm(), score.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Add score failed");
        }
    }

    public void updateScore(Score score) throws IllegalArgumentException {
        String sql = "UPDATE scores SET attendance_score = ?, midterm_score = ?, final_score = ?, description = ? WHERE student_id = ? AND course_code = ?";
        try {
            int rowsAffected = dbConnect.executeUpdate(sql, score.getAttendance(), score.getMidTerm(), score.getFinalTerm(), score.getDescription(), score.getStudentId(), score.getCourseId());
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Score does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Update score failed");
        }
    }

    public void deleteScore(Score score) throws IllegalArgumentException {
        if (!isExistScore(score)) {
            throw new IllegalArgumentException("Score does not exist");
        }
        String sql = "DELETE FROM scores WHERE student_id = ? AND course_code = ?";
        try {
            dbConnect.executeUpdate(sql, score.getStudentId(), score.getCourseId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Delete score failed");
        }
    }

    private boolean isExistScore(Score score) {
        String sql = "SELECT * FROM scores WHERE student_id = ? AND course_code = ?";
        try (ResultSet rs = dbConnect.executeQuery(sql, score.getStudentId(), score.getCourseId())) {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
