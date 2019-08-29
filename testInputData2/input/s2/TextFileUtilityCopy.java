/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NWTiS_1
 */
@Entity
@Table(name = "ADRESE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adrese.findAll", query = "SELECT a FROM Adrese a"),
    @NamedQuery(name = "Adrese.findByIdadresa", query = "SELECT a FROM Adrese a WHERE a.idadresa = :idadresa"),
    @NamedQuery(name = "Adrese.findByAdresa", query = "SELECT a FROM Adrese a WHERE a.adresa = :adresa"),
    @NamedQuery(name = "Adrese.findByLatitude", query = "SELECT a FROM Adrese a WHERE a.latitude = :latitude"),
    @NamedQuery(name = "Adrese.findByLongitude", query = "SELECT a FROM Adrese a WHERE a.longitude = :longitude")})
public class Adrese implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDADRESA")
    private Integer idadresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ADRESA")
    private String adresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "LATITUDE")
    private String latitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "LONGITUDE")
    private String longitude;

    public Adrese() {
    }

    public Adrese(Integer idadresa) {
        this.idadresa = idadresa;
    }

    public Adrese(Integer idadresa, String adresa, String latitude, String longitude) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getIdadresa() {
        return idadresa;
    }

    public void setIdadresa(Integer idadresa) {
        this.idadresa = idadresa;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idadresa != null ? idadresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adrese)) {
            return false;
        }
        Adrese other = (Adrese) object;
        if ((this.idadresa == null && other.idadresa != null) || (this.idadresa != null && !this.idadresa.equals(other.idadresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student1.ejb.eb.Adrese[ idadresa=" + idadresa + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author NWTiS_1
 */
@Entity
@Table(name = "DNEVNIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dnevnik.findAll", query = "SELECT d FROM Dnevnik d"),
    @NamedQuery(name = "Dnevnik.findById", query = "SELECT d FROM Dnevnik d WHERE d.id = :id"),
    @NamedQuery(name = "Dnevnik.findByKorisnik", query = "SELECT d FROM Dnevnik d WHERE d.korisnik = :korisnik"),
    @NamedQuery(name = "Dnevnik.findByUrl", query = "SELECT d FROM Dnevnik d WHERE d.url = :url"),
    @NamedQuery(name = "Dnevnik.findByIpadresa", query = "SELECT d FROM Dnevnik d WHERE d.ipadresa = :ipadresa"),
    @NamedQuery(name = "Dnevnik.findByVrijeme", query = "SELECT d FROM Dnevnik d WHERE d.vrijeme = :vrijeme"),
    @NamedQuery(name = "Dnevnik.findByTrajanje", query = "SELECT d FROM Dnevnik d WHERE d.trajanje = :trajanje"),
    @NamedQuery(name = "Dnevnik.findByStatus", query = "SELECT d FROM Dnevnik d WHERE d.status = :status")})
public class Dnevnik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "KORISNIK")
    private String korisnik;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "URL")
    private String url;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "IPADRESA")
    private String ipadresa;
    @Column(name = "VRIJEME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRAJANJE")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;

    public Dnevnik() {
    }

    public Dnevnik(Integer id) {
        this.id = id;
    }

    public Dnevnik(Integer id, String korisnik, String url, String ipadresa, int trajanje, int status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.ipadresa = ipadresa;
        this.trajanje = trajanje;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dnevnik)) {
            return false;
        }
        Dnevnik other = (Dnevnik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student1.ejb.eb.Dnevnik[ id=" + id + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.sb;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author NWTiS_1
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.foi.nwtis.student1.ejb.eb.Adrese;

/**
 *
 * @author NWTiS_1
 */
@Stateless
public class AdreseFacade extends AbstractFacade<Adrese> {
    @PersistenceContext(unitName = "student1_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdreseFacade() {
        super(Adrese.class);
    }
    
    public List<Adrese> findByAdresa(String zaAdresu) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Adrese> adresa = cq.from(Adrese.class);
        Expression<String> premaAdresi = adresa.get("adresa");
        cq.where(cb.and(cb.equal(premaAdresi, zaAdresu)));
        return getEntityManager().createQuery(cq).getResultList();    
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student1.ejb.eb.Dnevnik;

/**
 *
 * @author NWTiS_1
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {
    @PersistenceContext(unitName = "student1_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DnevnikFacade() {
        super(Dnevnik.class);
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.student1.rest.klijenti.GMKlijent;
import org.foi.nwtis.student1.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student1.web.podaci.Lokacija;
import org.foi.nwtis.student1.web.podaci.MeteoPodaci;
import org.foi.nwtis.student1.web.podaci.MeteoPrognoza;

/**
 *
 * @author NWTiS_1
 */
@Stateless
@LocalBean
public class MeteoAdresniKlijent {

    private String apiKey;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    public MeteoPodaci dajVazeceMeteoPodatke(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);

        OWMKlijent owmk = new OWMKlijent(this.apiKey);

        MeteoPodaci mp = owmk.getRealTimeWeather(l.getLatitude(),
                l.getLongitude());     
        
        return mp;
    }

    public MeteoPrognoza[] dajMeteoPrognoze(String adresa, int brojDana) {
        Lokacija l = dajLokaciju(adresa);
        
        
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
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.rest.klijenti;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.student1.web.podaci.Lokacija;

/**
 *
 * @author nwtis_1
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;

    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JSONObject jo = new JSONObject(odgovor);
            JSONObject obj = jo.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            Lokacija loc = new Lokacija(obj.getString("lat"), obj.getString("lng"));

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student1.rest.klijenti;

/**
 *
 * @author teacher2
 */
public class GMRESTHelper {
    private static final String GM_BASE_URI = "http://maps.google.com/";    

    public GMRESTHelper() {
    }

    public static String getGM_BASE_URI() {
        return GM_BASE_URI;
    }
        
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.rest.klijenti;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.student1.web.podaci.MeteoPodaci;
import org.foi.nwtis.student1.web.podaci.MeteoPrognoza;
import org.foi.nwtis.student1.web.podaci.MeteoPrognozaPodaci;

/**
 *
 * @author nwtis_1
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JSONObject jo = new JSONObject(odgovor);
            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJSONObject("sys").getLong("sunrise")));
            mp.setSunSet(new Date(jo.getJSONObject("sys").getLong("sunset")));

            mp.setTemperatureValue(Float.parseFloat(jo.getJSONObject("main").getString("temp")));
            mp.setTemperatureMin(Float.parseFloat(jo.getJSONObject("main").getString("temp_min")));
            mp.setTemperatureMax(Float.parseFloat(jo.getJSONObject("main").getString("temp_max")));
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(Float.parseFloat(jo.getJSONObject("main").getString("pressure")));
            mp.setHumidityUnit("%");

            mp.setPressureValue(Float.parseFloat(jo.getJSONObject("main").getString("humidity")));
            mp.setHumidityUnit("hPa");

            mp.setWindSpeedValue(Float.parseFloat(jo.getJSONObject("wind").getString("speed")));
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(Float.parseFloat(jo.getJSONObject("wind").getString("deg")));
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJSONObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJSONArray("weather").getJSONObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJSONArray("weather").getJSONObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getLong("dt")));

            return mp;

        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mp;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student1.rest.klijenti;

/**
 *
 * @author teacher2
 */
public class OWMRESTHelper {
    private static final String OWM_BASE_URI = "http://api.openweathermap.org/data/2.5/";    
    private String apiKey;


    public OWMRESTHelper(String apiKey) {
        this.apiKey = apiKey;
    }

    public static String getOWM_BASE_URI() {
        return OWM_BASE_URI;
    }

    public static String getOWM_Current_Path() {
        return "weather";
    }
        
    public static String getOWM_Forecast_Path() {
        return "forecast";
    }
        
    public static String getOWM_ForecastDaily_Path() {
        return "forecast/daily";
    }
        
    public static String getOWM_StationsNear_Path() {
        return "station/find";
    }
        
    public static String getOWM_Station_Path() {
        return "station";
    }
        
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student1.web.podaci;

/**
 *
 * @author teacher2
 */
public class Adresa {
    private long idadresa;
    private String adresa;
    private Lokacija geoloc;

    public Adresa() {
    }

    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public long getIdadresa() {
        return idadresa;
    }

    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.podaci;

/**
 *
 * @author teacher2
 */
public class Lokacija {

    private String latitude;
    private String longitude;

    public Lokacija() {
    }

    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.podaci;

import java.util.Date;

/**
 *
 * @author teacher2
 */
public class MeteoPodaci {

    private Date sunRise;
    private Date sunSet;

    private Float temperatureValue;
    private Float temperatureMin;
    private Float temperatureMax;
    private String temperatureUnit;

    private Float humidityValue;
    private String humidityUnit;

    private Float pressureValue;
    private String pressureUnit;

    private Float windSpeedValue;
    private String windSpeedName;
    private Float windDirectionValue;
    private String windDirectionCode;
    private String windDirectionName;

    private int cloudsValue;
    private String cloudsName;

    private String visibility;

    private Float precipitationValue;
    private String precipitationMode;
    private String precipitationUnit;

    private int weatherNumber;
    private String weatherValue;
    private String weatherIcon;
    private Date lastUpdate;

    public MeteoPodaci() {
    }

    public MeteoPodaci(Date sunRise, Date sunSet, Float temperatureValue, Float temperatureMin, Float temperatureMax, String temperatureUnit, Float humidityValue, String humidityUnit, Float pressureValue, String pressureUnit, Float windSpeedValue, String windSpeedName, Float windDirectionValue, String windDirectionCode, String windDirectionName, int cloudsValue, String cloudsName, String visibility, Float precipitationValue, String precipitationMode, String precipitationUnit, int weatherNumber, String weatherValue, String weatherIcon, Date lastUpdate) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.temperatureValue = temperatureValue;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.temperatureUnit = temperatureUnit;
        this.humidityValue = humidityValue;
        this.humidityUnit = humidityUnit;
        this.pressureValue = pressureValue;
        this.pressureUnit = pressureUnit;
        this.windSpeedValue = windSpeedValue;
        this.windSpeedName = windSpeedName;
        this.windDirectionValue = windDirectionValue;
        this.windDirectionCode = windDirectionCode;
        this.windDirectionName = windDirectionName;
        this.cloudsValue = cloudsValue;
        this.cloudsName = cloudsName;
        this.visibility = visibility;
        this.precipitationValue = precipitationValue;
        this.precipitationMode = precipitationMode;
        this.precipitationUnit = precipitationUnit;
        this.weatherNumber = weatherNumber;
        this.weatherValue = weatherValue;
        this.weatherIcon = weatherIcon;
        this.lastUpdate = lastUpdate;
    }

    public Date getSunRise() {
        return sunRise;
    }

    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

    public Date getSunSet() {
        return sunSet;
    }

    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

    public Float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(Float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public Float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public Float getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(Float humidityValue) {
        this.humidityValue = humidityValue;
    }

    public String getHumidityUnit() {
        return humidityUnit;
    }

    public void setHumidityUnit(String humidityUnit) {
        this.humidityUnit = humidityUnit;
    }

    public Float getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(Float pressureValue) {
        this.pressureValue = pressureValue;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

    public void setWindSpeedValue(Float windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

    public String getWindSpeedName() {
        return windSpeedName;
    }

    public void setWindSpeedName(String windSpeedName) {
        this.windSpeedName = windSpeedName;
    }

    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

    public void setWindDirectionValue(Float windDirectionValue) {
        this.windDirectionValue = windDirectionValue;
    }

    public String getWindDirectionCode() {
        return windDirectionCode;
    }

    public void setWindDirectionCode(String windDirectionCode) {
        this.windDirectionCode = windDirectionCode;
    }

    public String getWindDirectionName() {
        return windDirectionName;
    }

    public void setWindDirectionName(String windDirectionName) {
        this.windDirectionName = windDirectionName;
    }

    public int getCloudsValue() {
        return cloudsValue;
    }

    public void setCloudsValue(int cloudsValue) {
        this.cloudsValue = cloudsValue;
    }

    public String getCloudsName() {
        return cloudsName;
    }

    public void setCloudsName(String cloudsName) {
        this.cloudsName = cloudsName;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Float getPrecipitationValue() {
        return precipitationValue;
    }

    public void setPrecipitationValue(Float precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

    public String getPrecipitationMode() {
        return precipitationMode;
    }

    public void setPrecipitationMode(String precipitationMode) {
        this.precipitationMode = precipitationMode;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    public int getWeatherNumber() {
        return weatherNumber;
    }

    public void setWeatherNumber(int weatherNumber) {
        this.weatherNumber = weatherNumber;
    }

    public String getWeatherValue() {
        return weatherValue;
    }

    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.podaci;

import java.util.Date;

/**
 *
 * @author teacher2
 */
public class MeteoPrognoza {

    private String adresa;
    private int dan;
    private Date datum;
    private MeteoPrognozaPodaci prognoza;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(String adresa, int dan, MeteoPrognozaPodaci prognoza) {
        this.adresa = adresa;
        this.dan = dan;
        this.prognoza = prognoza;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public MeteoPrognozaPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPrognozaPodaci prognoza) {
        this.prognoza = prognoza;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }


}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.podaci;

/**
 *
 * @author Sname3
 */
public class MeteoPrognozaPodaci {

    private float temp;
    private float pressure;
    private float humidity;
    private String weather;
    private float clouds;
    private float windSpeed;
    private float windDeg;
    private String dt;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public float getClouds() {
        return clouds;
    }

    public void setClouds(float clouds) {
        this.clouds = clouds;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(float windDeg) {
        this.windDeg = windDeg;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
    

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.zrna;

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

/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@SessionScoped
public class OdabirAdresaPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    
    @EJB
    private MeteoAdresniKlijent meteoAdresniKlijent;
    @EJB
    private AdreseFacade adreseFacade;
    
    private String novaAdresa;
    private String odabranaAdresaZaDodati;
    private String odabranaAdresaZaMaknuti;
    private String azuriranaAdresa;
    private String originalnaAdresa;
    private List<String> aktivneAdrese;
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
     * Creates a new instance of OdabirAdresaPrognoza
     */
    public OdabirAdresaPrognoza() {
    }
    
    public String getNovaAdresa() {
        return novaAdresa;
    }
    
    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresa = novaAdresa;
    }
    
    public String getOdabranaAdresaZaDodati() {
        return odabranaAdresaZaDodati;
    }
    
    public void setOdabranaAdresaZaDodati(String odabranaAdresaZaDodati) {
        this.odabranaAdresaZaDodati = odabranaAdresaZaDodati;
    }
    
    public String getOdabranaAdresaZaMaknuti() {
        return odabranaAdresaZaMaknuti;
    }
    
    public void setOdabranaAdresaZaMaknuti(String odabranaAdresaZaMaknuti) {
        this.odabranaAdresaZaMaknuti = odabranaAdresaZaMaknuti;
    }
    
    public String getAzuriranaAdresa() {
        return azuriranaAdresa;
    }
    
    public void setAzuriranaAdresa(String azuriranaAdresa) {
        this.azuriranaAdresa = azuriranaAdresa;
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
        return aktivneAdrese;
    }
    
    public void setAktivneAdrese(List<String> aktivneAdrese) {
        this.aktivneAdrese = aktivneAdrese;
    }
    
    public List<String> getPrognozaAdrese() {
        return prognozaAdrese;
    }
    
    public void setPrognozaAdrese(List<String> prognozaAdrese) {
        this.prognozaAdrese = prognozaAdrese;
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
        return null;
    }

    /**
     * zatvaranje obrasca za ažuriranje
     *
     * @return
     */
    public Object zatvoriPrognozu() {
        prikaziPrognoze = false;
        return null;
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
        
        return null;
    }
    
    public boolean isPrikaziAzuriranje() {
        return prikaziAzuriranje;
    }
    
    public void setPrikaziAzuriranje(boolean prikaziAzuriranje) {
        this.prikaziAzuriranje = prikaziAzuriranje;
    }
    
    public boolean isPrikaziPrognoze() {
        return prikaziPrognoze;
    }
    
    public void setPrikaziPrognoze(boolean prikaziPrognoze) {
        this.prikaziPrognoze = prikaziPrognoze;
    }
    
    public int getBrojDana() {
        return brojDana;
    }
    
    public void setBrojDana(int brojDana) {
        this.brojDana = brojDana;
    }
    
    public MeteoPrognoza[] getMeteoPrognoza() {
        return meteoPrognoza;
    }
    
    public void setMeteoPrognoza(MeteoPrognoza[] meteoPrognoza) {
        this.meteoPrognoza = meteoPrognoza;
    }
    
    public List<MeteoPrognoza> getMpp() {
        return mpp;
    }
    
    public void setMpp(List<MeteoPrognoza> mpp) {
        this.mpp = mpp;
    }
    
    public String getPoruka() {
        return poruka;
    }
    
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    
    public boolean isPrikaziError() {
        return prikaziError;
    }
    
    public void setPrikaziError(boolean prikaziError) {
        this.prikaziError = prikaziError;
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student1.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.student1.ejb.eb.Dnevnik;
import org.foi.nwtis.student1.ejb.sb.DnevnikFacade;

/**
 *
 * @author Sname3
 */
@ManagedBean
@SessionScoped
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

    public void setDp(List<Dnevnik> dp) {
        this.dp = dp;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * metoda za prikazivanje obrasca za filtriranje
     *
     * @return
     */
    public Object otvoriFiltriranje() {
        prikaziFiltriranje = true;
        return null;
    }

    /**
     * metoda za zatvaranja obrasca za filtriranje koja također resetira listu
     * pdp kako bi se opet vidjeli svi podaci iz baze
     *
     * @return
     */
    public Object zatvoriFiltriranje() {
        prikaziFiltriranje = false;
        pdp = null;
        return null;
    }

    public boolean isPrikaziFiltriranje() {
        return prikaziFiltriranje;
    }

    public void setPrikaziFiltriranje(boolean prikaziFiltriranje) {
        this.prikaziFiltriranje = prikaziFiltriranje;
    }

    public String getTrajanjeDo() {
        return trajanjeDo;
    }

    public void setTrajanjeDo(String trajanjeDo) {
        this.trajanjeDo = trajanjeDo;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getTrajanjeOd() {
        return trajanjeOd;
    }

    public void setTrajanjeOd(String trajanjeOd) {
        this.trajanjeOd = trajanjeOd;
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