package atm;

import ui.MainFrame;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for handling that need to be save to files outside.
 */
public class FileHandler implements Serializable {
    private final String path;

    public FileHandler() {
        path = new File("phase2/data/").getAbsolutePath() + '\\';
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
        } catch (IOException | ClassNotFoundException f) {
            MainFrame.showErrorMessage("Manager File corrupted / does not exist. Manager recovery failed.");
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
        try {
            if (file.exists()) {
                content = readFrom(extFile);
                content.add(contents);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String item : content) {
                    writer.write(item);
                    writer.newLine();
                }
                writer.close();

            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(contents);
                writer.close();

            }
        } catch (IOException e) {
            MainFrame.showErrorMessage(String.format("Something went wrong, file %s not saved.",
                    extFile.getFileName()));
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
            MainFrame.showErrorMessage(String.format("File %s not found.", file.getFileName()));
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

    public void deleteFirstLine(ExternalFiles file) {
        ArrayList<String> data = readFrom(file);
        data.remove(0);
        clearFile(file);

        StringBuilder strData = new StringBuilder();
        for (String line : data) {
            strData.append(line).append("\n");
        }

        saveTo(file, strData.toString());
    }

    public boolean checkFileExist(String filename) {
        File file = new File(path + filename);
        return file.exists();
    }

}
