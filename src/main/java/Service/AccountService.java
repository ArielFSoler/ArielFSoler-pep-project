package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){

        if(account.getUsername() == "" || account.getUsername() == null || account.getPassword().length() < 4)
            return null;
        else
            return accountDAO.insertAccount(account);
    }

    public Account loginUser(Account account){
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
