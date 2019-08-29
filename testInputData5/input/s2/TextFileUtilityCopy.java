/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.rest.klijenti;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student8.web.podaci.Lokacija;

/**
 * Klasa GMKlijent koriste se za preuzimanje geolakcijskih podataka putem Google Maps API
 * 
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;

    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

    /**
     * Metoda prima jedan parametar(adresa) te zatim priprema zahtjev te ga stavlja u JSON format
     * Ukoliko je parametar korektan vraća objekt Lokacije, inače vraća null
     * @param adresa
     * @return 
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
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student8.rest.klijenti;

/**
 * Pomoćna klasa za GMKlijenta
 * @author student8
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
package org.foi.nwtis.student8.rest.klijenti;

import java.io.StringReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.student8.web.podaci.MeteoPodaci;

/**
 * OWMKlijent je klasa koja se koristi za dohvacanje svih meteoroloskih podataka
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
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student8.rest.klijenti;

/**
 * Klasa pomagac omogucuje lakse spajanje na servis
 * @author student8
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
package org.foi.nwtis.student8.rest.serveri;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author grupa_1
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.foi.nwtis.student8.rest.serveri.MeteoRESTResource.class);
        resources.add(org.foi.nwtis.student8.rest.serveri.MeteoRESTResourceContainer.class);
    }
    
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.rest.serveri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.rest.klijenti.GMKlijent;
import org.foi.nwtis.student8.web.PreuzmiMeteoPodatke;
import org.foi.nwtis.student8.web.podaci.Adresa;
import org.foi.nwtis.student8.web.podaci.Lokacija;

/**
 * REST Web Service
 * Rest servis koji vraca gradove te pojedini grad ovisno o prilozenom ID-u
 * @author grupa_1
 */
public class MeteoRESTResource {

    private String id;

    /**
     * Creates a new instance of MeteoRESTResource
     */
    private MeteoRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the MeteoRESTResource
     */
    public static MeteoRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MeteoRESTResource class.
        return new MeteoRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.student8.rest.serveri.MeteoRESTResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    
    public String getJson(@Context ServletContext servletContext) {
        BP_Konfiguracija konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        ArrayList<String> listaAdresa = new ArrayList<>();
        GMKlijent gmk = new GMKlijent();
        String upit = "SELECT * FROM ADRESE";

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoRESTResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection conn = DriverManager.getConnection(konfig.getServerDatabase() + konfig.getUserDatabase(), konfig.getAdminUsername(), konfig.getAdminPassword());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(upit);

            while (rs.next()) {
                listaAdresa.add(rs.getString("ADRESA"));
            }
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(MeteoRESTResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        int br = Integer.parseInt(id);
        if (br < listaAdresa.size()) {
            JsonObjectBuilder jbf = Json.createObjectBuilder();
            jbf.add("adresa", listaAdresa.get(br));
            return jbf.build().toString();
        } else {
            return "";
        }
    }

    /**
     * PUT method for updating or creating an instance of MeteoRESTResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content
    ) {
    }

    /**
     * DELETE method for resource MeteoRESTResource
     */
    @DELETE
    public void delete() {
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.rest.serveri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.rest.klijenti.GMKlijent;

/**
 * REST Web Service
 * Rest servis koji vraca gradove te pojedini grad ovisno o prilozenom ID-u
 * @author grupa_1
 */
@Path("/meteoREST")
public class MeteoRESTResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MeteoRESTResourceContainer
     */
    public MeteoRESTResourceContainer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.student8.rest.serveri.MeteoRESTResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@Context ServletContext servletContext) {
        //ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        
        BP_Konfiguracija konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        ArrayList<String> listaAdresa = new ArrayList<>();
        GMKlijent gmk = new GMKlijent();
        String upit = "SELECT * FROM ADRESE";

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoRESTResourceContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection conn = DriverManager.getConnection(konfig.getServerDatabase() + konfig.getUserDatabase(), konfig.getAdminUsername(), konfig.getAdminPassword());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(upit);

            while (rs.next()) {
                listaAdresa.add(rs.getString("ADRESA"));
            }
            conn.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(MeteoRESTResourceContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String a : listaAdresa) {
            jab.add(a);
        }
        JsonObjectBuilder jbf = Json.createObjectBuilder();
        jbf.add("adrese", jab);
        return jbf.build().toString();
    }

    /**
     * POST method for creating an instance of MeteoRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content
    ) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public MeteoRESTResource getMeteoRESTResource(@PathParam("id") String id
    ) {
        return MeteoRESTResource.getInstance(id);
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.rest.klijenti.GMKlijent;
import org.foi.nwtis.student8.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student8.web.podaci.Lokacija;
import org.foi.nwtis.student8.web.podaci.MeteoPodaci;

/**
 * Servlet koji sluzi za procesiranje korisnickih zahtjeva
 * Moguce je dodavanje gradova u bazu, ispisivanje geolokacijskih
 * te meteoroloskih podataka
 * @author grupa_1
 */
@WebServlet(name = "DodajAdresu", urlPatterns = {"/dodajAdresu"})
public class DodajAdresu extends HttpServlet {

    private String adresa = "";
    private BP_Konfiguracija bpk;
    private String APPID;

    public void init() throws ServletException {
        this.bpk = (BP_Konfiguracija) getServletContext().getAttribute("BP_Konfig");
        this.APPID = bpk.getAPPID();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DodajAdresu</title>");
            out.println("</head>");
            out.println("<body>");

            if (request.getParameter("adresa") != null) {
                adresa = request.getParameter("adresa");

                GMKlijent gmk = new GMKlijent();
                Lokacija lokacija = gmk.getGeoLocation(adresa);

                if (request.getParameter("dohvatGP") != null) {
                    out.println("<h3>");
                    out.println(adresa);
                    out.println("</h3>");
                    out.println("<ul><li>Latitude: ");
                    out.println(lokacija.getLatitude());
                    out.println("</li><li>Longitude: ");
                    out.println(lokacija.getLongitude());
                    out.println("</li></ul>");
                }

                if (request.getParameter("spremiGP") != null) {
                    String upisiUBazu = "INSERT INTO ADRESE VALUES (DEFAULT, ?, ?, ?)";
                    try {
                        Class.forName(bpk.getDriverDatabase());
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(DodajAdresu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        java.sql.Connection con = DriverManager.getConnection(bpk.getServerDatabase() + bpk.getUserDatabase(), bpk.getAdminUsername(), bpk.getAdminPassword());
                        PreparedStatement stmt = con.prepareStatement(upisiUBazu);
                        stmt.setString(1, this.adresa);
                        stmt.setString(2, lokacija.getLatitude());
                        stmt.setString(3, lokacija.getLongitude());
                        stmt.executeUpdate();

                    } catch (SQLException ex) {
                        Logger.getLogger(DodajAdresu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("<h3>");
                    out.println(adresa);
                    out.println("</h3>");
                    out.println("<h5>");
                    out.println("Adresa " + this.adresa + " s podacima latitude " + lokacija.getLatitude() + " i longitude " + lokacija.getLongitude() + " dodana u bazu.");
                    out.println("</h5>");
                }

                if (request.getParameter("dohvatMP") != null) {
                    OWMKlijent owmk = new OWMKlijent(this.APPID);
                    MeteoPodaci mp = owmk.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());   
                    out.println("<h3>");
                    out.println(adresa);
                    out.println("</h3>");
                    out.println("<ul><li>Zadnji update: ");
                    out.println(mp.getLastUpdate());
                    out.println("</li><li>Temperatura: ");
                    out.println(mp.getTemperatureValue() + " " + mp.getTemperatureUnit());
                    out.println("</li><li>Tlak zraka: ");
                    out.println(mp.getPressureValue() + " " + mp.getPressureUnit());
                    out.println("</li>");
                    out.println("</li><li>Vlažnost zraka: ");
                    out.println(mp.getHumidityValue() + " " + mp.getHumidityUnit());
                    out.println("</li>");
                    out.println("</li><li>Vrijeme općenito: ");
                    out.println(mp.getWeatherValue());
                    out.println("</li>");
                    out.println("</li><li>Izlazak sunca: ");
                    out.println(mp.getSunRise());
                    out.println("</li>");
                    out.println("</li><li>Zalazak sunca: ");
                    out.println(mp.getSunSet());
                    out.println("</li>");
                    out.println("</ul>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.student8.web.podaci;

/**
 * Klasa koja sluzi kao model za dodavanje adresa
 * @author student8
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
package org.foi.nwtis.student8.web.podaci;

/**
 * Klasa koja služi kao model za dodavanje lokacija
 * @author student8
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
package org.foi.nwtis.student8.web.podaci;

import java.util.Date;

/**
 * Klasa koja služi kao model za dodavanje meteo podataka
 * @author student8
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
package org.foi.nwtis.student8.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.rest.klijenti.GMKlijent;
import org.foi.nwtis.student8.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student8.web.podaci.Lokacija;
import org.foi.nwtis.student8.web.podaci.MeteoPodaci;

/**
 * Dretva koja u pravilnim vremenskim intervalima preuzima podatke o gradovima
 * iz baze te za svaki pojedini grad ispituje trenutno meteorolosko stanje
 * te zatim te podatke zapisuje u bazu
 * @author grupa_1
 */
public class PreuzmiMeteoPodatke extends Thread {

    private int interval;
    private BP_Konfiguracija konfig;
    private String URL;
    private String DBUser;
    private String DBPass;
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Map<Integer, String> listaAdresa = null;

    private String APPID;
    private GMKlijent gmk = null;
    private Lokacija lokacija = null;
    private OWMKlijent owmk = null;
    private MeteoPodaci mp = null;
    
    /**
     * Konstruktor primarno namijenjen za inicijalizaciju varijabli
     * @param bp_konfig 
     */

    public PreuzmiMeteoPodatke(BP_Konfiguracija bp_konfig) {
        this.konfig = bp_konfig;
        this.interval = Integer.parseInt(konfig.getThreadInterval());
        this.URL = konfig.getServerDatabase() + konfig.getUserDatabase();
        this.DBUser = konfig.getAdminUsername();
        this.DBPass = konfig.getAdminPassword();
        this.APPID = konfig.getAPPID();

        this.owmk = new OWMKlijent(this.APPID);
        this.gmk = new GMKlijent();
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Pozivanje odgovorajucih metoda te stavljanje dretve na spavanje
     */
    @Override
    public void run() {
        while (true) {
            listaAdresa = dohvatiAdrese();

            for (Map.Entry<Integer, String> entry : listaAdresa.entrySet()) {
                upisujUMeteo(entry.getKey(), entry.getValue());
            }

            try {
                Thread.sleep(this.interval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Dohvacanje svih gradova iz tablice adrese te spremanje u tip podatka Map<Integer, String>
     * @return 
     */
    private Map<Integer, String> dohvatiAdrese() {
        String upit = "SELECT * FROM ADRESE";
        Map<Integer, String> adrese = new HashMap<>();

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.conn = DriverManager.getConnection(this.URL, this.DBUser, this.DBPass);
            this.stmt = conn.createStatement();
            this.rs = stmt.executeQuery(upit);

            while (this.rs.next()) {
                adrese.put(this.rs.getInt("IDADRESA"), this.rs.getString("ADRESA"));
            }

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        return adrese;
    }

    private void upisujUMeteo(int id, String adresa) {
        String upit = "INSERT INTO METEO VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.lokacija = gmk.getGeoLocation(adresa);
        MeteoPodaci mp = this.owmk.getRealTimeWeather(lokacija.getLatitude(), lokacija.getLongitude());

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.conn = DriverManager.getConnection(this.URL, this.DBUser, this.DBPass);
            this.pstmt = this.conn.prepareStatement(upit);

            pstmt.setInt(1, id);
            pstmt.setString(2, adresa);
            pstmt.setString(3, this.lokacija.getLatitude());
            pstmt.setString(4, this.lokacija.getLongitude());
            pstmt.setString(5, mp.getWeatherValue());
            pstmt.setString(6, Integer.toString(mp.getWeatherNumber()));
            pstmt.setFloat(7, mp.getTemperatureValue());
            pstmt.setFloat(8, mp.getTemperatureMin());
            pstmt.setFloat(9, mp.getTemperatureMax());
            pstmt.setFloat(10, mp.getHumidityValue());
            pstmt.setFloat(11, mp.getPressureValue());
            pstmt.setFloat(12, mp.getWindSpeedValue());
            pstmt.setFloat(13, mp.getWindDirectionValue());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(mp.getLastUpdate());          
            pstmt.setDate(14, java.sql.Date.valueOf(date));

            pstmt.executeUpdate();
            
            System.out.println("Podaci za adresu " + adresa + " upisani u tablicu meteo");
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DodajAdresu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.student8.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.web.PreuzmiMeteoPodatke;

/**
 * Web application lifecycle listener.
 * Klasa SlusacAplikacije sluzi za pripremanje aplikacije za normalno izvrsavanje
 * @author grupa_1
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    static private ServletContext context = null;
    private PreuzmiMeteoPodatke pmp;

    public static ServletContext getContext() {
        return context;
    }
    
    /**
     * U metodi se trazi lokacija datoteke konfiguracije te
     * se pokrece dretva
     * @param sce 
     */

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String konfiguracija = context.getInitParameter("konfiguracija");
        String putanje = context.getRealPath("/WEB-INF") + File.separator;

        BP_Konfiguracija konfig = null;
        try {
            konfig = new BP_Konfiguracija(putanje + konfiguracija);
            context.setAttribute("BP_Konfig", konfig);
            System.out.println("Ucitana konfiguracija.");
            pmp = new PreuzmiMeteoPodatke(konfig);
            pmp.start();
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * U ovoj metodi izvrsavaju se akcije za zaustavljanje dretve
     * @param sce 
     */

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(pmp != null && pmp.isAlive()) {
            pmp.interrupt();
            System.out.println("Dretva zaustavljena");
        }
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.ws.serveri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.student8.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.student8.rest.klijenti.GMKlijent;
import org.foi.nwtis.student8.rest.klijenti.OWMKlijent;
import org.foi.nwtis.student8.web.PreuzmiMeteoPodatke;
import org.foi.nwtis.student8.web.podaci.Adresa;
import org.foi.nwtis.student8.web.podaci.Lokacija;
import org.foi.nwtis.student8.web.podaci.MeteoPodaci;

/**
 * SOAP servis koji nudi cetiri operacije dajSveAdrese() ...
 * dajSveMeteoPodatkeZaAdresu
 * @author grupa_1
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    @Resource
    private WebServiceContext context;

    private static int brojac = 0;
    private BP_Konfiguracija konfig = null;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajSveAdrese")
    public java.util.List<Adresa> dajSveAdrese() {
        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        this.konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        List<Adresa> listaAdresa = new ArrayList<>();
        GMKlijent gmk = new GMKlijent();
        String upit = "SELECT * FROM ADRESE";

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection conn = DriverManager.getConnection(konfig.getServerDatabase() + konfig.getUserDatabase(), konfig.getAdminUsername(), konfig.getAdminPassword());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(upit);

            while (rs.next()) {
                Lokacija lokacija = gmk.getGeoLocation(rs.getString("ADRESA"));
                Adresa adresa = new Adresa(brojac++, rs.getString("ADRESA"), lokacija);
                listaAdresa.add(adresa);
            }

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaAdresa;
    }

    Adresa dajAdresu(String adresa) {

        GMKlijent gmk = new GMKlijent();
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        Adresa a = new Adresa(brojac++, adresa, lokacija);

        return a;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajVazeceMeteoPodatkeZaAdresu")
    public MeteoPodaci dajVazeceMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        Adresa a = dajAdresu(adresa);
        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        this.konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");
        String APPID = konfig.getAPPID();

        OWMKlijent owmk = new OWMKlijent(APPID);
        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatkeZaAdresu")
    public MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        String upit = "SELECT T1.* FROM METEO AS T1 WHERE T1.IDMETEO = (SELECT MAX(IDMETEO) FROM METEO AS T2 WHERE T2.ADRESASTANICE='" + adresa + "')";
        MeteoPodaci mp = null;

        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        this.konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection conn = DriverManager.getConnection(konfig.getServerDatabase() + konfig.getUserDatabase(), konfig.getAdminUsername(), konfig.getAdminPassword());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(upit);

            while (rs.next()) {
                mp = new MeteoPodaci();

                mp.setTemperatureValue(rs.getFloat("TEMP"));
                mp.setTemperatureMin(rs.getFloat("TEMPMIN"));
                mp.setTemperatureMax(rs.getFloat("TEMPMAX"));
                mp.setHumidityValue(rs.getFloat("VLAGA"));
                mp.setPressureValue(rs.getFloat("TLAK"));
                mp.setWindSpeedValue(rs.getFloat("VJETAR"));
                mp.setWindDirectionValue(rs.getFloat("VJETARSMJER"));
                mp.setLastUpdate(rs.getDate("PREUZETO"));
                mp.setWeatherValue(rs.getString("VRIJEME"));
                mp.setWeatherNumber(rs.getInt("VRIJEMEOPIS"));
            }

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mp;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaAdresu")
    public ArrayList<MeteoPodaci> dajSveMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        String upit = "SELECT * FROM METEO WHERE ADRESASTANICE='" + adresa + "'";
        ArrayList<MeteoPodaci> listaMP = null;

        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        this.konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection conn = DriverManager.getConnection(konfig.getServerDatabase() + konfig.getUserDatabase(), konfig.getAdminUsername(), konfig.getAdminPassword());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(upit);
            listaMP = new ArrayList<>();

            while (rs.next()) {
                MeteoPodaci mp = new MeteoPodaci();

                mp.setTemperatureValue(rs.getFloat("TEMP"));
                mp.setTemperatureMin(rs.getFloat("TEMPMIN"));
                mp.setTemperatureMax(rs.getFloat("TEMPMAX"));
                mp.setHumidityValue(rs.getFloat("VLAGA"));
                mp.setPressureValue(rs.getFloat("TLAK"));
                mp.setWindSpeedValue(rs.getFloat("VJETAR"));
                mp.setWindDirectionValue(rs.getFloat("VJETARSMJER"));
                mp.setLastUpdate(rs.getDate("PREUZETO"));
                mp.setWeatherValue(rs.getString("VRIJEME"));
                mp.setWeatherNumber(rs.getInt("VRIJEMEOPIS"));

                listaMP.add(mp);
            }

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaMP;
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.web.zrna;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.xml.ws.WebServiceRef;
import org.foi.nwtis.student8.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.student8.ws.serveri.Adresa;
import org.foi.nwtis.student8.ws.serveri.GeoMeteoWS_Service;
import org.foi.nwtis.student8.ws.serveri.MeteoPodaci;

/**
 * Bean OdabirAdresa sluzi za kontroliranje akcije na istoimenoj JSF stranici
 * @author grupa_1
 */
@Named(value = "odabirAdresa")
@SessionScoped
public class OdabirAdresa implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/student8_zadaca_3_1/GeoMeteoWS.wsdl")
    private GeoMeteoWS_Service service;

    private List<Adresa> adrese;
    private String odabranaAdresa;
    private Map<String, Object> listaAdresa = new LinkedHashMap<>();

    private String vrijeme = "";
    private int vrijemeOpis = 0;
    private Date preuzeto = null;
    private float temp = 0;
    private float tempMax = 0;
    private float tempMin = 0;
    private float vlaga = 0;
    private float tlak = 0;
    private float vjetar = 0;
    private float vjetarSmjer = 0;

    /**
     * Creates a new instance of OdabirAdresa
     */
    public OdabirAdresa() {
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public int getVrijemeOpis() {
        return vrijemeOpis;
    }

    public void setVrijemeOpis(int vrijemeOpis) {
        this.vrijemeOpis = vrijemeOpis;
    }

    public Date getPreuzeto() {
        return preuzeto;
    }

    public void setPreuzeto(Date preuzeto) {
        this.preuzeto = preuzeto;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getVjetar() {
        return vjetar;
    }

    public void setVjetar(float vjetar) {
        this.vjetar = vjetar;
    }

    public float getVlaga() {
        return vlaga;
    }

    public void setVlaga(float vlaga) {
        this.vlaga = vlaga;
    }

    public float getTlak() {
        return tlak;
    }

    public void setTlak(float tlak) {
        this.tlak = tlak;
    }

    public float getVjetarSmjer() {
        return vjetarSmjer;
    }

    public void setVjetarSmjer(float vjetarSmjer) {
        this.vjetarSmjer = vjetarSmjer;
    }

    public List<Adresa> getAdrese() {
        adrese = MeteoWSKlijent.dajSveAdrese();
        return adrese;
    }

    public void setAdrese(List<Adresa> adrese) {
        this.adrese = adrese;
    }

    public String getOdabranaAdresa() {
        return odabranaAdresa;
    }

    public void setOdabranaAdresa(String odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    public void spremiPodatke() {
        MeteoPodaci mp = dajVazeceMeteoPodatkeZaAdresu(this.odabranaAdresa);

        setTemp(mp.getTemperatureValue());
        setTempMax(mp.getTemperatureMax());
        setTempMin(mp.getTemperatureMin());
        setVlaga(mp.getHumidityValue());
        setTlak(mp.getPressureValue());
        setVjetar(mp.getWindSpeedValue());
        setVjetarSmjer(mp.getWindDirectionValue());
        setVrijeme(mp.getWeatherValue());
        setVrijemeOpis(mp.getWeatherNumber());
    }

    public Map<String, Object> getListaAdresa() {
        adrese = MeteoWSKlijent.dajSveAdrese();
        listaAdresa = new LinkedHashMap<>();
        for (Adresa a : adrese) {
            listaAdresa.put(a.getAdresa(), a.getAdresa());
        }
        return listaAdresa;
    }

    public void setListaAdresa(Map<String, Object> listaAdresa) {
        this.listaAdresa = listaAdresa;

    }

    private MeteoPodaci dajVazeceMeteoPodatkeZaAdresu(java.lang.String adresa) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.student8.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatkeZaAdresu(adresa);
    }

    public String getWelcomeMessage() {

        StringBuilder sb = new StringBuilder();
        if (this.odabranaAdresa != null) {
            sb.append("Lokacija: " + this.odabranaAdresa);
            sb.append("\n");
            sb.append("Temperatura: " + this.temp);
            sb.append("\n");
            sb.append("Maksimalna temperatura: " + this.tempMax);
            sb.append("\n");
            sb.append("Minimalna temperatura: " + this.tempMin);
            sb.append("\n");
            sb.append("Vlaga: " + this.vlaga);
            sb.append("\n");
            sb.append("Tlak: " + this.tlak);
            sb.append("\n");
            sb.append("Vjetar: " + this.vjetar);
            sb.append("\n");
            sb.append("Vjetar smjer: " + this.vjetarSmjer);
            sb.append("\n");
            sb.append("Vrijeme: " + this.vrijeme);
            sb.append("\n");
            sb.append("Vrijeme opis: " + this.vrijemeOpis);
            sb.append("\n");
        }
        return sb.toString();
    }
}/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student8.ws.klijenti;

/**
 * Klasa MeteoWSKlijent služi za pozivanje operacije ws-a dajSveAdrese()
 * @author Sname6
 */
public class MeteoWSKlijent {

    public static java.util.List<org.foi.nwtis.student8.ws.serveri.Adresa> dajSveAdrese() {
        org.foi.nwtis.student8.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.student8.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.student8.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveAdrese();
    }
    
}