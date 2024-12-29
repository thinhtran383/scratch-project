package org.example.lib.services;

import org.example.lib.models.Book;
import org.example.lib.models.Borrow;
import org.example.lib.repositories.IBorrowRepo;
import org.example.lib.repositories.impl.BorrowRepoImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BorrowService {
    private final IBorrowRepo borrowRepo;
    private final BookService bookService;
    private final ReaderService readerService;

    public BorrowService() {
        borrowRepo = new BorrowRepoImpl();
        bookService = new BookService();
        readerService = new ReaderService();
    }

    public List<Borrow> getAllBorrows() {
        return borrowRepo.findAll();
    }

    public void borrowBook(Borrow borrow) {
        if (!bookService.isExistBook(borrow.getBookId())) {
            throw new IllegalArgumentException("Không tìm thấy sách");
        }

        if (!readerService.isExistReader(borrow.getReaderId())) {
            throw new IllegalArgumentException("Không tìm thấy độc giả");
        }

        if (borrowRepo.countBorrowedNotReturned(borrow.getReaderId()) >= 3) {
            throw new IllegalArgumentException("Độc giả đã mượn quá số lượng sách quy định(3)");
        }

        Book book = bookService.getBookById(borrow.getBookId());

        if (book.getQuantity() == 0) {
            throw new IllegalArgumentException("Sách đã hết");
        }

        book.setQuantity(book.getQuantity() - 1);
        bookService.updateBook(book);

        borrowRepo.save(borrow);
    }

    public void returnBook(Borrow borrow) {
        Book book = bookService.getBookById(borrow.getBookId());

        book.setQuantity(book.getQuantity() + 1);
        bookService.updateBook(book);

        borrowRepo.returnBook(borrow);
    }

    public List<Borrow> findBorrowedReaders(String readerId) {
        return borrowRepo.findBorrowedReaders(readerId);
    }

    public List<Borrow> getAllBookByBorrowId(String id) {
        return borrowRepo.findAll().stream()
                .filter(b -> b.getId().equals(id))
                .toList();
    }

    public String generateBorrowId() {
        return String.format("BR%d04", (int) (Math.random() * 10000));
    }

    public void reNewBorrow(Borrow borrow) {
        borrowRepo.renewBorrow(borrow.getId());
    }

    public List<Borrow> filterByStatus(String status) {
        if (status.equals("Tất cả")) {
            return borrowRepo.findAll();
        }

        return borrowRepo.findAll().stream()
                .filter(borrow -> borrow.getStatus().equals(status))
                .toList();
    }


    public void updateIfLate() {
        List<Borrow> borrows = borrowRepo.findAll();

        List<String> idLate = borrows.stream()
                .filter(borrow -> borrow.getDueDate().isBefore(LocalDate.now()) && borrow.getStatus().equals("Chưa trả"))
                .map(Borrow::getId)
                .toList();

        borrowRepo.updateStatusToLate(idLate);
    }

    public List<Borrow> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return borrowRepo.findAll().stream()
                .filter(borrow -> borrow.getId().toLowerCase().contains(lowerKeyword)
                        || borrow.getBookId().toLowerCase().contains(lowerKeyword)
                        || borrow.getReaderId().toLowerCase().contains(lowerKeyword)
                        || borrow.getBookName().toLowerCase().contains(lowerKeyword)
                        || borrow.getReaderName().toLowerCase().contains(lowerKeyword)
                )
                .toList();
    }
}
