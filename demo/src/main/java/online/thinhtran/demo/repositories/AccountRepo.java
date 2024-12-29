package online.thinhtran.demo.repositories;

import online.thinhtran.demo.models.Account;
import online.thinhtran.demo.utils.DbConnect;

import java.util.List;

public class AccountRepo { // lay du lieu
    private final DbConnect dbConnect = DbConnect.getInstance();

    public boolean isValidAccount(Account account){
        String sql = "select * from Accounts where username = ? and password = ?";

        List<Account> result = dbConnect.executeReturnQuery(sql,
                Account.class,
                account.getUsername(),
                account.getPassword()
        );

        return !result.isEmpty();
    }
}
