package online.thinhtran.demo.models;

public class Grade {
    private Integer student_id;
    private Integer assignment_id;
    private Float grade;

    public Grade() {
    }

    public Grade(Integer student_id, Integer assignment_id, Float grade) {
        this.student_id = student_id;
        this.assignment_id = assignment_id;
        this.grade = grade;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(Integer assignment_id) {
        this.assignment_id = assignment_id;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "student_id=" + student_id +
                ", assignment_id=" + assignment_id +
                ", grade=" + grade +
                '}';
    }
}
