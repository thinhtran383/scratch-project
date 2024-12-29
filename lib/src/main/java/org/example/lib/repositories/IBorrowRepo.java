package org.example.lib.repositories;

import org.example.lib.models.Borrow;
import org.example.lib.models.Reader;

import java.util.List;

public interface IBorrowRepo extends ICrudAction<Borrow, String> {
    void returnBook(Borrow borrow);
    List<Borrow> findBorrowedReaders(String readerId);
    void renewBorrow(String id);
    int countBorrowedNotReturned(String readerId);

    void updateStatusToLate(List<String> id);
}
