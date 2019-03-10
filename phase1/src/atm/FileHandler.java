package atm;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for handling that need to be save to files outside.
 */
public class FileHandler implements Serializable {
    private String path;

    public FileHandler() {
        path = new File("phase1/data/").getAbsolutePath() + '\\';
    }

    public String getPath() {
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
            FileInputStream fileRead = new FileInputStream(path + "BankData.txt");
            ObjectInputStream input = new ObjectInputStream(fileRead);
            information = (BankManager) input.readObject();
            fileRead.close();
            input.close();
        } catch (FileNotFoundException f) {
            System.out.println("File not found, read failed");
        } catch (IOException i) {
            System.out.println("There is a problem when reading the file, file information failed to load.");
        } catch (ClassNotFoundException e) {
            System.out.println("The require classes is not found. Possible corruption in class file!");
        }
        return information;
    }

    /**
     * Save the files to the corresponding file in data folder.
     *
     * @param extFile  the target external file defined in {@link ExternalFiles}
     * @param contents The information you would like to put in the class.
     */
    public void saveTo(ExternalFiles extFile, String contents) {
        String filename = extFile.getFileName();
        ArrayList<String> content;
        File file = new File(path + filename);

        if (file.exists()) {
            try {
                content = readFrom(extFile);
                content.add(contents);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String item : content) {
                    writer.write(item);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Something went wrong, information not saved.");
            }
        } else {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(contents);
                writer.close();
            } catch (IOException e) {
                System.out.println("Something went wrong, information not saved.");
            }

        }

    }

    /**
     * Reads the file that filename designates. If file not exist, return null.
     *
     * @param file the target external file defined in {@link ExternalFiles}
     * @return A ArrayList, each element in the list represent one line in the file, if the file is not found, return null.
     */
    public ArrayList<String> readFrom(ExternalFiles file) {
        String filename = file.getFileName();
        ArrayList<String> content = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path + filename));
            while (scanner.hasNext()) {
                content.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return content;
    }

    public void clearFile(ExternalFiles file) {
        try {
            PrintWriter writer = new PrintWriter(new File(path + file.getFileName()));
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean checkFileExist(String filename) {
        File file = new File(path + filename);
        return file.exists();
    }

}
