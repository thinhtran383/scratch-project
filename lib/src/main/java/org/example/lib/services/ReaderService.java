package org.example.lib.services;

import org.example.lib.models.Reader;
import org.example.lib.repositories.IReaderRepo;
import org.example.lib.repositories.impl.ReaderRepoImpl;

import java.time.LocalDate;
import java.util.List;

public class ReaderService {
    private final IReaderRepo readerRepo;

    public ReaderService() {
        readerRepo = new ReaderRepoImpl();
    }

    public List<Reader> getAllReaders() {
        return readerRepo.findAll();
    }

    public void createNewReader(Reader reader) {
        if (readerRepo.isEmailExist(reader.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (readerRepo.isPhoneNumberExist(reader.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists!");
        }

        readerRepo.save(reader);
    }

    public void deleteReader(Reader reader) {
        readerRepo.delete(reader);
    }

    public void updateReader(Reader reader) {
        Reader existingReader = readerRepo.findById(reader.getId());

        if (existingReader == null) {
            throw new IllegalArgumentException("Reader does not exist!");
        }

        if (!existingReader.getEmail().equals(reader.getEmail())
                && readerRepo.isEmailExist(reader.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        if (!existingReader.getPhoneNumber().equals(reader.getPhoneNumber())
                && readerRepo.isPhoneNumberExist(reader.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại!");
        }

        readerRepo.save(reader);
    }
    public List<Reader> search(String keyword) {
        String lowCaseKeyword = keyword.toLowerCase().trim();

        return readerRepo.findAll().stream()
                .filter(reader -> reader.getFullName().toLowerCase().contains(lowCaseKeyword)
                        || reader.getEmail().toLowerCase().contains(lowCaseKeyword)
                        || reader.getPhoneNumber().toLowerCase().contains(lowCaseKeyword)
                        || reader.getAddress().toLowerCase().contains(lowCaseKeyword)
                        || reader.getId().toLowerCase().contains(lowCaseKeyword))
                .toList();
    }

    public String generateReaderId() {
        return String.format("R%04d", (int) (Math.random() * 10000));
    }

    public boolean isExistReader(String readerId) {
        return readerRepo.findById(readerId) != null;
    }

    public List<String> getReaderId() {
        return readerRepo.findAll().stream().map(r -> String.format("%s - %s", r.getId(), r.getFullName())).toList();
    }


    public String getReaderNameById(String readerId) {
        return readerRepo.getReaderNameById(readerId);
    }
}
