/*
 * Copyright (C) 2015 Matija Novak <matija.novak@foi.hr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package helper.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Klasa koja sadrži helper metode za rad sa falovima, poput copy, move, delte i
 * sl.
 *
 * @author Matija Novak
 *
 */
public class FileManager {

    private static String TAG = FileManager.class.getName();

    /**
     * Metoda koja kopira file sa jedne putanje na drugu i pod određenim imenom
     *
     * @param inputPath
     * @param inputFile
     * @param outputPath
     * @param outputFile
     */
    public static void copyFile(String inputPath, String inputFile, String outputPath, String outputFile) {

        InputStream in = null;
        OutputStream out = null;

        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath + File.separator + inputFile);
            out = new FileOutputStream(outputPath + File.separator + outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            System.out.println(TAG + fnfe1.getMessage());
        } catch (Exception e) {
            System.out.println(TAG + e.getMessage());
        }

    }

    public static boolean deleteFile(File file) {
        try {
            if (file.delete()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean fileExists(String path, String name) {
        File file = new File(path + File.separator + name);
        return file.exists();
    }
}
