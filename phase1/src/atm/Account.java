package atm;

import java.util.ArrayList;

abstract class Account {

    abstract int getBalance();

    abstract ArrayList<Transaction> getTransaction();


}
