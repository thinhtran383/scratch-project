package com.example.library.repository;

import com.example.library.model.Author;
import javafx.collections.ObservableList;

public interface IAuthorRepository {
    ObservableList<Author> getAllAuthor();
    String getAuthorIdByName(String author);
}
