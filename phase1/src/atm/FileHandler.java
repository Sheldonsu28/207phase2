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

    /**
     * Create a new user.
     * @param username  User'sname.
     * @param password  User's password.
     * @throws Exception Throws I/O exception when there is a problem.
     * If the User's name is already used, call this method with the same username will completely replace the
     * original file.
     */
    public void createUser(String username, String password) throws Exception{
        OutputStream newUser = new FileOutputStream(username + ".properties");
        userData.setProperty("Username",username);
        userData.setProperty("Password",password);
        userData.store(newUser,null);
    }

    /**
     * add a account to user.
     * @param user The user's username
     * @param accountType A string represent the account type.
     * @param balance The balance you want to put in to the account.
     * @throws Exception
     * Be aware that if you input the same account twice, it will overrides the first account.
     * If you want to have multiple cheque account, you can name it as cheque account1 , cheque account2 etc.
     */
    public void addAccountToUser(String user,String accountType,String balance) throws Exception{
        OutputStream newUser = new FileOutputStream(user + ".properties");
        userData.setProperty(accountType, balance);
    }
    //TODO implement setTime(), getTime(),getAccountBalance(), setAccountBalance(), getPassword().

}
