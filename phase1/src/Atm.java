import atm.BankManager;
import atm.FileHandler;
import ui.Session;

public class Atm {

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        BankManager manager = fileHandler.readManagerData();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> fileHandler.saveManagerData(manager)));

        Session newSession = new Session(manager);

        newSession.performSession();
    }

}
