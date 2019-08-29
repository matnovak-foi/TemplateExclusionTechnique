

public class GMKlijent {
    /**
     * Metoda prima jedan parametar(adresa) te zatim priprema zahtjev te ga stavlja u JSON format
     * Ukoliko je parametar korektan vraća objekt Lokacije, inače vraća null
     * @param adresa
     * @return 
     */

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

*/

public class OWMKlijent {
}/*

*/
public class ApplicationConfig extends Application {
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
public class MeteoRESTResource {
/*
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
public class MeteoRESTResourceContainer {
/*
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
}
    }
}/*
*/

public class Adresa {
}/*

*/
public class Lokacija {
}/*

*/
public class MeteoPodaci {
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

*/
public class SlusacAplikacije implements ServletContextListener {
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
        
    }
public MeteoPodaci dajVazeceMeteoPodatkeZaAdresu( String adresa) {
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
*/
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

    public void spremiPodatke() {
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
*/
