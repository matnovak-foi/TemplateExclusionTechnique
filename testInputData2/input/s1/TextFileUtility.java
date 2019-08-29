/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.ejb.eb;

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
        return "org.foi.nwtis.student2.ejb.eb.Adrese[ idadresa=" + idadresa + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.ejb.eb;

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
        return "org.foi.nwtis.student2.ejb.eb.Dnevnik[ id=" + id + " ]";
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.ejb.sb;

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
package org.foi.nwtis.student2.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.foi.nwtis.student2.ejb.eb.Adrese;

/**
 *
 * @author NWTiS_1
 */
@Stateless
public class AdreseFacade extends AbstractFacade<Adrese> {
    @PersistenceContext(unitName = "student2_zadaca_4_1PU")
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
package org.foi.nwtis.student2.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student2.ejb.eb.Dnevnik;

/**
 *
 * @author NWTiS_1
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {
    @PersistenceContext(unitName = "student2_zadaca_4_1PU")
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
package org.foi.nwtis.student2.ejb.sb;

import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.student2.rest.klijenti.GMKlijent;
import org.foi.nwtis.student2.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student2.web.podaci.Lokacija;
import org.foi.nwtis.student2.web.podaci.MeteoPodaci;
import org.foi.nwtis.student2.web.podaci.MeteoPrognoza;

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

    public ArrayList<MeteoPrognoza> dajMeteoPrognoze(String adresa, int brojDana) {
        Lokacija l = dajLokaciju(adresa);
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
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student2.rest.klijenti;

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
import org.foi.nwtis.student2.web.podaci.Lokacija;

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

package org.foi.nwtis.student2.rest.klijenti;

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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.student2.web.podaci.MeteoPodaci;
import org.foi.nwtis.student2.web.podaci.MeteoPrognoza;

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

        return null;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student2.rest.klijenti;

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

package org.foi.nwtis.student2.web.podaci;

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
package org.foi.nwtis.student2.web.podaci;

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
package org.foi.nwtis.student2.web.podaci;

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
package org.foi.nwtis.student2.web.podaci;

import java.util.Date;

/**
 *
 * @author teacher2
 */
public class MeteoPrognoza {

    private String adresa;
    private int dan;
    private MeteoPodaci prognoza;
    private Date datum;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(String adresa, int dan, MeteoPodaci prognoza) {
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

    public MeteoPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPodaci prognoza) {
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
package org.foi.nwtis.student2.web.zrna;

import java.net.InetAddress;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.student2.ejb.eb.Dnevnik;
import org.foi.nwtis.student2.ejb.sb.DnevnikFacade;

/**
 * Abstraktna klasa koju implemetiraju drugi beanovi, sadrži metodu za 
 * zapisivanje u dnevnik kako se nebi duplao kod
 * @author Sname4
 */
public abstract class BaseBean {

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
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.student2.ejb.eb.Adrese;
import org.foi.nwtis.student2.ejb.sb.AdreseFacade;
import org.foi.nwtis.student2.ejb.sb.MeteoAdresniKlijent;
import org.foi.nwtis.student2.web.podaci.Lokacija;
import org.foi.nwtis.student2.web.podaci.MeteoPrognoza;

/**
 * ManagedBean klasa koja se koristi za pregledAdresa.xhtml
 */
@ManagedBean
@SessionScoped
public class OdabirAdresaPrognoza extends BaseBean implements Serializable {

    private static final int STATUS_OK = 200;
    private static final int STATUS_ERROR = 400;

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
    private List<String> prognozaAdrese;
    private List<MeteoPrognoza> meteoPrognoza;
    private int brojDana;

    private boolean prikaziAzuriranje = false;
    private boolean prikaziPrognoze = false;

    /**
     * Creates a new instance of OdabirAdresaPrognoza
     */
    public OdabirAdresaPrognoza() {
    }

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
        return null;
    }

    /**
     * Otvaranje forme za ažuriranje adrese
     *
     * @return
     */
    public Object otvoriAzuriranjeAdrese() {
        vrijemePozivanjaMetode = System.currentTimeMillis();
        prikaziAzuriranje = true;
        originalnaAdresa = odabranaAdresaZaDodati;
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

    public List<String> getAktivneAdrese() {
        aktivneAdrese = new ArrayList<>();
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

    public void setAktivneAdrese(List<String> aktivneAdrese) {
        this.aktivneAdrese = aktivneAdrese;
    }

    public List<String> getPrognozaAdrese() {
        return prognozaAdrese;
    }

    public void setPrognozaAdrese(List<String> prognozaAdrese) {
        this.prognozaAdrese = prognozaAdrese;
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

    public String getOriginalnaAdresa() {
        return originalnaAdresa;
    }

    public void setOriginalnaAdresa(String originalnaAdresa) {
        this.originalnaAdresa = originalnaAdresa;
    }

    public List<MeteoPrognoza> getMeteoPrognoza() {
        return meteoPrognoza;
    }

    public void setMeteoPrognoza(List<MeteoPrognoza> meteoPrognoza) {
        this.meteoPrognoza = meteoPrognoza;
    }

    public int getBrojDana() {
        return brojDana;
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

    /**
     * Pretraživanje tablice sa dnevnikom na temelju upisane vrijednosti
     */
    private void pretraziDnevnik() {
        if (zapisiPretrazivanje) {
            vrijemePozivanjaMetode = System.currentTimeMillis();
        }
        if (rijecZaPretrazivanje == null || rijecZaPretrazivanje.isEmpty()) {
            return;
        }
        dnevnik.removeIf(s -> !s.getKorisnik().contains(rijecZaPretrazivanje));

        if (zapisiPretrazivanje) {
            zapisiUDnevnik(vrijemePozivanjaMetode, 200);
            zapisiPretrazivanje = false;
        }
    }

    /**
     * Filtriranje tablice sa dnevnikom na temelju upisanih parametara
     */
    private void filtrirajDnevnik() {
        if (zapisiFiltriranje) {
            vrijemePozivanjaMetode = System.currentTimeMillis();
        }

        // provjeri jeli upisana IP adresa
        if (!(ipAdresa == null || ipAdresa.isEmpty())) {
            dnevnik.removeIf(s -> !s.getIpadresa().contains(ipAdresa));
        }

        // provjeri jeli upisano trajanje
        if (trajanje != 0) {
            dnevnik.removeIf(i -> i.getTrajanje() != trajanje);
        }

        // provjeri jeli upisan status
        if (status != 0) {
            dnevnik.removeIf(i -> i.getStatus() != status);
        }

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

    /**
     * Uključivanje zapisivanja pretrazivanja u dnevnik, služi kako se nebi svaki put pisalo
     * prilikom osvježavanja stranice
     */
    public void ukljuciZapisivanjePretrazivanja() {
        zapisiPretrazivanje = true;
    }

    /**
     * Uključivanje zapisivanja filtriranja u dnevnik, služi kako se nebi svaki put pisalo
     * prilikom osvježavanja stranice
     */
    public void ukljuciZapisivanjeFiltriranja() {
        zapisiFiltriranje = true;
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

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }

    public String getRijecZaPretrazivanje() {
        return rijecZaPretrazivanje;
    }

    public void setRijecZaPretrazivanje(String rijecZaPretrazivanje) {
        this.rijecZaPretrazivanje = rijecZaPretrazivanje;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
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

    public String getOdDatuma() {
        return odDatuma;
    }

    public void setOdDatuma(String odDatuma) {
        this.odDatuma = odDatuma;
    }

    public String getDoDatuma() {
        return doDatuma;
    }

    public void setDoDatuma(String doDatuma) {
        this.doDatuma = doDatuma;
    }

    public boolean isZapisiPretrazivanje() {
        return zapisiPretrazivanje;
    }

    public void setZapisiPretrazivanje(boolean zapisi) {
        this.zapisiPretrazivanje = zapisi;
    }

    public boolean isZapisiFiltriranje() {
        return zapisiFiltriranje;
    }

    public void setZapisiFiltriranje(boolean zapisiFiltriranje) {
        this.zapisiFiltriranje = zapisiFiltriranje;
    }

}