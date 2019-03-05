import atm.BankManager;
import atm.FileHandler;
import ui.Interface;

public class Atm {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        BankManager manager = fileHandler.readManagerData();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> fileHandler.saveManagerData(manager)));


        //  TODO interface main invoke
        Interface mainInterface = new Interface(manager);
        mainInterface.activateInterface();
    }
}
