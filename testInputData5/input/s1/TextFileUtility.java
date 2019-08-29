package org.foi.nwtis.student7.bazaPodataka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * </p>Klasa <b>BazaPodataka</b> služi kao omotač za spajanje i pristup na
 * bazu podataka. Tokom instanciranja objekta ono se spaja na vanjsku
 * bazu podataka na temelju proslijeđenih informacija u obliku objekta
 * klase <b>BazaPodatakaInfo</b>.</p>
 * <p>Nudi samo dvije metode za korištenje baze podataka:</p>
 * 
 * <ul>
 * <li><b>select(String)</b> - služi za upite tipa SELECT,</li>
 * <li><b>update(String)</b> - služi za upite tipa UPDATE, INSERT, DELETE, itd.</li>
 * </ul>
 * 
 * <p>Ova klasa nasljeđuje sučelje <b>AutoCloseable</b> te je potrebno instancu klase
 * <b>BazaPodataka</b> zatvoriti nakon korištenja pozivom metode <b>close()</b>.</p>
 * @author Sname2
 */
public class BazaPodataka implements AutoCloseable
{
	private Connection conn = null;
	private Statement stmt = null;
	
	private boolean zatvoreno = false;
	
	/**
	 * Konstruktor klase <b>BazaPodataka</b>. Na temelju informacija
	 * proslijđenih preko parametra, koji je tipa <b>BazaPodatakaInfo</b>, 
	 * odmah se spaja na traženu bazu podataka.
	 * @param info informacije za spajanje na bazu podataka
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	public BazaPodataka(BazaPodatakaInfo info) throws ClassNotFoundException, SQLException
	{
		Class.forName(info.getDriver());
		conn = DriverManager.getConnection(info.getServer() + info.getBazaPodataka(), info.getKorisnik(), info.getLozinka());
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	/**
	 * Metoda koja služi za izvršavanje upita tipa SELECT.
	 * @param sql SQL upit
	 * @return redovi koje vraća upit
	 * @throws SQLException 
	 */
	public ResultSet select(String sql) throws SQLException
	{
		if (zatvoreno)
			throw new SQLException("Veza prema bazi podataka je zatvorena");
		
		return stmt.executeQuery(sql);
	}
	
	/**
	 * Metoda koja služi za izvršavanje upita tipa UPDATE, INSERT, DELETE, itd.
	 * @param sql SQL upit
	 * @return broj redova na koje je upit utjecao
	 * @throws SQLException 
	 */
	public int update(String sql) throws SQLException
	{
		if (zatvoreno)
			throw new SQLException("Veza prema bazi podataka je zatvorena");
		
		return stmt.executeUpdate(sql);
	}
	
	/**
	 * Metoda za zatvaranje veze s bazom podataka.
	 * @throws SQLException 
	 */
	@Override
	public void close() throws SQLException
	{
		if (stmt != null) stmt.close();
		if (conn != null) conn.close();
		zatvoreno = true;
	}
	
}package org.foi.nwtis.student7.bazaPodataka;

/**
 * <p>Klasa <b>BazaPodatakaInfo</b> služi za definiranje postavki
 * potrebnih za spajanje na bazu podataka. Postavke su sljedeće:</p>
 * <ul>
 *	<li><b>driver</b> - JDBC driver koji omogućava komuniciranje s određenom bazom podataka,</li>
 *	<li><b>server</b> - adresa poslužitelja na kojem se nalazi baza podataka,</li>
 *	<li><b>bazaPodataka</b> - baza podataka kojoj se želi pristupiti</li>
 *	<li><b>korisnik</b> - korisničko ime za pristup bazi podataka,</li>
 *	<li><b>lozinka</b> - lozinka za pristup bazi podataka,</li>
 * </ul>
 * @author Sname2
 */
public class BazaPodatakaInfo
{
	private String driver;
	private String server;
	private String bazaPodataka;
	private String korisnik;
	private String lozinka;
	
	/**
	 * Konstruktor klase <b>BazaPodatakaInfo</b>.
	 */
	public BazaPodatakaInfo() {}

	/**
	 * Konstruktor klase <b>BazaPodatakaInfo</b>.
	 * @param driver - JDBC driver za pristup određenoj bazi podataka
	 * @param server - adresa poslužitelja na kojoj se nalazi baza podataka
	 * @param bazaPodataka - naziv baze podataka kojoj se želi pristupiti
	 * @param korisnik - korisničko ime za pristup bazi podataka
	 * @param lozinka - lozinka za pristup bazi podataka
	 */
	public BazaPodatakaInfo(String driver, String server, String bazaPodataka, String korisnik, String lozinka)
	{
		this.driver = driver;
		this.server = server;
		this.bazaPodataka = bazaPodataka;
		this.korisnik = korisnik;
		this.lozinka = lozinka;
	}

	/**
	 * Getter metoda koja vraća JDBC driver.
	 * @return JDBC driver
	 */
	public String getDriver()
	{
		return driver;
	}

	/**
	 * Getter metoda koja vraća adresu poslužitelja na kojoj se nalazi
	 * bazi podataka.
	 * @return adresa poslužitelja
	 */
	public String getServer()
	{
		return server;
	}
	
	/**
	 * Getter metoda koja vraća naziv baze podataka.
	 * @return naziv baze podataka.
	 */
	public String getBazaPodataka()
	{
		return bazaPodataka;
	}

	/**
	 * Getter metoda koja vraća korisničko ime za pristup bazi podataka.
	 * @return koriničko ime
	 */
	public String getKorisnik()
	{
		return korisnik;
	}

	/**
	 * Getter metoda koja vraća lozinku za pristup bazi podataka.
	 * @return lozinka
	 */
	public String getLozinka()
	{
		return lozinka;
	}

	/**
	 * Setter metoda koja postavlja JDBC driver.
	 * @param driver JDBC driver
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	/**
	 * Setter metoda koja postavlja adresu poslužitelja na kojoj
	 * se nalazi baza podataka.
	 * @param server adresa poslužitelja
	 */
	public void setServer(String server)
	{
		this.server = server;
	}
	
	/**
	 * Setter metoda koja postavlja naziv baze podataka kojoj se
	 * želi pristupiti na serveru.
	 * @param bazaPodataka naziv baze podataka
	 */
	public void setBazaPodataka(String bazaPodataka)
	{
		this.bazaPodataka = bazaPodataka;
	}

	/**
	 * Setter metoda koja postavlja korisničko ime za pristup
	 * bazi podataka na poslužitelju.
	 * @param korisnik korisničko ime
	 */
	public void setKorisnik(String korisnik)
	{
		this.korisnik = korisnik;
	}

	/**
	 * Setter metoda koja postavlja lozinku za pristup bazi
	 * podataka na poslužitelju.
	 * @param lozinka lozinka
	 */
	public void setLozinka(String lozinka)
	{
		this.lozinka = lozinka;
	}
}package org.foi.nwtis.student7.dretve;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;

/**
 * Implementacija sučelja <b>Runnable</b> koja, kada se pokrene u dretvi,
 * dohvaća adrese pohranjene u bazi podataka, dohvaća meterološke podatke
 * na temelju dohvaćenih adresa i te meterološke podatke pohranjuje u
 * bazu podataka.
 */
public class PreuzmiMeteoPodatke implements Runnable
{
	private final ProsirenaKonfiguracija konf;
	private final BazaPodataka bazaPodataka;
	private final int interval;
	
	private boolean raditi = true;
	
	private final ArrayList<Adresa> adrese = new ArrayList<>();
	private final HashMap<Adresa, MeteoPodaci> meteoPodaciPoAdresi = new HashMap<>();
	
	/**
	 * Konstruktor klase <b>PreuzmiMeteoPodatke</b>. Kao parametar prima instancu
	 * klase <b>ProsirenaKonfiguracija</b> kako bi se omogućio pristup određenim
	 * postavkama.
	 * @param konf Instanca klase <b>ProsirenaKonfiguracija</b> 
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	public PreuzmiMeteoPodatke(ProsirenaKonfiguracija konf) throws ClassNotFoundException, SQLException
	{
		this.konf = konf;
		this.interval = Integer.parseInt(konf.getDownloadInterval()) * 1000;
		
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());
		
		bazaPodataka = new BazaPodataka(info);
	}
	
	/**
	 * Metoda koja se izvodi u dretvi, nakon što se dretvi proslijedi objekt
	 * ove klase. Ova metoda dohvaća adrese iz baze podataka, dohvaća meterološke 
	 * podatke tih adresa preko REST Web servisa i pohranjuje dobivene podatke
	 * u bazu podataka.
	 */
	@Override
	public void run()
	{
		while (raditi) {
			try {
				Thread.sleep(interval);
				
				meteoPodaciPoAdresi.clear();
				adrese.clear();
				
				dohvatiAdreseIzBazePodataka();
				dohvatiMeteoPodatkePoAdresama();
				pohraniMeteoPodatke();
				
				System.out.println("Unjeti su meteorološki podaci.");
			}
			catch (InterruptedException ex) {
				raditi = false;
				System.out.println("PreuzmiMeteoPodatke završava s radom.");
			}
			catch (SQLException ex) {
				System.out.println("Greška u dodavanju meteoroloških podataka u bazu podataka: " + ex.getMessage());
			}
		}
		
		try {
			bazaPodataka.close();
		}
		catch (SQLException ex) {
			System.out.println("Baza podataka se ne da zatvoriti: " + ex.getMessage());
		}
	}

	/**
	 * Privatna metoda koja iz baze podataka dohvaća sve informacije o
	 * svim adresama.
	 */
	private void dohvatiAdreseIzBazePodataka()
	{
		try {
			String sql = "SELECT IDADRESA, ADRESA, LATITUDE, LONGITUDE FROM ADRESE";
			ResultSet rezultat = bazaPodataka.select(sql);
			while (rezultat.next()) {
				long id = rezultat.getLong(1);
				String adresa = rezultat.getString(2);
				String latitude = rezultat.getString(3);
				String longitude = rezultat.getString(4);
				
				Lokacija lokacija = new Lokacija(latitude, longitude);
				adrese.add(new Adresa(id, adresa, lokacija));
			}
		}
		catch (SQLException ex) {
			System.out.println("Greška u dohvaćanju adresa iz baze podataka: " + ex.getMessage());
		}
	}

	/**
	 * Privatna metoda koja, na temelju liste adresa, dohvaća meterološke podatke
	 * tih adresa na temelju poziva REST Web servisa.
	 */
	private void dohvatiMeteoPodatkePoAdresama()
	{
		OWMKlijent owm = new OWMKlijent(konf.getApiKey());
		for (Adresa adresa : adrese) {
			MeteoPodaci meteo = owm.getRealTimeWeather(adresa.getGeoloc().getLatitude(), adresa.getGeoloc().getLongitude());
			meteoPodaciPoAdresi.put(adresa, meteo);
		}
	}

	/**
	 * Privatna metoda koja pohranjuje dohvaćene meterološke podatke u bazu podataka.
	 * @throws SQLException 
	 */
	private void pohraniMeteoPodatke() throws SQLException
	{
		String sqlBase = "INSERT INTO METEO VALUES "
			+ "(DEFAULT, %d, '%s', '%s', '%s', '%s', '%s', %f, %f, %f, %f, %f, %f, %f, DEFAULT)";
		
		Set<Adresa> setAdresa = meteoPodaciPoAdresi.keySet();
		Iterator<Adresa> iterAdresa = setAdresa.iterator();
		while (iterAdresa.hasNext()) {
			Adresa adresa = iterAdresa.next();
			MeteoPodaci meteo = meteoPodaciPoAdresi.get(adresa);
			String sql = String.format(Locale.US, sqlBase, 
				adresa.getIdadresa(),
				adresa.getAdresa(),
				adresa.getGeoloc().getLatitude(), 
				adresa.getGeoloc().getLongitude(),
				meteo.getWeatherValue(), 
				meteo.getWeatherValue(),
				meteo.getTemperatureValue(),
				meteo.getTemperatureMin(), 
				meteo.getTemperatureMax(), 
				meteo.getHumidityValue(),
				meteo.getPressureValue(), 
				meteo.getWindSpeedValue(), 
				meteo.getWindDirectionValue()
			);
			System.out.println(sql);
			bazaPodataka.update(sql);
		}
	}
}package org.foi.nwtis.student7.konfiguracija;

import org.foi.nwtis.student7.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student7.konfiguracije.bp.BP_Konfiguracija;

/**
 * <p>Klasa <b>ProsirenaKonfiguracija</b> proširuje klasu <b>BP_Konfiguracija</b> i
 * implementira sučelje <b>ProsirenaKonfiguracijaSucelje</b> u kojem je navedeno
 * koje metode ova klasa treba implementirati.</p>
 * <p>Svrha ove klase je da proširi dostupne metode za čitanje postavki u nekoj
 * konfiguracijskj datoteci.</p>
 */
public class ProsirenaKonfiguracija extends BP_Konfiguracija implements ProsirenaKonfiguracijaSucelje
{
	/**
	 * Konstruktor klase <b>ProsirenaKonfiguracija</b>.
	 * @param datoteka konfiguracijska datoteka
	 * @throws NemaKonfiguracije 
	 */
	public ProsirenaKonfiguracija(String datoteka) throws NemaKonfiguracije
	{
		super(datoteka);
	}

	@Override
	public String getApiKey()
	{
		return konf.dajPostavku("api.key");
	}

	@Override
	public String getDownloadInterval()
	{
		return konf.dajPostavku("download.interval");
	}
}package org.foi.nwtis.student7.konfiguracija;

/**
 * Sučelje koje definira dodatne metoda kojima bi se proširilo
 * klasa za konfiguraciju.
 */
public interface ProsirenaKonfiguracijaSucelje
{
	/**
	 * Dohvaća API ključ iz konfiguracijske datoteke.
	 * @return API ključ
	 */
	public String getApiKey();
	
	/**
	 * Dohvaća interval preuzimanja meteroloških podataka.
	 * @return interval preuzimanja meteroloških podataka
	 */
	public String getDownloadInterval();
}package org.foi.nwtis.student7.rest.klijenti;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.web.podaci.Lokacija;

/**
 * Klasa <b>GMKlijent</b> je preuzeta s Moodle stranica kolegija "Napredne Web
 * tehnologije i servisi", a služi za poziv Google Maps REST servisa koji vraća
 * geografsku širinu i geografsku dužinu neke adrese.
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;

	/**
	 * Konstruktor klase <b>GMKlijent</b>.
	 */
    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

	/**
	 * Vraća geografsku lokaciju dane adrese u obliku objeta klase <b>Lokacija</b>.
	 * @param adresa adresa za koju se provjerava geografska lokacija
	 * @return geografska lokacija kao objekt klase <b>Lokacija</b>
	 */
    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();
                     
            JsonObject obj = jo.getJsonArray("results")
                    .getJsonObject(0)
                    .getJsonObject("geometry")
                    .getJsonObject("location");

            Lokacija loc = new Lokacija(obj.getJsonNumber("lat").toString(), obj.getJsonNumber("lng").toString());

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}package org.foi.nwtis.student7.rest.klijenti;

/**
 * Klasa <b>GMRESTHelper</b> je preuzeta s Moodle stranica kolegija "Napredne Web tehnologije
 * i servisi".  Ova klasa je pomoćna klasa za klasu <b>GMKlijent</b>. Vraća samo temeljni
 * URI za Google Maps stranicu.
 */
public class GMRESTHelper {
    private static final String GM_BASE_URI = "http://maps.google.com/";    

	/**
	 * Konstruktor klase <b>GMRESTHelper</b>.
	 */
    public GMRESTHelper() {
    }

	/**
	 * Javna statična metoda koja vraća temeljni URI za Google Maps stranicu.
	 * @return temeljni URI za Google Maps stranicu.
	 */
    public static String getGM_BASE_URI() {
        return GM_BASE_URI;
    }
        
}package org.foi.nwtis.student7.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;

/**
 * <p>Klasa <b>OWMKlijent</b> je preuzeta s Moodle stranica kolegija "Napredne Web tehnologije
 * i servisi". Ova klasa poziva REST servis koji vraća trenutačne meterološke podatke na danoj
 * geografskij širini i dužini. </p>
 * <p>Ova klasa je također ažurirana da preuzme vremensku prognozu na danoj geografskoj širini
 * i dužini.</p>
 */
public class OWMKlijent 
{
    String apiKey;
    OWMRESTHelper helper;
    Client client;

	/**
	 * Konstruktor klase <b>OWMKlijent</b>.
	 * @param apiKey API ključ za preuzimanje meteroloških podataka.
	 */
    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

	/**
	 * Na temelju dane geografske širine i dužine preuzima trenutačne meteorološke podatke.
	 * @param latitude geografska širina
	 * @param longitude geografska dužina
	 * @return meteorološki podaci
	 */
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
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue()*1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue()*1000));
            
            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");
            
            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");
            
            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");
            
            mp.setWindSpeedValue(new Double(jo.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");
            
            mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");
            
            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");
            
            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));
            
            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue()*1000));
            return mp;
            
        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
	
	/**
	 * Na temelju dane geografske širine i dužine preuzima vremensku prognozu te lokacije za
	 * sljedeća 5 dana u razmaku od 3 sata.
	 * @param latitude geografska širina
	 * @param longitude geografska dužina
	 * @return vremenska prognoza za sljedeća 5 dana, 3 sata razmaka
	 */
	public List<MeteoPodaci> getWeatherForecast(String latitude, String longitude)
	{
		WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
		
		String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
		try {
			List<MeteoPodaci> prognoza = new ArrayList<>();
			JsonObject jo = Json.createReader(new StringReader(odgovor)).readObject();
			
			JsonArray ja = jo.getJsonArray("list");
			for (int i = 0; i < ja.size(); i++) {
				MeteoPodaci mp = new MeteoPodaci();
				
				mp.setTemperatureValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
				mp.setTemperatureMin(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
				mp.setTemperatureMax(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
				mp.setPressureValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
				mp.setHumidityValue(new Double(ja.getJsonObject(i).getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
				mp.setWeatherValue(ja.getJsonObject(i).getJsonArray("weather").getJsonObject(0).getString("description"));
				mp.setWindSpeedValue(new Double(ja.getJsonObject(i).getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
				mp.setWindDirectionValue(new Double(ja.getJsonObject(i).getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
				mp.setLastUpdate(new Date(ja.getJsonObject(i).getJsonNumber("dt").longValue() * 1000L));
				
				prognoza.add(mp);
			}
			
			return prognoza;
		} catch (Exception ex) {
			Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}package org.foi.nwtis.student7.rest.klijenti;

/**
 * Klasa <b>OWMRESTHelper</b> je preuzeta s Moodle stranica kolegija "Napredne Web
 * tehnologije i servisi". Ova klasa je pomoćna klasa za klasu <b>OWMKlijent</b>.
 * Sadrži metode koji vraćaju osnovnu putanju to OpenWeatherMaps REST servisa i dodatne
 * putanje specifičnih servisa.
 */
public class OWMRESTHelper {
    private static final String OWM_BASE_URI = "http://api.openweathermap.org/data/2.5/";    
    private String apiKey;

	/**
	 * Konstruktor klase <b>OWMRESTHelper</b>.
	 * @param apiKey API ključ za OpenWeatherMaps REST servise.
	 */
    public OWMRESTHelper(String apiKey) {
        this.apiKey = apiKey;
    }

	/**
	 * Vraća osnovnu putanju do OpenWeatherMaps servisa.
	 * @return osnovna putanja do OpenWeatherMaps servisa
	 */
    public static String getOWM_BASE_URI() {
        return OWM_BASE_URI;
    }

	/**
	 * Vraća putanju do servisa za dohvat trenutnih meteoroloških podataka.
	 * @return putanja do servisa za dohvat trenutnih meteoroloških podataka
	 */
    public static String getOWM_Current_Path() {
        return "weather";
    }
        
	/**
	 * Vraća putanju do servisa za vremensku prognozu.
	 * @return putanja do servisa za vremensku prognozu
	 */
    public static String getOWM_Forecast_Path() {
        return "forecast";
    }
        
	/**
	 * Vraća putanju do servisa koji vraća vremensku prognozu po danu.
	 * @return putanja do servisa koji vraća vremensku prognozu po danu
	 */
    public static String getOWM_ForecastDaily_Path() {
        return "forecast/daily";
    }
        
	/**
	 * Vraća putanju do servisa za pregled obližnjih meteoroloških stanica.
	 * @return putanja do servisa za provjeru obližnjih meteoroloških stanica
	 */
    public static String getOWM_StationsNear_Path() {
        return "station/find";
    }
        
	/**
	 * Vraća putanju do servisa za pregled meteoroloških stanica.
	 * @return putanja do servisa za pregled meteoroloških stanica
	 */
    public static String getOWM_Station_Path() {
        return "station";
    }
}package org.foi.nwtis.student7.rest.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;

/**
 * REST servis koji omogućuje poziv metoda Web servisa za dohvat svih adresa pohranjenih
 * u bazi podataka i poziv metode Web servisa za dohvat važećih meteroloških podataka
 * (pod 'važeće' se misli zadnje pohranjene u bazi podataka).
 */
@Path("adreseREST")
public class AdreseRESTResource
{
	private final ProsirenaKonfiguracija konf;

	/**
	 * Konstruktor klase <b>AdreseRESTResource</b>.
	 */
	public AdreseRESTResource()
	{
		konf = (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
	}

	/**
	 * Metoda REST servisa koja učitava sve pohranjene adrese u bazi podataka i šalje
	 * ih natrag klijentu. Adrese su formatirane u JSON obliku.
	 * @return lista svih adresa iz baze podataka u JSON formatu
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String dajSveAdrese()
	{
		JsonArrayBuilder adreseJAB = Json.createArrayBuilder();
		boolean greska = false;

		try (BazaPodataka bp = new BazaPodataka(getInfo())) {
			ResultSet rezultat = bp.select("SELECT * FROM ADRESE");
			while (rezultat.next()) {
				long id = rezultat.getLong(1);
				String adresa = rezultat.getString(2);
				String latitude = rezultat.getString(3);
				String longitude = rezultat.getString(4);

				JsonObjectBuilder adresaJOB = Json.createObjectBuilder();
				adresaJOB.add("id", id);
				adresaJOB.add("adresa", adresa);
				adresaJOB.add("latitude", latitude);
				adresaJOB.add("longitude", longitude);

				adreseJAB.add(adresaJOB);
			}
			rezultat.close();
		}
		catch (SQLException ex) {
			System.out.println("Greška s bazom podataka u REST web servisu dajSveAdrese: " + ex.getMessage());
			greska = true;
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Greška s JDBC driver-om u REST web servisu dajSveAdrese: " + ex.getMessage());
			greska = true;
		}

		JsonObjectBuilder glavniJOB = Json.createObjectBuilder();
		if (!greska) {
			glavniJOB.add("adrese", adreseJAB);
			glavniJOB.add("status", 0);
		}
		else {
			glavniJOB.add("status", 1);
		}

		return glavniJOB.build().toString();
	}
	
	/**
	 * Metoda REST servisa koja, na temelju danog ID-a adrese, dohvaća važeće meterološke
	 * podatke te adresa i šalje ih natrag klijentu. Meterološki podaci su formatirani u
	 * JSON obliku.
	 * @param id ID adrese u bazi podataka
	 * @return meterološki podaci u JSON formatu
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String dajVazeceMeteoPodatke(@PathParam("id") long id)
	{
		JsonObjectBuilder meteoJOB = Json.createObjectBuilder();

		try (BazaPodataka bp = new BazaPodataka(getInfo())) {
			ResultSet rezultatAdresa = bp.select("SELECT * FROM ADRESE WHERE IDADRESA=" + id);
			if (rezultatAdresa.next()) {
				String latitude = rezultatAdresa.getString(3);
				String longitude = rezultatAdresa.getString(4);
				
				String sql = String.format("SELECT * FROM METEO WHERE LATITUDE='%s' AND LONGITUDE='%s'", latitude, longitude);				
				ResultSet rezultatMeteo = bp.select(sql);
				if (rezultatMeteo.last()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
					Date preuzeto = new Date(rezultatMeteo.getTimestamp("PREUZETO").getTime());
					
					meteoJOB.add("vrijeme", rezultatMeteo.getString("VRIJEME"));
					meteoJOB.add("temp", rezultatMeteo.getString("TEMP"));
					meteoJOB.add("tempMin", rezultatMeteo.getString("TEMPMIN"));
					meteoJOB.add("tempMax", rezultatMeteo.getString("TEMPMAX"));
					meteoJOB.add("vlaga", rezultatMeteo.getString("VLAGA"));
					meteoJOB.add("tlak", rezultatMeteo.getString("TLAK"));
					meteoJOB.add("vjetar", rezultatMeteo.getString("VJETAR"));
					meteoJOB.add("vjetarSmjer", rezultatMeteo.getString("VJETARSMJER"));
					meteoJOB.add("preuzeto", sdf.format(preuzeto));
					meteoJOB.add("status", 0);
				}
				else {
					meteoJOB.add("status", 1);
				}
				rezultatMeteo.close();
			}
			else {
				meteoJOB.add("status", 1);
			}
			rezultatAdresa.close();
		}
		catch (SQLException ex) {
			System.out.println("Greška s bazom podataka u REST web servisu dajAdresu: " + ex.getMessage());
			meteoJOB.add("status", 1);
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Greška s JDBC driver-om u REST web servisu dajAdresu: " + ex.getMessage());
			meteoJOB.add("status", 1);
		}
		
		return meteoJOB.build().toString();
	}

	/**
	 * Privatna i pomoćna metoda koja kreira i vraća objekt <b>BazaPodatakaInfo</b> kako
	 * bi se moglo pristupiti bazi podataka.
	 * @return objekt klase <b>BazaPodatakaInfo</b>
	 */
	private BazaPodatakaInfo getInfo()
	{
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());

		return info;
	}
}package org.foi.nwtis.student7.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.GMKlijent;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;

/**
 * Servlet za stranicu "dodajAdresu.jsp". Sadrži logiku za spremanje adrese u bazu podataka,
 * dohvat i prikaz geografskih podataka te adrese i pregled meteroloških podataka te adrese.
 */
public class DodajAdresu extends HttpServlet
{
	/**
	 * Metoda koja procesira zahtjeve korisnika. Može dohvatiti geografske podatke unesene adrese,
	 * prikazati ih, pohraniti ih te dohvatiti i prikazati meterološke podatke unesene adrese
	 * @param request zahtjev klijenta
	 * @param response odgovor poslužitelja
	 * @throws ServletException
	 * @throws IOException 
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		
		Enumeration<String> enumParametri = request.getParameterNames();
		if (!enumParametri.hasMoreElements())
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		String[] imenaParametara = new String[2];
		for (int i = 0; i < imenaParametara.length && enumParametri.hasMoreElements(); i++) {
			imenaParametara[i] = enumParametri.nextElement();
		}
		
		switch (imenaParametara[1]) {
			case "dohvatiGeo": {
				String adresaString = request.getParameter(imenaParametara[0]);
				HttpSession sesija = request.getSession();

				try {
					GMKlijent gmk = new GMKlijent();
					Lokacija lokacija = gmk.getGeoLocation(adresaString);
					Adresa adresa = new Adresa(1, adresaString, lokacija);

					sesija.setAttribute("dohvacenaAdresa", adresa);
					request.setAttribute("akcija", "dohvatiGeo");
					System.out.println("Dohvaćaju se geo podaci za: " + adresaString);
				}
				catch (Exception ex) {
					System.out.println("Ne mogu se naći podaci za adresu: " + adresaString);
				}
			} break;
			
			case "vidiGeo": {
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null)
					request.setAttribute("akcija", "vidiGeo");
			} break;
			
			case "spremiAdresu": {
				ProsirenaKonfiguracija konf = 
					(ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
				
				BazaPodatakaInfo info = new BazaPodatakaInfo();
				info.setDriver(konf.getDriverDatabase());
				info.setServer(konf.getServerDatabase());
				info.setBazaPodataka(konf.getUserDatabase());
				info.setKorisnik(konf.getUserUsername());
				info.setLozinka(konf.getUserPassword());
				
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null) {
					try (BazaPodataka bp = new BazaPodataka(info)) {
						if (!adresaPostoji(bp, dohvacenaAdresa)) {
							String sqlInsert = String.format(
								"INSERT INTO ADRESE(ADRESA, LATITUDE, LONGITUDE) VALUES ('%s', '%s', '%s')",
								dohvacenaAdresa.getAdresa(),
								dohvacenaAdresa.getGeoloc().getLatitude(),
								dohvacenaAdresa.getGeoloc().getLongitude()
							);

							bp.update(sqlInsert);

							request.setAttribute("akcija", "spremiAdresuNEW");
							System.out.println("Preuzete adrese su spremljene u bazu podataka.");
						}
						else {
							request.setAttribute("akcija", "spremiAdresuEXISTS");
							System.out.println("Pokušala se dodati već postojeća adresa.");
						}
					}
					catch (SQLException | ClassNotFoundException ex) {
						System.out.println("Dogodila se s bazom podataka: " + ex.getMessage());
					}
				}
			} break;
			
			case "dohvatiMeteo": {
				HttpSession sesija = request.getSession();
				Adresa dohvacenaAdresa = (Adresa) sesija.getAttribute("dohvacenaAdresa");
				
				if (dohvacenaAdresa != null) {
					ProsirenaKonfiguracija konf
						= (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");

					OWMKlijent owm = new OWMKlijent(konf.getApiKey());
					MeteoPodaci meteoPodaci 
						= owm.getRealTimeWeather(dohvacenaAdresa.getGeoloc().getLatitude(), dohvacenaAdresa.getGeoloc().getLongitude());
					
					request.setAttribute("akcija", "dohvatiMeteo");
					sesija.setAttribute("meteo", meteoPodaci);
					System.out.println("Dohvaćeni su meta podaci.");
				}
			} break;
		}
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	/**
	 * Pomoćna privatna metoda koja provjerava postoji li već unesena adresa, odnosna slična adresa
	 * s istim geografskim podacima, u bazi podataka.
	 * @param bp objekt klase <b>BazaPodataka</b> preko koje će se provjeriti postojanost adrese
	 * @param adresa objekt klase <b>Adresa</b> za koju se provvjerava postojanost
	 * @return <b>true</b> ako postoji, <b>false</b> ako ne postoji
	 * @throws SQLException 
	 */
	private boolean adresaPostoji(BazaPodataka bp, Adresa adresa) throws SQLException
	{
		boolean postoji = false;
		String sqlSelect = String.format(
			"SELECT * FROM ADRESE WHERE LATITUDE='%s' AND LONGITUDE='%s'",
			adresa.getGeoloc().getLatitude(),
			adresa.getGeoloc().getLongitude()
		);

		ResultSet rezultat = bp.select(sqlSelect);
		while (rezultat.next() && !postoji) {
			String latitude = rezultat.getString("LATITUDE");
			String longitude = rezultat.getString("LONGITUDE");
			boolean uvjet1 = latitude.equals(adresa.getGeoloc().getLatitude());
			boolean uvjet2 = longitude.equals(adresa.getGeoloc().getLongitude());
			if (uvjet1 && uvjet2)
				postoji = true;
		}		
		
		return postoji;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	@Override
	public String getServletInfo()
	{
		return "Short description";
	}

}package org.foi.nwtis.student7.web.podaci;

/**
 * Klasa preuzeta s Moodle stranica kolegija "Napredne Web tehnologije i servisi". Drži
 * informacije o adresama.
 */
public class Adresa {
    private long idadresa;
    private String adresa;
    private Lokacija geoloc;

	/**
	 * Konstruktor klase <b>Adresa</b>.
	 */
    public Adresa() {
    }

	/**
	 * Konstruktor klase <b>Adresa</b>.
	 * @param idadresa ID adrese
	 * @param adresa konkretna adresa u obliku <b>String</b>-a.
	 * @param geoloc geografska lokacija adrese
	 */
    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

	/**
	 * Vraća geografsku lokaciju adrese u obliku instance klase <b>Lokacija</b>.
	 * @return geografska lokacija adrese
	 */
    public Lokacija getGeoloc() {
        return geoloc;
    }

	/**
	 * Postavlja novu geografsku lokaciju adrese kao objekt klase <b>Lokacija</b>.
	 * @param geoloc nova geografska lokacija adrese
	 */
    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

	/**
	 * Vraća ID adrese.
	 * @return ID adrese
	 */
    public long getIdadresa() {
        return idadresa;
    }

	/**
	 * Postavlja ID adrese
	 * @param idadresa ID adrese
	 */
    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

	/**
	 * Vraća konkretnu adresu u <b>String</b> obliku.
	 * @return <b>String</b> adresa
	 */
    public String getAdresa() {
        return adresa;
    }

	/**
	 * Postavlja konkretnu adresu u <b>String</b> obliku.
	 * @param adresa <b>String</b> adresa
	 */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}package org.foi.nwtis.student7.web.podaci;

/**
 * Klasa <b>Lokacija</b> je preuzeta s Moodle stranica kolegija "Napredne Web
 * tehnologije i servisi". Sadrži geografske informacije o nekoj objektu.
 */
public class Lokacija {

    private String latitude;
    private String longitude;

	/**
	 * Konstruktor klase <b>Lokacija</b>.
	 */
    public Lokacija() {
    }

	/**
	 * Konstruktor klase <b>Lokacija</b>.
	 * @param latitude geografska širina
	 * @param longitude geografska širina
	 */
    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
	/**
	 * Postavlja geografsku širinu i dužinu lokacije.
	 * @param latitude geografska širina
	 * @param longitude geografska dužina 
	 */
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

	/**
	 * Vraća geografsku širinu.
	 * @return geografska širina
	 */
    public String getLatitude() {
        return latitude;
    }

	/**
	 * Postavlja geografsku širinu.
	 * @param latitude geografska širina
	 */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

	/**
	 * Vraća geografsku dužinu.
	 * @return geografska dužina
	 */
    public String getLongitude() {
        return longitude;
    }

	/**
	 * Postavlja geografsku dužinu.
	 * @param longitude geografska dužina
	 */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}package org.foi.nwtis.student7.web.podaci;

import java.util.Date;

/**
 * Klasa <b>MeteoPodaci</b> je preuzeta sa Moodle stranice kolegija "Napredne Web tehnologije
 * i servisi". Ova klasa predstavlja meteorološke podatke.
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

	/**
	 * Konstruktor klase <b>MeteoPodaci</b>.
	 */
    public MeteoPodaci() {
    }

	/**
	 * Konstruktor klase <b>MeteoPodaci</b>.
	 * @param sunRise
	 * @param sunSet
	 * @param temperatureValue
	 * @param temperatureMin
	 * @param temperatureMax
	 * @param temperatureUnit
	 * @param humidityValue
	 * @param humidityUnit
	 * @param pressureValue
	 * @param pressureUnit
	 * @param windSpeedValue
	 * @param windSpeedName
	 * @param windDirectionValue
	 * @param windDirectionCode
	 * @param windDirectionName
	 * @param cloudsValue
	 * @param cloudsName
	 * @param visibility
	 * @param precipitationValue
	 * @param precipitationMode
	 * @param precipitationUnit
	 * @param weatherNumber
	 * @param weatherValue
	 * @param weatherIcon
	 * @param lastUpdate 
	 */
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

	/**
	 * Getter metoda koja vraća vrijeme izlaska sunca.
	 * @return vrijeme izlaska sunca
	 */
    public Date getSunRise() {
        return sunRise;
    }

	/**
	 * Setter metoda koja postavlja vrijeme izlaska sunca.
	 * @param sunRise vrijeme izlaska sunca
	 */
    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

	/**
	 * Getter metoda koja vraća vrijeme zalaska sunca.
	 * @return vrijeme zalaska sunca
	 */
    public Date getSunSet() {
        return sunSet;
    }

	/**
	 * Setter metoda koja vraća vrijeme zalaska sunca.
	 * @param sunSet vrijeme zalaska sunca
	 */
    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

	/**
	 * Getter metoda koja vraća vrijednost temperature.
	 * @return vrijednost temperature
	 */
    public Float getTemperatureValue() {
        return temperatureValue;
    }

	/**
	 * Setter metoda koja postavlja vrijednost temperature.
	 * @param temperatureValue vrijednost temperature
	 */
    public void setTemperatureValue(Float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

	/**
	 * Getter metoda koja vraća minimalnu vrijednost temperature.
	 * @return minimalna vrijednost temperature
	 */
    public Float getTemperatureMin() {
        return temperatureMin;
    }

	/**
	 * Setter metoda koja postavlja minimalnu vrijednost temperature.
	 * @param temperatureMin minimalna vrijednost temperature
	 */
    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

	/**
	 * Getter metoda koja vraća maksimalnu vrijednost temperature.
	 * @return maksimalna vrijednost temperature
	 */
    public Float getTemperatureMax() {
        return temperatureMax;
    }

	/**
	 * Setter metoda koja postavlja maksimalnu vrijednost temperature.
	 * @param temperatureMax maksimalna vrijednost temperature
	 */
    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

	/**
	 * Getter metoda koja vraća temperaturnu jedinicu.
	 * @return temperaturna jedinica
	 */
    public String getTemperatureUnit() {
        return temperatureUnit;
    }

	/**
	 * Setter metoda koja postavlja temperaturnu jedinicu.
	 * @param temperatureUnit temperaturna jedinica
	 */
    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

	/**
	 * Getter metoda koja vraća vlažnost zraka.
	 * @return vlažnost zraka
	 */
    public Float getHumidityValue() {
        return humidityValue;
    }

	/**
	 * Setter metoda koja postavlja vlažnost zraka.
	 * @param humidityValue vlažnost zraka
	 */
    public void setHumidityValue(Float humidityValue) {
        this.humidityValue = humidityValue;
    }

	/**
	 * Getter metoda koja vraća jedinicu vlažnosti zraka.
	 * @return jedinica vlažnosti zraka
	 */
    public String getHumidityUnit() {
        return humidityUnit;
    }

	/**
	 * Setter metoda koja postavlja jedinicu vlažnosti zraka.
	 * @param humidityUnit jedinica vlažnosti zraka
	 */
    public void setHumidityUnit(String humidityUnit) {
        this.humidityUnit = humidityUnit;
    }

	/**
	 * Getter metoda koja vraća tlak zraka.
	 * @return tlak zraka
	 */
    public Float getPressureValue() {
        return pressureValue;
    }

	/**
	 * Setter metoda koja postavlja tlak zraka.
	 * @param pressureValue tlak zraka
	 */
    public void setPressureValue(Float pressureValue) {
        this.pressureValue = pressureValue;
    }

	/**
	 * Getter metoda koja vraća jedinicu tlaka zraka.
	 * @return jedinica tlaka zraka
	 */
    public String getPressureUnit() {
        return pressureUnit;
    }

	/**
	 * Setter metoda koja postavlja jedinicu tlaka zraka.
	 * @param pressureUnit jedinica tlaka zraka
	 */
    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

	/**
	 * Getter metoda koja vraća brzinu vjetra.
	 * @return brzina vjetra
	 */
    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

	/**
	 * Setter metoda koja postavlja brzinu vjetra.
	 * @param windSpeedValue brzina vjetra
	 */
    public void setWindSpeedValue(Float windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

	/**
	 * Getter metoda koja vraća naziv brzine vjetra.
	 * @return naziv brzine vjetra
	 */
    public String getWindSpeedName() {
        return windSpeedName;
    }

	/**
	 * Setter metoda koja postavlja naziv brzine vjetra.
	 * @param windSpeedName naziv brzine vjetra
	 */
    public void setWindSpeedName(String windSpeedName) {
        this.windSpeedName = windSpeedName;
    }

	/**
	 * Getter metoda koja vraća smjer puhanja vjetra.
	 * @return smjer puhanja vjetra
	 */
    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

	/**
	 * Setter metoda koja postavlja smjer puhanja vjetra.
	 * @param windDirectionValue smjer puhanja vjetra
	 */
    public void setWindDirectionValue(Float windDirectionValue) {
        this.windDirectionValue = windDirectionValue;
    }

	/**
	 * Getter metoda koja vraća oznaku za smjer puhanja vjetra.
	 * @return oznaka za smjer puhanja vjetra
	 */
    public String getWindDirectionCode() {
        return windDirectionCode;
    }

	/**
	 * Setter metoda koja postavlja oznaku za smjer puhanja vjetra.
	 * @param windDirectionCode oznaka za smjer puhanja vjetra
	 */
    public void setWindDirectionCode(String windDirectionCode) {
        this.windDirectionCode = windDirectionCode;
    }

	/**
	 * Getter metoda koja vraća naziv smjera puhanja vjetra.
	 * @return naziv smjera puhanja vjetra
	 */
    public String getWindDirectionName() {
        return windDirectionName;
    }

	/**
	 * Setter metoda koja postavlja naziv smjera puhanja vjetra.
	 * @param windDirectionName naziv smjera puhanja vjetra
	 */
    public void setWindDirectionName(String windDirectionName) {
        this.windDirectionName = windDirectionName;
    }

	/**
	 * Getter metoda koja vraća kodni broj oblačnosti.
	 * @return kodni broj oblačnosti
	 */
    public int getCloudsValue() {
        return cloudsValue;
    }

	/**
	 * Setter metoda koja postavlja kodni broj oblačnosti.
	 * @param cloudsValue kodni broj oblačnosti
	 */
    public void setCloudsValue(int cloudsValue) {
        this.cloudsValue = cloudsValue;
    }

	/**
	 * Getter metoda koja vraća naziv oblačnosti.
	 * @return naziv oblačnosti
	 */
    public String getCloudsName() {
        return cloudsName;
    }

	/**
	 * Setter metoda koja postavlja naziv oblačnosti.
	 * @param cloudsName naziv oblačnosti
	 */
    public void setCloudsName(String cloudsName) {
        this.cloudsName = cloudsName;
    }

	/**
	 * Getter metoda koja vraća vidljivost.
	 * @return vidljivost
	 */
    public String getVisibility() {
        return visibility;
    }

	/**
	 * Setter metoda koja postavlja vidljivost.
	 * @param visibility vidljivost
	 */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

	/**
	 * Getter metoda koja vraća vrijednost oborina?
	 * @return vrijednost oborina?
	 */
    public Float getPrecipitationValue() {
        return precipitationValue;
    }

	/**
	 * Setter metoda koja postavlja vrijednost oborina?
	 * @param precipitationValue vrijednost oborina?
	 */
    public void setPrecipitationValue(Float precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

	/**
	 * Getter metoda koja vraća "precipitation mode".
	 * @return "precipitation mode"
	 */
    public String getPrecipitationMode() {
        return precipitationMode;
    }

	/**
	 * Setter metoda koja postavlja "precipitation mode".
	 * @param precipitationMode "precipitation mode"
	 */
    public void setPrecipitationMode(String precipitationMode) {
        this.precipitationMode = precipitationMode;
    }

	/**
	 * Getter metoda koja vraća jedinicu za vrijednost oborina?
	 * @return jedinica za vrijednost oborina?
	 */
    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

	/**
	 * Setter metoda koja postavlja jedinicu za vrijednost oborina?
	 * @param precipitationUnit jedinica za vrijednost oborina?
	 */
    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

	/**
	 * Getter metoda koja vraća kodnu oznaku vremena.
	 * @return kodna oznaka vremena
	 */
    public int getWeatherNumber() {
        return weatherNumber;
    }

	/**
	 * Setter metoda koja postavlja kodnu oznaku vremena.
	 * @param weatherNumber kodna oznaka vremena
	 */
    public void setWeatherNumber(int weatherNumber) {
        this.weatherNumber = weatherNumber;
    }

	/**
	 * Getter metoda koja vraća opis vremena.
	 * @return opis vremena
	 */
    public String getWeatherValue() {
        return weatherValue;
    }

	/**
	 * Setter metoda koja postavlja opis vremena.
	 * @param weatherValue opis vremena
	 */
    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

	/**
	 * Getter metoda koja vraća ikonu za vrijeme.
	 * @return ikona za vrijeme
	 */
    public String getWeatherIcon() {
        return weatherIcon;
    }

	/**
	 * Setter metoda koja postavlja ikonu za vrijeme
	 * @param weatherIcon ikona za vrijeme
	 */
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

	/**
	 * Getter metoda koja vraća vrijeme zadnjeg ažuriranja.
	 * @return vrijeme zadnjeg ažuriranja
	 */
    public Date getLastUpdate() {
        return lastUpdate;
    }

	/**
	 * Setter metoda koja postavlja vrijeme zadnjeg ažuriranja.
	 * @param lastUpdate vrijeme zadnjeg ažuriranja
	 */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}package org.foi.nwtis.student7.web.slusaci;

import java.io.File;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student7.dretve.PreuzmiMeteoPodatke;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.konfiguracije.NemaKonfiguracije;

/**
 * Slušač aplikacije koji postavlja objekt klase <b>ProsirenaKonfiguracija</b> u atribut konteksta
 * i pokreže dretvu za preuzimanje meteroloških podataka.
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener
{
	private static ServletContext servletContext; 
	private Thread dretvaPreuzimanja;
	
	/**
	 * Postavlja atribut konfiguracije i pokreće dretvu za preuzimanje meteroloških podataka.
	 * @param sce 
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		servletContext = sce.getServletContext();
		
		try {
			postaviAtributKonfiguracije();
			postaviDretvuZaPreuzimanjeMeteoPodataka();
		}
		catch (NemaKonfiguracije ex) {
			System.out.println("Greška prilikom postavljanje atributa konfiguracije: " + ex.getMessage());
			System.out.println("Dretva je prekinuta. Ispravite grešku i ponovno isporučite aplikaciju");
		}
		catch (ClassNotFoundException | SQLException ex) {
			System.out.println("Greška kod povezivanja s bazom podataka: " + ex.getMessage());
		}
	}

	/**
	 * Kada se aplikacija gasi, ova metoda prekida dretvu koja se izvršava prije završetka
	 * same aplikacije.
	 * @param sce 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) 
	{
		if (dretvaPreuzimanja != null && dretvaPreuzimanja.isAlive())
			dretvaPreuzimanja.interrupt();
	}

	/**
	 * Javna statična metoda koja vraća instancu <b>ServletContext</b> klase za buduće
	 * korištenje u drugim klasama.
	 * @return instanca klase <b>ServletContext</b>
	 */
	public static ServletContext getServletContext()
	{
		return servletContext;
	}
	
	/**
	 * Privatna metoda koja postavlja objekt klase <b>ProsirenaKonfiguracija</b> u
	 * atribut konteksta.
	 * @throws NemaKonfiguracije 
	 */
	private void postaviAtributKonfiguracije() throws NemaKonfiguracije
	{
		String datoteka = servletContext.getInitParameter("konfiguracija");
		String putanja = servletContext.getRealPath("/WEB-INF") + File.separator;
		
		ProsirenaKonfiguracija konf = new ProsirenaKonfiguracija(putanja + datoteka);
		servletContext.setAttribute("konfiguracija", konf);
	}

	/**
	 * Privatna metoda koja pokreće metodu za preuzimanje meteroloških podataka.
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	private void postaviDretvuZaPreuzimanjeMeteoPodataka() throws ClassNotFoundException, SQLException
	{
		ProsirenaKonfiguracija konf = (ProsirenaKonfiguracija) servletContext.getAttribute("konfiguracija");
		PreuzmiMeteoPodatke pmp = new PreuzmiMeteoPodatke(konf);
		dretvaPreuzimanja = new Thread(pmp, "PreuzmiMeteoPodatke");
		dretvaPreuzimanja.start();
	}
}package org.foi.nwtis.student7.ws.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.student7.bazaPodataka.BazaPodataka;
import org.foi.nwtis.student7.bazaPodataka.BazaPodatakaInfo;
import org.foi.nwtis.student7.konfiguracija.ProsirenaKonfiguracija;
import org.foi.nwtis.student7.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student7.web.podaci.MeteoPodaci;
import org.foi.nwtis.student7.web.slusaci.SlusacAplikacije;

/**
 * SOAP servis koji omogućava poziv metoda za dohvat svih dosad pospremljenih meteroloških 
 * podataka dane adrese i poziv metoda koja vraća prognozu vremena za danu adresu.
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS
{
	private ProsirenaKonfiguracija konf;
	
	/**
	 * Metoda SOAP Web servisa koja vraća sve pohranjene meterološke podatke dane adrese.
	 * @param address adresa za koju se traži meterološki podaci
	 * @return meterološki podaci dane adrese
	 */
	@WebMethod(operationName = "dajSveMeteoPodatke")
	public List<MeteoPodaci> dajSveMeteoPodatke(@WebParam(name = "address") String address)
	{
		List<MeteoPodaci> preuzetiMeteoPodaci = new ArrayList<>();
		
		if (address != null && address.length() > 0) {
			try (BazaPodataka bp = new BazaPodataka(getInfo())) {
				String sql1 = String.format("SELECT * FROM ADRESE WHERE ADRESA='%s'", address);
				
				ResultSet rezultatAdresa = bp.select(sql1);
				if (rezultatAdresa.next()) {
					String latitude = rezultatAdresa.getString(3);
					String longitude = rezultatAdresa.getString(4);
					
					String sql2 = String.format(
						"SELECT * FROM METEO WHERE LATITUDE='%s' AND LONGITUDE='%s'",
						latitude,
						longitude
					);
					
					ResultSet rezultatMeteo = bp.select(sql2);
					while (rezultatMeteo.next()) {
						MeteoPodaci meteo = new MeteoPodaci();
						meteo.setWeatherValue(rezultatMeteo.getString("VRIJEME"));
						meteo.setTemperatureValue(rezultatMeteo.getFloat("TEMP"));
						meteo.setTemperatureMin(rezultatMeteo.getFloat("TEMPMIN"));
						meteo.setTemperatureMax(rezultatMeteo.getFloat("TEMPMAX"));
						meteo.setHumidityValue(rezultatMeteo.getFloat("VLAGA"));
						meteo.setPressureValue(rezultatMeteo.getFloat("TLAK"));
						meteo.setWindSpeedValue(rezultatMeteo.getFloat("VJETAR"));
						meteo.setWindDirectionValue(rezultatMeteo.getFloat("VJETARSMJER"));
						meteo.setLastUpdate(new Date(rezultatMeteo.getTimestamp("PREUZETO").getTime()));
						
						preuzetiMeteoPodaci.add(meteo);
					}
				}
			}
			catch (SQLException ex) {
				System.out.println("Greška s bazom podataka u SOAP web servisu dajMeteoPodatke: " + ex.getMessage());
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Greška s JDBC driver-om u SOAP web servisu dajMeteoPodatke: " + ex.getMessage());
			}
		}		
		
		return preuzetiMeteoPodaci;
	}
	
	/**
	 * Metoda SOAP Web servisa koji vraća vremensku prognozu (za sljedeća 5 dana u razmaku od 3 sata) za
	 * danu adresu.
	 * @param address adresa za koju se traži vremenska prognoza
	 * @return vremenska prognoza dane adrese
	 */
	@WebMethod(operationName = "dajPrognozu")
	public List<MeteoPodaci> dajPrognozu(@WebParam(name = "address") String address)
	{
		List<MeteoPodaci> prognoza = new ArrayList<>();
		
		if (address != null && address.length() > 0) {
			try (BazaPodataka bp = new BazaPodataka(getInfo())) {
				OWMKlijent owm = new OWMKlijent(konf.getApiKey());
				String sql = String.format("SELECT * FROM ADRESE WHERE ADRESA='%s'", address);
				
				ResultSet rezultatAdresa = bp.select(sql);
				if (rezultatAdresa.next()) {
					String latitude = rezultatAdresa.getString(3);
					String longitude = rezultatAdresa.getString(4);
					prognoza = owm.getWeatherForecast(latitude, longitude);
				}
			}
			catch (SQLException ex) {
				System.out.println("Greška s bazom podataka u SOAP web servisu dajPrognozu: " + ex.getMessage());
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Greška s JDBC driver-om u SOAP web servisu dajPrognozu: " + ex.getMessage());
			}
		}		
		
		return prognoza;
	}
		
	/**
	 * Privatna i pomoćna metoda koja kreira i vraća objekt <b>BazaPodatakaInfo</b> kako
	 * bi se moglo pristupiti bazi podataka.
	 * @return objekt klase <b>BazaPodatakaInfo</b>
	 */
	private BazaPodatakaInfo getInfo()
	{
		if (konf == null)
			konf = (ProsirenaKonfiguracija) SlusacAplikacije.getServletContext().getAttribute("konfiguracija");
		
		BazaPodatakaInfo info = new BazaPodatakaInfo();
		info.setDriver(konf.getDriverDatabase());
		info.setServer(konf.getServerDatabase());
		info.setBazaPodataka(konf.getUserDatabase());
		info.setKorisnik(konf.getUserUsername());
		info.setLozinka(konf.getUserPassword());

		return info;
	}
}package org.foi.nwtis.student7.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Klasa <b>AdreseRESTKlijent</b> omogućava pristup REST servisima kreiranim
 * u projektu "student7_zadaca_3_1". Sve metode (osim metode <b>close()</b>)
 * otvaraju vezu s REST servisom i vraćaju resurs koje REST servis nudi. U
 * slučaju ove klase svi resursi su tipa <b>String</b> i formatirani JSON
 * strukturom.
 */
public class AdreseRESTKlijent
{
	private WebTarget webTarget;
	private Client client;
	private static final String BASE_URI = "http://localhost:8084/student7_zadaca_3_1/REST";

	/**
	 * Konstruktor klase <b>AdreseRESTKlijent</b>
	 */
	public AdreseRESTKlijent()
	{
		client = javax.ws.rs.client.ClientBuilder.newClient();
		webTarget = client.target(BASE_URI).path("adreseREST");
	}

	/**
	 * Kontaktira REST servis "dajVazeceMeteoPodatke" i, na temelju danog ID-a adrese, vraća
	 * zadnje upisane meteorološke podatke.
	 * @param id ID adrese it tablice ADRESE
	 * @return meteorološki podaci u JSON formatu
	 * @throws ClientErrorException 
	 */
	public String dajVazeceMeteoPodatke(String id) throws ClientErrorException
	{
		WebTarget resource = webTarget;
		resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
		return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
	}

	/**
	 * Kontaktira REST servis "dajSveAdrese" i vraća sve adrese iz baze podataka.
	 * @return adrese u JSON formatu
	 * @throws ClientErrorException 
	 */
	public String dajSveAdrese() throws ClientErrorException
	{
		WebTarget resource = webTarget;
		return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
	}

	/**
	 * Zatvara klijenta i onemogućuje daljnje pozive REST servisa.
	 */
	public void close()
	{
		client.close();
	}
}package org.foi.nwtis.student7.web.podaci;

/**
 * Klasa preuzeta s Moodle stranica kolegija "Napredne Web tehnologije i servisi". Drži
 * informacije o adresama.
 */
public class Adresa {
    private long idadresa;
    private String adresa;
    private Lokacija geoloc;

	/**
	 * Konstruktor klase <b>Adresa</b>.
	 */
    public Adresa() {
    }

	/**
	 * Konstruktor klase <b>Adresa</b>.
	 * @param idadresa ID adrese
	 * @param adresa konkretna adresa u obliku <b>String</b>-a.
	 * @param geoloc geografska lokacija adrese
	 */
    public Adresa(long idadresa, String adresa, Lokacija geoloc) {
        this.idadresa = idadresa;
        this.adresa = adresa;
        this.geoloc = geoloc;
    }

	/**
	 * Vraća geografsku lokaciju adrese u obliku instance klase <b>Lokacija</b>.
	 * @return geografska lokacija adrese
	 */
    public Lokacija getGeoloc() {
        return geoloc;
    }

	/**
	 * Postavlja novu geografsku lokaciju adrese kao objekt klase <b>Lokacija</b>.
	 * @param geoloc nova geografska lokacija adrese
	 */
    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

	/**
	 * Vraća ID adrese.
	 * @return ID adrese
	 */
    public long getIdadresa() {
        return idadresa;
    }

	/**
	 * Postavlja ID adrese
	 * @param idadresa ID adrese
	 */
    public void setIdadresa(long idadresa) {
        this.idadresa = idadresa;
    }

	/**
	 * Vraća konkretnu adresu u <b>String</b> obliku.
	 * @return <b>String</b> adresa
	 */
    public String getAdresa() {
        return adresa;
    }

	/**
	 * Postavlja konkretnu adresu u <b>String</b> obliku.
	 * @param adresa <b>String</b> adresa
	 */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}package org.foi.nwtis.student7.web.podaci;

/**
 * Klasa <b>Lokacija</b> je preuzeta s Moodle stranica kolegija "Napredne Web
 * tehnologije i servisi". Sadrži geografske informacije o nekoj objektu.
 */
public class Lokacija {

    private String latitude;
    private String longitude;

	/**
	 * Konstruktor klase <b>Lokacija</b>.
	 */
    public Lokacija() {
    }

	/**
	 * Konstruktor klase <b>Lokacija</b>.
	 * @param latitude geografska širina
	 * @param longitude geografska širina
	 */
    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
	/**
	 * Postavlja geografsku širinu i dužinu lokacije.
	 * @param latitude geografska širina
	 * @param longitude geografska dužina 
	 */
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

	/**
	 * Vraća geografsku širinu.
	 * @return geografska širina
	 */
    public String getLatitude() {
        return latitude;
    }

	/**
	 * Postavlja geografsku širinu.
	 * @param latitude geografska širina
	 */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

	/**
	 * Vraća geografsku dužinu.
	 * @return geografska dužina
	 */
    public String getLongitude() {
        return longitude;
    }

	/**
	 * Postavlja geografsku dužinu.
	 * @param longitude geografska dužina
	 */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
}package org.foi.nwtis.student7.web.zrna;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.foi.nwtis.student7.rest.klijenti.AdreseRESTKlijent;
import org.foi.nwtis.student7.web.podaci.Adresa;
import org.foi.nwtis.student7.web.podaci.Lokacija;
import org.foi.nwtis.student7.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.student7.ws.serveri.MeteoPodaci;

/**
 * Klasa <b>OdabirAdresa</b> služi kao ManagedBean za "index.xhtml". Omogućava učitavanje adresa preko
 * Web servisa, dohvat i prikaz meteoroloških podataka za odabrane adrese te dohvat i prikaz vremenske prognoze.
 */
@Named(value = "odabirAdresa")
@RequestScoped
public class OdabirAdresa
{
	
	private final List<Adresa> popisAdresa = new ArrayList<>();
	private long[] odabraniIDevi = {};
	
	private final List<MeteoPodaci> dohvaceniMeteoPodaci = new ArrayList<>();
	private final Map<String, MeteoPodaci> dohvaceniMultiMeteoPodaci = new LinkedHashMap<>();
	private String akcija = "NEMA";
	
	/**
	 * Konstruktor klase <b>OdabirAdresa</b>. Učitava adrese sa REST Web servisa u projektu
	 * "student7_zadaca_3_1".
	 */
	public OdabirAdresa() 
	{
		try {
			AdreseRESTKlijent adreseRESTKlijent = new AdreseRESTKlijent();
			String sveAdrese = adreseRESTKlijent.dajSveAdrese();
			JsonObject jsonTemelj = Json.createReader(new StringReader(sveAdrese)).readObject();

			if (jsonTemelj.getInt("status") == 0) {
				JsonArray jsonAdrese = jsonTemelj.getJsonArray("adrese");
				for (int i = 0; i < jsonAdrese.size(); i++) {
					JsonObject jsonAdresa = jsonAdrese.getJsonObject(i);

					long id = jsonAdresa.getInt("id");
					String adresa = jsonAdresa.getString("adresa");
					String latitude = jsonAdresa.getString("latitude");
					String longitude = jsonAdresa.getString("longitude");

					Lokacija lokacija = new Lokacija(latitude, longitude);
					popisAdresa.add(new Adresa(id, adresa, lokacija));
				}
			}

			adreseRESTKlijent.close();
		}
		catch (Exception ex) {
			System.out.println("Web servisi se ne mogu kontaktirati.");
		}
	}
	
	/**
	 * Vraća listu adresa.
	 * @return lista adresa
	 */
	public List<Adresa> getPopisAdresa()
	{
		return popisAdresa;
	}

	/**
	 * Vraća ID-eve odabranih adresa
	 * @return ID-evi odabranih adresa
	 */
	public long[] getOdabraniIDevi()
	{
		return odabraniIDevi;
	}

	/**
	 * Postavlja ID-eve odabranih adresa.
	 * @param odabraniIDevi ID-evi odabranih adresa
	 */
	public void setOdabraniIDevi(long[] odabraniIDevi)
	{
		this.odabraniIDevi = odabraniIDevi;
	}
	
	/**
	 * Vraća broj odabranih adresa, odnosno broj ID-eva odabranih adresa.
	 * @return broj odabranih adresa
	 */
	public int getBrojOdabranihIDeva()
	{
		return odabraniIDevi.length;
	}
	
	/**
	 * Vraća listu odabranih adresa. Za ovu listu se filtrira lista svih adresa
	 * na temelju odabranih ID-eva.
	 * @return lista odabranih adresa
	 */
	public List<Adresa> getOdabraneAdrese()
	{
		List<Adresa> odabraneAdrese = new ArrayList<>();
		
		for (long id : odabraniIDevi) {
			boolean pronadjeno = false;
			for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
				Adresa adresa = popisAdresa.get(i);
				if (adresa.getIdadresa() == id) {
					odabraneAdrese.add(adresa);
					pronadjeno = true;
				}
			}
		}
		
		return odabraneAdrese;
	}

	/**
	 * Vraća dohvaćenu listu meteo podataka.
	 * @return lista dohvaćenih meteo podataka
	 */
	public List<MeteoPodaci> getDohvaceniMeteoPodaci()
	{
		return dohvaceniMeteoPodaci;
	}

	/**
	 * 
	 * @return 
	 */
	public Map<String, MeteoPodaci> getDohvaceniMultiMeteoPodaci()
	{
		return dohvaceniMultiMeteoPodaci;
	}

	/**
	 * <p>Vraća akciju koja se želi izvršiti. Akcija može biti jedna od sljedećih vrijednosti:</p>
	 * <ul>
	 *    <li>NEMA</li>
	 *    <li>PRIKAZI_METEO</li>
	 *    <li>PRIKAZI_PROGNOZU</li>
	 *    <li>PRIKAZI_VAZECE_METEO</li>
	 * </ul>
	 * @return akcija
	 */
	public String getAkcija()
	{
		return akcija;
	}
	
	/**
	 * Na temelju odabranog ID-a poziva se Web servis koji vraća sve meteo podatke koji
	 * su povezani s adresom koja u bazi podataka ima ID jednak varijabli <b>odabraniIDevi[0]</b>.
	 */
	public void dohvatiMeteoPodatke() 
	{
		long odabraniID = odabraniIDevi[0];
		Adresa odabranaAdresa = null;
		
		boolean pronadjeno = false;
		for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
			if (popisAdresa.get(i).getIdadresa() == odabraniID) {
				odabranaAdresa = popisAdresa.get(i);
				pronadjeno = true;
			}
		}
		
		if (odabranaAdresa != null) {
			dohvaceniMeteoPodaci.clear();
			dohvaceniMeteoPodaci.addAll(MeteoWSKlijent.dajSveMeteoPodatke(odabranaAdresa.getAdresa()));
			akcija = "PRIKAZI_METEO";
		}
	}
	
	/**
	 * Na temelju odabranog ID-a poziva se Web servis koji vraća vremensku prognozu odabrane
	 * adrese.
	 */
	public void dohvatiPrognozu()
	{
		long odabraniID = odabraniIDevi[0];
		Adresa odabranaAdresa = null;
		
		boolean pronadjeno = false;
		for (int i = 0; i < popisAdresa.size() && !pronadjeno; i++) {
			if (popisAdresa.get(i).getIdadresa() == odabraniID) {
				odabranaAdresa = popisAdresa.get(i);
				pronadjeno = true;
			}
		}
		
		if (odabranaAdresa != null) {
			dohvaceniMeteoPodaci.clear();
			dohvaceniMeteoPodaci.addAll(MeteoWSKlijent.dajPrognozu(odabranaAdresa.getAdresa()));
			akcija = "PRIKAZI_PROGNOZU";
		}
	}
	
	/**
	 * Na temelju odabranih adresa poziva se Web servis koji vraća važeće meteo podatke za svaku odabranu
	 * adresu.
	 */
	public void dohvatiVazeceMeteoPodatke()
	{
		
		List<Adresa> odabraneAdrese = new ArrayList<>();
		
		for (int i = 0; i < odabraniIDevi.length; i++) {
			boolean pronadjeno = false;
			for (int j = 0; j < popisAdresa.size() && !pronadjeno; j++) {
				if (popisAdresa.get(j).getIdadresa() == odabraniIDevi[i]) {
					odabraneAdrese.add(popisAdresa.get(j));
					pronadjeno = true;
				}
			}
		}
		
			if (!odabraneAdrese.isEmpty()) {
			AdreseRESTKlijent adreseRESTKlijent = new AdreseRESTKlijent();
			dohvaceniMultiMeteoPodaci.clear();
			for (Adresa a : odabraneAdrese) {
				String odgovor = adreseRESTKlijent.dajVazeceMeteoPodatke(Long.toString(a.getIdadresa()));
				JsonObject jo = Json.createReader(new StringReader(odgovor)).readObject();

				if (jo.getInt("status") == 0) {
					MeteoPodaci mp = new MeteoPodaci();

					mp.setWeatherValue(jo.getString("vrijeme"));
					mp.setTemperatureValue(new Double(jo.getString("temp")).floatValue());
					mp.setTemperatureMin(new Double(jo.getString("tempMin")).floatValue());
					mp.setTemperatureMax(new Double(jo.getString("tempMax")).floatValue());
					mp.setHumidityValue(new Double(jo.getString("vlaga")).floatValue());
					mp.setPressureValue(new Double(jo.getString("tlak")).floatValue());
					mp.setWindSpeedValue(new Double(jo.getString("vjetar")).floatValue());
					mp.setWindDirectionValue(new Double(jo.getString("vjetarSmjer")).floatValue());

					dohvaceniMultiMeteoPodaci.put(a.getAdresa(), mp);
				}
			}
			akcija = "PRIKAZI_VAZECE_METEO";
			adreseRESTKlijent.close();
		}
	}
	
	/**
	 * Pomoćna metoda koja formatira datum na hrvatski način čitanja.
	 * @param datum datum koji se želi formatirati
	 * @return formatirani datum kao <b>String</b>
	 */
	public String formatirajDatum(Date datum) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
		return sdf.format(datum);
	}
}package org.foi.nwtis.student7.ws.klijenti;

/**
 * Klasa <b>MeteoWSKlijent</b> služi za kontaktiranje SOAP Web servisa iz projekta
 * "student7_zadaca_3_1" koji vraćaju meteorološke podatke (svaki servis vraća na
 * svoj način).
 */
public class MeteoWSKlijent
{
	/**
	 * Javna statička metoda koja kontaktira SOAP Web servis "dajSveMeteoPodatke" koji na temelju dane adrese
	 * vraća sve meteorološke podatke s te adrese.
	 * @param address adresa s koje se traže meteorološki podaci
	 * @return popis meteoroloških podataka
	 */
	public static java.util.List<org.foi.nwtis.student7.ws.serveri.MeteoPodaci> dajSveMeteoPodatke(java.lang.String address)
	{
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service();
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
		return port.dajSveMeteoPodatke(address);
	}

	/**
	 * Javna statična metoda koja kontaktira SOAP Web servis "dajPrognozu" koji na temelju dane adrese vraća
	 * prognozu vremena za tu adresu u sljedećih 5 dana po 3 sata razmaka.
	 * @param address adresa za koju se traži vremenska prognoza
	 * @return popis meteoroloških podataka kao vremenska prognoza
	 */
	public static java.util.List<org.foi.nwtis.student7.ws.serveri.MeteoPodaci> dajPrognozu(java.lang.String address)
	{
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.student7.ws.serveri.GeoMeteoWS_Service();
		org.foi.nwtis.student7.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
		return port.dajPrognozu(address);
	}
}