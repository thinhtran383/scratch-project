package org.example.lib.repositories.impl;

import org.example.lib.models.Account;
import org.example.lib.repositories.IAccountRepo;
import org.example.lib.utils.FileUtil;

import java.util.List;

public class AccountRepoImpl implements IAccountRepo {

    private final FileUtil<Account> fileUtil;
    private final List<Account> accounts;
    private static final String FILE_PATH = "src/main/resources/db/accounts.txt";


    public AccountRepoImpl() {
        fileUtil = new FileUtil<>();

        accounts = fileUtil.readFile(FILE_PATH, Account::mapToAccount);
    }

    @Override
    public Account findByUsername(String username) {
        return accounts.stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Account account) {
        Account existingAccount = findByUsername(account.getUsername());

        if (existingAccount != null) {
            existingAccount.setPassword(account.getPassword());
        } else {
            accounts.add(account);
        }

        fileUtil.writeFile(FILE_PATH, accounts, Account::mapToString);
    }



    @Override
    public void delete(Account account) {

    }




    @Override
    public Account findById(Integer id) {
        return null;
    }

    @Override
    public List<Account> findAll() {
        return accounts;
    }


}
