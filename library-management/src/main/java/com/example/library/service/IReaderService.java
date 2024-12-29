package com.example.library.service;

import com.example.library.model.Reader;
import javafx.collections.ObservableList;

public interface IReaderService {
    ObservableList<Reader> getAllReaders();
    ObservableList<String> getAllReaderId();
    String getReaderNameById(String readerId);
    void saveReader(Reader reader);
    void updateReader(Reader reader) throws Exception;
    void deleteReader(Reader reader);
    String getReaderId();
    Reader getReaderByUsername(String username);
}
