package org.example.lib.repositories.impl;

import org.example.lib.models.Book;
import org.example.lib.models.Reader;
import org.example.lib.repositories.IReaderRepo;
import org.example.lib.utils.FileUtil;

import java.util.List;

public class ReaderRepoImpl implements IReaderRepo {
    private final List<Reader> readers;
    private final FileUtil<Reader> fileUtil;
    private static final String FILE_PATH = "src/main/resources/db/readers.txt";

    public ReaderRepoImpl() {
        fileUtil = new FileUtil<>();
        readers = fileUtil.readFile(FILE_PATH, Reader::mapToReader);
    }

    @Override
    public void save(Reader reader) {
        Reader existingReader = findById(reader.getId());

        if (existingReader != null) {
            existingReader.setFullName(reader.getFullName());
            existingReader.setPhoneNumber(reader.getPhoneNumber());
            existingReader.setEmail(reader.getEmail());
            existingReader.setAddress(reader.getAddress());
            existingReader.setDob(reader.getDob());
        } else {
            readers.add(reader);
        }

        fileUtil.writeFile(FILE_PATH, readers, Reader::mapToString);
    }

    @Override
    public void delete(Reader reader) {
        readers.removeIf(r -> r.getId().equals(reader.getId()));
        fileUtil.writeFile(FILE_PATH, readers, Reader::mapToString);
    }

    @Override
    public Reader findById(String id) {
        return readers.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Reader> findAll() {
        readers.clear();
        readers.addAll(fileUtil.readFile(FILE_PATH, Reader::mapToReader));

        return readers;
    }

    @Override
    public String getReaderNameById(String id) {
        Reader reader = findById(id);
        return reader != null ? reader.getFullName() : null;
    }

    @Override
    public boolean isEmailExist(String email) {
        return readers.stream().anyMatch(r -> r.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean isPhoneNumberExist(String phoneNumber) {
        return readers.stream().anyMatch(r -> r.getPhoneNumber().equals(phoneNumber));
    }

}
