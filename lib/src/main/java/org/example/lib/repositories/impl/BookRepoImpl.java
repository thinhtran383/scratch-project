package org.example.lib.repositories.impl;

import org.example.lib.models.Book;
import org.example.lib.repositories.IBookRepo;
import org.example.lib.utils.FileUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookRepoImpl implements IBookRepo {
    private final List<Book> books;
    private final FileUtil<Book> fileUtil;
    private static final String FILE_PATH = "src/main/resources/db/books.txt";

    public BookRepoImpl() {
        fileUtil = new FileUtil<>();
        books = fileUtil.readFile(FILE_PATH, Book::mapToBook);
    }

    @Override
    public void save(Book book) {
        Book existingBook = findById(book.getId());

        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setPublishedDate(book.getPublishedDate());
            existingBook.setQuantity(book.getQuantity());
            existingBook.setCategory(book.getCategory());
        } else {
            books.add(book);
        }

        fileUtil.writeFile(FILE_PATH, books, Book::mapToString);

    }

    @Override
    public void delete(Book book) {
        books.removeIf(b -> Objects.equals(b.getId(), book.getId()));
        fileUtil.writeFile(FILE_PATH, books, Book::mapToString);
    }


    @Override
    public Book findById(String id) {
        return books.stream()
                .filter(book -> Objects.equals(book.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Book> findAll() {
        books.clear();
        books.addAll(fileUtil.readFile(FILE_PATH, Book::mapToBook));

        return books;
    }


    @Override
    public String getBookNameById(String id) {
        Book book = findById(id);
        return book != null ? book.getTitle() : null;
    }

    @Override
    public int getQuantityById(String id) {
        Book book = findById(id);
        return book != null ? book.getQuantity() : 0;
    }


}
