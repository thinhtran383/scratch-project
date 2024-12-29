package com.example.library.model;

import java.time.LocalDate;

public class Borrow {
    private String borrowId;
    private String readerId;
    private String bookName;
    private String readerName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String dueDate;
    private String status;
    private String bookId;

    public Borrow() {}

    public Borrow(String borrowId, String readerId, String bookName, String readerName, LocalDate borrowDate, LocalDate returnDate, String dueDate, String status, String bookId) {
        this.borrowId = borrowId;
        this.readerId = readerId;
        this.bookName = bookName;
        this.readerName = readerName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
        this.bookId = bookId;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "borrowId='" + borrowId + '\'' +
                ", readerId='" + readerId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", readerName='" + readerName + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", dueDate='" + dueDate + '\'' +
                ", status='" + status + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }

    public static BorrowBuilder builder() {
        return new BorrowBuilder();
    }

    public static class BorrowBuilder {
        private String borrowId;
        private String readerId;
        private String bookName;
        private String readerName;
        private LocalDate borrowDate;
        private LocalDate returnDate;
        private String dueDate;
        private String status;
        private String bookId;

        public BorrowBuilder borrowId(String borrowId) {
            this.borrowId = borrowId;
            return this;
        }

        public BorrowBuilder readerId(String readerId) {
            this.readerId = readerId;
            return this;
        }

        public BorrowBuilder bookName(String bookName) {
            this.bookName = bookName;
            return this;
        }

        public BorrowBuilder readerName(String readerName) {
            this.readerName = readerName;
            return this;
        }

        public BorrowBuilder borrowDate(LocalDate borrowDate) {
            this.borrowDate = borrowDate;
            return this;
        }

        public BorrowBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowBuilder dueDate(String dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public BorrowBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BorrowBuilder bookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public Borrow build() {
            return new Borrow(borrowId, readerId, bookName, readerName, borrowDate, returnDate, dueDate, status, bookId);
        }
    }
}
