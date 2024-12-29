package org.example.lib.models;

import java.time.LocalDate;
import java.util.Objects;

public class Borrow {
    private String id;
    private String readerId;
    private String bookId;
    private String readerName;
    private String bookName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public Borrow() {
    }

    public Borrow(String id, String readerId, String bookId, String readerName, String bookName, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, String status) {
        this.id = id;
        this.readerId = readerId;
        this.bookId = bookId;
        this.readerName = readerName;
        this.bookName = bookName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id='" + id + '\'' +
                ", readerId='" + readerId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", readerName='" + readerName + '\'' +
                ", bookName='" + bookName + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }






    public static Borrow mapToBorrow(String[] data) {
        return new Borrow(
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                LocalDate.parse(data[5]),
                LocalDate.parse(data[6]),
                data[7] == null || data[7].isEmpty() ? null : LocalDate.parse(data[7]),
                data[8]
        );
    }

    public static String mapToString(Borrow borrow) {
        return String.join(",",
                borrow.getId(),
                borrow.getReaderId(),
                borrow.getBookId(),
                borrow.getReaderName(),
                borrow.getBookName(),
                borrow.getBorrowDate().toString(),
                borrow.getDueDate().toString(),
                borrow.getReturnDate() == null ? "" : borrow.getReturnDate().toString(),
                borrow.getStatus()
        );
    }
}
