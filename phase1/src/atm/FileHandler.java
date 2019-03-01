package atm;
import java.io.*;
public class FileHandler {

    private FileOutputStream fileSave;
    private ObjectOutputStream output;
    private FileInputStream fileRead;
    private ObjectOutputStream input;

    public void FileHnadler() throws Exception{
        this.fileSave = new FileOutputStream("Outgoing.txt");
        this.output = new ObjectOutputStream(fileSave);
        this.fileRead = new FileInputStream("Outgoing.txt");
        //this.input = new ObjectInputStream(fileRead);
    }


    public void saveFile(BankManager data) throws Exception{
        output.writeObject(data);
        output.close();
        fileSave.close();
    }

    //TODO: implement readFile()
}
