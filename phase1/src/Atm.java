import atm.BankManager;
import ui.Interface;

public class Atm {
    public static void main(String[] args) {
        Interface mainInterface = new Interface();
        BankManager manager = new BankManager();

        //  TODO manager needs to be passed?

        mainInterface.activateInterface();
    }
}
