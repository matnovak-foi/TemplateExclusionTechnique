/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.kontrole;

import java.util.Date;
import java.util.List;
import javax.mail.Flags;

/**
 *
 * @author teacher2
 */
public class Poruka {
    private String id;
    private Date vrijeme;
    private String salje;
    private String predmet;
    private String vrsta;
    private int velicina;    
    private int brojPrivitaka;
    private Flags zastavice;
    private List<PrivitakPoruke> privitciPoruke;
    private boolean brisati;
    private boolean procitano;
    private String sadrzaj;

    public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano, String sadrzaj) {
        this.id = id;
        this.vrijeme = poslano;
        this.salje = salje;
        this.predmet = predmet;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.brojPrivitaka = brojPrivitaka;
        this.zastavice = zastavice;
        this.privitciPoruke = privitciPoruke;
        this.brisati = brisati;
        this.procitano = procitano;
        this.sadrzaj = sadrzaj;
        
    }

    public String getId() {
        return id;
    }

    public boolean isBrisati() {
        return brisati;
    }

    public void setBrisati(boolean brisati) {
        this.brisati = brisati;
    }

    public int getBrojPrivitaka() {
        return brojPrivitaka;
    }

    public Flags getZastavice() {
        return zastavice;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public String getPredmet() {
        return predmet;
    }

    public List<PrivitakPoruke> getPrivitciPoruke() {
        return privitciPoruke;
    }

    public boolean isProcitano() {
        return procitano;
    }

    public void setProcitano(boolean procitano) {
        this.procitano = procitano;
    }
    
    public String getSalje() {
        return salje;
    }

    public String getVrsta() {
        return vrsta;
    }

    public int getVelicina() {
        return velicina;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.kontrole;

/**
 *
 * @author teacher2
 */
public class PrivitakPoruke {

    private int broj;
    private String vrsta;
    private int velicina;
    private String datoteka;

    public PrivitakPoruke(int broj, String vrsta, int velicina, String datoteka) {
        this.broj = broj;
        this.vrsta = vrsta;
        this.velicina = velicina;
        this.datoteka = datoteka;
    }

    public int getBroj() {
        return broj;
    }

    public String getDatoteka() {
        return datoteka;
    }

    public int getVelicina() {
        return velicina;
    }

    public String getVrsta() {
        return vrsta;
    }
    
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.student9.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author student9c
 */
public class Provjere {

    private BP_Konfiguracija bpk;

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

/**
 *
 * @author NWTiS_1
 */
public class ObradaPoruka extends Thread {

    // todo preuzeti podatke iz postavki
    private String server = "127.0.0.1";
    private String korisnik;// = "servis@nwtis.nastava.foi.hr";
    private String lozinka;// = "123456";
    private String subject;
    private BP_Konfiguracija bp;
    private String direktorij;
    private Provjere provjeraDb = new Provjere();
    private String dPodaci;
    private String fileName;

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
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
                // Start the session
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", server);
                Session session = Session.getInstance(properties, null);

                // Connect to the store
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);

                Folder osnovnaMapa = store.getDefaultFolder();

                // todo preuzmi iz postavi
                String mapaNWTisPoruke = "NWTIS_poruke";
                if (!osnovnaMapa.getFolder(mapaNWTisPoruke).exists()) {
                    osnovnaMapa.getFolder(mapaNWTisPoruke).create(Folder.HOLDS_MESSAGES);
                }

                // todo preuzmi iz postavi
                String mapaNeNWTisPoruke = "Ostale_poruke";
                if (!osnovnaMapa.getFolder(mapaNeNWTisPoruke).exists()) {
                    osnovnaMapa.getFolder(mapaNeNWTisPoruke).create(Folder.HOLDS_MESSAGES);
                }

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

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
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
            long krajNano = System.nanoTime();
            double proteklo = (krajNano - pocetakNano)/1e6;
            Date datumKraj = new Date();
            try {
                Statistika(datumPocetak, datumKraj, proteklo, ukupPoruke, brojPoruka, dodano,
                        intUpdate, intRemove, intGreske, kreiraniDirektoriji, kreiraneDatoteke);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }

            // todo preuzeti iz postavki
            int brojSekundiZaSpavanje = 30;
            try {
                sleep(brojSekundiZaSpavanje * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
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
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", server);
                Session session = Session.getInstance(properties, null);

                // Connect to the store
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);
                MimeMessage message = new MimeMessage(session);

                Address fromAddress = new InternetAddress(korisnik);
                message.setFrom(fromAddress);

                // Parse and set the recipient addresses
                Address[] toAddresses = InternetAddress.parse(korisnik);
                message.setRecipients(Message.RecipientType.TO, toAddresses);

                // Set the subject and text
                message.setSubject("Statistika");
                message.setText(poruka);
                message.setSentDate(new Date());
                
                Transport.send(message);

    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.slusaci;

import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.student9.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author NWTiS_1
 */
public class SlusacAplikacije implements ServletContextListener {
    private ObradaPoruka obrada = new ObradaPoruka();    

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String direktorij = sce.getServletContext().getRealPath("/WEB-INF")
                + java.io.File.separator;
        String datoteka = direktorij + 
                sce.getServletContext().getInitParameter("BP_Konfiguracija");
        BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);
        sce.getServletContext().setAttribute("BP_Konfig", bpk);
        ArrayList ak = new ArrayList();
        sce.getServletContext().setAttribute("AktivniKorisnici", ak); 
        obrada.setBp(bpk);
        obrada.setDirektorij(direktorij);
        obrada.start();        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("BP_Konfig");
        sce.getServletContext().removeAttribute("AktivniKorisnici");
        obrada.interrupt();                
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.student9.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student9.web.kontrole.Provjere;

/**
 *
 * @author NWTiS_1
 */
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
    


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }
    
        public String saljiPoruku () {
        return "OK";
    }
    
    public String citajPoruku () {
        String korisnikProvjera = provjere.provjeraKorisnika(korisnik, lozinka);
        if(korisnikProvjera.equals("OK")){
        return "OK";
        } else return "ERROR";
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.zrna;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@SessionScoped
public class Lokalizacija {
   private static final Map<String, Object> jezici;

    public Map<String, Object> getJezici() {
        return jezici;
    }
   private String odabraniJezik;
   private Locale vazecaLokalizacija;
   
   static {
       jezici = new HashMap<>();
       jezici.put("Hrvatski", new Locale ("hr"));
       jezici.put("English", Locale.ENGLISH);
       jezici.put("Deutsch", Locale.GERMAN);
   }

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
        //vazecaLokalizacija = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        //odabraniJezik = vazecaLokalizacija.getLanguage();
    }

    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    public Locale getVazecaLokalizacija() {
        return vazecaLokalizacija;
    }

    public void setVazecaLokalizacija(Locale vazecaLokalizacija) {
        this.vazecaLokalizacija = vazecaLokalizacija;
    }
    
    
    
    public Object odaberiJezik () {
        Iterator i = jezici.keySet().iterator();
        while (i.hasNext()) {
            String kljuc = (String) i.next();
            Locale l = (Locale) jezici.get(kljuc);
            if (odabraniJezik.equals(l.getLanguage())) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale(l);
                vazecaLokalizacija = l;
                return "OK";
            }
        }
        return "ERROR";
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.foi.nwtis.student9.web.kontrole.Poruka;
/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@RequestScoped
public class PregledPoruke implements Serializable{
    private Poruka poruka;

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
        PregledSvihPoruka pregledSvih = (PregledSvihPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        poruka = pregledSvih.getOdabranaPoruka();
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.zrna;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.student9.web.kontrole.Poruka;
import org.foi.nwtis.student9.web.slusaci.ObradaPoruka;

/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@SessionScoped
public class PregledSvihPoruka {

    private String server;
    private String korisnik;
    private String lozinka;
    private List<Poruka> poruke;
    private Map<String, String> mape;
    private String odabranaMapa = "INBOX";
    private String idPoruka;
    private Poruka odabranaPoruka;

    /**
     * Creates a new instance of PregledSvihPoruka
     */
    public PregledSvihPoruka() {
        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        server = ep.getServer();
        korisnik = ep.getKorisnik();
        lozinka = ep.getLozinka();

        try {
            // Start the session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);

            // Connect to the store
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);

            Folder osnovnaMapa = store.getDefaultFolder();
            Folder[] folderi = osnovnaMapa.list();

            mape = new HashMap<>();

            for (Folder f : folderi) {
                mape.put(f.getName(), f.getName());
            }
            store.close();

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Poruka> getPoruka() throws IOException {
        // todo optimizirati broj čitanja poruka
        System.out.println("getPoruka");
        try {
            // Start the session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);

            // Connect to the store
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);

            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();

            poruke = new ArrayList<>();
            // Print each message
            for (int i = 0; i < messages.length; ++i) {
                MimeMessage m = (MimeMessage) messages[i];
                String tip = m.getContentType().toLowerCase();
                Poruka p = new Poruka(m.getHeader("Message-ID")[0],
                        m.getSentDate(), m.getFrom()[0].toString(),
                        m.getSubject(), m.getContentType(), m.getSize(), 0, m.getFlags(), null, false, true, (String) m.getContent());
                poruke.add(p);
            }
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        return poruke;
    }

    public void setPoruka(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    public Map<String, String> getMape() {
        return mape;
    }

    public void setMape(Map<String, String> mape) {
        this.mape = mape;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

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

    public String getIdPoruka() {
        return idPoruka;
    }

    public void setIdPoruka(String idPoruka) {
        this.idPoruka = idPoruka;
    }

    public Poruka getOdabranaPoruka() {
        return odabranaPoruka;
    }

    public void setOdabranaPoruka(Poruka odabranaPoruka) {
        this.odabranaPoruka = odabranaPoruka;
    }
    
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student9.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Store;

/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@RequestScoped
public class SlanjePoruke implements Serializable {
    private String server;
    private String korisnik;
    private String lozinka;

    private String salje;
    private String prima;
    private String predmet;
    private String poruka;

    /**
     * Creates a new instance of SlanjePoruke
     */
    public SlanjePoruke() {
    }

    public String getSalje() {
        return salje;
    }

    public void setSalje(String salje) {
        this.salje = salje;
    }

    public String getPrima() {
        return prima;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Object saljiPoruku() {
        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        String smtpHost = ep.getServer();
        server = ep.getServer();
        korisnik = ep.getKorisnik();
        lozinka = ep.getLozinka();
        

        try {
            // Create the JavaMail session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", smtpHost);

            Session session
                    = Session.getInstance(properties, null);

            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);

            // Construct the message
            MimeMessage message = new MimeMessage(session);

            // Set the from address
            Address fromAddress = new InternetAddress(salje);
            message.setFrom(fromAddress);

            // Parse and set the recipient addresses
            Address[] toAddresses = InternetAddress.parse(prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set the subject and text
            message.setSubject(predmet);
            message.setText(poruka);
            message.setSentDate(new Date());            
            
            spremiPoslano(store, message);

            Transport.send(message);
            
            
            

            return "OK";

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "ERROR";
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