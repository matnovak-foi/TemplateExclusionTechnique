
DATOTEKA_TestOpcija.java
package org.foi.nwtis.matnovak;

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
package org.foi.nwtis.matnovak.zadaca_1;

import java.io.Serializable;

/**
 *
 * @author grupa_3
 */
class Brod implements Serializable{
    public int xPozicija;
    public int yPozicija;
    public boolean potopljen;
    public String igrac;
    
}

DATOTEKA_Evidencija.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author grupa_3
 */
public class Evidencija implements Serializable{
    public ArrayList<String> igraci = new ArrayList<String>();
    public ArrayList<Brod> brodovi = new ArrayList<Brod>();
    public ArrayList<Potez> potezi = new ArrayList<Potez>();
    public int velicinaPloce;
    
}

DATOTEKA_KlijentSustava.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

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
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class KlijentSustava {
    protected String parametri;
    protected String posluzitelj = "127.0.0.1";//TODO preuzmi iz parametara
    protected int port = 9999;//TODO preuzmi iz parametara
    protected String korisnik = "matnovak";//TODO preuzmi iz parametara

    public KlijentSustava(String parametri) {
        this.parametri = parametri;
    }
    
    public void pokreni(){
        if(!provjeriParametre()){
            System.out.println("Problem s parametrima!");
        }
        
        try {
            Socket socket = new Socket(posluzitelj, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            
            String zahtjev = "USER " + korisnik + "; PLAY;";
            os.write(zahtjev.getBytes());
            os.flush();
            socket.shutdownOutput();
            
            StringBuffer odgovor = new StringBuffer();
            int bajt;
            while((bajt = is.read())!=-1){
                odgovor.append((char) bajt);
            }
            System.out.println("Odgovor: " + odgovor);
            os.close();
            is.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(KlijentSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private boolean provjeriParametre(){
        String sintaksa = "^-user -s ([a-zA-Z0-9_.]*) -port (9{4})";
        
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(this.parametri);
        boolean status = m.matches();
        if (status) {
            if(m.group(1) != null){
                System.out.println("Server: " + m.group(1));
            }
            if(m.group(2) != null){
                System.out.println("Port:" + m.group(2));
            }
            return true;
        } else {
            return false;
        }
    }
}

DATOTEKA_ObradaZahtjeva.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
class ObradaZahtjeva extends Thread {
    protected Socket socket = null;
    protected Konfiguracija konfig = null;
    
    ObradaZahtjeva(Socket socket, Konfiguracija konfig) {
        this.socket = socket;
        this.konfig = konfig;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            
            StringBuffer zahtjev = new StringBuffer();
            int bajt;
            while((bajt = is.read())!=-1){
                zahtjev.append((char) bajt);
            }
            System.out.println("Zahtjev: " + zahtjev);
            
            String odgovor = "OK;";
            os.write(odgovor.getBytes());
            os.flush();
            os.close();
            is.close();
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
package org.foi.nwtis.matnovak.zadaca_1;

/**
 *
 * @author grupa_3
 */
class Potez {
    public int xPozicija;
    public int yPozicija;
    public String igrac;
    public int runda;
}

DATOTEKA_SerijalizatorEvidencije.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grupa_3
 */
public class SerijalizatorEvidencije extends Thread {

    protected String datoteka;
    protected int vrijemeCiklusa = 10000;//TODO preuzmi iz konfiguracije
    protected ServerSustava serverSustava = null;

    public SerijalizatorEvidencije(String datoteka, ServerSustava server) {
        this.datoteka = datoteka;
        this.serverSustava = server;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        
        //TODO osigurati međusobnu isključivost s ostalim dretvama (ObradaZahtjeva)
        while (true) {
            try {
                FileOutputStream out = new FileOutputStream(datoteka);
                ObjectOutputStream s = new ObjectOutputStream(out);               
                s.writeObject(serverSustava.evidencija);
                s.close();
                out.close();
                sleep(vrijemeCiklusa);
                //TODO obavi korekciju spavanja za utrošeno vrijeme rada dretve
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    @Override
    public synchronized void start() {
        System.out.println("POKRENUTA DRETVA SERIJALIZACIJE!");
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}

DATOTEKA_ServerSustava.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class ServerSustava {
    protected String parametri;
    protected int port = 9999;//TODO učitaj iz konfiguracije
    protected Konfiguracija konfig = null;
    protected String datoteka = "NWTiS_matnovak.txt";//TODO preuzmi iz parametara
    protected boolean kraj = false;
    protected String datotekaEvidencije = "NWTiS_matnovak_ER.txt";//TODO preuzmi iz parametara
    protected Evidencija evidencija = new Evidencija();
    
    public ServerSustava(String parametri) {
        this.parametri = parametri;
    }
    
    public void pokreni(){
        if(!provjeriParametre()){
            System.out.println("Problem s parametrima!");
        }
        
        evidencija.brodovi.add(new Brod());
        SerijalizatorEvidencije se = new SerijalizatorEvidencije(datotekaEvidencije, this);
        se.start();
        
        try {
            konfig =KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            //TODO učitaj sve što je potrebno
            
            ServerSocket serverSocket = new ServerSocket(port);
            while(!kraj){
                Socket socket = serverSocket.accept();
                //TODO obrati pažnju na broj aktivnih dretvi
                ObradaZahtjeva obradaZahtjeva = new ObradaZahtjeva(socket,konfig);
                obradaZahtjeva.start();
            }
            
        } catch (IOException | NemaKonfiguracije ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private boolean provjeriParametre(){
        String sintaksa = "^-server -konf ([a-zA-Z0-9_.]*)( -load)?";
        
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(this.parametri);
        boolean status = m.matches();
        if (status) {
            if(m.group(1) != null){
                System.out.println("Datoteka: " + m.group(1));
            }
            if(m.group(2) != null){
                System.out.println("Učitavanje evidencije");
            }
            return true;
        } else {
            return false;
        }
    }
}

DATOTEKA_Zadaca_matnovak_1.java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.matnovak.zadaca_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Matija Novak <matija.novak@foi.hr>
 */
public class Zadaca_matnovak_1 {
    public static void main(String args[]){
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
            //TODO izbaciti nakon testiranja
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
            //TODO KRAJ
            if(m.group(1) != null)
            {
                ServerSustava serverSustava = new ServerSustava(p);
                serverSustava.pokreni();
            }
            else if(m.group(3) != null)
            {
                KlijentSustava klijentSustava = new KlijentSustava(p);
                klijentSustava.pokreni();
            }
        } else {
			System.out.println("Ne odgovara!");
		}
    }
}
