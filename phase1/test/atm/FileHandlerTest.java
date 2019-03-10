package atm;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileHandlerTest {
    private FileHandler fileHandler;
    private final ExternalFiles file = ExternalFiles.TEST_FILE;

    @Before
    public void before() {
        fileHandler = new FileHandler();
    }

    @Test
    public void testReadWrite() {
        String content1 = "Line1 content";
        String content2 = "Line2 content";

        fileHandler.saveTo(file, content1);
        fileHandler.saveTo(file, content2);

        assertEquals(content1, fileHandler.readFrom(file).get(0));
        assertEquals(content2, fileHandler.readFrom(file).get(1));
    }

    @Test
    public void testBankManagerSerialize() {
        BankManager manager = Mockito.mock(BankManager.class);

        fileHandler.saveManagerData(manager);

        BankManager retrivedManager = fileHandler.readManagerData();

        assertNotNull(retrivedManager);

        if (!new File(new File("phase1/data/").getAbsolutePath() + "\\BankData.txt").delete())
            throw new IllegalStateException("test BankData.txt not deleted.");
    }

}
