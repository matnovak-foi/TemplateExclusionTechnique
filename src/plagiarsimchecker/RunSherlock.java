/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarsimchecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.zip.ZipFile;
import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.GzipFilenameFilter;
import uk.ac.warwick.dcs.cobalt.sherlock.GzipHandler;
import uk.ac.warwick.dcs.cobalt.sherlock.Samelines;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcess;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessCallback;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessException;
import uk.ac.warwick.dcs.cobalt.sherlock.TextFileFilter;
import uk.ac.warwick.dcs.cobalt.sherlock.TokeniseFiles;
import uk.ac.warwick.dcs.cobalt.sherlock.ZipFilenameFilter;
import uk.ac.warwick.dcs.cobalt.sherlock.ZipHandler;

/**
 * Većina ove klase kopija je dijelova koda iz sherlocka
 *
 * @author Matija Novak
 */
public class RunSherlock implements SherlockProcessCallback  {

    private boolean preproces;
    private boolean compare;
    private boolean deleteMatchDir;

    public static void main(String args[])
    {
        if(args.length != 4)
        {
            System.out.println("Krivi broj argumenata");
            return;
        }
        System.out.println("Path: "+args[2]);
        System.out.println("Preprocess: "+args[0]);
        System.out.println("DetectPlagiarsim: "+args[1]);
        System.out.println("DeleteMatchFiles: "+args[3]);

        RunSherlock runSherlock =  new RunSherlock(Boolean.parseBoolean(args[0]), Boolean.parseBoolean(args[1]),Boolean.parseBoolean(args[3]),new File(args[2]));
        runSherlock.pokreni();
    }

    public RunSherlock(boolean preproces, boolean compare, boolean deleteMatchDir, File dir) {
        Settings.setSourceDirectory(dir);
        Settings.setRunningGUI(false);
        Settings.init();
        this.compare = compare;
        this.preproces = preproces;
        this.deleteMatchDir = deleteMatchDir;
    }
    
    /**
     * Pokreće sherlock da napravi pretprocesiranje i mechiranje Copied from
     *
     * @param preproces - dali radi pretprocesiranje
     * @param compare - dali radi detekciju plagiarizma
     * @see uk.ac.warwick.dcs.cobalt.sherlock.GUI
     */
    public void pokreni() {
        start_complete_search(preproces,compare);

        Settings.setFileList(processDirectory(Settings.getSourceDirectory()));

        // Determine if this is a natural language mode detection
        FileTypeProfile[] profs = Settings.getFileTypes();
        boolean natural = false;
        for (int i = 0; i < profs.length; i++) {
            if (i == Settings.SEN || i == Settings.ORI) {
                natural = natural && profs[i].isInUse();
            } else {
                natural = natural && !profs[i].isInUse();
            }
        }
        
        if (preproces) {
            System.out.println("Preproces files...");
            System.out.println(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss").format(new Date()));
            SherlockProcess process = new TokeniseFiles(this);
            process.setNatural(natural);
            process.setPriority(Thread.NORM_PRIORITY - 1);
            process.start();
            try {
                process.join();
            } catch (InterruptedException ex) {
                System.err.println("Error join thread preproces files");
            }
            System.out.println(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss").format(new Date()));
            System.out.println("Done processing");
        }

        if (compare) {
            System.out.println("Detecting plagiarism...");
            System.out.println(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss").format(new Date()));
            SherlockProcess process = new Samelines(this);
            process.setNatural(natural);
            process.setPriority(Thread.NORM_PRIORITY - 1);
            process.start();
            try {
                process.join();
            } catch (InterruptedException ex) {
                System.err.println("Error join thread detecting plagiarsim");
            }

            System.out.println(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss").format(new Date()));
            System.out.println("Done Detecting plagiarism");
        }

        System.out.println("Process finished.");
    }

    /**
     * Copied from
     *
     * @see uk.ac.warwick.dcs.cobalt.sherlock.GUI
     */
    private void start_complete_search(boolean preproces, boolean compare) {
        if (preproces) {
            System.out.println("Delete preprocesed files.");
            for (int i = 0; i < Settings.NUMBEROFFILETYPES; i++) {
                try {
                    File file = new File(Settings.getSourceDirectory(),
                            Settings.getFileTypes()[i].getDirectory());
                    if (file.exists()) {
                        deleteDir(file);
                    }
                } catch (IOException ioe) {
                    File file = new File(Settings.getSourceDirectory(),
                            Settings.getFileTypes()[i].getDirectory());
                    Date day = new Date(System.currentTimeMillis());
                    System.err.println(day + "-Cannot delete directory: "
                            + file.getAbsolutePath()
                            + " File skipped.");
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(Settings.getLogFile().getAbsolutePath(), true));
                        out.write(day + "-Cannot delete directory: "
                                + file.getAbsolutePath()
                                + " File skipped.");
                        out.newLine();
                        out.close();
                    } catch (IOException ioe2) {
                        //if failed to write to log file, write to stderr
                        System.err.println(day + "-Cannot write to log file. "
                                + "Cannot delete directory: "
                                + file.getAbsolutePath()
                                + " File skipped.");
                    }
                }
            }
        }
        if (compare && deleteMatchDir) {
            System.out.println("Delete match files.");
            //deletes match directory
            File matchDir = new File(Settings.getSourceDirectory(),
                    Settings.getSherlockSettings().getMatchDirectory());
            if (matchDir.exists()) {
                try {
                    deleteDir(matchDir);
                } catch (IOException ioe) {
                    Date day = new Date(System.currentTimeMillis());
                    System.err.println(day + "-Cannot delete directory: "
                            + matchDir.getAbsolutePath()
                            + " File skipped.");
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(Settings.getLogFile().getAbsolutePath(), true));
                        out.write(day + "-Cannot delete directory: "
                                + matchDir.getAbsolutePath()
                                + " File skipped.");
                        out.newLine();
                        out.close();
                    } catch (IOException ioe2) {
                        //if failed to write to log file, write to stderr
                        System.err.println(day + "-Cannot write to log file. "
                                + "Cannot delete directory: "
                                + matchDir.getAbsolutePath()
                                + " File skipped.");
                    }
                }
            }
        }
    }

    /*
     * Učitavanje svih fajlova koji se moraju pretprocesirati i/ili matche-irati
     * Copyed form
     * @see uk.ac.warwick.dcs.cobalt.sherlock.GUI
     * Unpack ZIP/GZIP files within each sub-directories in the given source
     * directory.
     */
    private File[] processDirectory(File dir) {
        uk.ac.warwick.dcs.cobalt.sherlock.DirectoryFilter dirfilter = new uk.ac.warwick.dcs.cobalt.sherlock.DirectoryFilter();
        ZipFilenameFilter zipfilter = new ZipFilenameFilter();
        GzipFilenameFilter gzipfilter = new GzipFilenameFilter();
        TextFileFilter textfilefilter = new TextFileFilter();

        //for each sub-directory, expand any zip/gzip files in it and its
        //sub-directories if any.
        File[] subdir;
        File[] zipfiles = dir.listFiles(zipfilter);
        File[] gzipfiles = dir.listFiles(gzipfilter);
        File[] list;
        File[] subdirfiles;
        LinkedList l = new LinkedList();

        //add files in current directory
        File[] files = dir.listFiles(textfilefilter);
        for (int i = 0; i < files.length; i++) {
            l.add(files[i]);

            //for each zip file in this directory if any
        }
        for (int i = 0; i < zipfiles.length; i++) {
            try {
                ZipHandler.unzip(new ZipFile(zipfiles[i]));
            } catch (IOException e1) {
                //write error log, skip this file and continue.
                Date day = new Date(System.currentTimeMillis());
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(Settings.getLogFile().getAbsolutePath(), true));
                    System.err.println(day + "-Cannont extract file: "
                            + zipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    out.write(day + "-Cannont extract file: "
                            + zipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    out.newLine();
                    out.close();
                } catch (IOException e2) {
                    //if failed to write to log, write to stderr
                    System.err.println(day + "-Cannot write to log file. "
                            + "Cannont extract file: "
                            + zipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    System.err.println(day + "-Cannot write to log file. "
                            + "Cannont extract file: "
                            + zipfiles[i].getAbsolutePath()
                            + " File skipped.");
                }
                continue;
            }
        }

        //for each gzip file in this directory if any
        for (int i = 0; i < gzipfiles.length; i++) {
            try {
                GzipHandler.gunzip(gzipfiles[i]);
            } catch (IOException e1) {
                //write error log, skip this file and continue.
                Date day = new Date(System.currentTimeMillis());
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(Settings.getLogFile().getAbsolutePath(),
                            true));
                    System.err.println(day + "-Cannont extract file: "
                            + gzipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    out.write(day + "-Cannont extract file: "
                            + gzipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    out.newLine();
                    out.close();
                } catch (IOException e2) {
                    //if failed to write to log file, write to stderr
                    System.err.println(day + "-Cannot write to log file. "
                            + "Cannont extract file: "
                            + gzipfiles[i].getAbsolutePath()
                            + " File skipped.");
                    System.err.println(day + "-Cannot write to log file. "
                            + "Cannont extract file: "
                            + gzipfiles[i].getAbsolutePath()
                            + " File skipped.");
                }
                continue;
            }
        }

        //for each sub-directory in this directory if any
        subdir = dir.listFiles(dirfilter);
        int count = Settings.filterSherlockDirs(subdir);
        File[] newDirs = new File[count];
        int pos = 0;
        for (int i = 0; i < subdir.length; i++) {
            if (subdir[i] != null) {
                newDirs[pos] = subdir[i];
                pos++;
            }
        }
        subdir = newDirs;
        for (int i = 0; i < subdir.length; i++) {
            subdirfiles = processDirectory(subdir[i]);
            for (int j = 0; j < subdirfiles.length; j++) {
                l.add(subdirfiles[j]);
            }
        }

        //store result in a File array and return.
        list = new File[l.size()];
        for (int i = 0; i < l.size(); i++) {
            list[i] = (File) l.get(i);

        }
        return list;
    }

    /**
     * Copyed form
     *
     * @see uk.ac.warwick.dcs.cobalt.sherlock.GUI Delete directory.
     */
    public static void deleteDir(File dir) throws IOException {
        File[] files = dir.listFiles();
        //delete files in this directory
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteDir(files[i]);
            } else {
                files[i].delete();
            }
        }

        //delete this directory
        dir.delete();
    }

    @Override
    public void exceptionThrown(SherlockProcessException spe) {
        System.err.println("process exception" + spe.getOriginalException());
    }
}
