import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import plagiarsimchecker.Configuration;
import plagiarsimchecker.PlagiarsimChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class MainTest {

    @Parameters(name = "{index}: TestDir({0})")
    public static Object[] data() {
      return new Object[] { "testInputData", "testInputData2", "testInputData3", "testInputData4","testInputData5","testInputData6" };
       // return new Object[] { "testInputData2" };

        //return new Object[] { "testInputData7" };//just to fix stackOverflow, not realy used for test takes to long it is covered in the testInputDataDeleteFile
    }

    File rootDir;

    @Parameter
    public String testInputData;
    File sourceDir;

    @org.junit.Before
    public void setUp() throws Exception {
        rootDir = new File(System.getProperty("user.dir"));
        if (!rootDir.getPath().contains("TemplateExclusionTechnique"))
            rootDir = new File(rootDir.getParentFile()+File.separator+"tools"+File.separator+"TemplateExclusionTechnique");

        System.out.println(rootDir);
        sourceDir = new File("sourceDir");
        sourceDir.mkdir();
        DirectoryFileUtility dfu = new DirectoryFileUtility();
        File inputDir = new File(rootDir.getPath() + File.separator +testInputData+File.separator+"input");
        dfu.copyDirectoryTree(inputDir,sourceDir);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        DirectoryFileUtility.deleteDirectoryTree(sourceDir);
    }

    @Test
    public void runTampleteExclude() throws IOException {
        Configuration configuration = new Configuration();
        configuration.amalgamate=true;
        configuration.concatanate=false;
        configuration.maxBackwardJump=1;
        /*configuration.maxForwardJump=3;
        configuration.maxJumpDiff=3;
        configuration.minRunLenght=3;
        configuration.strictness=2;
        configuration.minStringLength=8;*/
        configuration.maxForwardJump=10;
        configuration.maxJumpDiff=1;
        configuration.minRunLenght=5;
        configuration.strictness=1;
        configuration.minStringLength=1;
        PlagiarsimChecker plgChecker = new PlagiarsimChecker(configuration);
        plgChecker.runExclusionOfTemplate(sourceDir.getName(),"my_code","templateExcluded");

        assertTrue(sourceDir.exists());
        String[] subDirs = sourceDir.list();

        assertEquals(4,subDirs.length);
        assertThat(Arrays.asList(subDirs), hasItems("s1","s2","templateExcluded","my_code"));


        File original = new File(sourceDir.getPath()+File.separator+"templateExcluded");
        assertEquals(2,original.listFiles().length);

        File file1 = new File(original+File.separator+"TextFileUtility.java");
        File file1expected = new File(rootDir.getPath() + File.separator +testInputData+File.separator+"expected"+File.separator+"TextFileUtility.java");
        File file2 = new File(original+File.separator+"TextFileUtilityCopy.java");
        File file2expected = new File(rootDir.getPath() + File.separator +testInputData+File.separator+"expected"+File.separator+"TextFileUtilityCopy.java");
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        assertEquals(tfu.readFileContentToString(file1expected),tfu.readFileContentToString(file1));
        assertEquals(tfu.readFileContentToString(file2expected),tfu.readFileContentToString(file2));

    }

}