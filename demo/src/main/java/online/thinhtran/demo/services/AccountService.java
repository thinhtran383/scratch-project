package online.thinhtran.demo.services;

import online.thinhtran.demo.models.Account;
import online.thinhtran.demo.repositories.AccountRepo;

public class AccountService { // xu ly du lieu
    private final AccountRepo accountRepo;

    public AccountService(){
        accountRepo = new AccountRepo();
    }

    public boolean login(Account account){
        return accountRepo.isValidAccount(account);
    }
}
