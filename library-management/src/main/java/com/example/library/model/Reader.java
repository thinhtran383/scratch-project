package com.example.library.model;

import java.time.LocalDate;

public class Reader {
    private String readerId;
    private String readerName;
    private String readerEmail;
    private String readerPhone;
    private LocalDate readerDOB;
    private String readerAddress;
    private boolean isBlocked;
    private String username;
    private int userId;

    public Reader() {}

    public Reader(String readerId, String readerName, String readerEmail, String readerPhone, LocalDate readerDOB, String readerAddress, boolean isBlocked, String username, int userId) {
        this.readerId = readerId;
        this.readerName = readerName;
        this.readerEmail = readerEmail;
        this.readerPhone = readerPhone;
        this.readerDOB = readerDOB;
        this.readerAddress = readerAddress;
        this.isBlocked = isBlocked;
        this.username = username;
        this.userId = userId;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderEmail() {
        return readerEmail;
    }

    public void setReaderEmail(String readerEmail) {
        this.readerEmail = readerEmail;
    }

    public String getReaderPhone() {
        return readerPhone;
    }

    public void setReaderPhone(String readerPhone) {
        this.readerPhone = readerPhone;
    }

    public LocalDate getReaderDOB() {
        return readerDOB;
    }

    public void setReaderDOB(LocalDate readerDOB) {
        this.readerDOB = readerDOB;
    }

    public String getReaderAddress() {
        return readerAddress;
    }

    public void setReaderAddress(String readerAddress) {
        this.readerAddress = readerAddress;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "readerId='" + readerId + '\'' +
                ", readerName='" + readerName + '\'' +
                ", readerEmail='" + readerEmail + '\'' +
                ", readerPhone='" + readerPhone + '\'' +
                ", readerDOB=" + readerDOB +
                ", readerAddress='" + readerAddress + '\'' +
                ", isBlocked=" + isBlocked +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }

    public static ReaderBuilder builder() {
        return new ReaderBuilder();
    }

    public static class ReaderBuilder {
        private String readerId;
        private String readerName;
        private String readerEmail;
        private String readerPhone;
        private LocalDate readerDOB;
        private String readerAddress;
        private boolean isBlocked;
        private String username;
        private int userId;

        public ReaderBuilder readerId(String readerId) {
            this.readerId = readerId;
            return this;
        }

        public ReaderBuilder readerName(String readerName) {
            this.readerName = readerName;
            return this;
        }

        public ReaderBuilder readerEmail(String readerEmail) {
            this.readerEmail = readerEmail;
            return this;
        }

        public ReaderBuilder readerPhone(String readerPhone) {
            this.readerPhone = readerPhone;
            return this;
        }

        public ReaderBuilder readerDOB(LocalDate readerDOB) {
            this.readerDOB = readerDOB;
            return this;
        }

        public ReaderBuilder readerAddress(String readerAddress) {
            this.readerAddress = readerAddress;
            return this;
        }

        public ReaderBuilder isBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public ReaderBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ReaderBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Reader build() {
            return new Reader(readerId, readerName, readerEmail, readerPhone, readerDOB, readerAddress, isBlocked, username, userId);
        }
    }
}
