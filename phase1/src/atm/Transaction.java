package atm;

abstract class Transaction {

    abstract void perform();

    abstract void cancel();
}
