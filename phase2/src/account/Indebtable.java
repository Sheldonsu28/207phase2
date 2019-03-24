package account;

/**
 * Characterizes any account that allows being in debt (negative balance).
 */
public interface Indebtable {

    /**
     * Constant represents universal default debt limit.
     */
    double DEFAULT_DEBT_LIMIT = 1000000.0;

    /**
     * @return the debt limit of this account
     */
    double getDebtLimit();

    /**
     * Set the debt limit of this account.
     *
     * @param debtLimit the new debt limit to be set. If it is negative, it will be converted to positive
     *                  since debt limit is defined as being positive.
     */
    void setDebtLimit(double debtLimit);
}
