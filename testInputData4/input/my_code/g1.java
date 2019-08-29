
DATOTEKA_TestOpcija.java
/*
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
 * @author teacher2
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
}

DATOTEKA_Brod.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

/**
 *
 * @author grupa_1
 */
public class Brod {
    public String vlasnik;
    public int xPozicija;
    public int yPozicija;
    public boolean potopljen;
}

DATOTEKA_Evidencija.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author grupa_1
 */
public class Evidencija implements Serializable {
    public ArrayList<String> igraci = new ArrayList<>();
    public ArrayList<Brod> brodovi = new ArrayList<>();
    public ArrayList<Potez> potezi = new ArrayList<>();        
}

DATOTEKA_KlijentSustava.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author teacher2
 */
public class KlijentSustava {
    protected String parametri;
    protected String posluzitelj = "127.0.0.1"; // todo preuzmi pdoatke iz parametara
    protected int brojZaVrata = 8000; // todo preuzmi pdoatke iz parametara
    protected String korisnik = "teacher2"; // todo preuzmi pdoatke iz parametara

    public KlijentSustava(String parametri) {
        this.parametri = parametri;
    }
    
    public void kreni() {
        if(! provjeriParametre()) {
            System.out.println("Problem s parametrima");
            return;
        }
        try {
            Socket vrata = new Socket(posluzitelj, brojZaVrata);
            OutputStream os = vrata.getOutputStream();
            InputStream is = vrata.getInputStream();
            
            String zahtjev = "USER " + korisnik + "; PLAY;";
            os.write(zahtjev.getBytes());
            os.flush();
            vrata.shutdownOutput();
            
            StringBuffer odgovor = new StringBuffer();
            int bajt;
            
            while((bajt = is.read()) != -1) {
                odgovor.append((char) bajt);                        
            }
            is.close();
            System.out.println(odgovor);
            vrata.close();
        } catch (IOException ex) {
            Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean provjeriParametre() {
        String sintaksa = "^-user -s ([a-zA-Z0-9_.]*) -port (9{4})";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(this.parametri);
        boolean status = m.matches();
        if (status) {
            if(m.group(1) != null) {
                System.out.println("Server: " + m.group(1));
            }
            if(m.group(2) != null) {
                System.out.println("Port: " + m.group(2));
            }
            return true;
        } else {
            return true; // todo vrati na false i podesi RegEx
        }                        
    }
    
}

DATOTEKA_ObradaZahtjeva.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.teacher2.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_1
 */
public class ObradaZahtjeva extends Thread {
    protected Socket vrata = null;
    protected Konfiguracija konfig = null;
    
    public ObradaZahtjeva(Socket vrata, Konfiguracija konfig) {
        this.vrata = vrata;
        this.konfig = konfig;
    }
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            InputStream is = vrata.getInputStream();
            OutputStream os = vrata.getOutputStream();
            StringBuffer zahtjev = new StringBuffer();
            int bajt;
            while((bajt = is.read()) != -1) {
                zahtjev.append((char) bajt);                
            }
            System.out.println(zahtjev);
            String odgovor = "OK;";
            os.write(odgovor.getBytes());
            os.flush();
            is.close();
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

DATOTEKA_Potez.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

/**
 *
 * @author grupa_1
 */
public class Potez {
    public String vlasnik;
    public int xPozicija;
    public int yPozicija;
    public boolean potopljen;    
}

DATOTEKA_SerijalizatorEvidencije.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grupa_1
 */
public class SerijalizatorEvidencije extends Thread {

    protected String datoteka;
    protected ServerSustava serverSustava;
    protected int vrijemeCiklusa = 10000; // todo preuzmi iz konfiguracije

    public SerijalizatorEvidencije(String datoteka, ServerSustava serverSustava) {
        this.datoteka = datoteka;
        this.serverSustava = serverSustava;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        // todo osigirati međusobnu isključivost s ostalim dretvama (ObradaZahtjeva)
        while (true) {
            Evidencija evidencija = serverSustava.evidencija;
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(datoteka));
                oos.writeObject(evidencija);
                oos.close();
                sleep(vrijemeCiklusa); // todo obavi korenciju spavanja za utrošeno vrijem rada dretve
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}

DATOTEKA_ServerSustava.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.teacher2.konfiguracije.Konfiguracija;
import org.foi.nwtis.teacher2.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.teacher2.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author teacher2
 */
public class ServerSustava {

    protected String parametri;
    protected int port = 8000; //todo učitaj iz konfiguracije
    protected Konfiguracija konfig = null;
    protected String datoteka = "NWTiS_teacher2.txt"; //todo preuzmi iz parametara
    protected String datotekaEvidencije = "NWTiS_teacher2_ER.txt"; //todo preuzmi iz parametara
    protected boolean kraj = false;
    protected Evidencija evidencija;
    
    public ServerSustava(String parametri) {
        this.parametri = parametri;
    }

    public void kreni() {
        if (!provjeriParametre()) {
            System.out.println("Problem s parametrima");
        }
        SerijalizatorEvidencije se = new SerijalizatorEvidencije(datotekaEvidencije, this);
        se.start();

        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            ServerSocket ss = new ServerSocket(port);
            
            while(! kraj) {
                Socket vrata = ss.accept();
                // todo obrati pažnju na broj aktivnih dretvi
                ObradaZahtjeva oz = new ObradaZahtjeva(vrata, konfig);
                oz.start();
            }
            
        } catch (IOException | NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean provjeriParametre() {
        String sintaksa = "^-server -konf ([a-zA-Z0-9_]*)( -load)?";
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(this.parametri);
        boolean status = m.matches();
        if (status) {
            if (m.group(1) != null) {
                System.out.println("Datoteka: " + m.group(1));
            }
            if (m.group(2) != null) {
                System.out.println("Učitavanje evidencije");
            }
            return true;
        } else {
            return false;
        }
    }

}

DATOTEKA_Zadaca_teacher2_1.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.teacher2.zadaca_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author teacher2
 */
public class Zadaca_teacher2_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
            //todo izbaci nakon testiranja
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
            // kraj
            if (m.group(1) != null) {
                ServerSustava serverSustava = new ServerSustava(p);
                serverSustava.kreni();
            } else if (m.group(3) != null) {
                KlijentSustava klijentSustava = new KlijentSustava(p);
                klijentSustava.kreni();
            }
        } else {
            System.out.println("Ne odgovara!");
        }
    }

}
