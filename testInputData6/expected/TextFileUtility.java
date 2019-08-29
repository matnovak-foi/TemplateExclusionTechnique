

public class Poruka {
    private String sadrzaj;

public Poruka(String id, Date poslano, String salje, String predmet, String vrsta, int velicina, int brojPrivitaka, Flags zastavice, List<PrivitakPoruke> privitciPoruke, boolean brisati, boolean procitano, String sadrzaj) {
        this.sadrzaj = sadrzaj;
}
}
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
} catch (Exception e) {}

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
try(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){}
        } catch (Exception e) {}
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

}

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
}
}
public class EmailPovezivanje implements Serializable {

}
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.student2.web.kontrole.Poruka;
public class PregledPoruke {
}
import java.io.Serializable;

public class PregledSvihPoruka implements Serializable {
    private int brojPoruka = 10;

public List<Poruka> getPoruke() throws IOException {
        try {

            for (int i = 0; i < messages.length; ++i) {
                if (m.getSentDate() != null) {
                    System.out.println(m.getSentDate());
                }

}
        } catch (Exception e) {}
}
    
    public String pregledPoruke() {
        return "OK";
}
}
import java.util.Date;
import java.util.Properties;
public class SlanjePoruke {
public String saljiPoruku() {
try {
            message.setSentDate(new Date());
return "";
        } catch (Exception e) {}
return "";
    }
}
