
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
*/
public class MeteoAdresniKlijent {
public MeteoPrognoza[] dajMeteoPrognoze(String adresa, int brojDana) {
        
        //TODO api key je hardkodiran
        OWMKlijent owmk = new OWMKlijent("2e9347d77e1b419858b1707cc02b6ae3");
        MeteoPrognoza[] mp = owmk.getWeatherForecast(l.getLatitude(), l.getLongitude(), brojDana, adresa);
        
        return mp;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        return l;

}
}
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.student1.web.podaci.MeteoPodaci;
import org.foi.nwtis.student1.web.podaci.MeteoPrognoza;
import org.foi.nwtis.student1.web.podaci.MeteoPrognozaPodaci;

public class OWMKlijent {

    /**
     * Metoda dohvaća json i sprema ga u posebno kreiranu klasu za podatke
     * prognoze
     *
     * @param latitude
     * @param longitude
     * @param noDays
     * @param adresa
     * @return
     */
    public MeteoPrognoza[] getWeatherForecast(String latitude, String longitude, int noDays, String adresa) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_ForecastDaily_Path());

        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
        webResource = webResource.queryParam("cnt", noDays);

        MeteoPrognoza[] mp = new MeteoPrognoza[noDays];

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        //System.out.println("WEB LINK: " + webResource.getUri());

        try {
            JSONObject jo = new JSONObject(odgovor);
            JSONArray lista = jo.getJSONArray("list");
            //System.out.println("LISTA LENGHT: " + lista.length());

            for (int i = 0; i < noDays; i++) {
                JSONObject dan = lista.getJSONObject(i);

                MeteoPrognozaPodaci mpp = new MeteoPrognozaPodaci();
                mpp.setTemp(Float.parseFloat(dan.getJSONObject("temp").getString("day")));
                mpp.setPressure(Float.parseFloat(dan.getString("pressure")));
                mpp.setHumidity(Float.parseFloat(dan.getString("humidity")));
                mpp.setWeather(dan.getJSONArray("weather").getJSONObject(0).getString("description"));
                mpp.setClouds(Float.parseFloat(dan.getString("clouds")));
                mpp.setWindSpeed(Float.parseFloat(dan.getString("speed")));
                mpp.setWindDeg(Float.parseFloat(dan.getString("speed")));
                mpp.setDt(dan.getString("dt"));

                MeteoPrognoza metPro = new MeteoPrognoza();
                metPro.setAdresa(adresa);

                /**
                 * Na osnovi iteracije spremam datum za koji se odnosi prognoza
                 */
                //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                if (i > 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    c.add(Calendar.DATE, i);
                    date = c.getTime();
                }

                metPro.setDan(i);
                metPro.setDatum(date);
                metPro.setPrognoza(mpp);
                mp[i] = metPro;
            }
} catch (JSONException ex) {}
        
    }
}
public class MeteoPrognozaPodaci {
    

    public void setTemp(float temp) {
}

}
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.student1.ejb.eb.Adrese;
import org.foi.nwtis.student1.ejb.eb.Dnevnik;
import org.foi.nwtis.student1.ejb.sb.AdreseFacade;
import org.foi.nwtis.student1.ejb.sb.DnevnikFacade;
import org.foi.nwtis.student1.ejb.sb.MeteoAdresniKlijent;
import org.foi.nwtis.student1.web.podaci.Lokacija;
import org.foi.nwtis.student1.web.podaci.MeteoPrognoza;
public class OdabirAdresaPrognoza implements Serializable {
    private DnevnikFacade dnevnikFacade;

    private List<String> prognozaAdrese = new ArrayList<>();
    
    private MeteoPrognoza[] meteoPrognoza;
    private List<MeteoPrognoza> mpp = new ArrayList<>();
    
    private int brojDana;
    private String poruka;
    
    private boolean prikaziAzuriranje = false;
    private boolean prikaziPrognoze = false;
    private boolean prikaziError = false;
    
    public void spremiAkciju(String korisnik, int status, String url, String ip, int trajanje) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setIpadresa(ip);
        dnevnik.setKorisnik(korisnik);
        dnevnik.setUrl(url);
        dnevnik.setStatus(status);
        dnevnik.setTrajanje(trajanje);
        Date date = new Date();     
        dnevnik.setVrijeme(date);
        dnevnikFacade.create(dnevnik);
    }

    /**
     * Metoda dodaje string u listu prognozaAdrese
     */
    public void prebaciAdresu() {
        prognozaAdrese.add(odabranaAdresaZaDodati);
        spremiAkciju("student1", 0, "url", "localhost", 0);
    }

    /**
     * Metoda brise string iz liste prognozaAdrese dodaje ga u aktivneAdrese te
     * poziva funkciju UkloniPrognoze
     */
    public void prebaciAdresuNazad() {
        prognozaAdrese.remove(odabranaAdresaZaMaknuti);
        aktivneAdrese.add(odabranaAdresaZaMaknuti);
        UkloniPrognoze();
        spremiAkciju("student1", 1, "url", "localhost", 0);
    }

    /**
     * Metoda dohvaća prognoze za sve adrese u listi prognozaAdrese i sprema ih
     * u listu prognoza mpp
     */
    public void DohvatiPrognoze() {
        spremiAkciju("student1", 2, "url", "localhost", 0);
        mpp = new ArrayList<>();
        for (String adresa : prognozaAdrese) {
            MeteoPrognoza[] mp = meteoAdresniKlijent.dajMeteoPrognoze(adresa, brojDana);
            for (int i = 0; i < brojDana; i++) {
                mpp.add(mp[i]);
            }
        }
        
    }

    /**
     * Metoda brise prognoze iz liste prognoza na osnovi odabrane adrese
     */
    public void UkloniPrognoze() {
        List<MeteoPrognoza> pomocna = new ArrayList<>();
        for (MeteoPrognoza mp : mpp) {
            if (!mp.getAdresa().equals(odabranaAdresaZaMaknuti)) {
                pomocna.add(mp);
            }
        }
        mpp = pomocna;
    }
/**
     * Metoda dohvaća sve adrese iz baze i dodaje ih u aktivneAdrese ako se ne
     * nalaze u prognozaAdrese
     *
     * @return
     */
    public List<String> getAktivneAdrese() {
        aktivneAdrese = new ArrayList<>();
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            boolean uPrognozaAdrese = false;
            for (String adresa : prognozaAdrese) {
                if (adresa.equals(a.getAdresa())) {
                    uPrognozaAdrese = true;
                }
            }
            if (uPrognozaAdrese == false) {
                aktivneAdrese.add(a.getAdresa());
            }
        }
}
    /**
     * Metoda ubacije adresu u bazu podataka
     *
     * @return
     */
    public Object upisiAdresu() {
        spremiAkciju("student1", 3, "url", "localhost", 0);
        Adrese adresa = new Adrese();
        adresa.setAdresa(novaAdresa);
        Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
        adresa.setLatitude(l.getLatitude());
        adresa.setLongitude(l.getLongitude());
        adreseFacade.create(adresa);
        return null;
    }

    /**
     * Metoda prolazi kroz sve adrese u bazi i u slucaju da pronađe adresu ona
     * ju brise i ubacuje novu adresu u bazu
     *
     * @return
     */
    public Object azurirajAdresu() {
        spremiAkciju("student1", 4, "url", "localhost", 0);
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            if (a.getAdresa().equals(originalnaAdresa)) {
                adreseFacade.remove(a);
            }
        }
        Adrese adresa = new Adrese();
        adresa.setAdresa(azuriranaAdresa);
        Lokacija l = meteoAdresniKlijent.dajLokaciju(azuriranaAdresa);
        adresa.setLatitude(l.getLatitude());
        adresa.setLongitude(l.getLongitude());
        adreseFacade.create(adresa);
        prikaziAzuriranje = false;
        return null;
    }

    /**
     * prikazujem obrazac za azuriranje te spremam odabranu adresu u orginalnu
     * adresu te istu spremam u azuriranaAdresa kako bi odmah pisala adresa u
     * inputtextu
     *
     * @return
     */
    public Object otvoriAzuriranjeAdrese() {
        prikaziAzuriranje = true;
        originalnaAdresa = odabranaAdresaZaDodati;
        azuriranaAdresa = odabranaAdresaZaDodati;

}
    /**
     * otvaranje obrasca za prikazivanje prognoze ako je broj unesenih dana
     * između 1 i 16 ukoliko broj nije valjan ispisujem poruku greške
     *
     * @return
     */
    public Object otvoriPrikazivanjePrognoze() {
        if (brojDana > 0 && brojDana < 17) {
            prikaziError = false;
            DohvatiPrognoze();
            prikaziPrognoze = true;
        } else {
            prikaziError = true;
            poruka = "Broj dana mora biti između 1 i 16";
        }

}
    
    public boolean isPrikaziAzuriranje() {

}

}
public class PregledDnevnika implements Serializable {
    @EJB
    private DnevnikFacade dnevnikFacade;

    public List<Dnevnik> dp;
    public List<Dnevnik> pdp = null;
    private String korisnik;
    private String ipadresa;
    private String trajanjeOd;
    private String trajanjeDo;
    private String trajanje;
    private String status;
    private boolean prikaziFiltriranje = false;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {

    }

    /**
     * Getter za podatke iz dnevnika Ako je lista podataka pdp prazna samo
     * dohvaća sve vrijednosti iz baze, a ako lista pdp nije prazna onda vraća
     * listu pdp
     *
     * @return
     */
    public List<Dnevnik> getDp() {
        List<Dnevnik> dnevnik = dnevnikFacade.findAll();

        if (pdp == null) {
            dp = dnevnik;
            return dp;
        } else {
            return pdp;
        }

}
    /**
     * Metoda za filtriranje podataka na osnovi svih mogucih inputTextova. Za
     * svaki input privjerava ako se ta vrijednost trenutno nalazi u pomocnoj
     * listi te ako se nalazi sprema ju u drugu pomocnu listu i salje dalje
     */
    public void filtriranje() {
        pdp = new ArrayList<>();
        //Morao sam koristiti puno pomoćnih listi kako bih izbjegao ConcurrentModificationException
        List<Dnevnik> pomocnaLista = new ArrayList<>();
        List<Dnevnik> pomocnaLista2 = new ArrayList<>();
        List<Dnevnik> pomocnaLista3 = new ArrayList<>();
        List<Dnevnik> pomocnaLista4 = new ArrayList<>();
        List<Dnevnik> pomocnaLista5 = new ArrayList<>();
        if (korisnik.equals("")) {
            pdp = dp;
        } else {
            for (Dnevnik d : dp) {
                if (d.getKorisnik().equals(korisnik)) {
                    pdp.add(d);
                }
            }
        }
        if (ipadresa.equals("")) {
            pomocnaLista2 = pdp;
        } else {

            for (Dnevnik d : pdp) {
                if (d.getIpadresa().equals(ipadresa)) {
                    pomocnaLista.add(d);
                }
                pomocnaLista2 = pomocnaLista;
                pdp = pomocnaLista2;
            }
        }
        if (trajanje.equals("")) {
            pomocnaLista3 = pomocnaLista2;
        } else {
            for (Dnevnik d : pomocnaLista2) {
                if (d.getTrajanje() > Integer.parseInt(trajanje)) {
                    pomocnaLista5.add(d);
                }
                pomocnaLista3 = pomocnaLista5;
                pdp = pomocnaLista5;
            }
        }
        if (!status.equals("")) {
            for (Dnevnik d : pomocnaLista3) {
                if (d.getStatus() == Integer.parseInt(status)) {
                    pomocnaLista4.add(d);
                }
                pdp = pomocnaLista4;
            }
        }
    }
}
