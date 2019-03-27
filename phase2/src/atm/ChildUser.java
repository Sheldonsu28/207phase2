package atm;

import account.Account;

import java.util.List;

public class ChildUser extends User {

    private User parentAccount;

    ChildUser(String username, String password, User parent){
        super(username,password);
        this.parentAccount = parent;
    }


    @Override
    <T extends Account> void addAccount(T account) {
        System.out.println("You cannot add account by yourself.");
    }

    public User getParentAccount(){
        return this.parentAccount;
    }

}
