package com.studentmanagement.models;

public class Score {
    private String studentId;
    private String courseId;
    private double attendance;
    private double midTerm;
    private double finalTerm;
    private double total;
    private String description;

    public Score() {
    }

    public Score(String studentId, String courseId, double attendance, double midTerm, double finalTerm, double total, String description) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.attendance = attendance;
        this.midTerm = midTerm;
        this.finalTerm = finalTerm;
        this.total = total;
        this.description = description;
    }

    public Score(String studentId, String courseId, double attendance, double midTerm, double finalTerm, String description) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.attendance = attendance;
        this.midTerm = midTerm;
        this.finalTerm = finalTerm;
        this.description = description;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getMidTerm() {
        return midTerm;
    }

    public void setMidTerm(double midTerm) {
        this.midTerm = midTerm;
    }

    public double getFinalTerm() {
        return finalTerm;
    }

    public void setFinalTerm(double finalTerm) {
        this.finalTerm = finalTerm;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Score{" +
                "studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", attendance=" + attendance +
                ", midTerm=" + midTerm +
                ", finalTerm=" + finalTerm +
                ", total=" + total +
                ", description='" + description + '\'' +
                '}';
    }
}
