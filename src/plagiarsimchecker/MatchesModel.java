/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plagiarsimchecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import uk.ac.warwick.dcs.cobalt.sherlock.Match;
import uk.ac.warwick.dcs.cobalt.sherlock.MatchFilenameFilter;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;

/**
 *
 * @author Matija Novak
 */
public class MatchesModel {
     protected Match[] storedMatches;
     
     public Match[] getStoredMatches() {
        return storedMatches;
     }
      
     /**
     * Preuzeto iz Sherlock projekta i prerađeno
     *
     * @param match_dir
     * @param f1
     * @param f2
     * @param fileType
     * @param serialID
     * @param similarity
     * @return
     */
    public File getMatchFileName(String match_dir, String f1, String f2, int fileType, int serialID, int similarity) {
        File fileToSave = new File(match_dir,
                f1 + "-" + f2 + "-"
                + Settings.getFileTypes()[fileType].getExtension()
                + "-" + (serialID++) + "-"
                + similarity + "pc" + ".match");

        return fileToSave;
    }

        /**
     * Dohvača username pretpostavka da svi fajlovi imaju username_
     * @param file
     * @return 
     */
    public static String getFileUser(String file) {
        int undescore_index = file.indexOf("_");
        if (undescore_index == -1) {
            return file;
        }
        return file.substring(0, undescore_index);
    }
    
    /**
     * Dobivamo čisto ime fajla
     * @param f
     * @return 
     */
    public static String clearFileName(String f) {
        int dotindex = f.lastIndexOf('.');
        int slashindex = f.lastIndexOf(Settings.getFileSep());
        String fname = f.substring(slashindex + 1, dotindex);

        return fname;
    }
    
    /**
     * Preuzeto iz Sherlock projekta i prerađeno
     *
     * @param matches_dir
     * @return
     */
    public boolean loadMatches(String matches_dir) {
        // Get the match files, and create array for them as matches.
        File md = new File(matches_dir);
        File matchFiles[] = md.listFiles(new MatchFilenameFilter());
        storedMatches = new Match[matchFiles.length];

        // If there are no matches, return a null value
        if (storedMatches.length == 0) {
            return false;
        }

    // Deserialise the matches, loading the match into the matches array
        // and details into the data array.
        for (int x = 0; x < storedMatches.length; x++) {
            try {
                FileInputStream fis = new FileInputStream(matchFiles[x]);
                ObjectInputStream ois = new ObjectInputStream(fis);

                // Add this match to the array.
                storedMatches[x] = (Match) ois.readObject();

                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
        //e.printStackTrace();
                // If have an exception then this file does not contain a
                // valid match; set the storedMatches[x] entry to null.
                storedMatches[x] = null;

                System.out.println("The following file does not contain a valid "
                        + "match:\n" + matchFiles[x].getAbsolutePath()
                        + "\nFile skipped."
                        + "\nError logged in file sherlock.log in your HOME"
                        + " directory.");
            }
        } // for
        
        return true;
    } // loadMatches
      
}
