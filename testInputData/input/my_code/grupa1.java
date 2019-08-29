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
}