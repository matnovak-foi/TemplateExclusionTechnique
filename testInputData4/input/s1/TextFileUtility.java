/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

/**
 *  Klasa koja definira Aktivnosti koje admin izvodi nad serverom
 * @author student5
 */
public class AdministratorAktivnost {    

    public enum Aktivnost {
        Aktiviraj, Pauziraj, Zaustavi
    };
    
    private Aktivnost activity;

    public AdministratorAktivnost() {
        this.activity = Aktivnost.Aktiviraj;
    }

    public Aktivnost postaviAktivnost() {
        return activity;
    }

    public void dohvatiAktivnost(Aktivnost activity) {
        this.activity = activity;
    }    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Klasa za manipuliranje mogućnostima admina
 * @author student5
 */
public class AdministratorSustava {

    Matcher mParametri;

    AdministratorSustava(String parametri) throws Exception {

        String sintaksa = "^-admin +-s +(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(localhost)|((?!-))(xn--)?[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})) +-port +(\\d{4}) +-u +([^\\s]+) +-p +([^\\s]+)( +((-pause)|(\\-start)|(\\-stop)|(-clean)|(-stat)|(((-upload)|(-download)) +([^\\s]+))))?$";

        Pattern pattern = Pattern.compile(sintaksa, Pattern.DOTALL);
        this.mParametri = pattern.matcher(parametri);
        Matcher m = this.mParametri;
        boolean status = m.matches();
        if (!status) {
            System.out.println("Parametri za klijenta koje ste upisali ne odgovaraju!");
        }

        if (m.group(11) == null) {
            throw new Exception("Parametri za korisnika su krivo unešeni!");
        }
        if (m.group(12) == null) {
            throw new Exception("Parametri za lozinku su krivo unešeni!");
        }
        if (m.group(10) != null) {
            int brojPorta = Integer.parseInt(mParametri.group(10));
            if (brojPorta < 8000 || brojPorta > 9999) {
                throw new Exception("Niste upisali dobar broj porta!");
            }
        }
        String parametarUpload = mParametri.group(22);
        String parametarDownload = mParametri.group(23);
        String lokacijaDat = mParametri.group(24);

        if (parametarUpload != null) {
            File datUpload = new File(lokacijaDat);
            if (!datUpload.exists()) {
                System.out.println("Problem kod čitanja datoteke!");
            }
        }
        if (parametarDownload != null) {
            File datDownload = new File(lokacijaDat);
            if (datDownload.exists()) {
                System.out.println("Datoteka je već učitana!");
            }
        }
    }
    
    /**
     * Metoda za pokretanje rada administratora
     */
    void pokreniAdministratora() {
        SlanjeZahtjeva sz = new SlanjeZahtjeva();
        String server = mParametri.group(1);
        sz.postaviServer(server);
        
        int brojPorta;
        brojPorta = Integer.parseInt(mParametri.group(10));
        sz.postaviPort(brojPorta);

        String user, lozinka, posao, datUpload, datDownload, lokacijaDat, tekstZaServer;
        
        user = mParametri.group(11);
        lozinka = mParametri.group(12);
        posao = mParametri.group(14);
        datUpload = mParametri.group(22);
        datDownload = mParametri.group(23);
        lokacijaDat = mParametri.group(24);

        StringBuilder izgradiString = new StringBuilder();
        izgradiString.append("USER");
        izgradiString.append(" ");
        izgradiString.append(user);
        izgradiString.append("; ");
        izgradiString.append("PASSWD");
        izgradiString.append(" ");
        izgradiString.append(lozinka);
        izgradiString.append("; ");
        

        if (datUpload != null) {
            File datoteka = new File(lokacijaDat);
            long nVelicinaDat = datoteka.length();
            byte[] sadrzajDatUBajtima;
            try {
                sadrzajDatUBajtima = Files.readAllBytes(Paths.get(lokacijaDat));
            } catch (IOException e) {
                System.out.println("Pogreška kod uploada datoteke! " + e);
                return;
            }
            String sadrzajDatoteke = new String(sadrzajDatUBajtima, Charset.defaultCharset());

            izgradiString.append("UPLOAD");
            izgradiString.append(" ");
            izgradiString.append(nVelicinaDat);
            izgradiString.append("; ");
            izgradiString.append("\r\n");
            izgradiString.append(sadrzajDatoteke);
        } else if (datDownload != null) {
            sz.postaviLokaciju(lokacijaDat);
            izgradiString.append("DOWNLOAD;");
        } else {
            String naredbaMalo = posao.substring(1, posao.length());
            izgradiString.append(naredbaMalo.toUpperCase());
            izgradiString.append(";");        
        }

        tekstZaServer = izgradiString.toString();

        sz.postaviPosao(tekstZaServer);
        sz.postaviBrojPokusaja(7);
        sz.postaviInterval(3000);
        sz.postaviPauzu(1000);
        sz.postaviBrojPonavljanja(1);
        sz.postaviCekanje(1500);

        sz.start();
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Klasa koja treba spremiti evidenciju
 * todo
 * @author student5
 */
public class Evidencija implements Serializable {

    private HashMap<String, EvidencijaModel> evidencijaRada = new HashMap<>();

    public HashMap<String, EvidencijaModel> getEvidencijaRada() {
        return evidencijaRada;
    }

    public void setEvidencijaRada(HashMap<String, EvidencijaModel> evidencijaRada) {
        this.evidencijaRada = evidencijaRada;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Klasa koja treba spremiti pojedini model evidencije kako bi ga se poslije dohvatilo
 * @author student5
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
        //TODO dovrštiti unos/ažuriranje podataka zahtjeva
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
package org.foi.nwtis.student5.zadaca_1;

import java.io.File;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student5.konfiguracije.Konfiguracija;
import org.foi.nwtis.student5.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student5.konfiguracije.NemaKonfiguracije;

/**
 * Klasa za manipulciju rada s klijentoms
 * @author student5
 */
public class KlijentSustava {

    Konfiguracija konfig;

    protected String parametri;
    protected Matcher mParametri;
    private String server;
    private int port;
    private String user;
    private int cekaj;
    private String multi;
    private int ponavljaj;
    private File dat;
    private int sleepZadrziZahtjev;
    private int pricekajZahtjev;
    private int razmakDretvi = 0;
    private int brojDretvi;
    private int brojPonavljanjaDretvi;

    public KlijentSustava(String parametri) throws Exception {
        this.parametri = parametri;
        String sintaksa = "^-user +-s +(^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$|[^\\s]+) +-port (\\b[89][0-9]{3}\\b) +-u +([A-Za-z0-9\\-\\_]+) +-konf +([^\\s]+\\.txt|[^\\s]+\\.xml)( +-cekaj +(\\b([1-9][0-9]?|100)\\b))?( +-multi)?( +-ponavljaj +(\\b([2-9]|[1-9][0-9]|1[0-9]{2}|200)\\b))?$"; //radi

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(parametri);
        boolean status = m.matches();
        if (!status) {
            System.out.println("Parametri za klijenta koje ste upisali ne odgovaraju!");
        }

        if (m.group(2) != null) {
            int Brojporta = Integer.parseInt(m.group(2));
            if (Brojporta < 8000 || Brojporta > 9999) {
                throw new Exception("Niste upisali dobar broj porta!");
            }
        }

        if (m.group(3) == null) {
            throw new Exception("Problem sa imenom korisnika: koristite sam englesku abecedu!");
        }

        if (m.group(4) != null) {
            File datoteka = new File(m.group(4));

        } else {
            throw new Exception("Ne postoji ili nije učitana datoteka konfiguracije!");
        }

        if (m.group(6) != null) {
            int cekanje = Integer.parseInt(m.group(6));
            if (cekanje < 1 || cekanje > 100) {
                throw new Exception("Broj sekundi mora biti između 1 i 100!");
            }
        }

        if (m.group(10) != null) {
            int ponavljaj = Integer.parseInt(m.group(10));
            if (ponavljaj < 2 || ponavljaj > 100) {
                throw new Exception("Broj ponavljanja mora biti između 2 i 100!");
            }
        }

        this.mParametri = m;
    }

    public Matcher provjeraParametara(String p) throws Exception {
        String sintaksa = "^-user +-s +(^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$|[^\\s]+) +-port (\\b[89][0-9]{3}\\b) +-u +([A-Za-z0-9\\-\\_]+) +-konf +([^\\s]+\\.txt|[^\\s]+\\.xml)( +-cekaj +(\\b([1-9][0-9]?|100)\\b))?( +-multi)?( +-ponavljaj +(\\b([2-9]|[1-9][0-9]|1[0-9]{2}|200)\\b))?$";//radi

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (!status) {
            System.out.println("Parametri za klijenta koje ste upisali ne odgovaraju!");
        }

        //-server
        if (this.mParametri.group(1) != null) {
            server = this.mParametri.group(1);
            System.out.println(server);
        }

        //-port
        if (this.mParametri.group(2) != null) {
            port = Integer.parseInt(this.mParametri.group(2));
            System.out.println(port);
        }

        //-user
        if (this.mParametri.group(3) != null) {
            user = this.mParametri.group(3);
            System.out.println(user);
        }

        //-konf
        String datoteka = mParametri.group(4);
        dat = new File(datoteka);
        System.out.println(dat);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije za korisnika ne postoji.");

        }

        //-cekaj
        if (this.mParametri.group(6) != null) {
            cekaj = Integer.parseInt(this.mParametri.group(6));
            System.out.println(cekaj);

            if (cekaj < 1 || cekaj > 100) {
                try {
                    throw new Exception("Broj sekundi čekanja nije u intervalu između 1 i 100!");
                } catch (Exception ex) {
                    Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        //-multi
        if (this.mParametri.group(8) != null) {
            multi = this.mParametri.group(8);
            System.out.println(multi);
        }

        //-ponavljaj
        if (this.mParametri.group(10) != null) {
            ponavljaj = Integer.parseInt(this.mParametri.group(10));
            System.out.println(ponavljaj);
            if (ponavljaj < 2 || ponavljaj > 100) {
                try {
                    throw new Exception("Broj ponavljanja nije u intervalu između 2 i 100!");
                } catch (Exception ex) {
                    Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        return m;
    }

    public void pokreniKlijenta() {
        String datoteka = mParametri.group(4);
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Greška kod čitanja konfiguracije.", ex);
            return;
        }

        String server = mParametri.group(1);
        String brojPorta = mParametri.group(2);
        String user = mParametri.group(3);
        String ispisNaEkran = "USER " + user + "; TIME;";

        String cekanje = mParametri.group(6);
        if (cekanje != null) {
            sleepZadrziZahtjev = Integer.parseInt(cekanje);
            pricekajZahtjev = sleepZadrziZahtjev * 1000;
        } else {
            pricekajZahtjev = 0;
        }

        String multi = mParametri.group(8);
        if (multi != null) {
            String Dretve = konfig.dajPostavku("brojDretvi");
            if (Dretve != null) {
                int iscitaneDretve = Integer.parseInt(Dretve);
                brojDretvi = 1 + (int) (Math.random() * ((iscitaneDretve - 1) + 1));
            } else {
                System.out.println("Broj dretvi nije učitan, prekida se program!");
                return;
            }

            String razmakDretviString = konfig.dajPostavku("razmakDretvi");
            if (razmakDretviString != null) {
                int iscitanRazmakDretvi = Integer.parseInt(razmakDretviString);
                int vremenskiRazmakDretvi = iscitanRazmakDretvi * 1000;
                razmakDretvi = 1 + (int) (Math.random() * ((vremenskiRazmakDretvi - 1) + 1));
            } else {
                System.out.println("Razmak dretvi nije učitan, prekida se program!");
                return;
            }
        } else {
            brojDretvi = 1;
        }

        String iscitajBrojPonavljanjaDretvi = mParametri.group(10);
        if (iscitajBrojPonavljanjaDretvi != null) {
            brojPonavljanjaDretvi = Integer.parseInt(iscitajBrojPonavljanjaDretvi);
        } else {
            brojPonavljanjaDretvi = 1;
        }

        SlanjeZahtjeva[] sz = new SlanjeZahtjeva[brojDretvi];
        for (int i = 0; i < brojDretvi; i++) {
            SlanjeZahtjeva jednaDretvaIzPolja = new SlanjeZahtjeva();
            sz[i] = jednaDretvaIzPolja;
            try {
                jednaDretvaIzPolja.setKonfig(konfig);
            } catch (Exception ex) {
                Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, "Problem kod pridruživanja propertyja dretvi! ", ex);
            }
            jednaDretvaIzPolja.postaviServer(server);
            jednaDretvaIzPolja.postaviPort(Integer.parseInt(brojPorta));
            jednaDretvaIzPolja.postaviCekanje(sleepZadrziZahtjev);
            jednaDretvaIzPolja.postaviBrojPonavljanja(brojPonavljanjaDretvi);
            jednaDretvaIzPolja.postaviPosao(ispisNaEkran);
        }

        for (int i = 0; i < sz.length; i++) {
            sz[i].start();
            try {
                sleep(razmakDretvi);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Problem u slanju zahtjeva sa klijenta ka serveru!", ex);
            }
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

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
import org.foi.nwtis.student5.konfiguracije.Konfiguracija;
import org.foi.nwtis.student5.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student5.konfiguracije.NemaKonfiguracije;

/**
 * Klasa koja radi sa dretvama i obrađuje zahtjeve od klijenata i administratora te omogućuje obradu zahtjeva za pregledom
 * @author student5
 */
public class ObradaZahtjeva extends Thread {
    public enum StanjeDretve {

        Slobodna, Zauzeta
    };

    AdministratorAktivnost stanjeServera;

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

    public ObradaZahtjeva(ThreadGroup group, String name, AdministratorAktivnost stanjeServera) {
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
                sleep(1000 * 1000);
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
            odgovor = "ERROR 90; Sintaksa nije ispravna ili komanda nije dozvoljena";
        } else {
            String korisnik = mNaredba.group(1);
            String password = mNaredba.group(3);
            if (password != null) {
                isAdmin = provjeriKorisnika(korisnik, password);
            }
            String naredba = mNaredba.group(6);
            String uploadNaredba = mNaredba.group(8);

            if(stanjeServera.postaviAktivnost() == AdministratorAktivnost.Aktivnost.Pauziraj
                    && !isAdmin){
                odgovor = "ERROR 10; Server obrađuje samo administratorske zahtjeve.";
            }else if (uploadNaredba != null) {
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
                odgovor = "ERROR 90; Sintaksa nije ispravna ili komanda nije dozvoljena";
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
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }
        if (stanjeServera.postaviAktivnost() == AdministratorAktivnost.Aktivnost.Pauziraj) {
            stanjeServera.dohvatiAktivnost(AdministratorAktivnost.Aktivnost.Aktiviraj);
            System.out.println(" -- novo stanje: RADI");
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 02; Server nije pauziran";
        }
        return odgovor;
    }

    private String naredbaStop(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }

        stanjeServera.dohvatiAktivnost(AdministratorAktivnost.Aktivnost.Zaustavi);
        System.out.println(" -- novo stanje: STOPIRAN");
        boolean serijalizacijaOk = obaviSerijalizaciju();
        if (serijalizacijaOk) {
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 03; Greška kod gašenja.";
        }
        return odgovor;
    }

    private String naredbaPause(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }
        if (stanjeServera.postaviAktivnost() == AdministratorAktivnost.Aktivnost.Aktiviraj) {
            stanjeServera.dohvatiAktivnost(AdministratorAktivnost.Aktivnost.Pauziraj);
            System.out.println(" -- novo stanje: PAUZA");
            odgovor = "OK;";
        } else {
            odgovor = "ERROR 01; Server je pauziran.";
        }
        return odgovor;
    }

    private String naredbaStat(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }        
        String podaciEvidencije = dohvatiPodatkeEvidencije();
        if(podaciEvidencije == null){
            odgovor = "ERROR 05; Greška kod čitanja evidencije.";
        }else{
            odgovor = "OK; " + podaciEvidencije;
        }
        return odgovor;    
    }

    private String naredbaClean(boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }        
        boolean ociscenaEvidencija = ocistiEvidenciju();
        if(ociscenaEvidencija){
            odgovor = "OK;";
        }else{
            odgovor = "ERROR 04; Greška kod pražnjenja evidencije.";
        }
        return odgovor;     
    }
    
    private boolean obaviSerijalizaciju() {
        return false; //TODO
    }

    private String dohvatiPodatkeEvidencije() {
        return null; //TODO
    }

    private boolean ocistiEvidenciju() {
        return false; //TODO
    }

    private String naredbaUpload(String velicina, String sadrzaj, boolean admin) {
        String odgovor;
        if (!admin) {
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }

        String imeDatoteke = adminDatotekaIme.substring(0, adminDatotekaIme.lastIndexOf("."))
                + getCurrentTimeString("yyyyMMdd_HHmmss")
                + adminDatotekaIme.substring(adminDatotekaIme.lastIndexOf("."), adminDatotekaIme.length());

        PrintWriter writer = null;
        File novaDatoteka = new File(imeDatoteke);
        if (novaDatoteka.exists() && !novaDatoteka.isDirectory()) {
            odgovor = "ERROR 06; Datoteka s tim imenom već postoji na poslužitelju.";
            return odgovor;
        }

        try {
            novaDatoteka.createNewFile();
            writer = new PrintWriter(novaDatoteka);
            writer.write(sadrzaj);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Datoteka ne postoji.", ex);
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Nije moguće stvoriti datoteku", ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        long velicinaDobijeneDat = Long.parseLong(velicina);
        long novaDatotekaLen = novaDatoteka.length();
        if (novaDatotekaLen != velicinaDobijeneDat) {
            odgovor = "ERROR 07; Veličina primljene datoteke ne odgovara zadanoj veličini.";
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
            odgovor = "ERROR 00; Korisnik nije administrator ili lozinka ne odgovara";
            return odgovor;
        }
        byte[] sadrzajDatotekeBytes;
        try {
            sadrzajDatotekeBytes = Files.readAllBytes(Paths.get(konfigDatotekaIme));
        } catch (IOException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, "Greška kod čitanja datoteke za upload.\n" + ex.getMessage(), ex);
            odgovor = "ERROR 05; greška kod čitanja konfiguracijske datoteke.";
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
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, "Nema datoteke administratori.\n", ex);
        }
    }

    public void setKonfig(Konfiguracija konfig) {
        this.konfig = konfig;
        if (konfig.postojiPostavka("intervalDretve")) {
            this.trajanjeIntervala = Integer.parseInt(konfig.dajPostavku("intervalDretve"));
            this.trajanjeIntervala = trajanjeIntervala * 1000;
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
package org.foi.nwtis.student5.zadaca_1;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja sluzi za pregled evidencije dretvi
 * @author student5
 */
public class PregledSustava {

    protected String parametri;
    protected Matcher mParametri;

    public PregledSustava(String parametri) throws Exception {
        this.parametri = parametri;
        mParametri = provjeraParametara(parametri);
        if (mParametri == null) {
            throw new Exception("Parametri ne odgovaraju!");
        }
    }

    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-show -s ([^\\s]+\\.bin)$";

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

    public void pokreniPreglednika() {
        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije ne postoji!");
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
package org.foi.nwtis.student5.zadaca_1;

import org.foi.nwtis.student5.konfiguracije.Konfiguracija;

/**
 * Klasa koja serijalizira evidenciju
 * @author student5
 */
public class SerijalizatorEvidencije extends Thread {

    Konfiguracija konfig;

    public SerijalizatorEvidencije(Konfiguracija konfig) {
        super();
        this.konfig = konfig;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        // todo dovršiti za serijalizaciju evidencije
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
package org.foi.nwtis.student5.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.student5.konfiguracije.Konfiguracija;
import org.foi.nwtis.student5.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.student5.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student5.zadaca_1.ObradaZahtjeva.StanjeDretve;

/**
 * Klasa koji prihvaća zahtjeve ostalih dijelova prgorama i djeluje kao server
 * @author student5
 */
public class ServerSustava {

    protected String parametri;
    protected Matcher mParametri;
    private String korisnik;
    protected AdministratorAktivnost admin;

    public ServerSustava(String parametri) throws Exception {
        this.parametri = parametri;
        mParametri = provjeraParametara(parametri);

        if (mParametri == null) {
            throw new Exception("Parametri za server ne odgovaraju!");
        }

        this.admin = new AdministratorAktivnost();
        this.admin.postaviAktivnost();
    }

    public Matcher provjeraParametara(String p) {
        String sintaksa = "^-server -konf ([^\\s]+\\.xml|[^\\s]+\\.txt)( +-load)?$";

        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return m;
        } else {
            System.out.println("Sintaksa za server ne odgovara!");
            return null;
        }
    }

    public void pokreniServer() {
        String datoteka = mParametri.group(1);
        File dat = new File(datoteka);
        if (!dat.exists()) {
            System.out.println("Datoteka konfiguracije za server ne postoji!");
            return;
        }

        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);

            if (this.mParametri.group(2) != null) {
                String evidDatoteka = konfig.dajPostavku("evidDatoteka");
                ucitajSerijaliziranuEvidencije(evidDatoteka);

            }

//            if (this.mParametri.group(3) != null) { // -load
//                String datEvid = konfig.dajPostavku("evidDatoteka");
//                boolean ucitano = ucitajSerijaliziranuEvidenciju(datEvid);
//                if (!ucitano) {
//                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Greška kod pokretanja evidencije.");
//                    // Ne prekida se rad
//                }
//            }            
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Iznimka -> NEMA KONFIGURACIJE.", ex);
            return;
        }

        SerijalizatorEvidencije se = new SerijalizatorEvidencije(konfig);
        se.start();

        int brojDretvi = Integer.parseInt(konfig.dajPostavku("brojDretvi"));
        if (brojDretvi <= 0) {
            System.out.println("GREŠKA -> broj dretvi nije učitan ili nije ispravan broj.");
            return;
        }

        korisnik = "student5";
        ThreadGroup tg = new ThreadGroup(korisnik);
        ObradaZahtjeva[] OZdretve = new ObradaZahtjeva[brojDretvi];
        for (int i = 0; i < brojDretvi; i++) {
            OZdretve[i] = new ObradaZahtjeva(tg, korisnik + "_" + i, admin);
            OZdretve[i].setKonfig(konfig);
            OZdretve[i].setKonfigFileName(datoteka);
            OZdretve[i].setKonfigFileName(konfig.dajPostavku("adminDatoteka"));
            OZdretve[i].start();
        }

        int brojPorta = Integer.parseInt(konfig.dajPostavku("port"));
        if ((brojPorta <= 7999) || (brojPorta >= 9999)) {
            System.out.println("GREŠKA -> broj porta nije učitan ili nije između 8000 i 9999.");
            return;
        }

        try {
            ServerSocket ss = new ServerSocket(brojPorta);
            System.out.println("---Konfiguracija je učitana iz: " + datoteka + ":---");
            System.out.println("---Server je pokrenut i čeka na portu " + brojPorta + ":---");
            while (true) {
                Socket socket = ss.accept();

                ObradaZahtjeva oz = dajSlobodnuDretvu(OZdretve);
                if (oz == null) {
                    System.out.println("ERROR 80; Sve dretve su trenutno zauzete.");
                    continue;
                }
                oz.setStanje(ObradaZahtjeva.StanjeDretve.Zauzeta);
                oz.setSocket(socket);
                stanjaSvihDretvi(OZdretve);
                oz.interrupt();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, "Server nije pokrenut!", ex);
        }
    }

    public void ucitajSerijaliziranuEvidencije(String datoteka) {
        // todo dovršiti sami
    }

    private ObradaZahtjeva dajSlobodnuDretvu(ObradaZahtjeva[] dretve) {
        StanjeDretve slobodna = ObradaZahtjeva.StanjeDretve.Slobodna;
        for (int i = 0; i < dretve.length; i++) { // kreni od početka i nađi prvu slobodnu dretvu
            ObradaZahtjeva obradaDretvi = dretve[i];

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (obradaDretvi.getStanje() == slobodna) { //Dretva je slobodna
                for (int j = i; j < dretve.length - 1; j++) { // šalji dretvu na kraj, ostale pomakni naprijed
                    dretve[j] = dretve[j + 1];
                }
                dretve[dretve.length - 1] = obradaDretvi; //Postavi dretvu na kraj

                return obradaDretvi; //Vrati slobodnu dretvu
            }
        }
        return null;
    }

    ObradaZahtjeva stanjaSvihDretvi(ObradaZahtjeva[] dretve) {
        for (ObradaZahtjeva dretve1 : dretve) {
            if (dretve1.getStanje() == ObradaZahtjeva.StanjeDretve.Slobodna) {
                System.out.println("Dretva " + dretve1.getName() + " je SLOBODNA");
            }
            if (dretve1.getStanje() == ObradaZahtjeva.StanjeDretve.Zauzeta) {
                System.out.println("Dretva " + dretve1.getName() + " je ZAUZETA");
            }
        }
        return null;
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

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
import org.foi.nwtis.student5.konfiguracije.Konfiguracija;

/**
 * Klasa koja služi za slanje zahtjeva na server
 * @author student5
 */
public class SlanjeZahtjeva extends Thread {

    private Konfiguracija konfig;
    private String server;
    private int port;
    private String komanda;
    private long cekajPrijeZahtjeva;
    private int brojPonavljanja;
    private long interval;
    private long pauzaProblema;
    private int brojPokusajaProblema;
    private String lokacijaDatoteke;

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        long pocRadaDretve;
        long sleepDretve;
        for (int i = 0; i < brojPonavljanja; i++) {
            pocRadaDretve = System.currentTimeMillis();

            int neuspjeliPokusaji = 0;
            boolean radGotov = false;
            boolean sleepProblem = false;
            InputStream is = null;
            OutputStream os = null;
            Socket socket = null;
            do {
                try {
                    if (sleepProblem) {
                        try {
                            sleep(pauzaProblema);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlanjeZahtjeva.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                        sleepProblem = false;
                    }
                    socket = new Socket(server, port);
                    try {
                        sleep(cekajPrijeZahtjeva);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }

                    os = socket.getOutputStream();
                    is = socket.getInputStream();

                    os.write(komanda.getBytes());
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

                    String serverOdg = sb.toString();
                    if (serverOdg.isEmpty()) {
                        neuspjeliPokusaji++;
                        sleepProblem = true;
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO,
                                "Server ne odgovara");
                    } else {
                        radGotov = obradiOdgovorPosluzitelja(serverOdg);
                    }
                } catch (IOException ex) {
                    neuspjeliPokusaji++;
                    sleepProblem = true;
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO, "Broj neuspjelih pokusaja:" + neuspjeliPokusaji);
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Greska kod zatvaranja input streama!");
                    }
                }

                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Greska kod zatvaranja output strema!");
                    }
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        System.out.println("Greska kod zatvaranja socketa!");
                    }
                }

                if (neuspjeliPokusaji == brojPokusajaProblema) {
                    System.out.println("Prekid se rad programa.");
                    break;
                }
                if (!radGotov) {
                    neuspjeliPokusaji++;
                }
            } while (!radGotov);

            if (radGotov) {
                neuspjeliPokusaji = 0;
            }

            sleepDretve = cekajOdredenoVrijeme(pocRadaDretve);
            Logger.getLogger(ServerSustava.class.getName()).log(Level.INFO, getName() + " zavrsila ciklus: " + i);
            try {
                sleep(sleepDretve);
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
        } else if (odgovorPosluzitelja.startsWith("ERROR ")) {
            System.out.println(odgovorPosluzitelja);
            posaoObavljen = true;
        } else if (odgovorPosluzitelja.startsWith("DATA ")) {
            Pattern dataPattern = Pattern.compile("^DATA (.+); \\r\\n(.+)$", Pattern.DOTALL);
            Matcher mData = dataPattern.matcher(odgovorPosluzitelja);
            if (!mData.matches()) {
                System.out.println("Primljeni neprepoznatljivi podaci:" + odgovorPosluzitelja);
                posaoObavljen = false;
            } else {
                System.out.println("Primljeni podaci od servera. Veličina podataka: " + mData.group(1));
                posaoObavljen = spremiDatoteku(mData.group(2), mData.group(1));
            }
        } else {
            System.out.println("Primljen neprepoznatljiv odgovor");
            posaoObavljen = false;
        }
        return posaoObavljen;
    }

    private boolean spremiDatoteku(String podaci, String velicina) {
        File file = new File(this.lokacijaDatoteke);
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.write(podaci);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministratorSustava.class.getName()).log(Level.SEVERE, "Greška kod stvaranja datoteke koja se preuzima.\n" + ex.getMessage(), ex);
            return false;
        }
        long dobivenaVelicina = Long.parseLong(velicina);
        long stvarnaVelicina = file.length();
        if (dobivenaVelicina == stvarnaVelicina) {
            return true;
        } else {
            file.delete();
            return false;
        }
    }

    public void setKonfig(Konfiguracija konfig) throws Exception {
        this.konfig = konfig;

        String intervalDretvePostavka = ucitajPostavku("intervalDretve");
        if (intervalDretvePostavka != null) {
            postaviInterval(Long.parseLong(intervalDretvePostavka) * 1000);
        } else {
            throw new Exception("Greška kod učitavanja intervala dretve");
        }

        String pauzaProblemaPostavka = ucitajPostavku("pauzaProblema");
        if (pauzaProblemaPostavka != null) {
            postaviPauzu(Long.parseLong(pauzaProblemaPostavka) * 1000);
        } else {
            throw new Exception("Greška kod učitavanja pauze problema");
        }

        String brojPokusajaPostavka = ucitajPostavku("brojPokusajaProblema");
        if (brojPokusajaPostavka != null) {
            postaviBrojPokusaja(Integer.parseInt(brojPokusajaPostavka));
        } else {
            throw new Exception("Greška kod učitavanja brojPokusajaProblema");
        }
    }

    private long cekajOdredenoVrijeme(final long pocetakObrade) {
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

    public void postaviInterval(long interval) {
        this.interval = interval;
    }

    public void postaviPauzu(long pauza) {
        this.pauzaProblema = pauza;
    }

    public void postaviBrojPokusaja(int brojPokusaja) {
        this.brojPokusajaProblema = brojPokusaja;
    }

    public void postaviServer(String server) {
        this.server = server;
    }

    public void postaviPort(int port) {
        this.port = port;
    }

    void postaviPosao(String naredba) {
        this.komanda = naredba;
    }

    void postaviCekanje(long cekanjePrijeSlanjaZahtjeva) {
        this.cekajPrijeZahtjeva = cekanjePrijeSlanjaZahtjeva;
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

    void postaviLokaciju(String lokacijaDatoteke) {
        this.lokacijaDatoteke = lokacijaDatoteke;
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Glavna pokretačka klasa zadace 1, prihvaca i obrađuje argumente
 * @author student5
 */
public class Zadaca_student5_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();

        Zadaca_student5_1 zadaca = new Zadaca_student5_1();
        Matcher m = zadaca.provjeraParametara(p);

        if (m == null) {
            return;
        }
        if (m.group(1) != null) {
            try {
                ServerSustava server = new ServerSustava(p);
                server.pokreniServer();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_student5_1.class.getName()).log(Level.SEVERE, "Problem kod pokretanja servera!", ex);
            }
        }
        if (m.group(2) != null) {
            try {
                AdministratorSustava admin = new AdministratorSustava(p);
                admin.pokreniAdministratora();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_student5_1.class.getName()).log(Level.SEVERE, "Problem kod pokretanja administratora!", ex);
            }
        }
        if (m.group(3) != null) {
            try {
                KlijentSustava klijent = new KlijentSustava(p);
                klijent.pokreniKlijenta();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_student5_1.class.getName()).log(Level.SEVERE, "Problem kod pokretanja klijenta!", ex);
            }
        }
        if (m.group(4) != null) {
            try {
                PregledSustava ps = new PregledSustava(p);
                ps.pokreniPreglednika();
            } catch (Exception ex) {
                Logger.getLogger(Zadaca_student5_1.class.getName()).log(Level.SEVERE, "Problem kod pokretanja pregleda sustava!", ex);
            }
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
            System.out.println("Parametri u regexu ne odgovaraju!");
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
/**
 *
 * @author student5
 */
public class TestOpcija {

    /**
     * @param args the command line arguments
     */
	 
    public static void main(String[] args) {
	
	// -server -konf datoteka(.txt | .xml) [-load]
        String sintaksa1 = "^-server -konf ([^\\s]+\\.(?i)txt|xml)( +-load)?$";
        String sintaksa = "(^-server.+)|(^-admin.+)|(^-user.+)|(^-show.+)";
                                                                   
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
        } else {
			System.out.println("Ne odgovara!");
		}
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author NWTiS_1
 */
public class ServerSustavaTest {

    public ServerSustavaTest() {
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
            p = "-server -konf NWTiS_student5_1.txt";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
            p = "-server -konf NWTiS_student5_1.txt -load";
            result = instance.provjeraParametara(p);
            assertNotNull(result);
        } catch (Exception ex) {
            Logger.getLogger(ServerSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student5.zadaca_1;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author student5
 */
public class Zadaca_student5_1Test {
    
    public Zadaca_student5_1Test() {
    }

    /**
     * Test of main method, of class Zadaca_student5_1.
     */
    @Ignore
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Zadaca_student5_1.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provjeraParametara method, of class Zadaca_student5_1.
     */
    @Test
    public void testProvjeraParametara() {
        System.out.println("provjeraParametara");
        String p = "-pero";
        Zadaca_student5_1 instance = new Zadaca_student5_1();
        Object expResult = null;
        Object result = instance.provjeraParametara(p);
        assertNull(result);
        p = "-server";
        result = instance.provjeraParametara(p);
        assertNull(result);
        p = "-server -konf NWTiS_student5_1.txt";
        result = instance.provjeraParametara(p);
        assertNotNull(result);
        p = "-admin -konf NWTiS_student5_1.txt";
        result = instance.provjeraParametara(p);
        assertNotNull(result);
    }
    
}