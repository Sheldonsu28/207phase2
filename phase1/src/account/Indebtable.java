package account;

public interface Indebtable {

    double DEFAULT_DEBT_LIMIT = 1000000.0;

    double getDebtLimit();

    void setDebtLimit(double debtLimit);
}
