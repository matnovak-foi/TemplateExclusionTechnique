/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web;

/**
 *
 * @author grupa_2
 * prikaz podataka o datotekama
 */
public class Datoteka {
        public String apsolutna_putanja;
        public String naziv_datoteke;
        public int velicina;
        public String datum_i_vrijeme_kreiranja;
    public Datoteka(){
        
    }

    public String getApsolutna_putanja() {
        return apsolutna_putanja;
    }

    public void setApsolutna_putanja(String apsolutna_putanja) {
        this.apsolutna_putanja = apsolutna_putanja;
    }

    public String getNaziv_datoteke() {
        return naziv_datoteke;
    }

    public void setNaziv_datoteke(String naziv_datoteke) {
        this.naziv_datoteke = naziv_datoteke;
    }

    public int getVelicina() {
        return velicina;
    }

    public void setVelicina(int velicina) {
        this.velicina = velicina;
    }

    public String getDatum_i_vrijeme_kreiranja() {
        return datum_i_vrijeme_kreiranja;
    }

    public void setDatum_i_vrijeme_kreiranja(String datum_i_vrijeme_kreiranja) {
        this.datum_i_vrijeme_kreiranja = datum_i_vrijeme_kreiranja;
    }
    
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web;

import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.student4.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student4.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student4.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author Sname4 provjeraPoruka je klasa koja se pokrece u
 * SlusacuAplikacije. obrada poruka te njihov zapis u tablicu ELEMENTI
 */
public class provjeraPoruka extends Thread {

    private BP_Konfiguracija konfig;

    private String email_posluzitelj;// 127.0.0.1
    private String korisnicko_ime;// servis@nwtis.nastava.foi.hr
    private String lozinka;// 123456
    private String predmet;//NWTiS poruka
    private String emailAdresa;
    private String emailPredmet;
    private int interval;// = 300;
    String naziv;

    private FacesContext context = FacesContext.getCurrentInstance();
    private ServletContext sc = (ServletContext) context.getExternalContext().getContext();

    BP_Konfiguracija bp = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");

    public provjeraPoruka() {
        super();
    }

    public void setKonfig(BP_Konfiguracija konfig) {
        this.konfig = konfig;
    }

    public BP_Konfiguracija getKonfig() {
        return konfig;
    }

    /**
     * zapis u tablicu ELEMENTI
     *
     * @param vrsta
     * @param naziv
     */
    public void bazaPodataka(String vrsta, String naziv) {

        String connUrl = bp.getServerDatabase() + bp.getUserDatabase();
        String sql = "INSERT INTO ELEMENTI(VRSTA, NAZIV) VALUE('" + vrsta + "','" + naziv + "');";
        try {
            Class.forName(bp.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            System.out.println("ERROR");
            return;
        }
        try (
                Connection conn = DriverManager.getConnection(connUrl, bp.getUserUsername(), bp.getUserPassword());
                java.sql.Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                System.out.println("NOT_OK");
                return;

            }

            
            System.out.println("naziv" + naziv);
            System.out.println("vrsta" + vrsta);

        } catch (SQLException ex) {
            System.out.println("ERROR");

        }

    }

    /**
     * za update kod slanja maila- provjerava postoji li zapis u tablici ELEMENTI
     *
     * @return NAZIV
     */
    public String bazaPodatakaProvjeraUpdate() {

        String connUrl = bp.getServerDatabase() + bp.getUserDatabase();
        String sql = "SELECT VRSTA, NAZIV FROM ELEMENTI";
        try {
            Class.forName(bp.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            return "ERROR";
        }
        try (
                Connection conn = DriverManager.getConnection(connUrl, bp.getUserUsername(), bp.getUserPassword());
                java.sql.Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {

            if (!rs.next()) {
                return "NOT_OK";
            }

            String VRSTA = rs.getString("VRSTA");
            String NAZIV = rs.getString("NAZIV");
            System.out.println("VRSTA " + VRSTA + "  NAZIV " + NAZIV);
            return NAZIV;

        } catch (SQLException ex) {
            return "ERROR";
        }

    }

    @Override
    public synchronized void start() {
        try {
            String path = sc.getRealPath("WEB-INF");
            String datoteka = sc.getInitParameter("konfiguracija");
            Properties p = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + java.io.File.separator + datoteka).dajSvePostavke();

            email_posluzitelj = p.getProperty("email_posluzitelj");
            korisnicko_ime = p.getProperty("korisnicko_ime");
            lozinka = p.getProperty("lozinka");
            predmet = p.getProperty("predmet");
            interval = Integer.parseInt(p.getProperty("interval"));
            emailAdresa = p.getProperty("email_adresa");
            emailPredmet = p.getProperty("email_predmet");

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(provjeraPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.start();
    }

    @Override
    public void run() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;

        int brojIspravnih = 0;
        int ostalePoruke = 0;
        int brojDatoteka = 0;
        int ukupnoPoruke = 0;

        long pocetakObrade;
        long krajObrade;

        while (true) {
            pocetakObrade = System.currentTimeMillis();
            try {
                System.out.println("--------------processing mails started-----------------");
                session = Session.getDefaultInstance(System.getProperties(), null);

                System.out.println("getting the session for accessing email.");
                store = session.getStore("imap");

                store.connect(email_posluzitelj, korisnicko_ime, lozinka);
                System.out.println("Connection established with IMAP server.");

                // Get a handle on the default folder
                folder = store.getDefaultFolder();

                System.out.println("Getting the Inbox folder.");

                // Retrieve the "Inbox"
                folder = folder.getFolder("inbox");

                //Reading the Email Index in Read / Write Mode
                folder.open(Folder.READ_WRITE);

                // Retrieve the messages
                messages = folder.getMessages();
                Message[] ispravne = null;
                Message[] ostale = null;

                // Loop over all of the messages
                for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                    // Retrieve the next message to be read
                    message = messages[messageNumber];
                    ukupnoPoruke++;

                    if (message.getSubject().startsWith(predmet)) {

                        // Retrieve the message content
                        messagecontentObject = message.getContent();

                        String ADDiliUPDATE = "";
                        String VRSTA = "";

                        if (messagecontentObject instanceof Multipart) {
                            System.out.println("poruka s privitkom");
                        } else{

                            sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
                            //contentType = message.getContentType();

                            System.out.println("If the personal information has no entry, check the address "
                                    + "for the sender information.");

                            if (sender == null) {
                                sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                                System.out.println("sender in NULL. Printing Address:" + sender);
                            }
                            System.out.println("Sender -." + sender);

                            // Get the subject information
                            subject = message.getSubject();

                            System.out.println("subject=" + subject);

                            if (subject.equals("NWTiS poruka")) {
                                System.out.println("Obrađujemo poruku!");
                            

                            // Loop over the parts of the email
                            for (int i = 0; i < multipart.getCount(); i++) {
                                // Retrieve the next part
                                part = multipart.getBodyPart(i);

                                // Get the content type
                                contentType = part.getContentType();

                                // Display the content type
                                System.out.println("Content: " + contentType);

                                if (contentType.startsWith("TEXT/PLAIN")) {

                                    String sintaksa = "^(GRAD[a-zA-Z]|TVRTKA[a-zA-Z]);(ADD|UPDATE);";
                                    Matcher m;
                                    String content = message.getContent().toString().trim().replace("\r\n", ";").replace(" ", "");
                                    Pattern pattern = Pattern.compile(sintaksa);
                                    m = pattern.matcher(content);
                                    boolean status = m.matches();

                                    if (status) {
                                        VRSTA = m.group(0);
                                        ADDiliUPDATE = m.group(0);

                                        if (ADDiliUPDATE.contains("ADD")) {

                                            naziv = m.group(1);
                                            if(VRSTA.contains("GRAD")){
                                                VRSTA = "GRAD";
                                            }
                                            else{
                                                VRSTA="TVRTKA";
                                            }
                                            bazaPodataka(VRSTA, naziv);
                                            brojIspravnih++;

                                            //provjera web mjesta
                                            final URL url = new URL("http://www.foi.unzig.hr");
                                            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                                            // huc.setRequestMethod("HEAD");
                                            int responseCode = huc.getResponseCode();

                                            if (responseCode != 404) {
                                                System.out.println("GOOD");

                                                SimpleDateFormat datum = new SimpleDateFormat("yyyy.MM.dd hh.mm.ss");
                                                File filed = new File("C:\\Users\\Sname4\\Documents\\NetBeansProjects\\student4\\student4_zadaca_2\\web\\WEB-INF\\podaci\\" + naziv + "_" + datum + ".txt");
                                                if (!filed.exists()) {
                                                    if (filed.mkdir()) {
                                                        System.out.println("directory is created");
                                                        brojDatoteka++;
                                                    }
                                                } else {
                                                    System.out.println("directory exist");
                                                }

                                            } else {
                                                System.out.println("BAD--ne postoji stranica");
                                            }

                                        } else if (ADDiliUPDATE.contains("UPDATE")) {

                                            naziv = m.group(1);
                                            if(VRSTA.contains("GRAD")){
                                                VRSTA = "GRAD";
                                            }
                                            else{
                                                VRSTA="TVRTKA";
                                            }
                                            if (bazaPodatakaProvjeraUpdate() == naziv) {
                                                bazaPodataka(VRSTA, naziv);
                                                brojIspravnih++;
                                            } else {
                                                System.out.println("ne postoji  zapis u tablici!!");
                                            }

                                        } else {
                                            ostalePoruke++;
                                        }
                                    }
                                } else {

                                    System.out.println("Nepoznata vrsta poruke");
                                }
                            }
                            } else {
                                System.out.println("NE obrađujemo poruku!");
                            }
                        }
                    } else {
                        ostalePoruke++;
                    }
                        

                }
                // Close the folder
                folder.close(true);
                // Close the message store
                store.close();

                krajObrade = System.currentTimeMillis();
                //slanje email poruke
                try {
                    // Create the JavaMail session
                    java.util.Properties properties = System.getProperties();
                    properties.put("mail.smtp.host", email_posluzitelj);

                    Session emailSession = Session.getInstance(properties, null);

                    // Construct the message
                    MimeMessage emailMessage = new MimeMessage(emailSession);

                    // Set the from address
                    Address fromAddress = new InternetAddress(this.korisnicko_ime);
                    emailMessage.setFrom(fromAddress);

                    // Parse and set the recipient addresses
                    Address[] toAddresses = InternetAddress.parse(this.emailAdresa);
                    emailMessage.setRecipients(Message.RecipientType.TO, toAddresses);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
                    String poc = formatter.format(new Date(pocetakObrade));
                    String kraj = formatter.format(new Date(krajObrade));
                    long trajanje = (krajObrade - pocetakObrade) * 1000;

                    String sadrzaj = "Obrada započela u: " + poc + "Obrada završila u: " + kraj + "Trajanje obrade: " + trajanje
                            + "Sveukupan broj poruka: " + ukupnoPoruke + "Broj poruka u ovom prolazu: " + messages.length
                            + "Broj ispravnih poruka: " + brojIspravnih
                            + "Broj ostalih poruka: " + ostalePoruke + "broj datoteka: " + brojDatoteka;

                    // Set the subject and text
                    emailMessage.setSubject(emailPredmet);
                    emailMessage.setText(sadrzaj);

                    System.out.println("SLANJE PORUKE " + emailAdresa);
                    Transport.send(emailMessage);
                    System.out.println("PORUKA POSLANA");

                } catch (AddressException e) {
                } catch (SendFailedException e) {
                } catch (MessagingException e) {
                }

            } catch (AuthenticationFailedException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (FolderClosedException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (FolderNotFoundException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (NoSuchProviderException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (ReadOnlyFolderException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (StoreClosedException e) {
                System.out.println("Not able to process the mail reading.");
            } catch (Exception e) {
                System.out.println("Not able to process the mail reading.");
            }

            try {
                sleep(interval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(provjeraPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student4.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student4.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student4.web.provjeraPoruka;

/**
 * Web application lifecycle listener.
 *
 * @author grupa_2 Dodana je instanca za provjeru poruka, kako bi se mogla
 * pokrenuti dretva
 */
@WebListener()
public class SlusacAplikacije implements ServletContextListener {

    private provjeraPoruka poruke;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();
        String dir = context.getRealPath("/WEB-INF");
        String datoteka = dir + File.separator
                + context.getInitParameter("konfiguracija");

        BP_Konfiguracija bp_konfig = null;
        try {

            bp_konfig = new BP_Konfiguracija(datoteka);
            context.setAttribute("BP_Konfig", bp_konfig);
            System.out.println("server:" + bp_konfig.getServerDatabase());

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        poruke = new provjeraPoruka();
        poruke.setKonfig(bp_konfig);
        poruke.start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (poruke != null && !poruke.isInterrupted()) {
            poruke.interrupt();
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;

/**
 *
 * @author grupa_2
 */
@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {
private static Map<String, Object> jezici = new HashMap<String, Object>();
    private String odabraniJezik;
    private Locale vazeciJezik;

   
    static{
        jezici.put("hr", new Locale("hr"));
        jezici.put("en", new Locale("en"));
        jezici.put("de", new Locale("de"));
    }

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
        odabraniJezik = "hr";
        vazeciJezik = (Locale) jezici.get(odabraniJezik);
     
        
    }
    
     public String getOdabraniJezik() {
        
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    public Locale getVazeciJezik() {
       //  vazeciJezik =  FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return vazeciJezik;
    }

   
    public Object odaberiJezik(){
        if(this.odabraniJezik != null || jezici.get(this.odabraniJezik )!= null){
            FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)jezici.get(this.odabraniJezik));
            this.vazeciJezik = (Locale) jezici.get(this.odabraniJezik);
            return "OK";
            
        }
        else{
            return "ERROR";
        }
        
    }

    public  Map<String, Object> getJezici() {
        return jezici;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Sname4
 */
@Named(value = "pregledDatoteke")
@RequestScoped
public class PregledDatoteke {
    
    //public String datoteka;
    

    /**
     * Creates a new instance of PregledDatoteke
     */
    public PregledDatoteke() {
    }
    
    public String povratakPregledPreuzetihPodataka(){
        return "OK";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web.zrna;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.student4.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student4.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author grupa_2
 */
@Named(value = "pregledPreuzetihPodataka")
@RequestScoped
public class PregledPreuzetihPodataka {

    private String email_posluzitelj;// nwtis.nastava.foi.hr
    private String korisnicko_ime;// service@nwtis.nastava.foi.hr
    private String lozinka;// 123456
    private String praznoKorime;
    private String praznoLozinka;
    private String praznoPosluzitelj;
    private String error;
    private String datoteka;
    private Map<String, String> mape;
    private String odabranaMapa = "";
    private String odabranaPoruka;
    private String porukaID;

    private String ispravnePoruke;
    private String ostalePoruke;
    private String inbox;
    
   

    private FacesContext context = FacesContext.getCurrentInstance();
    private ServletContext sc = (ServletContext) context.getExternalContext().getContext();

    /**
     * Creates a new instance of PregledPreuzetihPodataka
     */
    public PregledPreuzetihPodataka() {
        
        String path = sc.getRealPath("WEB-INF");
        String datoteka = sc.getInitParameter("konfiguracija");
        Properties p;
        try {
            p = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + File.separator + datoteka).dajSvePostavke();
            ispravnePoruke = p.getProperty("ispravnePoruke");
            ostalePoruke = p.getProperty("ostalePoruke");
            inbox = "inbox";

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(PregledPreuzetihPodataka.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public String pregledDatoteke() {
        if (!datoteka.isEmpty()) {
            return "OK";
        } else {
            return "NOT_OK";
        }
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student4.web.zrna;

import static com.oracle.jrockit.jfr.ContentType.Address;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author grupa_2
 * klasa za slanje poruke
 */
@Named(value = "slanjePoruke")
@RequestScoped
public class SlanjePoruke {
    private String salje;
    private String prima;
    private String predmet;
    private String tekst;
    private String tipPoruke;
    private String posluzitelj = "localhost";//TODO procitati iz konfiguracije
    private String poruka = "";

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

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    /**
     * finkcija za slanje poruke
     * @return 
     */
    public String saljiPoruku(){
      try {
            // Create the JavaMail session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", posluzitelj);

            Session session = Session.getInstance(properties, null);

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
            message.setText(tekst);

            Transport.send(message);

            poruka = "Poruka je uspjesno poslana!";
            
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "ERROR "+ e.getMessage();
            return "ERROR";
        } 

    }
    /**
     * getter poruke
     * @return poruka
     */
    public String getPoruka() {
        return poruka;
    }
    /**
     * setter poruke
     * @param poruka 
     */

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    
    
    
}