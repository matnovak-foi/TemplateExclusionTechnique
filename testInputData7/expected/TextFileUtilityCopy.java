

public class CitiesFacade extends AbstractFacade<Cities> {
    /**
     * u metodi filtrirajGradove se pomoću criteria API - dohvaćaju gradovi na temelju odabranih država koje se šalju funkciji kao parametar
     * iz klase Cities se traže gradovi koji su u državi koja je poslana kao parametar
     * @param drzava parametar koji se šalje, sadrži listu država iz kojih želimo dobit gradove
     * @return vraća se popis gradova koji se nalaze u odabranim državama
     */
    public List<Cities> filtrirajGradove(Set<String> drzava) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Cities> gradovi = cq.from(Cities.class);
        cq.select(gradovi);
        cq.where(gradovi.<String>get("citiesPK").<String>get("state").in(drzava));
        
        return em.createQuery(cq).getResultList();
    }
    /**
     *  u metodi filtrirajGradove se pomoću criteria API - dohvaćaju gradovi na temelju odabranih država koje se šalju funkciji kao parametar
     * iz klase Cities se traže gradovi koji su u državi koja je poslana kao parametar
     * @param drzava parametar koji se šalje, sadrži listu država iz kojih želimo dobit gradove
     * @param naziv slova koja su unešena u filter gradova, da se dobe samo gradovi koji sadržavaju unesena slova
     * @return vraćaju se gradovi koji sadržavaju slova iz filtera
     */

}
public class StatesFacade extends AbstractFacade<States> {
    /**
     * u metodi filtrirajDrzave se pomoću criteria API - dohvaćaju države na temelju unešenih slova u filter koje se šalju u parametru.
     * iz klase States se traže države koje počinju sa slovima unesenim kao parametar
     * @param naziv slova upisana u filter država
     * @return lista država koja počinje na slova unešena u filter država
     */
    public List<States> filtrirajDrzave(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<States> drzave = cq.from(States.class);
        cq.select(drzave);
        cq.where(cb.like(drzave.<String>get("name"), naziv + "%"));
        
        return em.createQuery(cq).getResultList();
        
    
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student10.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.student10.ejb.eb.Cities;
import org.foi.nwtis.student10.ejb.eb.ZipCodes;

/**
 *
 * @author nwtis_3
 */
@Stateless
public class ZipCodesFacade extends AbstractFacade<ZipCodes> {

    @PersistenceContext(unitName = "student10_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZipCodesFacade() {
        super(ZipCodes.class);
    }

    /**
     * u metodi filtrirajZipove se pomoću criteria API - dohvaćaju zipovi na temelju odabranih gradova koje se šalju kao parametar.
     * najprije se parametar razdvaja tako da ostaje samo naziv grada te se on dalje koristi
     * iz klase ZipCodes se traže zipovi koji sadrže uneseni grad koji je poslan kao parametar
     * @param cities geradovi za koje se traže zip kodovi, razdvaja se da ostane samo naziv grada
     * @return lista zip kodova za odabrane gradove
     */
    public List<ZipCodes> filtrirajZipove(Set<String> cities) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<ZipCodes> zipCodes = cq.from(ZipCodes.class);
        cq.select(zipCodes);
        zipCodes.<String>get("cities").<String>get("name");
        ArrayList<String> zipovi = new ArrayList<String>();
        for (String str : cities) {
            zipovi.add(str.substring(str.lastIndexOf("-") + 2));
        }
        cq.where(zipCodes.<String>get("cities").<String>get("name").in(zipovi));
        return em.createQuery(cq).getResultList();
    }

    public List<ZipCodes> filtrirajZipove(Set<String> cities, String naziv) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<ZipCodes> zipCodes = cq.from(ZipCodes.class);
        cq.select(zipCodes);
        zipCodes.<String>get("cities").<String>get("name");
        ArrayList<String> zipovi = new ArrayList<String>();
        for (String str : cities) {
            zipovi.add(str.substring(str.lastIndexOf("-") + 2));
        }

        cq.where(cb.and(
                zipCodes.<String>get("cities").<String>get("name").in(zipovi),
                zipCodes.<String>get("zip").in(naziv)));

        return em.createQuery(cq).getResultList();
    }
}

public class WeatherBugKlijent {
    private String mjWeatherBugKod = "A6444976485";

    public LiveWeatherData dajMeteoPodatke(String zip) {
    return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, mjWeatherBugKod);
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.student10.ejb.eb.Cities;
import org.foi.nwtis.student10.ejb.eb.States;
import org.foi.nwtis.student10.ejb.eb.ZipCodes;
import org.foi.nwtis.student10.ejb.sb.CitiesFacade;
import org.foi.nwtis.student10.ejb.sb.StatesFacade;
import org.foi.nwtis.student10.ejb.sb.WeatherBugKlijent;
import org.foi.nwtis.student10.ejb.sb.ZipCodesFacade;

/**
 *
 * @author nwtis_3
 * Ova klasa služi za pozivanje država, gradova, zip kodova, pozivanje njihovog filtriranja,
 * pripremanje podataka za ispis
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
    
    private String filterZipova;
    private Map<String, Object> popisZipova;
    private List<String> popisZipovaOdabrano;
    private Map<String, Object> odabraniZipovi;
    private List<String> odabraniZipoviOdabrano;
    private List<LiveWeatherData> meteoPodaci;

    /**
     * Creates a new instance of OdabiZipKodovaZaGradove
     */
    public OdabiZipKodovaZaGradove() {
    }

    public String getFilterDrzava() {
        return filterDrzava;
    }

    public void setFilterDrzava(String filterDrzava) {
        this.filterDrzava = filterDrzava;
    }

    public Map<String, Object> getPopisDrzava() {
        popisDrzava = new TreeMap<String, Object>();

        List<States> drzave;
        if (filterDrzava == null || filterDrzava.trim().isEmpty()){        
        drzave = statesFacade.findAll();
        }else{
        drzave = statesFacade.filtrirajDrzave(filterDrzava.toUpperCase());
        }
        for (States d : drzave) {
            popisDrzava.put(d.getName(), d.getName());
        }

}
public Map<String, Object> getPopisGradova() {

        if (odabraneDrzave == null || odabraneDrzave.isEmpty()){}
        
        List<Cities> gradovi;
        if (filterGradova == null || filterGradova.trim().isEmpty()){
            gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet());
        }else {
            
            gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet(), filterGradova.toUpperCase());
        }
        
        for (Cities grad : gradovi){
          
            String g = grad.getCitiesPK().getState() + " - "
                    + grad.getCitiesPK().getCounty() + " - "
                    + grad.getCitiesPK().getCity(); 
            
            //TODO ključ citi c state za sortiranje
        popisGradova.put(g, g);
        }   
        return popisGradova;
}
public Map<String, Object> getPopisZipova() {

        if (odabraniGradovi == null || odabraniGradovi.isEmpty()){}
        for (ZipCodes zip : zipovi){
           Cities zas = zip.getCities();
          
          String ispis = zip.getZip().toString() + " - " + zas.getCitiesPK().getState() +
                           " - " + zas.getCitiesPK().getCounty() + 
                            " - " + zas.getCitiesPK().getCity();
        popisZipova.put(ispis, ispis);
        }
        
        return popisZipova;
    }

    public void setPopisZipova(Map<String, Object> popisZipova) {
        this.popisZipova = popisZipova;
    }

    public List<String> getPopisZipovaOdabrano() {
        return popisZipovaOdabrano;
    }

    public void setPopisZipovaOdabrano(List<String> popisZipovaOdabrano) {
        this.popisZipovaOdabrano = popisZipovaOdabrano;
    }

    public Map<String, Object> getOdabraniZipovi() {
        return odabraniZipovi;
    }

    public void setOdabraniZipovi(Map<String, Object> odabraniZipovi) {
        this.odabraniZipovi = odabraniZipovi;
    }

    public List<String> getOdabraniZipoviOdabrano() {
        return odabraniZipoviOdabrano;
    }

    public void setOdabraniZipoviOdabrano(List<String> odabraniZipoviOdabrano) {
        this.odabraniZipoviOdabrano = odabraniZipoviOdabrano;
    }
    
    /**
     * metoda dodajDrzavu dodaje državu na popis odabranih država ukoliko je drzava označena te več nije odabrana
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
     * metoda obrisiDrzavu uklanja odabranu drzavu s popisa odabranih drzava ukoliko je odabrana
     * @return 
*/
    
    /**
     * metoda dodajGrad dodaje grad na popis odabranih gradova ukoliko je grad odabran te več nije na popisu
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
public String dodajZip() {
        
 if (RCCdummy) {}
        
        
    return "";
    }
    
    /**
     * metoda obrisiGrad brise odabrani grad s popisa odabranih gradova
     * @return 
     */
    public String obrisiGrad() {
        if(odabraniGradovi != null && !odabraniGradovi.isEmpty() &&
                odabraniGradoviOdabrano != null &&
                !odabraniGradoviOdabrano.isEmpty()){
        for (String g : odabraniGradoviOdabrano){
        odabraniGradovi.remove(g);
        }
        }
        
    return "";
    }
    
    /**
     * metoda obrisiZip brise odabrani zip kod sa popisa odabranih zip kodova
     * @return 
     */
    public String obrisiZip() {
        if(odabraniZipovi != null && !odabraniZipovi.isEmpty() &&
                odabraniZipoviOdabrano != null &&
                !odabraniZipoviOdabrano.isEmpty()){
        for (String y : odabraniZipoviOdabrano){
        odabraniZipovi.remove(y);
        }
        }
        
    return "";
    }
    
    /**
     * metoda dohvatiZipKodove stvara novu listu zip kodova na temelju odabranih gradova ukoliko su gradovi odabrani
     * @return 
     */
    public String dohvatiZipKodove() {
        popisZipova = new TreeMap<String, Object>();
        if (odabraniGradovi == null || odabraniGradovi.isEmpty()){
        return "";
        }
        
        
           List<ZipCodes>  zipovi = zipCodesFacade.filtrirajZipove(odabraniGradovi.keySet());
for (ZipCodes zip : zipovi){
        popisZipova.put(zip.getZip().toString(), zip.getZip());
        }
            
    return "";
    }

    public List<LiveWeatherData> getMeteoPodaci() {
        return meteoPodaci;
}
    
    
}
