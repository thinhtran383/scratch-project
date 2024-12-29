package org.example.lib.repositories.impl;

import org.example.lib.models.Book;
import org.example.lib.models.Borrow;
import org.example.lib.models.Reader;
import org.example.lib.repositories.IBorrowRepo;
import org.example.lib.utils.FileUtil;

import java.time.LocalDate;
import java.util.List;

public class BorrowRepoImpl implements IBorrowRepo {
    private final List<Borrow> borrows;
    private final FileUtil<Borrow> fileUtil;
    private static final String FILE_PATH = "src/main/resources/db/borrows.txt";

    public BorrowRepoImpl() {
        fileUtil = new FileUtil<>();
        borrows = fileUtil.readFile(FILE_PATH, Borrow::mapToBorrow);
    }

    @Override
    public void save(Borrow borrow) {
        borrows.add(borrow);
        fileUtil.writeFile(FILE_PATH, borrows, Borrow::mapToString);
    }

    @Override
    public void delete(Borrow borrow) {
    }

    @Override
    public Borrow findById(String id) {
        return borrows.stream().filter(borrow -> borrow.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Borrow> findAll() {
        borrows.clear();
        borrows.addAll(fileUtil.readFile(FILE_PATH, Borrow::mapToBorrow));

        return borrows;
    }

    @Override
    public void returnBook(Borrow borrow) {
        borrows.stream().filter(b -> b.getId().equals(borrow.getId()))
                .findFirst()
                .ifPresent(b -> {
                    b.setReturnDate(LocalDate.now());
                    b.setStatus("Đã trả");
                });

        fileUtil.writeFile(FILE_PATH, borrows, Borrow::mapToString);

    }

    @Override
    public List<Borrow> findBorrowedReaders(String readerId) {
        borrows.clear();
        findAll();

        return borrows.stream().filter(borrow -> borrow.getReaderId().equals(readerId))
                .toList();
    }

    @Override
    public void renewBorrow(String id) {
        borrows.stream().filter(b -> b.getId().equals(id))
                .findFirst()
                .ifPresent(b -> {
                    b.setDueDate(b.getDueDate().plusDays(7));
                });

        fileUtil.writeFile(FILE_PATH, borrows, Borrow::mapToString);
    }

    @Override
    public int countBorrowedNotReturned(String readerId) {
        return (int) borrows.stream().filter(borrow -> borrow.getReaderId().equals(readerId) && borrow.getStatus().equals("Chưa trả")).count();
    }

    @Override
    public void updateStatusToLate(List<String> id) {
        borrows.stream().filter(b -> id.contains(b.getId()))
                .forEach(b -> b.setStatus("Quá hạn"));

        fileUtil.writeFile(FILE_PATH, borrows, Borrow::mapToString);
    }
}
