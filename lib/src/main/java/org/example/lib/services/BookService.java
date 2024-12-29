package org.example.lib.services;

import org.example.lib.models.Book;
import org.example.lib.repositories.IBookRepo;
import org.example.lib.repositories.impl.BookRepoImpl;

import java.util.List;

public class BookService {
    private final IBookRepo bookRepo;

    public BookService() {
        bookRepo = new BookRepoImpl();
    }

    public void createNewBook(Book book) {
        bookRepo.save(book);
    }

    public void deleteBook(Book book) {
        bookRepo.delete(book);
    }

    public void updateBook(Book book) {
        bookRepo.save(book);
    }

    public List<Book> getAllBook() {
        return bookRepo.findAll();
    }

    public List<Book> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return bookRepo.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword)
                        || book.getAuthor().toLowerCase().contains(lowerKeyword)
                        || book.getPublisher().toLowerCase().contains(lowerKeyword)
                        || book.getCategory().toLowerCase().contains(lowerKeyword)
                        || book.getId().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    public String generateBookId() {
        return String.format("B%04d", (int) (Math.random() * 10000));
    }


    public boolean isExistBook(String bookId) {
        return bookRepo.findById(bookId) != null;
    }

    public List<String> getBookId() {
        return bookRepo.findAll().stream().map(b -> String.format("%s - %s", b.getId(), b.getTitle())).toList();
    }

    public String getBookNameById(String id) {
        return bookRepo.getBookNameById(id);
    }

    public Book getBookById(String id){
        return bookRepo.findById(id);
    }
}
