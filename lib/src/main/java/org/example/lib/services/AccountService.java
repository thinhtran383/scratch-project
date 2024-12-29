package org.example.lib.services;

import org.example.lib.models.Account;
import org.example.lib.repositories.IAccountRepo;
import org.example.lib.repositories.impl.AccountRepoImpl;
import org.example.lib.utils.UserContex;

public class AccountService {
    private final IAccountRepo accountRepo;

    public AccountService() {
        accountRepo = new AccountRepoImpl();
    }


    public boolean login(String username, String password) {
        Account account = accountRepo.findByUsername(username);

        if (account == null) {
            return false;
        }

        if (account.getPassword().equals(password)) {
            UserContex.getInstance().setUsername(username);
            UserContex.getInstance().setPassword(password);

            return true;
        }

        return false;
    }


    public void changePassword(String newPassword) {
        Account account = accountRepo.findByUsername(UserContex.getInstance().getUsername());

        if (account != null) {
            account.setPassword(newPassword);

            accountRepo.save(account);
        }
    }


}
