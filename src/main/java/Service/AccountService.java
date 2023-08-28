package Service;

import Model.Account;
import DAO.AccountDAO;

import java.sql.SQLException;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) throws SQLException, UserErrorException {
        if (account.getUsername() != ""
                && account.getPassword().length() >= 4
                && !accountDAO.usernameExists(account.getUsername())) {
            return accountDAO.insertAccount(account);
        } else {
            throw new UserErrorException();
        }
    }

    public Account getAccountByUsernameAndPassword(String username, String password) throws SQLException {
        return accountDAO.selectAccountByUsernameAndPassword(username, password);
    }
}
