package atm;

import java.io.*;
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
        path = new File("phase1/data/").getAbsolutePath() + '\\';
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
            FileOutputStream fileSave = new FileOutputStream(path + "BankData.txt");
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
            System.out.println("There is a problem when reading the file, file information did not load.");
        } catch (ClassNotFoundException e) {
            System.out.println("The require classes is not found");
        }
        return information;
    }

   public void saveTo(String filename,String contents){
        ArrayList<String> content;
        File file = new File(path + filename);
        if (file.exists()){
            try {
                content = readFrom(filename);
                content.add(contents );
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String item : content) {
                    writer.write(item);
                    writer.newLine();
                }
                writer.close();
            }catch(IOException e) {
                System.out.println("Something went wrong, information not saved.");
            }
        }else{
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(contents);
                writer.close();
            }catch(IOException e){
                System.out.println("Something went wrong, information not saved.");
            }

        }
    }

    public ArrayList<String> readFrom(String filename) {
        ArrayList<String> content = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path + filename));
            while (scanner.hasNext()) {
                content.add(scanner.nextLine());
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found, return null");
        }
        return content;
    }
}
