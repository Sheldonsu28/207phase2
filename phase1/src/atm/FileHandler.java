package atm;
import java.io.*;
import java.util.Properties;
public class FileHandler {
    private Properties date;
    private Properties userData;
    public FileHandler(){
        date = new Properties();
        userData = new Properties();
    }

    /**
     *
     * @param time the time you want to set in string.
     * @throws Exception Throws I/O exceptions if there is a problem.
     */
    public void setTime(String time) throws Exception{
        OutputStream Time = new FileOutputStream("timeConfig.properties");
        date.setProperty("date",time);
        date.store(Time,null);

    }

    /**
     * Do not call this method before the time is first set, do so will result in FileNotFoundException.
     * @return void
     * @throws Exception Throws I/O Exception if files mot found.
     *
     */
    public String getTime() throws Exception {
        InputStream Time = new FileInputStream("timeConfig.properties");
        date.load(Time);
        return date.getProperty("date");
    }


    //TODO implement setTime(), getTime(),getAccountBalance(), setAccountBalance(), getPassword().

}
