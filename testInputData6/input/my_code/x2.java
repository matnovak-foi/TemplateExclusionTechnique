/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.kontrole;

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

    public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano) {
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

    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.kontrole;

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
package org.foi.nwtis.matnovak.web.slusaci;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author NWTiS_2
 */
public class ObradaPoruka extends Thread {

    private String server = "127.0.0.1";
    private String korisnik = "servis@nwtis.nastava.foi.hr";
    private String lozinka = "123456";

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        while (true) {
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
                
                //TODO preuzmi i postavi
                String mapaNWTisPoruke = "NWTIS_poruke";
                if(!osnovnaMapa.getFolder(mapaNWTisPoruke).exists()){
                    osnovnaMapa.getFolder(mapaNWTisPoruke).create(Folder.HOLDS_MESSAGES);
                }
                
                //TODO preuzmi i postavi
                String mapaNeNWTisPoruke = "Ostale_poruke";
                if(!osnovnaMapa.getFolder(mapaNeNWTisPoruke).exists()){
                    osnovnaMapa.getFolder(mapaNeNWTisPoruke).create(Folder.HOLDS_MESSAGES);
                }
                
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                
                Message[] messages = folder.getMessages();
                
                String NWTiS_predmet = "NWTiS poruka";
                
                // Print each message
                for (int i = 0; i < messages.length; ++i) {
                    MimeMessage m = (MimeMessage) messages[i];
                    String tip = m.getContentType().toLowerCase();
                    if(tip.equals("text/plain")){
                        if(m.getSubject().startsWith(NWTiS_predmet)){
                            String sadrzaj = (String) m.getContent();
                            System.out.println(sadrzaj);
                            //TODO dovršiti dalje
                        }
                    }
                }

            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);            } 
            catch (IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //todo preuzeti iz postavki
            int brojSekundiZaSpavanje = 30;
            try{
                sleep(brojSekundiZaSpavanje * 1000);
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
                break;
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.slusaci;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author NWTiS_2
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ObradaPoruka op;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String direktorij = sce.getServletContext().
                    getRealPath("/WEB-INF") + File.separator;
        String datoteka = direktorij + sce.getServletContext().
                    getInitParameter("konfiguracija");
        BP_konfiguracija bpk = new BP_konfiguracija(datoteka);
        sce.getServletContext().setAttribute("BP_Konfig", bpk);
        ArrayList ak = new ArrayList();
        sce.getServletContext().setAttribute("AktivniKorisnici", ak);
        
        op = new ObradaPoruka();
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
package org.foi.nwtis.matnovak.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@SessionScoped
public class EmailPovezivanje {

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
package org.foi.nwtis.matnovak.web.zrna;

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
public class Lokalizacija {

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
package org.foi.nwtis.matnovak.web.zrna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.matnovak.web.kontrole.Poruka;

/**
 *
 * @author NWTiS_2
 */
@ManagedBean
@RequestScoped
public class PregledSvihPoruka {

    private String server;
    private String korisnik;
    private String lozinka;
    private List<Poruka> poruke;
    private Map<String, String> mape;
    private String odabranaMapa = "INBOX";

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

    public List<Poruka> getPoruke() {
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
                Poruka p = new Poruka(m.getHeader("Message-ID")[0], m.getSentDate(),
                            m.getFrom()[0].toString(), m.getSubject(), m.getContentType(),
                            m.getSize(), 0, m.getFlags(), null, false, true);
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
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.zrna;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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