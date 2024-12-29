drop database if exists student_management;

create database student_management;
use student_management;


-- Create Users Table
CREATE TABLE Users
(
    user_id  INT PRIMARY KEY identity (1,1),
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL,
);

-- Create Students Table
CREATE TABLE Students
(
    student_id    varchar(100) PRIMARY KEY,
    full_name     NVARCHAR(100) NOT NULL,
    gender        varchar(10)       NOT NULL,
    date_of_birth DATE          NOT NULL,
    phone_number  VARCHAR(15)   NOT NULL,
    address       NVARCHAR(255) NOT NULL
);

-- Create Courses Table
CREATE TABLE Courses
(
    course_code VARCHAR(10)   PRIMARY KEY ,
    course_name NVARCHAR(100) NOT NULL,
    credits     INT           NOT NULL
);

-- Create Scores Table
CREATE TABLE Scores
(
    score_id         INT PRIMARY KEY identity (1,1),
    student_id       varchar(100)  NOT NULL,
    course_code        varchar(10)  NOT NULL,
    attendance_score DECIMAL(5, 2) NOT NULL,
    midterm_score    DECIMAL(5, 2) NOT NULL,
    final_score      DECIMAL(5, 2) NOT NULL,
    total_score      AS (attendance_score * 0.1 + midterm_score * 0.4 + final_score * 0.5),
    description      varchar(max),
    FOREIGN KEY (student_id) REFERENCES Students (student_id) ON DELETE CASCADE On UPDATE CASCADE,
    FOREIGN KEY (course_code) REFERENCES Courses (course_code) ON DELETE CASCADE On UPDATE CASCADE
);


-- Insert sample data into Students
INSERT INTO Students (student_id, full_name, gender, date_of_birth, phone_number, address)
VALUES ('S001', 'John Doe', 'Male', '2000-01-01', '1234567890', '123 Main St');

-- Insert sample data into Courses
INSERT INTO Courses (course_code, course_name, credits)
VALUES ('CS101', 'Introduction to Computer Science', 3);

-- Insert sample data into Scores
INSERT INTO Scores (student_id, course_code, attendance_score, midterm_score, final_score, description)
VALUES ('S001', 'CS101', 9.0, 8.5, 9.0, 'Good performance');
insert into Users (username, password) values ('1', 'c4ca4238a0b923820dcc509a6f75849b');
