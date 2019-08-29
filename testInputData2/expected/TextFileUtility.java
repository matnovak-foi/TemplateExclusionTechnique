
public class Adrese implements Serializable {
}/*
*/
public class AbstractFacade {
}/*
*/

public class AdreseFacade extends AbstractFacade<Adrese> {
}/*
*/
public class DnevnikFacade extends AbstractFacade<Dnevnik> {
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.ejb.sb;

import java.util.ArrayList;

public class MeteoAdresniKlijent {
public ArrayList<MeteoPrognoza> dajMeteoPrognoze(String adresa, int brojDana) {
        if (l == null) {
            return null;
        }
        OWMKlijent owmk = new OWMKlijent("3e42147a3c12ac686ecafc5c42c98ebb");
        ArrayList<MeteoPrognoza> mp = owmk.getWeatherForecast(l.getLatitude(), l.getLongitude(), brojDana);
        
        return mp;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        return l;
}
}
public class GMRESTHelper {
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.rest.klijenti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;

public class OWMKlijent {

    public ArrayList<MeteoPrognoza> getWeatherForecast(String latitude, String longitude, int noDays) {
        MeteoPrognoza[] mPrognoza = new MeteoPrognoza[noDays + 1];

        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_ForecastDaily_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("cnt", noDays);
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

        try {
            JSONObject jo = new JSONObject(odgovor);
            JSONArray ja = jo.getJSONArray("list");
            int limit = jo.getInt("cnt");

            for (int i = 0; i < limit; i++) {
                mPrognoza[i] = new MeteoPrognoza();
                
                mPrognoza[i].setDan(i + 1);
                mPrognoza[i].setAdresa(jo.getJSONObject("city").getString("name"));
                mPrognoza[i].setDatum(new Date(ja.getJSONObject(i).getLong("dt") * 1000));
                
                MeteoPodaci mp = new MeteoPodaci();

                mp.setTemperatureMax(Float.parseFloat(ja.getJSONObject(i).getJSONObject("temp").getString("max")));
                mp.setTemperatureMin(Float.parseFloat(ja.getJSONObject(i).getJSONObject("temp").getString("min")));
                mp.setWindDirectionValue(Float.parseFloat(ja.getJSONObject(i).getString("deg")));
                
                mPrognoza[i].setPrognoza(mp);
           }
            
            return new ArrayList<>(Arrays.asList(mPrognoza));
        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }

}
}

public class MeteoPrognoza {
}
public class BaseBean {
    @EJB
    protected DnevnikFacade dnevnikFacade;

    protected long vrijemePozivanjaMetode;

    /**
     * Zapis akcije na stranici u dnevnik unutar baze podataka
     *
     * @param pocetak vrijeme početka izvršavanja akcije
     * @param status 200 - OK / 400 - ERROR
     */
    protected void zapisiUDnevnik(long pocetak, int status) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik("student2");
        String IP = null;
        String url = null;

        // dohvati IP
        try {
            IP = InetAddress.getLocalHost().toString();
        } catch (Exception ex) {
        }
        if (IP == null || IP.length() <= 0) {
            dnevnik.setIpadresa("n/a");
        } else {
            dnevnik.setIpadresa(IP);
        }

        // dohvati trenutni URL
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = origRequest.getRequestURI();
        if (url != null) {
            dnevnik.setUrl(url);
        } else {
            dnevnik.setUrl("n/a");
        }

        dnevnik.setTrajanje((int) (System.currentTimeMillis() - pocetak));
        dnevnik.setVrijeme(new Date());
        dnevnik.setStatus(status);

        dnevnikFacade.create(dnevnik);
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class OdabirAdresaPrognoza extends BaseBean implements Serializable {
/**
     * Dodavanje odabrane adrese iz liste svih adresa u listu za dohvaćanje
     * meteo podataka
     */
    public void dodajAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prognozaAdrese.add(odabranaAdresaZaDodati);
        aktivneAdrese.removeIf(s -> s.equals(odabranaAdresaZaDodati));

        // sortiraj abecedno
        Collections.sort(prognozaAdrese, Collator.getInstance(new Locale("hr", "HR")));

        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
    }

    /**
     * Uklanjanje odabrane adrese iz liste adresa za dohvaćanje meteo podataka i
     * prebacivanje u listu sa svim adresama
     */
    public void ukloniAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        aktivneAdrese.add(odabranaAdresaZaMaknuti);
        prognozaAdrese.removeIf(s -> s.equals(odabranaAdresaZaMaknuti));

        // sortiraj abecedno
        Collections.sort(prognozaAdrese, Collator.getInstance(new Locale("hr", "HR")));

        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
    }

    /**
     * Upisivanje nove adrese zajedno sa njenim geolokacijskim podacima u bazu
     * podataka
     *
     * @return null
     */
    public Object upisiAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        // ako je prazno zapiši pogrešku u dnevnik
        if (novaAdresa == null || novaAdresa.isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
            // ako nije prazno spremi novu adresu
        } else {
            Adrese adresa = new Adrese();
            adresa.setAdresa(novaAdresa);
            Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
            adresa.setLatitude(l.getLatitude());
            adresa.setLongitude(l.getLongitude());
            adreseFacade.create(adresa);
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        }
        return null;
    }

    /**
     * Ažuriranje odabrane adrese u bazi podataka
     *
     * @return null
     */
    public Object azurirajAdresu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        List<Adrese> adrese = adreseFacade.findAll();
        Adrese adresaZaAzuriranje = new Adrese();

        // pronađi traženu adresu u listi svih adresa
        for (Adrese a : adrese) {
            if (a.getAdresa().equals(originalnaAdresa)) {
                adresaZaAzuriranje = a;
            }
        }

        // ako adresa ne postoji, izađi iz metode
        if (adresaZaAzuriranje.getAdresa() == null || adresaZaAzuriranje.getAdresa().isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
            return null;
        }

        // dohvati nove geolokacijske podatke za adresu
        Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
        adresaZaAzuriranje.setLatitude(l.getLatitude());
        adresaZaAzuriranje.setLongitude(l.getLongitude());
        adresaZaAzuriranje.setAdresa(azuriranaAdresa);
        adreseFacade.edit(adresaZaAzuriranje);

        prikaziAzuriranje = false;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);

}

    
    public Object otvoriAzuriranjeAdrese() {
        azuriranaAdresa = odabranaAdresaZaDodati;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        return null;
    }

    /**
     * Dohvati meteo podatke za sve adrese dodane u listu za dohvaćanje meteo
     * podataka, dohvaćaju se podaci za broj dana koji je upisan u polje. Metoda
     * također otvara prikaz tablice ispod forme.
     */
    public void dohvatiMeteoPodatke() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        meteoPrognoza = new ArrayList<>();
        List<MeteoPrognoza> mp;

        for (String s : prognozaAdrese) {
            mp = meteoAdresniKlijent.dajMeteoPrognoze(s, brojDana);
            if (mp != null) {
                meteoPrognoza.addAll(mp);
            }
        }

        prikaziPrognoze = true;

        // zapisi u dnevnik
        if (!meteoPrognoza.isEmpty()) {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
        } else {
            zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_ERROR);
        }
    }

    /**
     * Zatvaranje prikaza tablice ispod forme
     */
    public void zatvoriTablicu() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prikaziPrognoze = false;
        zapisiUDnevnik(vrijemePozivanjaMetode, STATUS_OK);
}
public List<String> getAktivneAdrese() {
        if (prognozaAdrese == null) {
            prognozaAdrese = new ArrayList<>();
        }
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            if (!prognozaAdrese.contains(a.getAdresa())) {
                aktivneAdrese.add(a.getAdresa());
            }
        }

        // sortiraj abecedno
        Collections.sort(aktivneAdrese, Collator.getInstance(new Locale("hr", "HR")));

        return aktivneAdrese;

}
public List<String> getPrognozaAdrese() {
}
    /**
     * Metoda zabranjuje vrijednosti veće od 16 i manje od 1 da budu upisane kao
     * vrijednost dana za dohvaćanje meteo podataka
     *
     * @param brojDana broj dana za dohvaćanje meteo podataka za adresu
     */
    public void setBrojDana(int brojDana) {
        if (brojDana > 16) {
            this.brojDana = 16;
        } else if (brojDana < 1) {
            this.brojDana = 1;
        } else {
            this.brojDana = brojDana;
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.student2.ejb.eb.Dnevnik;

/**
 * ManagedBean klasa koja se koristi za pregledAdresa.xhtml
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika extends BaseBean implements Serializable {

    private List<Dnevnik> dnevnik;
    private String rijecZaPretrazivanje;
    private String ipAdresa;
    private String odDatuma;
    private String doDatuma;
    private int trajanje;
    private int status;
    private boolean zapisiPretrazivanje = false;
    private boolean zapisiFiltriranje = false;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {

}
private void filtrirajDnevnik() {
 if (RCCdummy) {}
        // provjeri jeli upisan datum od
        if (!(odDatuma == null || odDatuma.isEmpty())) {
            Date od = new Date(Long.parseLong(odDatuma) * 1000);
            dnevnik.removeIf(d -> d.getVrijeme().before(od));
        }

        // provjeri jeli upisan datum do
        if (!(doDatuma == null || doDatuma.isEmpty())) {
            Date dod = new Date(Long.parseLong(doDatuma) * 1000);
            dnevnik.removeIf(d -> d.getVrijeme().after(dod));
        }

        if (zapisiFiltriranje) {
            zapisiUDnevnik(vrijemePozivanjaMetode, 200);
            zapisiFiltriranje = false;
        }

}

    public List<Dnevnik> getDnevnik() {
        if (dnevnik == null) {
            dnevnik = new ArrayList<>();
        }

        dnevnik = dnevnikFacade.findAll();
        // provjeri dali treba pretraziti ili filtrirati
        pretraziDnevnik();
        filtrirajDnevnik();
        return dnevnik;

}
}
