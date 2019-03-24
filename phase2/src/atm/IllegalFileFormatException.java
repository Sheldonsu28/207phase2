package atm;

public class IllegalFileFormatException extends Exception {
    public IllegalFileFormatException(ExternalFiles file) {
        super(String.format("File %s is in wrong format or does not exist! Check README.txt for more information!",
                file.getFileName()));
    }
}
