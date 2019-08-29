

import org.foi.nwtis.student11.ejb.eb.Cities;
import org.foi.nwtis.student11.ejb.eb.States;

/**
 *
 * @author Sname1
 */
@Stateless
public class CitiesFacade extends AbstractFacade<Cities> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitiesFacade() {
        super(Cities.class);
    }
    
    /**
     * Metoda koja korištenje Criteria API upita pretražuje gradove na temelju država. Također
     * sortira gradove prema njegovom nazivu, abecedno.
     * @param drzava
     * @return 
     */
    public List<Cities> filtrirajGradove(Set<String> drzava){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Cities> gradovi = cq.from(Cities.class);
        cq.select(gradovi);
        cq.where(gradovi.<String>get("citiesPK").<String>get("state").in(drzava));
        cq.orderBy(cb.asc(gradovi.<String>get("name")));
        return em.createQuery(cq).getResultList();
    }
    /**
     * Metoda koja služi za filtriranje gradova prema njegovom nazivu
     * @param drzava
     * @param naziv
     * @return 
     */
    public List<Cities> filtrirajGradove(Set<String> drzava, String naziv){

}
    
}
public class StatesFacade extends AbstractFacade<States> {

    public List<States> filtrirajDrzave(String naziv){
        return em.createQuery(cq).getResultList();
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.foi.nwtis.student11.ejb.eb.Cities;
import org.foi.nwtis.student11.ejb.eb.ZipCodes;

/**
 *
 * @author Sname1
 */
@Stateless
public class ZipCodesFacade extends AbstractFacade<ZipCodes> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZipCodesFacade() {
        super(ZipCodes.class);
    }
    
        /**
         * Metoda koja putem Criteria API vraća zip kodove na temelju država. 
         * @param grad
         * @return 
         */
        public List<ZipCodes> filtrirajZipKodove(Set<String> grad){
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<ZipCodes> zipovi = cq.from(ZipCodes.class);
        
        cq.select(zipovi);
        cq.where(zipovi.<String>get("cities").<String>get("citiesPK").<String>get("state").in(grad));
        return em.createQuery(cq).getResultList();

    }

}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.WebServiceRef;
import net.wxbug.api.LiveWeatherData;
import net.wxbug.api.UnitType;
import net.wxbug.api.WeatherBugWebServices;

/**
 * Klasa koja se povezuje na WeatherBug servis i dohvaca meteo podatke.
 * @author Sname1
 */
@Stateless
@LocalBean
public class WeatherBugKlijent {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/api.wxbug.net/weatherservice.asmx.wsdl")
    private WeatherBugWebServices service;
    //private String mjWeatherBugKod = "A3776107717";
    
    public static String wb_code;

    public static String getWb_code() {
        return wb_code;
    }

    public static void setWb_code(String wb_code) {
        WeatherBugKlijent.wb_code = wb_code;
    }
    
    /**
     * Metoda koja dohvaca meteo podatke. Prosljeđuje joj se statička varijabla wb_code koja sadrži
     * WeatherBug API Code.
     * @param zip
     * @return 
     */

}
public class LiveWeatherData {
}
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.student11.ejb.eb.Cities;
import org.foi.nwtis.student11.ejb.eb.States;
import org.foi.nwtis.student11.ejb.eb.ZipCodes;
import org.foi.nwtis.student11.ejb.sb.CitiesFacade;
import org.foi.nwtis.student11.ejb.sb.StatesFacade;
import org.foi.nwtis.student11.ejb.sb.WeatherBugKlijent;
import static org.foi.nwtis.student11.ejb.sb.WeatherBugKlijent.wb_code;
import org.foi.nwtis.student11.ejb.sb.ZipCodesFacade;

/**
 * Klasa koja služi za dohvacanje država, gradova i zip kodova.
 *
 * @author Sname1
 */
@Named(value = "odabiZipKodovaZaGradove")
@SessionScoped
public class OdabiZipKodovaZaGradove implements Serializable {

    @EJB
    private WeatherBugKlijent weatherBugKlijent;
    @EJB
    private ZipCodesFacade zipCodesFacade;
    @EJB
    private CitiesFacade citiesFacade;
    @EJB
    private StatesFacade statesFacade;
    private String filterDrzava;
    private Map<String, Object> popisDrzava;
    private List<String> popisDrzavaOdabrano;
    private Map<String, Object> odabraneDrzave;
    private List<String> odabraneDrzaveOdabrano;
    private String filterGradova;
    private Map<String, Object> popisGradova;
    private List<String> popisGradovaOdabrano;
    private Map<String, Object> odabraniGradovi;
    private List<String> odabraniGradoviOdabrano;
    private String filterZipKodova;
    private Map<String, Object> popisZipKodova;
    private List<String> popisZipKodovaOdabrano;
    private Map<String, Object> odabraniZipKodovi;
    private List<String> odabraniZipKodoviOdabrano;
    LiveWeatherData live = new LiveWeatherData();
    private ArrayList<LiveWeatherData> podaci = new ArrayList<LiveWeatherData>();

    public String getFilterZipKodova() {
        return filterZipKodova;
    }

    public void setFilterZipKodova(String filterZipKodova) {
        this.filterZipKodova = filterZipKodova;
    }

    /**
     * Metoda koja dohvaća zip kodove na temelju odabranih država. Ispis je u
     * obliku ZIP - DRŽAVA - OKRUG - GRAD. Lista zip kodova se puni jedino ako
     * se nalazi barem jedan zapis u listi odabranih gradova.
     *
     * @return
     */
    public Map<String, Object> getPopisZipKodova() {
        popisZipKodova = new TreeMap<String, Object>();
        if (odabraniGradovi == null || odabraniGradovi.isEmpty()) {
            return popisZipKodova;
        }
        List<ZipCodes> zipovi;


        if (filterZipKodova == null || filterZipKodova.trim().isEmpty()) {
            zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());
        } else {

            zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());
        }
        for (ZipCodes zip : zipovi) {
            String z = zip.getZip() + " - " + zip.getCities().getCitiesPK().getState()
                    + " - " + zip.getCities().getCitiesPK().getCounty() + " - "
                    + zip.getCities().getCitiesPK().getCity();


            popisZipKodova.put(z, z);
        }
        return popisZipKodova;
    }

    /**
     * Metoda koja služi za dohvaćanje gradova na temelju odabranih država.
     * Ispis gradova je u obliku DRŽAVA - OKRUG - GRAD. Implementirano je
     * filtiranje gradova prema nazivu grada. Ukoliko je filter prazan prikazuju
     * se svi gradovi na temelju odabranih država, a ukoliko filter sadrži
     * ključnu riječ prikazuju se gradovi koji sadrže tu ključnu riječ u nazivu
     * grada.
     *
     * @return
     */
    public Map<String, Object> getPopisGradova() {
        popisGradova = new LinkedHashMap<String, Object>();
        if (odabraneDrzave == null || odabraneDrzave.isEmpty()) {
            return popisGradova;
        }

        List<Cities> gradovi;
}
    public String getFilterDrzava() {
        return filterDrzava;
    }

    public void setFilterDrzava(String filterDrzava) {
        this.filterDrzava = filterDrzava;
    }

    /**
     * Metoda kojom se dohvaćaju države. Ispisuju se sve države iz baze, te je
     * implementiran filter na temelju kojeg se može pretraživati država prema
     * ključnoj riječi.
     *
     * @return
     */
    public Map<String, Object> getPopisDrzava() {
        popisDrzava = new TreeMap<String, Object>();
}
    /**
     * Creates a new instance of OdabiZipKodovaZaGradove
     */
    public OdabiZipKodovaZaGradove() {
    }

    public String getFilterGradova() {
        return filterGradova;
    }

    public void setFilterGradova(String filterGradova) {
        this.filterGradova = filterGradova;
    }

    public void setPopisGradova(Map<String, Object> popisGradova) {
        this.popisGradova = popisGradova;
    }

    public List<String> getPopisGradovaOdabrano() {
        return popisGradovaOdabrano;
    }

    public void setPopisGradovaOdabrano(List<String> popisGradovaOdabrano) {
        this.popisGradovaOdabrano = popisGradovaOdabrano;
    }

    public Map<String, Object> getOdabraniGradovi() {
        return odabraniGradovi;
    }

    public void setOdabraniGradovi(Map<String, Object> odabraniGradovi) {
        this.odabraniGradovi = odabraniGradovi;
    }

    public List<String> getOdabraniGradoviOdabrano() {
        return odabraniGradoviOdabrano;
    }

    public void setOdabraniGradoviOdabrano(List<String> odabraniGradoviOdabrano) {
        this.odabraniGradoviOdabrano = odabraniGradoviOdabrano;
    }

    /**
     * Metoda koja služi za dodavanje država u listu odabranih država. Ukoliko
     * nije odabrana ni jedna država, ništa se ne radi, odnosno vraća se prazan
     * string.
     *
     * @return
     */
    public String dodajDrzavu() {
        if (popisDrzavaOdabrano == null || popisDrzavaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraneDrzave == null) {
            odabraneDrzave = new TreeMap<String, Object>();
        }
        for (String d : popisDrzavaOdabrano) {
            odabraneDrzave.put(d, d);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih država.
     *
     * @return
     */
    public String obrisiDrzavu() {
        if (odabraneDrzave != null && !odabraneDrzave.isEmpty() && odabraneDrzaveOdabrano != null && !odabraneDrzaveOdabrano.isEmpty()) {
            for (String d : odabraneDrzaveOdabrano) {
                odabraneDrzave.remove(d);
            }
        }
        return "";
    }

    /**
     * Metoda koja služi za dodavanje gradova u listu odabranih gradova. Ukoliko
     * nije odabran ni jedan grad, vraća se prazan string.
     *
     * @return
     */
    public String dodajGrad() {
        if (popisGradovaOdabrano == null || popisGradovaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraniGradovi == null) {
            odabraniGradovi = new TreeMap<String, Object>();
        }
        for (String d : popisGradovaOdabrano) {
            odabraniGradovi.put(d, d);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih gradova.
     *
     * @return
     */
    public String obrisiGrad() {
        if (odabraniGradovi != null && !odabraniGradovi.isEmpty()
                && odabraniGradoviOdabrano != null && !odabraniGradoviOdabrano.isEmpty()) {
            for (String g : odabraniGradoviOdabrano) {
                odabraniGradovi.remove(g);
            }
        }
        return "";
    }

    /**
     * Metoda koja služi za dohvaćanje gradova, te punjenje liste popisGradova,
     * na temelju odabranih država.
     *
     * @return
     */
    public String preuzmiGradove() {
        popisGradova = new TreeMap<String, Object>();
        if (odabraneDrzave == null || odabraneDrzave.isEmpty()) {
            return "";
        }

        List<Cities> gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet());
        for (Cities grad : gradovi) {
            popisGradova.put(grad.getName(), grad.getName());
        }
        return "";
    }

    /**
     * Metoda koja služi za preuzimanje zip kodova, te punjenje liste
     * popisZipKodova, na temelju odabranih država.
     *
     * @return
     */
    public String preuzmiZipKodove() {

        popisZipKodova = new TreeMap<String, Object>();
        if (odabraniGradovi == null || odabraniGradovi.isEmpty()) {
            return "";
        }
        //List<ZipCodes> zipovi = zipCodesFacade.findAll();
        List<ZipCodes> zipovi = zipCodesFacade.filtrirajZipKodove(odabraneDrzave.keySet());

        for (ZipCodes zip : zipovi) {
            popisZipKodova.put(zip.getZip().toString(), zip.getZip().toString());
        }

        return "";


    }

    /**
     * Metoda koja služi za dodavanje zip kodova u listu odabranih zip kodova.
     *
     * @return
     */
    public String dodajZipKod() {
        if (popisZipKodovaOdabrano == null || popisZipKodovaOdabrano.isEmpty()) {
            return "";
        }
        if (odabraniZipKodovi == null) {
            odabraniZipKodovi = new TreeMap<String, Object>();
        }
        for (String z : popisZipKodovaOdabrano) {
            odabraniZipKodovi.put(z, z);
        }
        return "";
    }

    /**
     * Metoda koja služi za brisanje odabranih zip kodova.
     *
     * @return
     */
    public String obrisiZipKod() {
        if (odabraniZipKodovi != null && !odabraniZipKodovi.isEmpty()
                && odabraniZipKodoviOdabrano != null && !odabraniZipKodoviOdabrano.isEmpty()) {
            for (String z : odabraniZipKodoviOdabrano) {
                odabraniZipKodovi.remove(z);
            }
        }
        return "";
    }

    /**
     * Metoda koja dohvaća WeatherBug code iz web.xml-a i sprema ga u varijablu
     * wb_code. Na temelju odabranih zip kodova prikazuje meteo podatke.
     *
     * @return
     */
    public String preuzmiMeteoPodatke() {

        if (odabraniZipKodovi == null || odabraniZipKodovi.isEmpty()) {
            return "";
        }
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            wb_code = (String) env.lookup("wb_code");
            System.out.println("kod: " + wb_code);
        } catch (NamingException ex) {
            Logger.getLogger(WeatherBugKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }


        podaci.clear();
        List<Object> list = new ArrayList<Object>(odabraniZipKodovi.values());
        for (int i = 0; i < list.size(); i++) {
            live = weatherBugKlijent.dajMeteoPodatke(list.get(i).toString());
            podaci.add(live);

}


return "";
    }

    
}