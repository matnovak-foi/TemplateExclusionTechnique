

public class Poruka {
    private String sadrzaj;

    public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano, String sadrzaj) {
}

    
}
public class Provjere {
    public String provjeraKorisnika(String korisnickoIme, String korisnickaLozinka) {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        BP_Konfiguracija bp = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        if (bp == null) {
            return "ERROR";
        }
        if (korisnickoIme == null || korisnickoIme.length() == 0 || korisnickaLozinka == null || korisnickaLozinka.length() == 0) {
            return "NOT_OK";
        }

        String connUrl = bp.getServer_database() + bp.getUser_database();
        String sql = "SELECT email_adresa, lozinka FROM polaznici "
                + "WHERE email_adresa = '" + korisnickoIme + "'" + " AND lozinka = '"
                + korisnickaLozinka + "'";

        try (
                Connection conn = DriverManager.getConnection(connUrl, bp.getUser_username(), bp.getUser_password());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                return "NOT_OK";
            }

            return "OK";

        } catch (SQLException ex) {
            return "ERROR";
        }
    }

    public String provjeraDbUser(String korisnickoIme, String korisnickaLozinka) {
        if (bpk == null) {
            return "ERROR";
        }
        if (korisnickoIme == null || korisnickoIme.length() == 0 || korisnickaLozinka == null || korisnickaLozinka.length() == 0) {
            return "NOT_OK";
        }

        String connUrl = bpk.getServer_database() + bpk.getUser_database();
        String sql = "SELECT kor_ime, lozinka FROM polaznici "
                + "WHERE kor_ime = '" + korisnickoIme + "'" + " AND lozinka = '"
                + korisnickaLozinka + "'";

        try (
                Connection conn = DriverManager.getConnection(connUrl, bpk.getUser_username(), bpk.getUser_password());
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                return "NOT_OK";
            }

            return "OK";

        } catch (SQLException ex) {
            return "ERROR";
        }
    }

    public void setBpk(BP_Konfiguracija bpk) {
        this.bpk = bpk;
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.slusaci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import org.foi.nwtis.student9.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student9.web.kontrole.Provjere;

public class ObradaPoruka extends Thread {
    private String korisnik;// = "servis@nwtis.nastava.foi.hr";
    private String lozinka;// = "123456";
    private String subject;
    private BP_Konfiguracija bp;
    private String direktorij;
    private Provjere provjeraDb = new Provjere();
    private String dPodaci;
    private String fileName;
public void run() {
        Date datumPocetak = new Date();
        int ukupPoruke = 0;
        while (true) {
            long pocetakNano = System.nanoTime();
            korisnik = bp.getEmail_username();
            lozinka = bp.getEmail_password();
            subject = bp.getEmail_subject();
            int brojPoruka = 0;
            int dodano = 0;
            int intUpdate = 0;
            int intRemove = 0;
            int intGreske = 0;
            String kreiraniDirektoriji = "";
            String kreiraneDatoteke = "";

try {
if (RCCdummy) {}
                Folder fValidated = store.getFolder(mapaNWTisPoruke);
                fValidated.open(Folder.READ_WRITE);

                Folder fOther = store.getFolder(mapaNeNWTisPoruke);
                fOther.open(Folder.READ_WRITE);

                Message[] messages = folder.getMessages();

                String NWTiS_predmet = "NWTiS poruka";
                dPodaci = "podaci";
                fileName = "rjecnik.txt";
                Path putanja = Paths.get(direktorij + dPodaci);
                if (Files.notExists(putanja, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                    File dirRoot = new File(direktorij + dPodaci);
                    dirRoot.mkdir();
                }
                String sPutanja = direktorij + dPodaci + "/";

                // Print each message
                for (int i = 0; i < messages.length; ++i) {
                    MimeMessage m = (MimeMessage) messages[i];
                    String tip = m.getContentType().toLowerCase().split(";")[0];
                    Message[] mLimbo = folder.getMessages(i + 1, i + 1);
                    String regex = "(USER)(.*)(.;)(.*)(PASSWORD)(.*)(.;)(.*)(KEY)(.*)(.;)(.*)(VALUE)(.*)(.;)(.*)(ADD|REMOVE|UPDATE)(;)";

                    if (tip.equals("text/plain")) {
                        if (m.getSubject().startsWith(NWTiS_predmet)) {
                            String sadrzaj = (String) m.getContent();
                            if (sadrzaj.trim().matches(regex)) {
                                String[] splitSadrzaj = sadrzaj.trim().split(";");
                                String sUser = splitSadrzaj[0].split("\\s+")[1];
                                String sPass = splitSadrzaj[1].split("\\s+")[2];
                                provjeraDb.setBpk(bp);
                                if (provjeraDb.provjeraDbUser(sUser, sPass).equals("OK")) {
                                    Path putanjaUser = Paths.get(sPutanja + sUser);
                                    if (Files.notExists(putanjaUser, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                                        File dirRoot = new File(sPutanja + sUser);
                                        dirRoot.mkdir();
                                        kreiraniDirektoriji += sPutanja + sUser + "; ";
                                    }
                                    String sFile = sPutanja + sUser + "/" + fileName;
                                    FileOutputStream out;
                                    Path putanjaDat = Paths.get(sFile);
                                    Properties propMail = new Properties();
                                    if (Files.exists(putanjaDat, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                                        FileInputStream write = new FileInputStream(sFile);
                                        propMail.load(write);
                                    }
                                    else {
                                        kreiraneDatoteke += sFile + "; ";
                                    }

                                    out = new FileOutputStream(sFile);
                                    String sAction = splitSadrzaj[4].split("\\s+")[1];
                                    String sKey = splitSadrzaj[2].split("\\s+")[2];
                                    String sValue = splitSadrzaj[3].split("\\s+")[2];
                                    switch (sAction) {
                                        case "ADD":
                                            dodano++;
                                            propMail.setProperty(sKey, sValue);
                                            propMail.store(out, korisnik);
                                            break;
                                        case "REMOVE":
                                            intRemove++;
                                            propMail.remove(sKey, sValue);
                                            propMail.store(out, korisnik);
                                            break;
                                        case "UPDATE":
                                            intUpdate++;
                                            propMail.setProperty(sKey, sValue);
                                            propMail.store(out, korisnik);
                                            break;
                                    }

                                    out.close();
                                    ukupPoruke++;
                                    brojPoruka++;

                                    fValidated.appendMessages(mLimbo);
                                    m.setFlag(Flags.Flag.DELETED, true);
                                } else {
                                    intGreske++;
                                    fOther.appendMessages(mLimbo);
                                    m.setFlag(Flags.Flag.DELETED, true);
                                }

                            } else {
                                intGreske++;
                                fOther.appendMessages(mLimbo);
                                m.setFlag(Flags.Flag.DELETED, true);
                            }
                        } else {
                            intGreske++;
                            fOther.appendMessages(mLimbo);
                            m.setFlag(Flags.Flag.DELETED, true);
                        }
                    } else {
                        intGreske++;
                        fOther.appendMessages(mLimbo);
                        m.setFlag(Flags.Flag.DELETED, true);
                    }

}
            } catch (Exception e) {}
try {} catch (MessagingException ex) {}
try {} catch (InterruptedException ex) {}
        }

    }
    public void setBp(BP_Konfiguracija bp) {
        this.bp = bp;
    }

    public void setDirektorij(String direktorij) {
        this.direktorij = direktorij;
    }
    
    public void Statistika(Date datumPocetak, Date datumKraj,
            double vrijemeProteklo, int ukupPoruke, int brojPoruka,
            int dodani, int update, int remove, int greske,
            String kreiraniDirektoriji, String kreiraneDatoteke) throws NoSuchProviderException, MessagingException {
                
                String poruka = "Pocetak: " + datumPocetak.toString() + "\r\n " +
                        "Kraj: " + datumKraj.toString() + "\r\n " + "Proteklo: " + vrijemeProteklo + 
                        "\r\n " + "Ukupno poruka" + ukupPoruke + "\r\n " +
                        "Ciklus br. poruka: " + brojPoruka + "\r\n " +
                        "Br. dodanih svojstva: " + dodani + "\r\n " +
                        "Br. ažuriranih svojstava: " + update + "\r\n " + 
                        "Br. izbrisanih svojstava: " + remove + "\r\n " +
                        "Br. nepravilnih poruka: " + greske + "\r\n " + 
                        "Kreirane mape: " + kreiraniDirektoriji + "\r\n " +
                        "Kreirane datoteke: " + kreiraneDatoteke;

                // Set the subject and text
                message.setSubject("Statistika");
                message.setText(poruka);
                message.setSentDate(new Date());
                
                Transport.send(message);

    }
}
import org.foi.nwtis.student9.konfiguracije.bp.BP_Konfiguracija;

public class SlusacAplikacije implements ServletContextListener {
    private ObradaPoruka obrada = new ObradaPoruka();    
public void contextDestroyed(ServletContextEvent sce) {
        obrada.interrupt();                
    }
}
@ManagedBean
@SessionScoped
public class EmailPovezivanje {    
    
    private String server = "127.0.0.1" ;
    private String korisnik;// ="servis@nwtis.nastava.foi.hr" ;
    private String lozinka;// = "" ;
    private String subject;
    Provjere provjere = new Provjere();
/**
     * Creates a new instance of EmailPovezivanje
     */
    
    public EmailPovezivanje() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        BP_Konfiguracija bp = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        korisnik = bp.getEmail_username();
        lozinka = bp.getEmail_password();
        subject = bp.getEmail_subject();          
}

    
}
public class Lokalizacija {
   
public Object odaberiJezik () {
return "";
    }
}
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.foi.nwtis.student9.web.kontrole.Poruka;

public class PregledPoruke implements Serializable{
    private Poruka poruka;

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
        PregledSvihPoruka pregledSvih = (PregledSvihPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        poruka = pregledSvih.getOdabranaPoruka();

}

}
public class PregledSvihPoruka {
    private String idPoruka;
    private Poruka odabranaPoruka;
public String odaberiMapu() throws IOException {
        this.getPoruka();
        return "";
    }
    
    public  String pregledPoruke() {
        for(Poruka p : poruke) {
            if (p.getId().equals(idPoruka)) {
                odabranaPoruka = p;
                break;
                
            }
        }
        return "OK";

}
}
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Store;

public class SlanjePoruke implements Serializable {
public Object saljiPoruku() {
try {
            Transport.send(message);

return "";

        } catch (Exception e) {}
return "";
    }
    
    public void spremiPoslano(Store store, MimeMessage message) throws MessagingException {            
            Folder folder = store.getFolder("SENT");
            if (! folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.appendMessages(new Message[] {message});
            System.out.println("Poruka je pohranjena!"); 

        
    }
}
