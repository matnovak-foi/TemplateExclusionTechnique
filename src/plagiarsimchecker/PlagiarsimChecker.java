/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarsimchecker;

import helper.fileFilters.DirectoryFilter;
import helper.file.FileManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.foi.common.JavaCodePartsRemover;
import org.foi.common.filesystem.file.TextFileUtility;
import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.Match;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;

/**
 * Dijelovi kod kod provjere direktorija i pokretanja sherlocka je kopirano iz
 * samog sherlocka jer sherlock je napravljen da radi sa grafičkim sučeljem pa
 * je dio taj izbačen i posebno napravljeno.
 *
 * @author Matija Novak
 */
public class PlagiarsimChecker {

    private Configuration configuration;
    private int deleteItemAttempts = 10;
    //String putanjaDoPlagiarismJar;

    public PlagiarsimChecker(Configuration configuration) {
        this.configuration = configuration;
    }

    private void initSherlockSettings(String source_dir){
        Settings.setSourceDirectory(new File(source_dir));
        Settings.init();
        Settings.getSherlockSettings().setJava(true);
    }

    /**
     * @param preprocess - radi pretprocesiranje
     * @param detectPlagiarism - radi detekciju plagijarizma
     */
    private void checkPlagiarsim(String source_dir, boolean preprocess, boolean detectPlagiarism) {
        initSherlockSettings(source_dir);
        //setAllTypeSettings(true, true, false, 1, 3, 3, 8, 3, 2);//DEFAULT

        setAllTypeSettings(true, configuration.amalgamate, configuration.concatanate, configuration.maxBackwardJump, configuration.maxForwardJump, configuration.minRunLenght, configuration.minStringLength, configuration.maxJumpDiff, configuration.strictness);//CALIBRATED
        //setTypeSettings(Settings.TOK, true, false, 1, 3, 3, 5, 1, 2);
        enableOriginal();
        checkPlagiarsim(source_dir, preprocess, detectPlagiarism, true);
    }
    
    /**
     * @param preprocess - radi pretprocesiranje
     * @param detectPlagiarism - radi detekciju plagijarizma
     */
    private void checkPlagiarsim(String source_dir, boolean preprocess, boolean detectPlagiarism, boolean deleteMatchDir) {
        System.out.println("START");
        try {
            Runtime runTime = Runtime.getRuntime();
            //System.out.println(java_home_dir+" -cp " + putanjaDoPlagiarismJar + File.separator + "TemplateExclusionTechnique.jar plagiarsimchecker.RunSherlock " + String.valueOf(preprocess) + " " + String.valueOf(detectPlagiarism) + " " + source_dir + " "+String.valueOf(deleteMatchDir));
            System.out.println(" -cp " + File.separator + "TemplateExclusionTechnique.jar plagiarsimchecker.RunSherlock " + String.valueOf(preprocess) + " " + String.valueOf(detectPlagiarism) + " " + source_dir + " "+String.valueOf(deleteMatchDir));
            RunSherlock sherlock = new RunSherlock(preprocess,detectPlagiarism,deleteMatchDir,new File(source_dir));
            sherlock.pokreni();
            FileTypeProfile profile = Settings.getFileTypes()[Settings.ORI];
            System.out.println("Detection params:");
            System.out.println("MFJ: "+profile.getMaxForwardJump()+" MRL: "+profile.getMinRunLength()+
                    " MBJ: "+profile.getMaxBackwardJump()+" MJD: "+profile.getMaxJumpDiff()+
                    " MSL: "+profile.getMinStringLength()+" STR: "+profile.getStrictness()+
                    " Amalgate"+profile.getAmalgamate()+" Concatenate "+profile.getConcatanate());
            /*Process process = runTime.exec(java_home_dir+" -cp " + putanjaDoPlagiarismJar + File.separator + "TemplateExclusionTechnique.jar plagiarsimchecker.RunSherlock " + String.valueOf(preprocess) + " " + String.valueOf(detectPlagiarism) + " " + source_dir + " "+String.valueOf(deleteMatchDir));

            String line;

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();

            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }
            error.close();

            process.waitFor();
*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("END");
    }

    private void checkDeleteDetectSimilarityInFiles(String source_dir, String compare_file_name, MatchesModel mm) {
        int count = 0;//count similarity
        deleteItemAttempts--;
        System.out.println("DELETE ATTEMPT: "+deleteItemAttempts);
        //tu ide delete similarity inače
        deleteSimilarityWithFile(compare_file_name, mm);
        checkPlagiarsim(source_dir, false, true);
        mm.loadMatches(source_dir + File.separator + Settings.getSherlockSettings().getMatchDirectory());
        for (Match storedMatch : mm.storedMatches) {
            if (storedMatch.getFile1().contains(compare_file_name) || storedMatch.getFile2().contains(compare_file_name)) {
                count += storedMatch.getSimilarity();
                if (count > 0) {//ako ostane 5 fajlova s manje od 3% sličnosti s našim fajlom ignoriramo
                    if(deleteItemAttempts>0)
                        checkDeleteDetectSimilarityInFiles(source_dir, compare_file_name, mm);
                    break;
                }
            }
        }
    }

    private void deleteSimilarityWithFile(String compare_file_name, MatchesModel mm) {
        File file = null;
        for (Match storedMatch : mm.storedMatches) {
            if (storedMatch != null) {
                String delete_line_in_file = "";
                int start_line = 0;
                int end_line = 0;

                if (storedMatch.getFile1().contains(compare_file_name)) {
                    delete_line_in_file = storedMatch.getFile2();
                    start_line = storedMatch.getRun().getStartCoordinates().getLineNoInFile2();
                    end_line = storedMatch.getRun().getEndCoordinates().getLineNoInFile2();
                } else if (storedMatch.getFile2().contains(compare_file_name)) {
                    delete_line_in_file = storedMatch.getFile1();
                    start_line = storedMatch.getRun().getStartCoordinates().getLineNoInFile1();
                    end_line = storedMatch.getRun().getEndCoordinates().getLineNoInFile1();
                }

                if (start_line == 0 && end_line == 0) {
                    continue;
                }

                String dir = Settings.getSourceDirectory() + File.separator;
                System.out.println(dir + delete_line_in_file + " " + start_line + " - " + end_line + " - " + storedMatch.getSimilarity() + "%");

                file = new File(dir + delete_line_in_file);
                deleteLineFromFile(file, start_line, end_line);
            }
        }
        deleteUnnecesaryEmptyLines(file);
    }

    public void deleteUnnecesaryEmptyLines(File file) {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        try {
            String content = tfu.readFileContentToString(file);
            JavaCodePartsRemover javaCodePartsRemover = new JavaCodePartsRemover();
            content = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(content);
            content = javaCodePartsRemover.removeEmptyElseFinallyStatement(content);
            //content = javaCodePartsRemover.removeAnyEmptyMethod(content);
            //content = javaCodePartsRemover.removeEmptyClasses(content);
            content = javaCodePartsRemover.removeLeftoverWhiteSpaces(content);

            file.delete();
            tfu.createFileWithText(file,content);
        } catch (IOException e) {
            throw new TextFileUtility.FileReadWriteException("Problem template exclusion remove white spaces at the end! File:"+file.getPath());
        }
    }

    /**
     * Birsanje Linija iz nekog fajla, dio kopiran iz sherlocka
     *
     * @see uk.ac.warwick.dcs.cobalt.sherlock.ComparePane
     * @param file
     * @param startLine
     * @param endLine
     */
    public void deleteLineFromFile(File file, int startLine, int endLine) {
        // Keep track of the lines in the file - so we know when to display or not.
        int lineNo = 1;
        // Used to read from the file.
        String inputString = "";

        try {
            File temp_file = new File(file.getParent() + File.separator + "temp_" + file.getName());
            BufferedWriter writeToFile = new BufferedWriter(new FileWriter(temp_file));
            BufferedReader readFromFile = new BufferedReader(new FileReader(file));

            LineDeleteHelper deleteHelper = new LineDeleteHelper();
            String toDeleteBlock = "";
            while ((inputString = readFromFile.readLine()) != null) {

                // Read in all of the file. Only output the lines that aren't
                // #line xxx
                // However, make sure that update the line number count.
                if (inputString.startsWith("#line ")) {
                    writeToFile.write(inputString + "\n");
                    continue;
                }

                if (inputString.startsWith("DATOTEKA_")) {
                    writeToFile.write(inputString + "\n");
                } else if (lineNo == startLine) { // If at start of matched suspicious section:
                    //System.out.println("Start" + lineNo);
                    //System.out.println(inputString);
                    toDeleteBlock += inputString + "\n";
                } else if (lineNo == endLine) { // If at end of matched suspicious section:
                    //System.out.println(inputString);
                    //System.out.println("End" + lineNo);

                    toDeleteBlock += inputString + "\n";
                    String toKeepBlock = deleteHelper.deleteOkLines(toDeleteBlock);
                    //System.out.println("PART TO KEEP \n\n "+toKeepBlock);

                    //ENSURE THAT SAME LINE NUMBERS REMAIN
                    int missingLines = getNumberOfMissingLines(startLine, endLine, toKeepBlock);
                    writeNewLinesToFile(writeToFile, missingLines);
                    writeToFile.write(toKeepBlock + "\n");

                } // If displaying whole file, within three lines of the
                else if ((lineNo > startLine && lineNo < endLine)) { // limits, or between matched section:
                    //System.out.println(inputString);
                    toDeleteBlock += inputString + "\n";
                } else {
                    writeToFile.write(inputString + "\n");
                }

                // Read the next line.
                lineNo++;

            } // while reading in the file.

            readFromFile.close();
            writeToFile.flush();
            writeToFile.close();
            readFromFile = null;
            writeToFile = null;
            if (file.delete()) {
                System.out.println("     Deleteing temp file! " + file.getAbsolutePath());
            } else {
                while (true) {
                    System.gc();
                    System.err.println("Deleteing temp file! " + file.getAbsolutePath());
                    Thread.sleep(2000);
                    if (file.delete()) {
                        System.out.println("     Deleteing temp file! " + file.getAbsolutePath());
                        break;
                    }
                }
            }
            temp_file.renameTo(file);

        } catch (Exception a) {
            // no worries as if the file doesn't exist, we won't get this far.
        }
    }

    private void writeNewLinesToFile(BufferedWriter writeToFile, int missingLines) throws IOException {
        for(int i=missingLines;i>1;i--){
            writeToFile.write("\n");
        }
    }

    private int getNumberOfMissingLines(int startLine, int endLine, String toKeepBlock) {
        int numberOfLines = endLine-startLine+1;
        char[] block = toKeepBlock.toCharArray();
        int numberOfNewLines = 0;
        for(int i = 0; i<block.length;i++){
            if(block[i]=="\n".toCharArray()[0]){
                //System.out.println("Size"+"\n".toCharArray().length);
                numberOfNewLines++;
            }
        }
        return numberOfLines-numberOfNewLines;
    }

    /**
     * set all profileTypeSettings
     *
     * @param useTokenized
     */
    private static void setAllTypeSettings(boolean useTokenized, boolean amalgamate, boolean concatanate, int maxBackwardJump, int maxForwardJump, int minRunLenght, int minStringLength, int maxJumpDiff, int strictness) {
        for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {
            FileTypeProfile profile = Settings.getFileTypes()[x];
            if (x == Settings.SEN || x == Settings.COM) {
                profile.setInUse(false);
            } else if (x == Settings.TOK && !useTokenized) {
                profile.setInUse(false);
            } else {
                profile.setInUse(false);
            }

            profile.setAmalgamate(amalgamate);
            profile.setConcatanate(concatanate);
            profile.setMaxBackwardJump(maxBackwardJump);
            profile.setMaxForwardJump(maxForwardJump);
            profile.setMinRunLength(minRunLenght);
            profile.setMinStringLength(minStringLength);
            profile.setMaxJumpDiff(maxJumpDiff);
            profile.setStrictness(strictness);

            profile.store();
        }
    }

    private static void setTypeSettings(int type, boolean amalgamate, boolean concatanate, int maxBackwardJump, int maxForwardJump, int minRunLenght, int minStringLength, int maxJumpDiff, int strictness) {
        FileTypeProfile profile = Settings.getFileTypes()[type];

        profile.setAmalgamate(amalgamate);
        profile.setConcatanate(concatanate);
        profile.setMaxBackwardJump(maxBackwardJump);
        profile.setMaxForwardJump(maxForwardJump);
        profile.setMinRunLength(minRunLenght);
        profile.setMinStringLength(minStringLength);
        profile.setMaxJumpDiff(maxJumpDiff);
        profile.setStrictness(strictness);

        profile.store();
    }

    public void runExclusionOfTemplate(String source_dir, String prof_files_dir, String excludeDirName) {
        initSherlockSettings(source_dir);
        System.out.println("START runExclusionOfTemplate");
        List<String> unvanted_dirs = new ArrayList<>();
        for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {
            FileTypeProfile profile = Settings.getFileTypes()[x];
            File file = new File(source_dir, profile.getDirectory());
            if (file.exists()) {
                try {
                    RunSherlock.deleteDir(file);
                    File dir = new File(source_dir + File.separator + profile.getDirectory());
                    dir.mkdirs();
                } catch (IOException ex) {
                    System.err.println("Greška kod brisanja foldera za profil " + profile.getDirectory());
                }
            }

            unvanted_dirs.add(profile.getDirectory());
        }

        unvanted_dirs.add(Settings.getSherlockSettings().getMatchDirectory());
        File md = new File(source_dir);
        File[] subdirs = md.listFiles(new DirectoryFilter());

        for (File subdir : subdirs) {

            if (subdir.getName().equalsIgnoreCase(prof_files_dir)) {
                continue;
            }

            if (!unvanted_dirs.contains(subdir.getName())) {
                System.out.println("START Exclusion for: "+subdir.getAbsolutePath());
                String prof_file_name = findBestSutibleProfesorFile(source_dir, prof_files_dir, subdir.getAbsolutePath());//tu ide moj naziv pravog prof filea

                System.out.println(subdir.getParent() + " " + subdir.getName());
                if (!FileManager.fileExists(subdir.getAbsolutePath(), prof_file_name)) {
                    FileManager.copyFile(source_dir + File.separator + prof_files_dir, prof_file_name, subdir.getAbsolutePath(), prof_file_name);
                }

                System.out.println("BEST PROF FILE FOR " + subdir.getAbsolutePath() + " IS " + prof_file_name);

                PlagiarsimChecker checkerOneDir = new PlagiarsimChecker(configuration);
                checkerOneDir.checkPlagiarsim(subdir.getAbsolutePath(), true, true);
                MatchesModel mm = new MatchesModel();
                mm.loadMatches(subdir.getAbsolutePath() + File.separator + Settings.getSherlockSettings().getMatchDirectory());
                checkerOneDir.deleteItemAttempts = 10;
                checkerOneDir.checkDeleteDetectSimilarityInFiles(subdir.getAbsolutePath(), prof_file_name, mm);

                for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {//kopiranje svih fajlova u odgovarajuće direktorije
                    FileTypeProfile profile = Settings.getFileTypes()[x];
                    if (profile.isInUse()) {
                        File profile_dir = new File(subdir.getAbsolutePath() + File.separator + profile.getDirectory());
                        File[] profile_files = profile_dir.listFiles();
                        for (File profile_file : profile_files) {
                            if (!profile_file.getName().contains(prof_file_name)) {
                                String fileNameWithoutExtension = profile_file.getName().substring(0,profile_file.getName().lastIndexOf("."));
                                FileManager.copyFile(profile_file.getParent(), profile_file.getName(), source_dir+File.separator+excludeDirName, fileNameWithoutExtension);
                            }
                        }
                    }
                }
                System.out.println("END Exclusion for: "+subdir.getAbsolutePath());
            }
        }
        System.out.println("END runExclusionOfTemplate");
        //cleanSherlockFiles(source_dir);
    }

    private void cleanSherlockFiles(String source_dir){
        for(File files : new File(source_dir).listFiles()){
            if(files.getName().contains(".ini") || files.getName().contains(Settings.getSherlockSettings().getMatchDirectory()) ){
                files.delete();
            }
        }
    }

    private String findBestSutibleProfesorFile(String source_dir, String prof_files_dir, String student_dir) {
        File md = new File(source_dir + File.separator + prof_files_dir);
        File[] files = md.listFiles();

        int max_sum_similarity = -1;
        int max_sum_lines = -1;
        String output_name = "";

        for (File prof_file : files) {

            System.out.println(prof_file.getParent() + " " + prof_file.getName());
            if (FileManager.fileExists(prof_file.getParent(), prof_file.getName())) {
                FileManager.copyFile(prof_file.getParent(), prof_file.getName(), student_dir, prof_file.getName());
            }

            PlagiarsimChecker checkerOneDir = new PlagiarsimChecker(configuration);
            checkerOneDir.checkPlagiarsim(student_dir, true, true);
            MatchesModel mm = new MatchesModel();
            mm.loadMatches(student_dir + File.separator + Settings.getSherlockSettings().getMatchDirectory());

            int sum_similarity = 0;
            int sum_lines = 0;
            System.out.println("Match length: "+mm.getStoredMatches().length);
            for (Match match : mm.getStoredMatches()) {
                if (match.getFileType() == Settings.ORI) {
                    sum_similarity += match.getSimilarity();
                    if (match.getFile1().contains(prof_file.getName())) {
                        //System.out.println("F2"+match.getFile2());
                        sum_lines += match.getRun().getEndCoordinates().getLineNoInFile2() - match.getRun().getStartCoordinates().getLineNoInFile2();
                    } else {
                        //System.out.println("F1"+match.getFile1());
                        sum_lines += match.getRun().getEndCoordinates().getLineNoInFile1() - match.getRun().getStartCoordinates().getLineNoInFile1();
                    }
                }
            }

            if (max_sum_similarity < sum_similarity) {
                System.out.println("SIM VEĆA ZA Grupu "+prof_file.getName()+" izosi "+sum_similarity);
                max_sum_similarity = sum_similarity;
                max_sum_lines = sum_lines;
                output_name = prof_file.getName();
            } else if (max_sum_similarity == sum_similarity && max_sum_lines < sum_lines) {
                System.out.println("SIM VEĆA ZA Grupu "+prof_file.getName()+" izosi "+sum_similarity+" lines "+sum_lines);
                output_name = prof_file.getName();
                max_sum_similarity = sum_similarity;
                max_sum_lines = sum_lines;
            }

            if (FileManager.fileExists(student_dir, prof_file.getName())) {
                File to_delete = new File(student_dir + File.separator + prof_file.getName());
                FileManager.deleteFile(to_delete);
            }
        }

        return output_name;
    }

    private void enableOriginal() {
        for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {
            FileTypeProfile profile = Settings.getFileTypes()[x];
            if (x == Settings.ORI) {
                profile.setInUse(true);
            } else {
                profile.setInUse(false);
            }

            profile.store();
        }
    }
}
