package atm;

public class IllegalFileFormatException extends Exception {
    public IllegalFileFormatException(ExternalFiles file) {
        super(String.format(
                "Part of file %s is in wrong format or the file does not exist! Check README.txt for more information!",
                file.getFileName()));
    }
}
