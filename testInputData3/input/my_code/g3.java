
DATOTEKA_SlusacAplikacije.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.matnovak.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author grupa_3
 */
public class SlusacAplikacije implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String dir = context.getRealPath("/WEB-INF");
        System.out.println("WEB-INF dir: " + dir);
        String datoteka = dir + File.separator + context.getInitParameter("konfiguracija");
        BP_Konfiguracija bp_konfig = null;
        try {
            bp_konfig = new BP_Konfiguracija(datoteka);
            System.out.println("Server: " + bp_konfig.getServerDatabase());
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        context.setAttribute("BP_Konfig", bp_konfig);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
     
    }
}

DATOTEKA_Lokalizacija.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;

/**
 *
 * @author grupa_3
 */
@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {

    private static Map<String, Object> jezici = new HashMap<String, Object>();
    private String odabraniJezik;
    private Locale vazeciJezik;

    static {
        jezici.put("hr", new Locale("hr"));
        jezici.put("en", new Locale("en"));
        jezici.put("de", new Locale("de"));
    }

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
        odabraniJezik = "hr";
        vazeciJezik = new Locale("hr");
    }

    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    public Locale getVazeciJezik() {
        //vazeciJezik = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return vazeciJezik;
    }

    public Object odaberiJezik() {
        if (odabraniJezik != null && jezici.get(odabraniJezik) != null) {

            FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) jezici.get(this.odabraniJezik));
            vazeciJezik = (Locale) jezici.get(this.odabraniJezik);
            return "OK";
        } else {
            return "ERROR";
        }
    }

    public Map<String, Object> getJezici() {
        return jezici;
    }

}

DATOTEKA_SlanjePoruke.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.web.zrna;

import static java.lang.ProcessBuilder.Redirect.from;
import static java.lang.ProcessBuilder.Redirect.to;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static javax.ws.rs.client.Entity.text;

/**
 *
 * @author grupa_3
 */
@Named(value = "slanjePoruke")
@RequestScoped
public class SlanjePoruke {

    private String salje;
    private String prima;
    private String predmet;
    private String sadrzaj;
    private String poruka;
    private String posluzitelj = "localhost"; //TODO čitaj iz konfiguracije

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

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String posaljiPoruku() {

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
            message.setText(sadrzaj);

            Transport.send(message);

            poruka = "Poruka je uspješno poslana!";
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "ERROR: "+e.getMessage();
            return "ERROR";
        } 

        
    }
}
