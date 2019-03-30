package atm;

public class SymbolNotFoundException extends Exception {
    SymbolNotFoundException(String companyName) {
        super(String.format("Symbol of %s can not be found!", companyName));
    }

}
