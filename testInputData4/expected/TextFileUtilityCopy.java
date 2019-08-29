/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author student6
 */
public class AdministratorSustava {

    Matcher mParametri;
    SlanjeZahtjeva slanjeZahtjeva;

    AdministratorSustava(String parametri) throws Exception {

        String sintaksa = "^-admin +-s +(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(localhost)|((?!-))(xn--)?[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})) +-port +(\\d{4}) +-u +([^\\s]+) +-p +([^\\s]+)( +((-pause)|(\\-start)|(\\-stop)|(-clean)|(-stat)|(((-upload)|(-download)) +([^\\s]+))))?$";

        Pattern pattern = Pattern.compile(sintaksa, Pattern.DOTALL);
        this.mParametri = pattern.matcher(parametri);
        boolean status = mParametri.matches();
        if (!status) {
            return;
        }
        
        if (mParametri == null) {
            throw new Exception("Parametri admina ne odgovaraju!");
        }

        String imeKorisnika = mParametri.group(11);
        if (!imeKorisnika.matches("^[a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-]+$")) {
            throw new Exception("Ime korisnika je neispravno!");
        }

        String passwordAdmina = mParametri.group(12);
        if (!passwordAdmina.matches("^[a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-\\#\\!]+$")) {
            throw new Exception("Lozinka je neispravna!");
        }

        int port = Integer.parseInt(mParametri.group(10));
        if (port < 8000 || port > 9999) {
            throw new Exception("Port je izvan dozvoljenih granica! (Dozvoljeno: 8000 - 9999)");
        }

        String uploadNaredba = mParametri.group(22);
        String downloadNaredba = mParametri.group(23);
        String putDatoteke = mParametri.group(24);

        if (uploadNaredba != null) {
            File datoteka = new File(putDatoteke);
            if (!datoteka.exists() || datoteka.isDirectory()) {
                throw new Exception("Ne može se učitati datoteka!");
            }
        } else if (downloadNaredba != null) {
            File datoteka = new File(putDatoteke);
            if (datoteka.exists()) {
                throw new Exception("Datoteka već postoji!");
            }
        }
    }

    void pokreniAdministrator() {
        slanjeZahtjeva = new SlanjeZahtjeva();
        String server = mParametri.group(1);
        slanjeZahtjeva.setServer(server);
        int port = Integer.parseInt(mParametri.group(10));
        slanjeZahtjeva.setPort(port);

        String imeKorisnika = mParametri.group(11);
        String password = mParametri.group(12);
        String naredba = mParametri.group(14);
        String uploadNaredba = mParametri.group(22);
        String downloadNaredba = mParametri.group(23);
        String putanjaDatoteke = mParametri.group(24);

        StringBuilder sb = new StringBuilder();
        sb.append("USER");
        sb.append(" ");
        sb.append(imeKorisnika);
        sb.append("; ");
        sb.append("PASSWD");
        sb.append(" ");
        sb.append(password);
        sb.append("; ");

        String porukaServeru;

        if (uploadNaredba != null) {
            File datoteka = new File(putanjaDatoteke);
            long velicinaDatoteke = datoteka.length();
            byte[] sadrzajDatotekeBytes;
            try {
                sadrzajDatotekeBytes = Files.readAllBytes(Paths.get(putanjaDatoteke));
            } catch (IOException ex) {
                Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška prilikom učitavanja datoteke.\n" + ex.getMessage(), ex);
                return;
            }
            String sadrzajDatoteke = new String(sadrzajDatotekeBytes, Charset.defaultCharset());

            sb.append("UPLOAD");
            sb.append(" ");
            sb.append(velicinaDatoteke);
            sb.append("; ");
            sb.append("\r\n");
            sb.append(sadrzajDatoteke);
        } else if (downloadNaredba != null) {
            slanjeZahtjeva.postaviPutanju(putanjaDatoteke);            
            sb.append("DOWNLOAD;");
        } else {
            String naredbaMalo = naredba.substring(1, naredba.length());
            sb.append(naredbaMalo.toUpperCase());
            sb.append(";");         
        }

        porukaServeru = sb.toString();

        slanjeZahtjeva.setNaredba(porukaServeru);
        slanjeZahtjeva.setBrojPokusajaProblema(2);
        slanjeZahtjeva.setInterval(6 * 1000);
        slanjeZahtjeva.setPauza(1111);
        slanjeZahtjeva.postaviBrojPonavljanja(1);
        slanjeZahtjeva.postaviCekanje(2 * 1000);
        
        slanjeZahtjeva.start();
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author student6
 */
public class Evidencija implements Serializable{
     private HashMap<String, EvidencijaModel> evidencijaRada = new HashMap<>();

    public HashMap<String, EvidencijaModel> getEvidencijaRada() {
        return evidencijaRada;
    }

    public void setEvidencijaRada(HashMap<String, EvidencijaModel> evidencijaRada) {
        this.evidencijaRada = evidencijaRada;
    } 
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author student6
 */
public class EvidencijaModel implements Serializable {

    private String oznaka;
    private Date prviZahtjev;
    private Date zadnjiZahtjev;
    private int ukupanBrojZahtjeva = 0;
    private int neuspjesanBrojZahtjeva = 0;
    private long ukupnoVrijemeRada = 0;
    private ArrayList<ZahtjeviKorisnika> zahtjevi = new ArrayList();

    public synchronized boolean dodajZahtjev(ZahtjeviKorisnika zahtjev) {
        //TODO ovo još treba dovršiti
        return true;
    }

    public EvidencijaModel(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public Date getPrviZahtjev() {
        return prviZahtjev;
    }

    public void setPrviZahtjev(Date prviZahtjev) {
        this.prviZahtjev = prviZahtjev;
    }

    public Date getZadnjiZahtjev() {
        return zadnjiZahtjev;
    }

    public void setZadnjiZahtjev(Date zadnjiZahtjev) {
        this.zadnjiZahtjev = zadnjiZahtjev;
    }

    public int getUkupanBrojZahtjeva() {
        return ukupanBrojZahtjeva;
    }

    public void setUkupanBrojZahtjeva(int ukupanBrojZahtjeva) {
        this.ukupanBrojZahtjeva = ukupanBrojZahtjeva;
    }

    public int getNeuspjesanBrojZahtjeva() {
        return neuspjesanBrojZahtjeva;
    }

    public void setNeuspjesanBrojZahtjeva(int neuspjesanBrojZahtjeva) {
        this.neuspjesanBrojZahtjeva = neuspjesanBrojZahtjeva;
    }

    public long getUkupnoVrijemeRada() {
        return ukupnoVrijemeRada;
    }

    public void setUkupnoVrijemeRada(long ukupnoVrijemeRada) {
        this.ukupnoVrijemeRada = ukupnoVrijemeRada;
    }

    public ArrayList<ZahtjeviKorisnika> getZahtjevi() {
        return zahtjevi;
    }

    public void setZahtjevi(ArrayList<ZahtjeviKorisnika> zahtjevi) {
        this.zahtjevi = zahtjevi;
    }

    public class ZahtjeviKorisnika {

        private String vrijeme;
        private String ipAdresa;
        private String zahtjev;
        private String odgovor;

        public ZahtjeviKorisnika(String vrijeme, String ipAdresa, String zahtjev, String odgovor) {
            this.vrijeme = vrijeme;
            this.ipAdresa = ipAdresa;
            this.zahtjev = zahtjev;
            this.odgovor = odgovor;
        }

        public String getVrijeme() {
            return vrijeme;
        }

        public void setVrijeme(String vrijeme) {
            this.vrijeme = vrijeme;
        }

        public String getIpAdresa() {
            return ipAdresa;
        }

        public void setIpAdresa(String ipAdresa) {
            this.ipAdresa = ipAdresa;
        }

        public String getZahtjev() {
            return zahtjev;
        }

        public void setZahtjev(String zahtjev) {
            this.zahtjev = zahtjev;
        }

        public String getOdgovor() {
            return odgovor;
        }

        public void setOdgovor(String odgovor) {
            this.odgovor = odgovor;
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student6.konfiguracije.Konfiguracija;
import org.foi.nwtis.student6.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student6.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author student6
 */
public class KlijentSustava {
    
    Konfiguracija konfig;
    
    protected String parametri;
    protected Matcher mParametri;

    public KlijentSustava(String parametri) throws Exception{
        this.parametri = parametri;
        String sintaksa = "^-user +-s +(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(localhost)|((?!-))(xn--)?[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})) +-port +(\\d{4}) +-u +([^\\s]+) +-konf +([^\\s]+(\\.xml|\\.txt))( +-cekaj +(\\d{1,3}))?( +-multi)?( +-ponavljaj +(\\d{1,3}))?$";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(parametri);
        boolean status = m.matches();
        if (!status) {
            throw new Exception("Ulazni parametri nisu dobri!");
        }
        if (m == null) {
            throw new Exception("Parametri klijenta nisu dobri!");
        }

        switch (m.group(13)) {
            case ".txt":
                break;
            case ".xml":
                break;
            default:
                throw new Exception("Ne može se obraditi datoteka koja ima oblik: " + m.group(13));
        }

        File datoteka = new File(m.group(12));
        if (!datoteka.exists() || datoteka.isDirectory()) {
            throw new Exception("Ne može se učitati datoteka!");
        }

        int port = Integer.parseInt(m.group(10));
        if (port < 8000 || port > 9999) {
            throw new Exception("Port je izvan granica! (Dozvoljeno: 8000 - 9999)");
        }

        if (m.group(15) != null) {
            int brojSekundi = Integer.parseInt(m.group(15));
            if (brojSekundi < 1 || brojSekundi > 100) {
                throw new Exception("Broj sekundi je izvan granica! (Dozvoljeno: 1 - 100)");
            }
        }

        if (m.group(18) != null) {
            int brojPonavljanja = Integer.parseInt(m.group(18));
            if (brojPonavljanja < 2 || brojPonavljanja > 100) {
                throw new Exception("Broj ponavljanja je izvan granica! (Dozvoljeno: 2 - 100)");
            }
        }

        String imeKorisnika = m.group(11);
        if (!imeKorisnika.matches("^[a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-]+$")) {
            throw new Exception("Ime korisnika je nedozvoljeno");
        }
        this.mParametri = m;        
    }
    
    public Matcher provjeraParametara(String p) throws Exception{
        String sintaksa = "^-user +-s +(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(localhost)|((?!-))(xn--)?[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})) +-port +(\\d{4}) +-u +([^\\s]+) +-konf +([^\\s]+(\\.xml|\\.txt))( +-cekaj +(\\d{1,3}))?( +-multi)?( +-ponavljaj +(\\d{1,3}))?$";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (!status) {
            throw new Exception("Ne odgovara!");
        }
        if (m == null) {
            throw new Exception("Parametri klijenta nisu dobri!");
        }

        switch (mParametri.group(13)) {
            case ".txt":
                break;
            case ".xml":
                break;
            default:
                throw new Exception("Ne može se obraditi datoteka koja ima oblik: " + mParametri.group(13));
        }

        File datoteka = new File(mParametri.group(12));
        if (!datoteka.exists() || datoteka.isDirectory()) {
            throw new Exception("Ne može se učitati datoteka!");
        }

        int port = Integer.parseInt(mParametri.group(10));
        if (port < 8000 || port > 9999) {
            throw new Exception("Port je izvan granica! (Dozvoljeno: 8000 - 9999)");
        }

        if (mParametri.group(15) != null) {
            int brojSekundi = Integer.parseInt(mParametri.group(15));
            if (brojSekundi < 1 || brojSekundi > 100) {
                throw new Exception("Broj sekundi je izvan granica! (Dozvoljeno: 1 - 100)");
            }
        }

        if (mParametri.group(18) != null) {
            int brojPonavljanja = Integer.parseInt(mParametri.group(18));
            if (brojPonavljanja < 2 || brojPonavljanja > 100) {
                throw new Exception("Broj ponavljanja je izvan granica! (Dozvoljeno: 2 - 100)");
            }
        }

        String imeKorisnika = mParametri.group(11);
        if (!imeKorisnika.matches("^[a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-]+$")) {
            throw new Exception("Ime korisnika je nedozvoljeno!");
        }
        return m;
    }
    
    public void pokreniKlijenta(){   
        String datoteka = mParametri.group(12);
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška prilikom čitanja konfiguracije.", ex);
            return;
        }
        
        String adresaServera = mParametri.group(1);
        String portServera = mParametri.group(10);
        String imeKorisnika = mParametri.group(11);
        String naredba = "USER " + imeKorisnika + "; TIME;";

        String cekanje = mParametri.group(15);
        long cekanjePrijeSlanjaZahtjeva;
        if (cekanje != null) {
            cekanjePrijeSlanjaZahtjeva = Long.parseLong(cekanje) * 1000;
        } else {
            cekanjePrijeSlanjaZahtjeva = 0;
        }

        long razmakIzmeduDretvi = 0;
        int brojDretvi;
        String multi = mParametri.group(16);
        if (multi != null) {
            String brojDretviPostavka = ucitajPostavku("brojDretvi");
            if (brojDretviPostavka != null) {
                int zadanBrojDretvi = Integer.parseInt(brojDretviPostavka);
                brojDretvi = 1 + (int) (Math.random() * ((zadanBrojDretvi - 1) + 1));
            } else {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, "Pojavila se pogreška prilikom učitavanja postavke: brojDretvi");
                return;
            }

            String razmakDretviString = ucitajPostavku("razmakDretvi");
            if (razmakDretviString != null) {
                int zadanRazmakDretvi = Integer.parseInt(razmakDretviString) * 1000;
                razmakIzmeduDretvi = 1 + (int) (Math.random() * ((zadanRazmakDretvi - 1) + 1));
            } else {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, "Pojavila se pogreška prilikom učitavanja postavke: razmakDretvi");
                return;
            }
        } else {
            brojDretvi = 1;
        }

        String brojPonavljanjaString = mParametri.group(18);
        int brojPonavljanja;
        if (brojPonavljanjaString != null) {
            brojPonavljanja = Integer.parseInt(brojPonavljanjaString);
        } else {
            brojPonavljanja = 1;
        }

        SlanjeZahtjeva[] dretve = new SlanjeZahtjeva[brojDretvi];
        for (int i = 0; i < brojDretvi; i++) {
            SlanjeZahtjeva dretva = new SlanjeZahtjeva();
            dretve[i] = dretva;
            try {
                dretva.setKonfig(konfig);
            } catch (Exception ex) {
                Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
            dretva.setServer(adresaServera);
            dretva.setPort(Integer.parseInt(portServera));
            
            dretva.postaviCekanje(cekanjePrijeSlanjaZahtjeva);
            dretva.postaviBrojPonavljanja(brojPonavljanja);
            
            dretva.setNaredba(naredba);            
        }
        
        for (int i = 0; i < dretve.length; i++) {
            dretve[i].start();
            try {
                sleep(razmakIzmeduDretvi);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    private String ucitajPostavku(String postavka) {
        if (konfig.postojiPostavka(postavka)) {
            return konfig.dajPostavku(postavka);
        } else {
            return null;
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student6.konfiguracije.Konfiguracija;
import org.foi.nwtis.student6.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student6.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author student6
 */
public class ObradaZahtjeva extends Thread {

    public enum StanjeDretve {

        Slobodna, Zauzeta
    };

    StanjeServera stanjeServera;

    private String konfigDatotekaIme;
    private String adminDatotekaIme;

    private Matcher provjeriParametre(String poruka, String sintaksa) {
        Pattern pattern = Pattern.compile(sintaksa, Pattern.DOTALL);
        Matcher m = pattern.matcher(poruka);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            return null;
        }
    }

    private Konfiguracija konfig;
    private Konfiguracija admins;
    private int trajanjeIntervala;
    private StanjeDretve stanje;
    private Socket socket;

    public ObradaZahtjeva(ThreadGroup group, String name, StanjeServera stanjeServera) {
        super(group, name);
        this.stanje = StanjeDretve.Slobodna;
        this.stanjeServera = stanjeServera;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (true) {
            try {
                sleep(350 * 1000);
            } catch (InterruptedException ex) {
                obradiZahtjev();
            }
        }
    }

    private void obradiZahtjev() {
        setStanje(StanjeDretve.Zauzeta);
        long pocetakIzvodenja = System.currentTimeMillis();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            StringBuilder sb = new StringBuilder();
            do {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            } while (is.available() > 0);

            System.out.println(sb.toString());
            String odgovor = dohvatiOdgovor(sb.toString());
            os.write(odgovor.getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (os != null) {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        long trajanjeCekanja = dohvatiCekanje(pocetakIzvodenja);
        try {
            sleep(trajanjeCekanja);
        } catch (InterruptedException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
        setStanje(StanjeDretve.Slobodna);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private String dohvatiOdgovor(String poruka) {
        String sintaksa = "^USER ([a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-]+);( PASSWD ([a-z0-9A-ZčćžšđČĆŽŠĐ\\_\\-\\#\\!]+);)? (((TIME|PAUSE|START|STOP|CLEAN|STAT|DOWNLOAD);)|((UPLOAD) (\\d+); \\r\\n(.+)))$";
        Matcher mNaredba = provjeriParametre(poruka, sintaksa);
        String odgovor;
        boolean isAdmin = false;
        if (mNaredba == null) {
            odgovor = "ERROR 90; Sintaksa nije ispravna ili komanda nije dozvoljena!";
        } else {
            String korisnik = mNaredba.group(1);
            String password = mNaredba.group(3);
            if (password != null) {
                isAdmin = provjeriKorisnika(korisnik, password);
            }
            String naredba = mNaredba.group(6);
            String uploadNaredba = mNaredba.group(8);

            if (stanjeServera.getStanje() == StanjeServera.Stanje.Pauza
                    && !isAdmin) {
                odgovor = "ERROR 10; Server može obraditi samo zahtjeve od strane administratora!";
            } else if (uploadNaredba != null) {
                String velicina = mNaredba.group(9);
                String sadrzaj = mNaredba.group(10);
                odgovor = naredbaUpload(velicina, sadrzaj, isAdmin);
            } else if (naredba.equals("TIME")) {
                odgovor = naredbaTime();
            } else if (naredba.equals("DOWNLOAD")) {
                odgovor = naredbaDownload(isAdmin);
            } else if (naredba.equals("START")) {
                odgovor = naredbaStart(isAdmin);
            } else if (naredba.equals("STOP")) {
                odgovor = naredbaStop(isAdmin);
            } else if (naredba.equals("PAUSE")) {
                odgovor = naredbaPause(isAdmin);
            } else if (naredba.equals("STAT")) {
                odgovor = naredbaStat(isAdmin);
            } else if (naredba.equals("CLEAN")) {
                odgovor = naredbaClean(isAdmin);
            } else {
                odgovor = "ERROR 90; Sintaksa nije ispravna ili komanda nije dozvoljena!";
            }
        }

        return odgovor;
    }

    private boolean provjeriKorisnika(String korisnik, String password) {
        if (admins.postojiPostavka(korisnik)) {
            String sifraKorisnika = admins.dajPostavku(korisnik);
            if (sifraKorisnika.equals(password)) {
                return true;
            }
        }
        return false;
    }

    private String naredbaStart(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }
        if (stanjeServera.getStanje() == StanjeServera.Stanje.Pauza) {
            stanjeServera.setStanje(StanjeServera.Stanje.Radi);
            System.out.println(" -- novo stanje: RADI");
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 02; Nije u stanju pauze!";
        }
        return odgovor;
    }

    private String naredbaPause(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }
        if (stanjeServera.getStanje() == StanjeServera.Stanje.Radi) {
            stanjeServera.setStanje(StanjeServera.Stanje.Pauza);
            System.out.println(" -- novo stanje: PAUZA");
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 01; Server je u stanju pauze!";
        }
        return odgovor;
    }

    private String naredbaStop(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }

        stanjeServera.setStanje(StanjeServera.Stanje.Stopiran);
        System.out.println(" -- novo stanje: STOPIRAN");
        boolean serijalizacijaOk = obaviSerijalizaciju();
        if (serijalizacijaOk) {
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 03; Pogreška prilikom prekida rada ili serijalizacije!";
        }
        return odgovor;
    }

    private String naredbaStat(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }
        String podaciEvidencije = dohvatiPodatkeEvidencije();
        if (podaciEvidencije == null) {
            odgovor = "ERROR 05; Pogreška prilikom čitanja evidencije!";
        } else {
            odgovor = "OK; " + podaciEvidencije;
        }
        return odgovor;
    }

    private String naredbaClean(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }
        boolean ociscenaEvidencija = ocistiEvidenciju();
        if (ociscenaEvidencija) {
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 04; pogreška prilikom pražnjenja evidencije!";
        }
        return odgovor;
    }

    private boolean obaviSerijalizaciju() {
        return false; //TODO za napraviti
    }

    private String dohvatiPodatkeEvidencije() {
        return null; //TODO za napraviti
    }

    private boolean ocistiEvidenciju() {
        return false; //TODO za napraviti
    }

    private String naredbaUpload(String velicina, String sadrzaj, boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }

        String imeDatoteke = adminDatotekaIme.substring(0, adminDatotekaIme.lastIndexOf("."))
                + getCurrentTimeString("yyyyMMdd_HHmmss")
                + adminDatotekaIme.substring(adminDatotekaIme.lastIndexOf("."), adminDatotekaIme.length());

        PrintWriter writer = null;
        File novaDatoteka = new File(imeDatoteke);
        if (novaDatoteka.exists() && !novaDatoteka.isDirectory()) {
            odgovor = "ERROR 06; Datoteka tog naziva već postoji na poslužitelju!";
            return odgovor;
        }

        try {
            novaDatoteka.createNewFile();
            writer = new PrintWriter(novaDatoteka);
            writer.write(sadrzaj);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Datoteka ne postoji!", ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Ne može se kreirati datoteku!", ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        long velicinaDobijeneDat = Long.parseLong(velicina);
        long novaDatotekaLen = novaDatoteka.length();
        if (novaDatotekaLen != velicinaDobijeneDat) {
            odgovor = "ERROR 07; Veličina primljenog sadržaja datoteke ne odgovara!";
            novaDatoteka.delete();
        } else {
            odgovor = "OK;";
        }
        return odgovor;
    }

    private String naredbaTime() {
        return "OK; " + getCurrentTimeString("yyyy.MM.dd HH:mm:ss");
    }

    private String naredbaDownload(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara!";
            return odgovor;
        }
        byte[] sadrzajDatotekeBytes;
        try {
            sadrzajDatotekeBytes = Files.readAllBytes(Paths.get(konfigDatotekaIme));
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška prilikom čitanja datoteke za upload!\n" + ex.getMessage(), ex);
            odgovor = "ERROR 05; Pogreška pri evidenciji!";
            return odgovor;
        }

        String sadrzajDatoteke = new String(sadrzajDatotekeBytes, Charset.defaultCharset());
        long velicinaDatoteke = sadrzajDatoteke.length();
        StringBuilder sb = new StringBuilder();
        sb.append("DATA ");
        sb.append(velicinaDatoteke);
        sb.append("; ");
        sb.append("\r\n");
        sb.append(sadrzajDatoteke);
        odgovor = sb.toString();
        return odgovor;
    }

    private String getCurrentTimeString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    private long dohvatiCekanje(final long pocetakObrade) {
        if (trajanjeIntervala == 0) {
            return 0;
        }
        long trajanjeObrade = System.currentTimeMillis() - pocetakObrade;
        int brojPotrosenihIntervala = (int) (trajanjeObrade / trajanjeIntervala);
        long trajanjeUnutarIntervala = trajanjeObrade - trajanjeIntervala * brojPotrosenihIntervala;
        return trajanjeIntervala - trajanjeUnutarIntervala;
    }

    void setKonfigFileName(String datoteka) {
        this.konfigDatotekaIme = datoteka;
    }

    void setAdminFileName(String datoteka) {
        this.adminDatotekaIme = datoteka;
        try {
            this.admins = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Nije pronađena datoteka administratori!\n", ex);
        }
    }

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
        if (konfig.postojiPostavka("intervalDretve")) {
            this.trajanjeIntervala = Integer.parseInt(konfig.dajPostavku("intervalDretve"));
            this.trajanjeIntervala = trajanjeIntervala * 1000; // milisekunde
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public StanjeDretve getStanje() {
        return stanje;
    }

    public void setStanje(StanjeDretve stanje) {
        this.stanje = stanje;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author student6
 */
public class PregledSustava {
     protected String parametri;
    protected Matcher mParametri;

    public PregledSustava(String parametri) throws Exception {
        this.parametri = parametri;
        
        mParametri = provjeraParametara(parametri);
        
        if(mParametri == null)
        {
            throw new Exception("Parametri ne odgovaraju!");
        }
    }
    
    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-show -s ([^\\s]+)$";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.err.println("Ne odgovara!");
            return null;
        }
    }
    
    public void pokreniPreglednika(){
        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if(!dat.exists()){
            System.out.println("Ne postoji datoteka konfiguracije!");
            return;
        }
        //TODO sami dovršiti
        return;
    } 
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import org.foi.nwtis.student6.konfiguracije.Konfiguracija;

/**
 *
 * @author student6
 */
public class SerijalizatorEvidencije extends Thread{
    Konfiguracija konfig;

    public SerijalizatorEvidencije(Konfiguracija konfig) {
        super();
        this.konfig = konfig;
    }

        @Override
    public void run() {
        
        //TODO dovrši za serijalizaciju evidencije
    }
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
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
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student6.konfiguracije.Konfiguracija;
import org.foi.nwtis.student6.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student6.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student6.zadaca_1.ObradaZahtjeva.StanjeDretve;

/**
 *
 * @author student6
 */
public class ServerSustava {

    protected String parametri;
    protected Matcher mParametri;
    protected StanjeServera stanjeServera;
    
    public ServerSustava(String parametri) throws Exception {
        this.parametri = parametri;        
        mParametri = provjeraParametara(parametri);

        if (mParametri == null) {
            throw new Exception("Parametri servera ne odgovaraju!");
        }
        
        this.stanjeServera = new StanjeServera();
        this.stanjeServera.setStanje(StanjeServera.Stanje.Radi);
    }

    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-server +-konf +([^\\s]+(\\.xml|\\.txt))( +-load)?$";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.err.println("Ne odgovara!");
            return null;
        }
    }

    public void pokreniServer() {

        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Ne postoji datoteka konfiguracije!");
            return;
        }

        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);

            if (this.mParametri.group(3) != null) { // -load
                String datEvid = konfig.dajPostavku("evidDatoteka");
                boolean ucitano = ucitajSerijaliziranuEvidenciju(datEvid);
                if (!ucitano) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška prilikom pokretanja evidencije!");
                    // Ne prekida se rad
                }
            }            
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Nema konfiguracije!", ex);
            return;
        }

        String brojDretviPostavka = ucitajPostavku(konfig, "brojDretvi");
        if (brojDretviPostavka == null) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška, nema broja dretvi!");
            return;
        }
        
        int brojDretvi = Integer.parseInt(brojDretviPostavka);

        ObradaZahtjeva[] dretve = new ObradaZahtjeva[brojDretvi];
        ThreadGroup tg = new ThreadGroup("student6");
        for (int i = 0; i < brojDretvi; i++) {
            dretve[i] = new ObradaZahtjeva(tg, "student6_" + i, stanjeServera);
            dretve[i].setKonfig(konfig);
            dretve[i].setKonfigFileName(datoteka);
            dretve[i].setAdminFileName(ucitajPostavku(konfig, "adminDatoteka"));
            dretve[i].start();
        }

        String portPostavka = ucitajPostavku(konfig, "port");
        if (portPostavka == null) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška, nema porta!");
            return;
        }
        int port = Integer.parseInt(portPostavka);       
      

        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket socket = ss.accept();
                ObradaZahtjeva oz = dajSlobodnuDretvu(dretve);
                if (oz == null) {
                    posaljiKlijentuPoruku(socket, "Nema slobodne dretve!");
                    continue;
                } 
                oz.setSocket(socket);
                oz.interrupt();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean ucitajSerijaliziranuEvidenciju(String datEvid) {
        //TODO dovršiti sami
        return false;
    }

    private ObradaZahtjeva dajSlobodnuDretvu(ObradaZahtjeva[] dretve) {
        StanjeDretve slobodna = ObradaZahtjeva.StanjeDretve.Slobodna; 
        for(int i = 0; i < dretve.length; i++){
            ObradaZahtjeva ob = dretve[i];
            System.out.println(ob.getName() + " - stanje dretve: " + ob.getStanje());
            
            if(ob.getStanje() == slobodna){ //Dretva je slobodna
                for(int j = i; j < dretve.length - 1; j++){
                    dretve[j] = dretve[j+1];
                }
                dretve[dretve.length-1] = ob; //Postavi dretvu na kraj
                
                return ob; //Vrati slobodnu dretvu
            }
        }
        return null;
    }
    
    private String ucitajPostavku(Konfiguracija konfig, String postavka) {
        if (konfig.postojiPostavka(postavka)) {
            return konfig.dajPostavku(postavka);
        } else {
            return null;
        }
    }
    
    private void posaljiKlijentuPoruku(Socket socket, String poruka) {
        Thread postar = new Thread() {
            @Override
            public void run() {
                try {                    
                    OutputStream os = socket.getOutputStream();
                    os.write(poruka.getBytes());
                    socket.shutdownOutput();
                    if(os != null){
                        os.close();
                    }
                    if(socket != null){
                        socket.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        };
        System.out.println("Šaljem klijentu: " + poruka);
        postar.start();
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student6.konfiguracije.Konfiguracija;

/**
 *
 * @author student6
 */
public class SlanjeZahtjeva extends Thread {

    private Konfiguracija konfig;
    private String server;
    private int port;
    private String naredba;
    private long cekanjePrijeSlanjaZahtjeva;
    private int brojPonavljanja;
    private long interval;
    private long pauza;
    private int brojPokusajaProblema;
    private String putanjaDatoteke;

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        long pocetakIzvodenja;
        long vrijemeSpavanja;
        for (int i = 0; i < brojPonavljanja; i++) {
            pocetakIzvodenja = System.currentTimeMillis();

            int brojNeuspjelihPokusaja = 0;
            boolean posaoObavljen = false;
            boolean spavanjeZbogProblema = false;
            InputStream is = null;
            OutputStream os = null;
            Socket socket = null;

            do {
                try {
                    if (spavanjeZbogProblema) {
                        try {
                            sleep(pauza);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                        spavanjeZbogProblema = false;
                    }

                    socket = new Socket(server, port);
                    try {
                        sleep(cekanjePrijeSlanjaZahtjeva);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                    
                    os = socket.getOutputStream();
                    is = socket.getInputStream();

                    os.write(naredba.getBytes());
                    os.flush();                   

                    StringBuilder sb = new StringBuilder();
                    do {
                        int znak = is.read();
                        if (znak == -1) {
                            break;
                        }
                        sb.append((char) znak);
                    } while (is.available() > 0);

                    socket.shutdownOutput();

                    String odgovorPosluzitelja = sb.toString();
                    if (odgovorPosluzitelja.isEmpty()) {
                        brojNeuspjelihPokusaja++;
                        spavanjeZbogProblema = true;
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO,
                                "Poslužitelj nije ništa vratio!");
                    } else {
                        posaoObavljen = obradiOdgovorPosluzitelja(odgovorPosluzitelja);
                    }
                } catch (IOException ex) {
                    brojNeuspjelihPokusaja++;
                    spavanjeZbogProblema = true;
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO, "[{0}] Iznimka! Broj neuspjelih pokusaja: {1}", new Object[]{getName(), brojNeuspjelihPokusaja});
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Pojavila se pogreška prilikom zatvaranja inputStream");
                    }
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Pojavila se pogreška prilikom zatvaranja outputStream");
                    }
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Pojavila se pogreška prilikom zatvaranja socket");
                    }
                }

                if (brojNeuspjelihPokusaja == brojPokusajaProblema) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO, getName() + "Zahtjev nije obrađen nakon " + brojNeuspjelihPokusaja + " pokušaja. Rad dretve se prekida!");
                    break;
                }
                if(!posaoObavljen){
                    brojNeuspjelihPokusaja++;
                }
            } while (!posaoObavljen);

            if (posaoObavljen) {
                brojNeuspjelihPokusaja = 0;
            }

            vrijemeSpavanja = izracunajVrijemeCekanja(pocetakIzvodenja);
            Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO, getName() + " zavrsila ciklus: " + i);
            try {
                sleep(vrijemeSpavanja);
            } catch (InterruptedException ex) {
                Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, "Dretva " + getName() + " Zaustavljena u spavanju." + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private boolean obradiOdgovorPosluzitelja(String odgovorPosluzitelja) {
        boolean posaoObavljen;
        if (odgovorPosluzitelja.equals("OK") | odgovorPosluzitelja.equals("OK;")) {
            System.out.println(getName() + ": Primljen odgovor od poslužitelja: " + odgovorPosluzitelja);
            posaoObavljen = true;
        } else if (odgovorPosluzitelja.startsWith("OK; \r\n")) {
            System.out.println("Podaci evidencije:");
            System.out.println(odgovorPosluzitelja.substring(odgovorPosluzitelja.indexOf("OK; ")));
            posaoObavljen = true;
        } else if (odgovorPosluzitelja.startsWith("OK; ")) {
            System.out.println(odgovorPosluzitelja);
            posaoObavljen = true;
        } else if(odgovorPosluzitelja.startsWith("ERROR ")){
            System.out.println(odgovorPosluzitelja);
            posaoObavljen = true;
        }else if(odgovorPosluzitelja.startsWith("DATA ")){
            Pattern dataPattern = Pattern.compile("^DATA (.+); \\r\\n(.+)$", Pattern.DOTALL);
            Matcher mData = dataPattern.matcher(odgovorPosluzitelja);
            if (!mData.matches()) {
                System.out.println("Primljeni su neprepoznatljivi podaci:" + odgovorPosluzitelja);
                posaoObavljen = false;
            } else {
                System.out.println("Primljeni su podaci od poslužitelja. Veličina podataka: " + mData.group(1));
                posaoObavljen = spremiDatoteku(mData.group(2), mData.group(1));
            }
        }else{
            System.out.println("Primljen je neprepoznatljiv odgovor!");
            posaoObavljen = false;
        }  
        return posaoObavljen;
    }

    private boolean spremiDatoteku(String podaci, String velicina) {
        File file = new File(this.putanjaDatoteke);
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.write(podaci);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, "Pojavila se pogreška prilikom stvaranja datoteke koja se preuzima.\n" + ex.getMessage(), ex);
            return false;
        }
        long dobivenaVelicina = Long.parseLong(velicina);
        long stvarnaVelicina = file.length();
        if (dobivenaVelicina == stvarnaVelicina) {
            return true;
        }else{
            file.delete();
            return false;
        }
    }

    public void setKonfig(Konfiguracija konfig) throws Exception {
        this.konfig = konfig;

        String intervalDretvePostavka = ucitajPostavku("intervalDretve");
        if (intervalDretvePostavka != null) {
            setInterval(Long.parseLong(intervalDretvePostavka) * 1000);
        } else {
            throw new Exception("Pojavila se pogreška prilikom učitavanja intervala dretve");
        }

        String pauzaProblemaPostavka = ucitajPostavku("pauzaProblema");
        if (pauzaProblemaPostavka != null) {
            setPauza(Long.parseLong(pauzaProblemaPostavka) * 1000);
        } else {
            throw new Exception("Pojavila se pogreška prilikom učitavanja pauze problema");
        }

        String brojPokusajaPostavka = ucitajPostavku("brojPokusajaProblema");
        if (brojPokusajaPostavka != null) {
            setBrojPokusajaProblema(Integer.parseInt(brojPokusajaPostavka));
        } else {
            throw new Exception("Pojavila se pogreška prilikom učitavanja brojPokusajaProblema");
        }
    }

    private long izracunajVrijemeCekanja(final long pocetakObrade) {
        if (interval == 0) {
            return 0;
        }
        long trajanjeObrade = System.currentTimeMillis() - pocetakObrade;
        long vrijemeSpavanja = interval - trajanjeObrade;
        if (vrijemeSpavanja > 0) {
            return vrijemeSpavanja;
        } else {
            return 0;
        }
    }

    public void setInterval(long intervalDretve) {
        this.interval = intervalDretve;
    }

    public void setPauza(long pauzaProblema) {
        this.pauza = pauzaProblema;
    }

    public void setBrojPokusajaProblema(int brojPokusajaProblema) {
        this.brojPokusajaProblema = brojPokusajaProblema;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    void setNaredba(String naredba) {
        this.naredba = naredba;
    }

    void postaviCekanje(long cekanjePrijeSlanjaZahtjeva) {
        this.cekanjePrijeSlanjaZahtjeva = cekanjePrijeSlanjaZahtjeva;
    }

    void postaviBrojPonavljanja(int brojPonavljanja) {
        this.brojPonavljanja = brojPonavljanja;
    }

    private String ucitajPostavku(String postavka) {
        if (konfig.postojiPostavka(postavka)) {
            return konfig.dajPostavku(postavka);
        } else {
            return null;
        }
    }

    void postaviPutanju(String putanjaDatoteke) {
        this.putanjaDatoteke = putanjaDatoteke;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

/**
 *
 * @author student6
 */
public class StanjeServera {    
    public enum Stanje {
        Radi, Pauza, Stopiran
    };
    
    private Stanje stanje;

    public StanjeServera() {
        this.stanje = Stanje.Radi;
    }

    public Stanje getStanje() {
        return stanje;
    }

    public void setStanje(Stanje stanje) {
        this.stanje = stanje;
    }    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author student6
 */
public class Zadaca_1_student6 {
  
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();

        Zadaca_1_student6 zadaca = new Zadaca_1_student6();
        Matcher m = zadaca.provjeraParametara(p);

        if (m == null) {
            return;
        } else if (m.group(1) != null) {
            try {
                ServerSustava server = new ServerSustava(p);
                server.pokreniServer();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_1_student6.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (m.group(2) != null) {
            try {
                AdministratorSustava admin = new AdministratorSustava(p);
                admin.pokreniAdministrator();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_1_student6.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (m.group(3) != null) {
            try {
                KlijentSustava klijent = new KlijentSustava(p);
                klijent.pokreniKlijenta();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_1_student6.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (m.group(4) != null) {
            PregledSustava ps = null;
            try {
                ps = new PregledSustava(p);
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_1_student6.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps.pokreniPreglednika();
        } else {
            System.out.println("Parametri ne odgovaraju!");
            return;
        }

    }

    public Matcher provjeraParametara(String p) {
        String sintaksa = "(^-server.+)|(^-admin.+)|(^-user.+)|(^-show.+)";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.out.println("Ne odgovara!");
            return null;
        }
    }
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

public class TestOpcija {
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student6.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author NWTiS_4
 */
public class ServerSustavaTest {
    
    public ServerSustavaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of provjeraParametara method, of class ServerSustava.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara");
        String p = "-server";
        ServerSustava instance;
        try {
            instance = new ServerSustava(p);
            Matcher expResult = null;
            Matcher result = instance.provjeraParametara(p);
            assertNull(result);
            p = "-server -konf NWTiS_student6 _1.txt";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            p = "-server -konf NWTiS_student6_1.txt -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
        } catch (Exception ex) {
            Logger.getLogger(ServerSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Test of pokreniServer method, of class ServerSustava.
     */
    @Test
    public void testPokreniServer() {
        System.out.println("pokreniServer");
        ServerSustava instance = null;
        instance.pokreniServer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
