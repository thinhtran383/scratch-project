package org.example.lib.repositories;

import org.example.lib.models.Account;

public interface IAccountRepo extends ICrudAction<Account, Integer> {
    Account findByUsername(String username);
}
