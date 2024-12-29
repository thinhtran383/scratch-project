package org.example.lib.repositories;

import org.example.lib.models.Book;

import java.util.Optional;

public interface IBookRepo extends ICrudAction<Book, String> {
    String getBookNameById(String id);
    int getQuantityById(String id);
}
