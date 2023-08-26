package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    public boolean usernameExists(String username){
        return accountDAO.usernameExists(username);
    }
}
