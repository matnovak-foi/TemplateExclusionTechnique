/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.kontrole;

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
    private String sadrzaj;
    private int velicina;    
    private int brojPrivitaka;
    private Flags zastavice;
    private List<PrivitakPoruke> privitciPoruke;
    private boolean brisati;
    private boolean procitano;

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
package org.foi.nwtis.student2.web.kontrole;

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
package org.foi.nwtis.student2.web.slusaci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.student2.konfiguracije.Konfiguracija;

/**
 *
 * @author NWTiS_2
 */
public class ObradaPoruka extends Thread {

    private String server;
    private String korisnik;
    private String lozinka;
    private String admin;
    private String noviDirektoriji = "";
    private String noveDatoteke = "";
    
    private Konfiguracija konfig;
    private double redniBroj = 0;
    private int ukupniBrojPoruka = 0;
    

    int brojSekundiZaSpavanje;

    private final String regex = "^USER ([a-zA-Z0-9_]+); PASSWORD ([a-zA-Z0-9_]+); KEY ([a-zA-Z0-9_]+); VALUE ([a-zA-Z0-9_]+); (ADD|UPDATE|DELETE);";

    public ObradaPoruka(Konfiguracija konfig) {
        this.konfig = konfig;
        server      = konfig.dajPostavku("adresa");
        korisnik    = konfig.dajPostavku("email");
        lozinka     = konfig.dajPostavku("lozinka");
        admin       = konfig.dajPostavku("admin");
    }

    @Override
    public void run() {
        brojSekundiZaSpavanje = Integer.parseInt(konfig.dajPostavku("interval"));
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("#.###");
        Pattern pattern = Pattern.compile(regex);

        while (true) {
            try {
                SimpleDateFormat pocetakObrade = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
                String pocetakObradeFormat = pocetakObrade.format(new Date());
                long pocetak = System.currentTimeMillis();
                int brojPorukaCiklus = 0;
                int brojDodanihPodataka = 0;
                int brojAzuriranihPodataka = 0;
                int brojObrisanihPodataka = 0;
                int brojPogresakaKodPodataka = 0;

                // Start the session
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", server);
                Session session = Session.getInstance(properties, null);

                // Connect to the store
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);

                // Open the INBOX folder
                Folder osnovnaMapa = store.getDefaultFolder();

                String mapaNWTisPoruke = konfig.dajPostavku("ispravne_poruke");
                if (!osnovnaMapa.getFolder(mapaNWTisPoruke).exists()) {
                    osnovnaMapa.getFolder(mapaNWTisPoruke).create(Folder.HOLDS_MESSAGES);
                }

                String mapaNWTisPorukeOstalo = konfig.dajPostavku("ostale_poruke");
                if (!osnovnaMapa.getFolder(mapaNWTisPorukeOstalo).exists()) {
                    osnovnaMapa.getFolder(mapaNWTisPorukeOstalo).create(Folder.HOLDS_MESSAGES);
                }

                String poslanePoruke = konfig.dajPostavku("poslane_poruke");
                if (!osnovnaMapa.getFolder(poslanePoruke).exists()) {
                    osnovnaMapa.getFolder(poslanePoruke).create(Folder.HOLDS_MESSAGES);
                }

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

                Message[] messages = folder.getMessages();

                String NWTiS_predmet = konfig.dajPostavku("trazeni_sadrzaj");

                // Print each message
                for (int i = 0; i < messages.length; ++i) {
                    ukupniBrojPoruka++;
                    brojPorukaCiklus++;
                    MimeMessage m = (MimeMessage) messages[i];
                    String tip = m.getContentType().toLowerCase();
                    if (tip.startsWith("text/plain")) {
                        if (m.getSubject().startsWith(NWTiS_predmet)) {
                            String sadrzaj = (String) m.getContent();
                            //TODO dovršiti dalje
                            Matcher matcher = pattern.matcher(sadrzaj.replaceAll("(\\r|\\n)", ""));
                            if (matcher.matches()) {
                                if (provjeriKorisnika(matcher.group(1), matcher.group(2))) {
                                    // premjesti u ispravne poruke folder
                                    premjestiPoruku(mapaNWTisPoruke, messages[i], store);
                                    // izbriši poruku iz inboxa
                                    izbrisiPoruku(folder, messages[i]);
                                    // provjeri dali je ADD, UPDATE ili DELETE
                                    Properties userProperties = new Properties();
                                    userProperties.setProperty(matcher.group(3), matcher.group(4));
                                    switch (matcher.group(5)) {
                                        case "ADD":
                                            brojDodanihPodataka++;
                                            dodajZapis(matcher.group(1), userProperties);
                                            break;
                                        case "DELETE":
                                            brojObrisanihPodataka++;
                                            izbrisiZapis(matcher.group(1), matcher.group(3));
                                            break;
                                        case "UPDATE":
                                            brojAzuriranihPodataka++;
                                            promijeniZapis(matcher.group(1), matcher.group(3), matcher.group(4));
                                            break;
                                    }
                                } else {
                                    // premjesti u ostale poruke folder
                                    premjestiPoruku(mapaNWTisPorukeOstalo, messages[i], store);
                                    // izbriši poruku iz inboxa
                                    izbrisiPoruku(folder, messages[i]);
                                }
                            } else {
                                // premjesti u ostale poruke folder
                                premjestiPoruku(mapaNWTisPorukeOstalo, messages[i], store);
                                // izbriši poruku iz inboxa
                                izbrisiPoruku(folder, messages[i]);
                            }
                        } else {
                            // premjesti u ostale poruke folder
                            premjestiPoruku(mapaNWTisPorukeOstalo, messages[i], store);
                            // izbriši poruku iz inboxa
                            izbrisiPoruku(folder, messages[i]);
                        }
                    }
                }

                // pošalji izvješće adminu
                SimpleDateFormat krajObrade = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
                long vrijemeObrade = System.currentTimeMillis() - pocetak;

                MimeMessage message = new MimeMessage(session);

                Address fromAddress = new InternetAddress(korisnik);
                message.setFrom(fromAddress);

                Address[] toAddresses = InternetAddress.parse(admin);
                message.setRecipients(Message.RecipientType.TO, toAddresses);

                message.setSubject(konfig.dajPostavku("predmet_admin") + " " + decimalFormat.format(redniBroj++ / 1000.0));
                message.setText(""
                        + "Obrada započela u: " + pocetakObradeFormat + " \n"
                        + "Obrada završila u: " + krajObrade.format(new Date()) + " \n"
                        + "Trajanje obrade u ms: " + vrijemeObrade + " \n"
                        + "Ukupan broj poruka: " + ukupniBrojPoruka + " \n"
                        + "Broj poruka: " + brojPorukaCiklus + " \n"
                        + "Broj dodanih podataka: " + brojDodanihPodataka + " \n"
                        + "Broj ažuriranih podataka: " + brojAzuriranihPodataka + " \n"
                        + "Broj obrisanih podataka: " + brojObrisanihPodataka + " \n"
                        + "Broj pogrešaka kod podataka: " + brojPogresakaKodPodataka + " \n"
                        + "Kreirani direktoriji: " + noviDirektoriji + " \n"
                        + "Kreirane datoteke: " + noveDatoteke + " \n");

                Transport.send(message);

                // spremi u poslane poruke
                Folder sent = store.getFolder(konfig.dajPostavku("poslane_poruke"));
                sent.appendMessages(new Message[]{message});

            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                sleep(brojSekundiZaSpavanje * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                break;
            }
        }
    }

    /**
     * Premjesti poruku u zadani folder
     *
     * @param folder folder u kojeg se premješta poruka
     * @param message poruka koja se premješta
     * @param store Store iz kojeg se dohvati folder
     */
    private void premjestiPoruku(String folder, Message message, Store store) {
        try {
            // premjesti u ispravne poruke folder
            Folder move = store.getFolder(folder);
            move.open(Folder.READ_WRITE);
            move.appendMessages(new Message[]{message});
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Brisanje zadane poruke
     *
     * @param folder folder iz kojeg se briše poruka
     * @param message poruka koja se briše
     */
    private void izbrisiPoruku(Folder folder, Message message) {
        try {
            Flags delete = new Flags(Flags.Flag.DELETED);
            folder.setFlags(new Message[]{message}, delete, true);
            folder.expunge();
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za autentikaciju korisnika, provjerava se u bazi podataka dali
     * postoji korisnik sa korisničkim imenom i lozinkom koji se predaju kao
     * parametri
     *
     * @param username korisničko ime
     * @param password lozinka
     * @return true ako postoji takav korisnik, inace false
     */
    private boolean provjeriKorisnika(String username, String password) {
        String server = konfig.dajPostavku("server.database");
        String baza = server + konfig.dajPostavku("admin.database");
        String korisnik = konfig.dajPostavku("user.username");
        String lozinka = konfig.dajPostavku("user.password");
        String driver = konfig.dajPostavku("driver.database.derby");
        String upit = "SELECT kor_ime, lozinka FROM polaznici";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);
            Statement naredba = veza.createStatement();
            ResultSet odgovor = naredba.executeQuery(upit);

            while (odgovor.next()) {
                // provjeri dali postoji korisnik sa danom lozinkom
                if (username.equals(odgovor.getString(1)) && password.equals(odgovor.getString(2))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Metoda za spremanje zapisa u Properties datoteku
     * @param folder direktorij u kojem se nalazi datoteka
     * @param properties key i value koji se zapisuje u datoteku
     */
    private void dodajZapis(String folder, Properties properties) {
        String fileName = konfig.dajPostavku("datoteka");
        String filePath = SlusacAplikacije.webinfPath + "/" + folder;
        String fullPath = filePath + "/" + fileName;

        if (!new File(filePath).exists()) {
            noviDirektoriji = noviDirektoriji.concat(folder + ", ");
        }
        if (!new File(fullPath).exists()) {
            noveDatoteke = noveDatoteke.concat(folder + File.separator + fileName);
        }
        File file = new File(fullPath);
        System.out.println(file.getAbsolutePath());
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, true);
            properties.store(os, null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Metoda za promjenu zapisa u Properties datoteci
     * @param folder direktorij u kojem se nalazi datoteka
     * @param key ključ kojem se mijenja vrijdnost
     * @param value nova vrijednost
     */
    private void promijeniZapis(String folder, String key, String value) {
        String fileName = konfig.dajPostavku("datoteka");
        String filePath = SlusacAplikacije.webinfPath + "/" + folder;
        String fullPath = filePath + "/" + fileName;

        try {
            Properties properties;
            try (FileInputStream fis = new FileInputStream(fullPath)) {
                properties = new Properties();
                properties.load(fis);
                fis.close();
            }

            try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                properties.setProperty(key, value);
                properties.store(fos, null);
                fos.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda za brisanje postavke iz Properties datoteke
     * @param folder direktorij u kojem se nalazi datoteka
     * @param key ključ kojemu brišemo zapis
     */
    private void izbrisiZapis(String folder, String key) {
        String fileName = konfig.dajPostavku("datoteka");
        String filePath = SlusacAplikacije.webinfPath + "/" + folder;
        String fullPath = filePath + "/" + fileName;

        try {
            Properties properties;
            try (FileInputStream fis = new FileInputStream(fullPath)) {
                properties = new Properties();
                properties.load(fis);
                properties.remove(key);
                fis.close();
            }
            try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                properties.store(fos, null);
                fos.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.slusaci;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student2.konfiguracije.Konfiguracija;
import org.foi.nwtis.student2.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student2.konfiguracije.NemaKonfiguracije;

/**
 * Web application lifecycle listener.
 *
 * @author NWTiS_2
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ObradaPoruka op;
    Konfiguracija konfig = null;
    public static String webinfPath;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        webinfPath = sce.getServletContext().
                getRealPath("/WEB-INF") + File.separator;
        String datoteka = webinfPath + sce.getServletContext().
                getInitParameter("konfiguracija");
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        sce.getServletContext().setAttribute("BP_Konfig", konfig);
        ArrayList ak = new ArrayList();
        sce.getServletContext().setAttribute("AktivniKorisnici", ak);
        
        System.out.println(webinfPath);
        
        op = new ObradaPoruka(konfig);
        op.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("BP_Konfig");
        sce.getServletContext().removeAttribute("AktivniKorisnici");
        
        if(op != null && !op.isInterrupted()){
            op.interrupt();
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@SessionScoped
public class EmailPovezivanje implements Serializable {

    private String server = "127.0.0.1";
    private String korisnik = "servis@nwtis.nastava.foi.hr";
    private String lozinka = "123456";
    
    /**
     * Creates a new instance of EmailPovezivanje
     */
    public EmailPovezivanje() {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    
    public String saljiPoruku(){
        return "OK";
    }
    
    public String citajPoruku(){
        return "OK";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@SessionScoped
public class Lokalizacija implements Serializable {

    private static final Map<String,Object> jezici;
    private String odabraniJezik;
    private Locale vazecaLokalizacija;
    
    static {
        jezici = new HashMap<>();
        jezici.put("Hrvatski", new Locale("hr"));
        jezici.put("English", Locale.ENGLISH);
        jezici.put("Deutsch", Locale.GERMAN);
    }
    
    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {/*
        vazecaLokalizacija = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        odabraniJezik = vazecaLokalizacija.getLanguage();*/
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
    
    public Map<String, Object> getJezici(){
        return jezici;
    }
        
    public Object odaberiJezik() {
        Iterator i = jezici.keySet().iterator();
        while (i.hasNext()) {
            String kljuc = (String) i.next();
            Locale l = (Locale) jezici.get(kljuc);
            if(odabraniJezik.equals(l.getLanguage())){
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
package org.foi.nwtis.student2.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.student2.web.kontrole.Poruka;

/**
 *
 * @author Sname5
 */
@ManagedBean
@RequestScoped
public class PregledPoruke {
    
    private Poruka poruka;

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }
    
    public String povratakPregledSvihPoruka() {
        return "OK";
    }
    
    public String povratakIzborJezika() {
        return "OK";
    }
    
    public String povratakPostavkeMaila() {
        return "OK";
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.student2.web.kontrole.Poruka;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@SessionScoped
public class PregledSvihPoruka implements Serializable {

    private String server;
    private String korisnik;
    private String lozinka;
    private List<Poruka> poruke;
    private Map<String, String> mape;
    private String odabranaMapa = "INBOX";
    private int brojPoruka = 10;

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

            // Open the INBOX folder
            Folder osnovnaMapa = store.getDefaultFolder();

            Folder[] podMape = osnovnaMapa.list();
            mape = new HashMap<>();
            for (Folder f : podMape) {
                mape.put(f.getName(), f.getName());
            }

            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Poruka> getPoruke() throws IOException {
        //TODO optimizirati broj čitanja prouka
        System.out.println("getPoruka");
        try {
            // Start the session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            Session session = Session.getInstance(properties, null);

            // Connect to the store
            Store store = session.getStore("imap");
            store.connect(server, korisnik, lozinka);

            // Open the INBOX folder
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();
            poruke = new ArrayList<>();
            // Print each message
            for (int i = 0; i < messages.length; ++i) {
                MimeMessage m = (MimeMessage) messages[i];
                String tip = m.getContentType().toLowerCase();
                if (m.getSentDate() != null) {
                    System.out.println(m.getSentDate());
                }
                Poruka p = new Poruka(m.getHeader("Message-ID")[0], m.getSentDate(),
                            m.getFrom()[0].toString(), m.getSubject(), m.getContentType(),
                            m.getSize(), 0, m.getFlags(), null, false, true, m.getContent().toString());
                poruke.add(p);
            }

            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
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

    public String odaberiMapu() {
        return "";
    }
    
    public String pregledPoruke() {
        return "OK";
    }

    public int getBrojPoruka() {
        return brojPoruka;
    }

    public void setBrojPoruka(int brojPoruka) {
        this.brojPoruka = brojPoruka;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.util.Date;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@RequestScoped
public class SlanjePoruke {

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

    public String saljiPoruku() {
        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        String smtpHost = ep.getServer();

        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", smtpHost);
            Session session = Session.getInstance(properties, null);

            MimeMessage message = new MimeMessage(session);

            Address fromAddress = new InternetAddress(salje);
            message.setFrom(fromAddress);
            
            Address[] toAddresses = InternetAddress.parse(prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            
            message.setSubject(predmet);
            message.setText(poruka);
            message.setSentDate(new Date());
            
            Transport.send(message);
            
            return "OK";
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (SendFailedException e){
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}