package com.example.library.model;

import java.time.LocalDate;

public class Book {
    private String bookId;
    private String bookName;
    private String author;
    private String category;
    private int quantity;
    private LocalDate publisher;




    public Book() {}

    public Book(String bookId, String bookName, String author, String category, int quantity, LocalDate publisher) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.publisher = publisher;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPublisher() {
        return publisher;
    }

    public void setPublisher(LocalDate publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", publisher=" + publisher +
                '}';
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public static class BookBuilder {
        private String bookId;
        private String bookName;
        private String author;
        private String category;
        private int quantity;
        private LocalDate publisher;

        public BookBuilder() {}

        public BookBuilder bookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public BookBuilder bookName(String bookName) {
            this.bookName = bookName;
            return this;
        }

        public BookBuilder author(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder category(String category) {
            this.category = category;
            return this;
        }

        public BookBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BookBuilder publisher(LocalDate publisher) {
            this.publisher = publisher;
            return this;
        }

        public Book build() {
            return new Book(bookId, bookName, author, category, quantity, publisher);
        }
    }
}
    