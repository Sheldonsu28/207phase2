package atm;

import java.io.*;

/**
 * This class is responsible for handling that need to be save to files outside.
 */
public class FileHandler {

    private FileOutputStream fileSave;
    private ObjectOutputStream output;
    private FileInputStream fileRead;
    private ObjectInputStream input;

    public FileHandler() {
        try {
            this.fileSave = new FileOutputStream("Outgoing.txt");
            this.output = new ObjectOutputStream(fileSave);
            this.fileRead = new FileInputStream("Outgoing.txt");
            this.input = new ObjectInputStream(fileRead);
        } catch (IOException e) {
            System.out.println("FileHandler initialization failed, operation can not be completed");
        }
    }

    /**
     * Serialize the object and save it to a file.
     *
     * @param data A bankManager object.
     */
    public void saveFile(BankManager data) {
        try {
            output.writeObject(data);
            output.close();
            fileSave.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the object store in the file and deserialize the object.
     *
     * @return return a Bank manager object.
     */
    public BankManager readFile() {
        BankManager information = null;
        try {
            information = (BankManager) input.readObject();
            fileRead.close();
            input.close();
        } catch (IOException a) {
            System.out.println("There is a problem when reading the file, file information didi not load.");
        } catch (ClassNotFoundException e) {
            System.out.println("The require classes is not found");
        }
        return information;
    }

}
