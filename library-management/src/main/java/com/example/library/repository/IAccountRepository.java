package com.example.library.repository;

import com.example.library.model.Account;

import java.util.Map;
import java.util.Optional;

public interface IAccountRepository { // ban thiet ke
    Optional<Account> getAccountAndRoleByUsername(String username);
    boolean isExistUsername(String username);
    void save(Account account);
    boolean isBlocked(String username);
    Map<String, String> getAccountInfoByEmail(String email);
    int getUserIdByUsername(String username);
}
