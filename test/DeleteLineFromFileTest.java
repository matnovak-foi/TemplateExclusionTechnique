import helper.file.FileManager;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import plagiarsimchecker.PlagiarsimChecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class DeleteLineFromFileTest {
    File testFile = new File("testDir"+File.separator+"testDeleteFile");
    TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    String text1 = "package org.foi.common.filesystem.file;\n" +
            "\n" +
            "import java.io.*;\n" +
            "import java.nio.charset.Charset;\n" +
            "import java.nio.charset.StandardCharsets;\n" +
            "import java.nio.file.Files;\n" +
            "import java.nio.file.OpenOption;\n" +
            "import java.nio.file.StandardOpenOption;\n" +
            "import java.nio.file.attribute.FileAttribute;\n" +
            "import org.foi.mpc.MPCexcpetions;\n" +
            "import javax.swing.plaf.synth.SynthOptionPaneUI;\n" +
            "\n" +
            "public class TextFileUtility {\n" +
            "Charset charset;\n" +
            "\t\n" +
            "    public TextFileUtility(Charset charset) {\n" +
            "        this.charset = charset;\n" +
            "    }" +
            "\n}";

    @Before
    public void setUp(){
        testFile.getParentFile().mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(testFile.getParentFile());
    }

    @Test
    public void deletesLinesInBlockPartWhereEverythingIsOk() throws IOException {
        tfu.createFileWithText(testFile,text1);
        PlagiarsimChecker plagiarsimChecker = new PlagiarsimChecker(null);
        plagiarsimChecker.deleteLineFromFile(testFile,1,12);
        String expected = "\n\n\n\n\n\n\n\n\n\n\n\npublic class TextFileUtility {\n" +
                "Charset charset;\n" +
                "\t\n" +
                "    public TextFileUtility(Charset charset) {\n" +
                "        this.charset = charset;\n" +
                "    }" +
                "\n}";
        assertEquals(expected,tfu.readFileContentToString(testFile));
    }

    @Test
    public void deletesLinesInBlockPartWhereSomethingShouldNoBeDeleted() throws IOException {
        tfu.createFileWithText(testFile,text1);
        PlagiarsimChecker plagiarsimChecker = new PlagiarsimChecker(null);
        plagiarsimChecker.deleteLineFromFile(testFile,1,13);
        String expected = "\n\n\n\n\n\n\n\n\n\n\n\npublic class TextFileUtility {\n" +
                "Charset charset;\n" +
                "\t\n" +
                "    public TextFileUtility(Charset charset) {\n" +
                "        this.charset = charset;\n" +
                "    }" +
                "\n}";
        assertEquals(expected,tfu.readFileContentToString(testFile));
    }

    @Test
    public void deleteUnnecesaryEmptyLinesLargeFile() throws IOException {
        File rootDir = new File(System.getProperty("user.dir"));
        if (!rootDir.getPath().contains("TemplateExclusionTechnique"))
            rootDir = new File(rootDir.getParentFile()+File.separator+"tools"+File.separator+"TemplateExclusionTechnique");

        File sourceDir = new File("sourceDir");
        sourceDir.mkdir();

        DirectoryFileUtility dfu = new DirectoryFileUtility();
        File inputDir = new File(rootDir.getPath() + File.separator +"testInputDataDeleteFile");
        dfu.copyDirectoryTree(inputDir,sourceDir);

        File file1 = new File(sourceDir.getPath()+File.separator+"input"+File.separator+"DeleteTestFile.java");
        File file1expected = new File(sourceDir.getPath()+File.separator+"expected"+File.separator+"DeleteTestFile.java");
        PlagiarsimChecker plagiarsimChecker = new PlagiarsimChecker(null);
        plagiarsimChecker.deleteUnnecesaryEmptyLines(file1);
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        assertEquals(tfu.readFileContentToString(file1expected),tfu.readFileContentToString(file1));
        DirectoryFileUtility.deleteDirectoryTree(sourceDir);
    }
}
