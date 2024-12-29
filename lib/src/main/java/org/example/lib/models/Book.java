package org.example.lib.models;

import java.time.LocalDate;

public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private LocalDate publishedDate;
    private int quantity;

    public Book() {
    }



    public Book(String id, String title, String author, String publisher, String category, LocalDate publishedDate, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.publishedDate = publishedDate;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static Book mapToBook(String[] data) {
        return new Book(data[0], data[1], data[2], data[3], data[4], LocalDate.parse(data[5]), Integer.parseInt(data[6]));
    }

    public static String mapToString(Book book) {
        return String.format("%s,%s,%s,%s,%s,%s,%d",
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCategory(),
                book.getPublishedDate(),
                book.getQuantity());
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", category='" + category + '\'' +
                ", publishedDate=" + publishedDate +
                ", quantity=" + quantity +
                '}';
    }
}
