package atm;

public enum ExternalFiles {
    DEPOSIT_FILE("deposit.txt"),
    CASH_ALERT_FILE("alert.txt"),
    BILLING_FILE("outgoing.txt"),
    MANAGER_DATA_FILE("BankData.txt"),
    PAYEE_DATA_FILE("payee.txt"),
    ACCOUNT_CREATION_REQUEST_FILE("accReq.txt"),
    TEST_FILE("testData.txt");

    private final String fileName;

    ExternalFiles(String filename) {
        this.fileName = filename;
    }

    public String getFileName() {
        return fileName;
    }
}
