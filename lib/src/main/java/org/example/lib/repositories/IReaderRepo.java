package org.example.lib.repositories;

import org.example.lib.models.Reader;

public interface IReaderRepo extends ICrudAction<Reader, String> {
    String getReaderNameById(String id);

    boolean isEmailExist(String email);
    boolean isPhoneNumberExist(String phoneNumber);

}
