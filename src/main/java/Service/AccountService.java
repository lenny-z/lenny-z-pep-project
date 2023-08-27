package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        if (account.getUsername() != ""
                && account.getPassword().length() >= 4
                && !accountDAO.usernameExists(account.getUsername())) {
            return accountDAO.insertAccount(account);
        }else{
            return null;
        }
    }

    public Account getAccountByUsernameAndPassword(String username, String password) {
        return accountDAO.selectAccountByUsernameAndPassword(username, password);
    }
}
