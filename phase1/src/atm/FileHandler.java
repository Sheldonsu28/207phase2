package atm;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for handling that need to be save to files outside.
 */
public class FileHandler {

    private FileInputStream fileRead;
    private ObjectInputStream input;
    private String path;

    public FileHandler() {
        path = new File("phase1/data/").getAbsolutePath();
    }

    public String getPath(){
        return this.path;
    }

    /**
     * Serialize the object and save it to a file.
     *
     * @param data A bankManager object.
     */
    public void saveManagerData(BankManager data) {
        try {
            FileOutputStream fileSave = new FileOutputStream(path + "BankData");
            ObjectOutputStream output = new ObjectOutputStream(fileSave);
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
    public BankManager readManagerData() {
        BankManager information = null;
        try {
            FileInputStream fileRead = new FileInputStream(path);
            ObjectInputStream input = new ObjectInputStream(fileRead);
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

   public void saveto(String filename,String contents){




    }

    public ArrayList readFrom(String filename){
        ArrayList content = new ArrayList();
        Scanner scanner = new Scanner(path + filename);
        while (scanner.hasNextLine()){
            content.add(scanner.nextLine());
        }
        return content;
    }



}
