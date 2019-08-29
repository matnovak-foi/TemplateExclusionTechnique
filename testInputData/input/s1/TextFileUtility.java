package org.foi.common.filesystem.file;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import org.foi.mpc.MPCexcpetions;
import javax.swing.plaf.synth.SynthOptionPaneUI;

public class TextFileUtility {
	
    public static class FileDoesNotExistException extends RuntimeException {}
    public static class FileAlreadyExistException extends RuntimeException {}
    public static class FileCreateException extends RuntimeException {
        public FileCreateException(String message) {
            super(message);
        }
    }
	
	Charset charset;
	
    public TextFileUtility(Charset charset) {
        this.charset = charset;
    }

    public File createFileWithText(File dir, String fileName, String text) throws IOException {
        if(dir == null || fileName == null)
            throw new MPCexcpetions.IsNullException();
		
        if(fileName=="")
            throw new FileCreateException("File name can not be Empty string!");
		
        File file = new File(dir.getPath()+File.separator+fileName);
        createFileWithText(file, text);
		
        return file;
    }

    public void createFileWithText(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
		
        createFileIfNotExists(file);
        writeTextToFile(file, text, StandardOpenOption.WRITE);
    }
    
    public String readFileContentToString(File file) throws IOException {
        if(file == null)
            throw new MPCexcpetions.IsNullException();
		
        BufferedReader reader = createBufferedReader(file);
		
        return readFileContent(reader);
    }
    
    public void appendTextToFile(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
		
        else if(!file.exists())
            throw new FileDoesNotExistException();
        
        writeTextToFile(file, text, StandardOpenOption.APPEND);
    }

    private void createFileIfNotExists(File file) {
        if (!file.exists()) {
            createEmptyFile(file);
        } else {
            throw new FileAlreadyExistException();
        }
    }

    private void createEmptyFile(File file) {
        try {
            Files.createFile(file.toPath(), new FileAttribute[]{});
        } catch (IOException ex) {
            throw new FileCreateException(ex.getMessage());
        }
    }

    private void writeTextToFile(File file, String text, OpenOption... options) throws IOException {
        BufferedWriter bf = null;
        try {
            bf = Files.newBufferedWriter(file.toPath(), charset, options);
            bf.write(text);
        } finally {
            closeCreatedBufferedWriter(bf);
        }
    }

    private void closeCreatedBufferedWriter(BufferedWriter bf) throws IOException {
        if (bf != null) {
            bf.close();
        }
    }
}