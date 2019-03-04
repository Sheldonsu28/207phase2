import atm.BankManager;
import atm.FileHandler;
import ui.Interface;

public class Atm {
    public static void main(String[] args) {
        Interface mainInterface = new Interface();
        FileHandler fileHandler = new FileHandler();

        BankManager manager = fileHandler.readManagerData();

        //  TODO manager needs to be passed?

        mainInterface.activateInterface();
    }
}
