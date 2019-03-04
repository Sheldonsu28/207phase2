package atm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileHandlerTest {
    private FileHandler fileHandler;
    private String path;

    @Before
    public void before() {
        fileHandler = new FileHandler();
        path = (new File("phase1/data/")).getAbsolutePath() + '\\';
    }

    @Test
    public void testReadWrite() {
        String filename = "testFile.txt";
        String content1 = "Line1 content";
        String content2 = "Line2 content";

        fileHandler.saveTo(filename, content1);
        fileHandler.saveTo(filename, content2);

        assertEquals(content1, fileHandler.readFrom(filename).get(0));
        assertEquals(content2, fileHandler.readFrom(filename).get(1));
    }

    @After
    public void after() {
        File[] files = (new File(path)).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("test")) {
                    if (!file.delete())
                        throw new IllegalStateException("Failed to delete test file: " + file.getAbsolutePath());
                }
            }
        }
    }

}