/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "CITIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cities.findAll", query = "SELECT c FROM Cities c"),
    @NamedQuery(name = "Cities.findByState", query = "SELECT c FROM Cities c WHERE c.citiesPK.state = :state"),
    @NamedQuery(name = "Cities.findByCounty", query = "SELECT c FROM Cities c WHERE c.citiesPK.county = :county"),
    @NamedQuery(name = "Cities.findByCity", query = "SELECT c FROM Cities c WHERE c.citiesPK.city = :city"),
    @NamedQuery(name = "Cities.findByName", query = "SELECT c FROM Cities c WHERE c.name = :name")})
public class Cities implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CitiesPK citiesPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NAME")
    private String name;
    @JoinColumns({
        @JoinColumn(name = "STATE", referencedColumnName = "STATE", insertable = false, updatable = false),
        @JoinColumn(name = "COUNTY", referencedColumnName = "COUNTY", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Counties counties;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cities")
    private List<ZipCodes> zipCodesList;

    public Cities() {
    }

    public Cities(CitiesPK citiesPK) {
        this.citiesPK = citiesPK;
    }

    public Cities(CitiesPK citiesPK, String name) {
        this.citiesPK = citiesPK;
        this.name = name;
    }

    public Cities(String state, String county, String city) {
        this.citiesPK = new CitiesPK(state, county, city);
    }

    public CitiesPK getCitiesPK() {
        return citiesPK;
    }

    public void setCitiesPK(CitiesPK citiesPK) {
        this.citiesPK = citiesPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Counties getCounties() {
        return counties;
    }

    public void setCounties(Counties counties) {
        this.counties = counties;
    }

    @XmlTransient
    public List<ZipCodes> getZipCodesList() {
        return zipCodesList;
    }

    public void setZipCodesList(List<ZipCodes> zipCodesList) {
        this.zipCodesList = zipCodesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citiesPK != null ? citiesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cities)) {
            return false;
        }
        Cities other = (Cities) object;
        if ((this.citiesPK == null && other.citiesPK != null) || (this.citiesPK != null && !this.citiesPK.equals(other.citiesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.Cities[ citiesPK=" + citiesPK + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sname1
 */
@Embeddable
public class CitiesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "COUNTY")
    private String county;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CITY")
    private String city;

    public CitiesPK() {
    }

    public CitiesPK(String state, String county, String city) {
        this.state = state;
        this.county = county;
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (state != null ? state.hashCode() : 0);
        hash += (county != null ? county.hashCode() : 0);
        hash += (city != null ? city.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitiesPK)) {
            return false;
        }
        CitiesPK other = (CitiesPK) object;
        if ((this.state == null && other.state != null) || (this.state != null && !this.state.equals(other.state))) {
            return false;
        }
        if ((this.county == null && other.county != null) || (this.county != null && !this.county.equals(other.county))) {
            return false;
        }
        if ((this.city == null && other.city != null) || (this.city != null && !this.city.equals(other.city))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.CitiesPK[ state=" + state + ", county=" + county + ", city=" + city + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "COUNTIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Counties.findAll", query = "SELECT c FROM Counties c"),
    @NamedQuery(name = "Counties.findByState", query = "SELECT c FROM Counties c WHERE c.countiesPK.state = :state"),
    @NamedQuery(name = "Counties.findByCounty", query = "SELECT c FROM Counties c WHERE c.countiesPK.county = :county"),
    @NamedQuery(name = "Counties.findByName", query = "SELECT c FROM Counties c WHERE c.name = :name")})
public class Counties implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CountiesPK countiesPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "counties")
    private List<Cities> citiesList;
    @JoinColumn(name = "STATE", referencedColumnName = "STATE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private States states;

    public Counties() {
    }

    public Counties(CountiesPK countiesPK) {
        this.countiesPK = countiesPK;
    }

    public Counties(CountiesPK countiesPK, String name) {
        this.countiesPK = countiesPK;
        this.name = name;
    }

    public Counties(String state, String county) {
        this.countiesPK = new CountiesPK(state, county);
    }

    public CountiesPK getCountiesPK() {
        return countiesPK;
    }

    public void setCountiesPK(CountiesPK countiesPK) {
        this.countiesPK = countiesPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Cities> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(List<Cities> citiesList) {
        this.citiesList = citiesList;
    }

    public States getStates() {
        return states;
    }

    public void setStates(States states) {
        this.states = states;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (countiesPK != null ? countiesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Counties)) {
            return false;
        }
        Counties other = (Counties) object;
        if ((this.countiesPK == null && other.countiesPK != null) || (this.countiesPK != null && !this.countiesPK.equals(other.countiesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.Counties[ countiesPK=" + countiesPK + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sname1
 */
@Embeddable
public class CountiesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "COUNTY")
    private String county;

    public CountiesPK() {
    }

    public CountiesPK(String state, String county) {
        this.state = state;
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (state != null ? state.hashCode() : 0);
        hash += (county != null ? county.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountiesPK)) {
            return false;
        }
        CountiesPK other = (CountiesPK) object;
        if ((this.state == null && other.state != null) || (this.state != null && !this.state.equals(other.state))) {
            return false;
        }
        if ((this.county == null && other.county != null) || (this.county != null && !this.county.equals(other.county))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.CountiesPK[ state=" + state + ", county=" + county + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "GRUPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupe.findAll", query = "SELECT g FROM Grupe g"),
    @NamedQuery(name = "Grupe.findByGrIme", query = "SELECT g FROM Grupe g WHERE g.grIme = :grIme"),
    @NamedQuery(name = "Grupe.findByNaziv", query = "SELECT g FROM Grupe g WHERE g.naziv = :naziv")})
public class Grupe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "GR_IME")
    private String grIme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NAZIV")
    private String naziv;

    public Grupe() {
    }

    public Grupe(String grIme) {
        this.grIme = grIme;
    }

    public Grupe(String grIme, String naziv) {
        this.grIme = grIme;
        this.naziv = naziv;
    }

    public String getGrIme() {
        return grIme;
    }

    public void setGrIme(String grIme) {
        this.grIme = grIme;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grIme != null ? grIme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupe)) {
            return false;
        }
        Grupe other = (Grupe) object;
        if ((this.grIme == null && other.grIme != null) || (this.grIme != null && !this.grIme.equals(other.grIme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.Grupe[ grIme=" + grIme + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "POLAZNICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Polaznici.findAll", query = "SELECT p FROM Polaznici p"),
    @NamedQuery(name = "Polaznici.findByKorIme", query = "SELECT p FROM Polaznici p WHERE p.korIme = :korIme"),
    @NamedQuery(name = "Polaznici.findByIme", query = "SELECT p FROM Polaznici p WHERE p.ime = :ime"),
    @NamedQuery(name = "Polaznici.findByPrezime", query = "SELECT p FROM Polaznici p WHERE p.prezime = :prezime"),
    @NamedQuery(name = "Polaznici.findByLozinka", query = "SELECT p FROM Polaznici p WHERE p.lozinka = :lozinka"),
    @NamedQuery(name = "Polaznici.findByEmailAdresa", query = "SELECT p FROM Polaznici p WHERE p.emailAdresa = :emailAdresa"),
    @NamedQuery(name = "Polaznici.findByVrsta", query = "SELECT p FROM Polaznici p WHERE p.vrsta = :vrsta"),
    @NamedQuery(name = "Polaznici.findByDatumKreiranja", query = "SELECT p FROM Polaznici p WHERE p.datumKreiranja = :datumKreiranja"),
    @NamedQuery(name = "Polaznici.findByDatumPromjene", query = "SELECT p FROM Polaznici p WHERE p.datumPromjene = :datumPromjene")})
public class Polaznici implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "KOR_IME")
    private String korIme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "IME")
    private String ime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PREZIME")
    private String prezime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOZINKA")
    private String lozinka;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "EMAIL_ADRESA")
    private String emailAdresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VRSTA")
    private int vrsta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_KREIRANJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumKreiranja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_PROMJENE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPromjene;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "polaznici")
    private List<PolazniciGrupe> polazniciGrupeList;

    public Polaznici() {
    }

    public Polaznici(String korIme) {
        this.korIme = korIme;
    }

    public Polaznici(String korIme, String ime, String prezime, String lozinka, String emailAdresa, int vrsta, Date datumKreiranja, Date datumPromjene) {
        this.korIme = korIme;
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.emailAdresa = emailAdresa;
        this.vrsta = vrsta;
        this.datumKreiranja = datumKreiranja;
        this.datumPromjene = datumPromjene;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public Date getDatumPromjene() {
        return datumPromjene;
    }

    public void setDatumPromjene(Date datumPromjene) {
        this.datumPromjene = datumPromjene;
    }

    @XmlTransient
    public List<PolazniciGrupe> getPolazniciGrupeList() {
        return polazniciGrupeList;
    }

    public void setPolazniciGrupeList(List<PolazniciGrupe> polazniciGrupeList) {
        this.polazniciGrupeList = polazniciGrupeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (korIme != null ? korIme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Polaznici)) {
            return false;
        }
        Polaznici other = (Polaznici) object;
        if ((this.korIme == null && other.korIme != null) || (this.korIme != null && !this.korIme.equals(other.korIme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.Polaznici[ korIme=" + korIme + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "POLAZNICI_GRUPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PolazniciGrupe.findAll", query = "SELECT p FROM PolazniciGrupe p"),
    @NamedQuery(name = "PolazniciGrupe.findByKorIme", query = "SELECT p FROM PolazniciGrupe p WHERE p.polazniciGrupePK.korIme = :korIme"),
    @NamedQuery(name = "PolazniciGrupe.findByGrIme", query = "SELECT p FROM PolazniciGrupe p WHERE p.polazniciGrupePK.grIme = :grIme")})
public class PolazniciGrupe implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PolazniciGrupePK polazniciGrupePK;
    @JoinColumn(name = "KOR_IME", referencedColumnName = "KOR_IME", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Polaznici polaznici;

    public PolazniciGrupe() {
    }

    public PolazniciGrupe(PolazniciGrupePK polazniciGrupePK) {
        this.polazniciGrupePK = polazniciGrupePK;
    }

    public PolazniciGrupe(String korIme, String grIme) {
        this.polazniciGrupePK = new PolazniciGrupePK(korIme, grIme);
    }

    public PolazniciGrupePK getPolazniciGrupePK() {
        return polazniciGrupePK;
    }

    public void setPolazniciGrupePK(PolazniciGrupePK polazniciGrupePK) {
        this.polazniciGrupePK = polazniciGrupePK;
    }

    public Polaznici getPolaznici() {
        return polaznici;
    }

    public void setPolaznici(Polaznici polaznici) {
        this.polaznici = polaznici;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (polazniciGrupePK != null ? polazniciGrupePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PolazniciGrupe)) {
            return false;
        }
        PolazniciGrupe other = (PolazniciGrupe) object;
        if ((this.polazniciGrupePK == null && other.polazniciGrupePK != null) || (this.polazniciGrupePK != null && !this.polazniciGrupePK.equals(other.polazniciGrupePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.PolazniciGrupe[ polazniciGrupePK=" + polazniciGrupePK + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sname1
 */
@Embeddable
public class PolazniciGrupePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "KOR_IME")
    private String korIme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "GR_IME")
    private String grIme;

    public PolazniciGrupePK() {
    }

    public PolazniciGrupePK(String korIme, String grIme) {
        this.korIme = korIme;
        this.grIme = grIme;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getGrIme() {
        return grIme;
    }

    public void setGrIme(String grIme) {
        this.grIme = grIme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (korIme != null ? korIme.hashCode() : 0);
        hash += (grIme != null ? grIme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PolazniciGrupePK)) {
            return false;
        }
        PolazniciGrupePK other = (PolazniciGrupePK) object;
        if ((this.korIme == null && other.korIme != null) || (this.korIme != null && !this.korIme.equals(other.korIme))) {
            return false;
        }
        if ((this.grIme == null && other.grIme != null) || (this.grIme != null && !this.grIme.equals(other.grIme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.PolazniciGrupePK[ korIme=" + korIme + ", grIme=" + grIme + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "STATES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "States.findAll", query = "SELECT s FROM States s"),
    @NamedQuery(name = "States.findByState", query = "SELECT s FROM States s WHERE s.state = :state"),
    @NamedQuery(name = "States.findByName", query = "SELECT s FROM States s WHERE s.name = :name")})
public class States implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "states")
    private List<Counties> countiesList;

    public States() {
    }

    public States(String state) {
        this.state = state;
    }

    public States(String state, String name) {
        this.state = state;
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Counties> getCountiesList() {
        return countiesList;
    }

    public void setCountiesList(List<Counties> countiesList) {
        this.countiesList = countiesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (state != null ? state.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof States)) {
            return false;
        }
        States other = (States) object;
        if ((this.state == null && other.state != null) || (this.state != null && !this.state.equals(other.state))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.States[ state=" + state + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sname1
 */
@Entity
@Table(name = "ZIP_CODES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZipCodes.findAll", query = "SELECT z FROM ZipCodes z"),
    @NamedQuery(name = "ZipCodes.findByZip", query = "SELECT z FROM ZipCodes z WHERE z.zip = :zip"),
    @NamedQuery(name = "ZipCodes.findByLatitude", query = "SELECT z FROM ZipCodes z WHERE z.latitude = :latitude"),
    @NamedQuery(name = "ZipCodes.findByLongitude", query = "SELECT z FROM ZipCodes z WHERE z.longitude = :longitude"),
    @NamedQuery(name = "ZipCodes.findByZipClass", query = "SELECT z FROM ZipCodes z WHERE z.zipClass = :zipClass")})
public class ZipCodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ZIP")
    private Integer zip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private double latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private double longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ZIP_CLASS")
    private String zipClass;
    @JoinColumns({
        @JoinColumn(name = "STATE", referencedColumnName = "STATE"),
        @JoinColumn(name = "COUNTY", referencedColumnName = "COUNTY"),
        @JoinColumn(name = "CITY", referencedColumnName = "CITY")})
    @ManyToOne(optional = false)
    private Cities cities;

    public ZipCodes() {
    }

    public ZipCodes(Integer zip) {
        this.zip = zip;
    }

    public ZipCodes(Integer zip, double latitude, double longitude, String zipClass) {
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipClass = zipClass;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getZipClass() {
        return zipClass;
    }

    public void setZipClass(String zipClass) {
        this.zipClass = zipClass;
    }

    public Cities getCities() {
        return cities;
    }

    public void setCities(Cities cities) {
        this.cities = cities;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zip != null ? zip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZipCodes)) {
            return false;
        }
        ZipCodes other = (ZipCodes) object;
        if ((this.zip == null && other.zip != null) || (this.zip != null && !this.zip.equals(other.zip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.student11.ejb.eb.ZipCodes[ zip=" + zip + " ]";
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Sname1
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
        q.setMaxResults(range[1] - range[0]);
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Cities> gradovi = cq.from(Cities.class);
        cq.select(gradovi);
        cq.where(cb.and(
                gradovi.<String>get("citiesPK").<String>get("state").in(drzava),
                cb.like(gradovi.<String>get("name"), naziv + "%")));
        cq.orderBy(cb.asc(gradovi.<String>get("name")));
        return em.createQuery(cq).getResultList();
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student11.ejb.eb.Counties;

/**
 *
 * @author Sname1
 */
@Stateless
public class CountiesFacade extends AbstractFacade<Counties> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CountiesFacade() {
        super(Counties.class);
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student11.ejb.eb.Grupe;

/**
 *
 * @author Sname1
 */
@Stateless
public class GrupeFacade extends AbstractFacade<Grupe> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GrupeFacade() {
        super(Grupe.class);
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student11.ejb.eb.Polaznici;

/**
 *
 * @author Sname1
 */
@Stateless
public class PolazniciFacade extends AbstractFacade<Polaznici> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PolazniciFacade() {
        super(Polaznici.class);
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.student11.ejb.eb.PolazniciGrupe;

/**
 *
 * @author Sname1
 */
@Stateless
public class PolazniciGrupeFacade extends AbstractFacade<PolazniciGrupe> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PolazniciGrupeFacade() {
        super(PolazniciGrupe.class);
    }
    
}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.student11.ejb.eb.States;

/**
 *
 * @author Sname1
 */
@Stateless
public class StatesFacade extends AbstractFacade<States> {
    @PersistenceContext(unitName = "student11_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatesFacade() {
        super(States.class);
    }
    
    public List<States> filtrirajDrzave(String naziv){
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
    public LiveWeatherData dajMeteoPodatke(String zip) {

        
        return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, wb_code);
    }

    private LiveWeatherData getLiveWeatherByUSZipCode(java.lang.String zipCode, net.wxbug.api.UnitType unittype, java.lang.String aCode) {
        net.wxbug.api.WeatherBugWebServicesSoap port = service.getWeatherBugWebServicesSoap();
        return port.getLiveWeatherByUSZipCode(zipCode, unittype, aCode);
    }
}
package net.wxbug.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LiveWeatherData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LiveWeatherData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuxTemperature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AuxTemperatureRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrIcon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DewPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Elevation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ElevationUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FeelsLike" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GustTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="GustWindSpeed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GustWindSpeedUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GustWindDirectionString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GustWindDirectionDegrees" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Humidity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HumidityUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HumidityHigh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HumidityLow" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HumidityRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InputLocationUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MoonPhase" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MoonPhaseImage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Pressure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PressureUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PressureHigh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PressureLow" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PressureRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PressureRateUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Light" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LightRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IndoorTemperature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IndoorTemperatureRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ObDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ObDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="RainMonth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainRateMax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainRateUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainToday" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RainYear" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StationIDRequested" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StationIDReturned" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StationURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sunrise" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Sunset" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Temperature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TemperatureHigh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TemperatureLow" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TemperatureRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TemperatureRateUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TemperatureUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TimeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TimeZoneOffset" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="WebUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WetBulb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindDirectionAvg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindDirectionDegrees" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindSpeed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindSpeedAvg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WindSpeedUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ZipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LiveWeatherData", propOrder = {
    "auxTemperature",
    "auxTemperatureRate",
    "city",
    "cityCode",
    "country",
    "currIcon",
    "currDesc",
    "dewPoint",
    "elevation",
    "elevationUnit",
    "feelsLike",
    "gustTime",
    "gustWindSpeed",
    "gustWindSpeedUnit",
    "gustWindDirectionString",
    "gustWindDirectionDegrees",
    "humidity",
    "humidityUnit",
    "humidityHigh",
    "humidityLow",
    "humidityRate",
    "inputLocationUrl",
    "moonPhase",
    "moonPhaseImage",
    "pressure",
    "pressureUnit",
    "pressureHigh",
    "pressureLow",
    "pressureRate",
    "pressureRateUnit",
    "light",
    "lightRate",
    "indoorTemperature",
    "indoorTemperatureRate",
    "latitude",
    "longitude",
    "obDate",
    "obDateTime",
    "rainMonth",
    "rainRate",
    "rainRateMax",
    "rainRateUnit",
    "rainToday",
    "rainUnit",
    "rainYear",
    "state",
    "stationIDRequested",
    "stationIDReturned",
    "stationName",
    "stationURL",
    "sunrise",
    "sunset",
    "temperature",
    "temperatureHigh",
    "temperatureLow",
    "temperatureRate",
    "temperatureRateUnit",
    "temperatureUnit",
    "timeZone",
    "timeZoneOffset",
    "webUrl",
    "wetBulb",
    "windDirection",
    "windDirectionAvg",
    "windDirectionDegrees",
    "windSpeed",
    "windSpeedAvg",
    "windSpeedUnit",
    "zipCode"
})
public class LiveWeatherData {

    @XmlElement(name = "AuxTemperature")
    protected String auxTemperature;
    @XmlElement(name = "AuxTemperatureRate")
    protected String auxTemperatureRate;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "CityCode")
    protected String cityCode;
    @XmlElement(name = "Country")
    protected String country;
    @XmlElement(name = "CurrIcon")
    protected String currIcon;
    @XmlElement(name = "CurrDesc")
    protected String currDesc;
    @XmlElement(name = "DewPoint")
    protected String dewPoint;
    @XmlElement(name = "Elevation")
    protected int elevation;
    @XmlElement(name = "ElevationUnit")
    protected String elevationUnit;
    @XmlElement(name = "FeelsLike")
    protected String feelsLike;
    @XmlElement(name = "GustTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar gustTime;
    @XmlElement(name = "GustWindSpeed")
    protected String gustWindSpeed;
    @XmlElement(name = "GustWindSpeedUnit")
    protected String gustWindSpeedUnit;
    @XmlElement(name = "GustWindDirectionString")
    protected String gustWindDirectionString;
    @XmlElement(name = "GustWindDirectionDegrees")
    protected String gustWindDirectionDegrees;
    @XmlElement(name = "Humidity")
    protected String humidity;
    @XmlElement(name = "HumidityUnit")
    protected String humidityUnit;
    @XmlElement(name = "HumidityHigh")
    protected String humidityHigh;
    @XmlElement(name = "HumidityLow")
    protected String humidityLow;
    @XmlElement(name = "HumidityRate")
    protected String humidityRate;
    @XmlElement(name = "InputLocationUrl")
    protected String inputLocationUrl;
    @XmlElement(name = "MoonPhase")
    protected int moonPhase;
    @XmlElement(name = "MoonPhaseImage")
    protected String moonPhaseImage;
    @XmlElement(name = "Pressure")
    protected String pressure;
    @XmlElement(name = "PressureUnit")
    protected String pressureUnit;
    @XmlElement(name = "PressureHigh")
    protected String pressureHigh;
    @XmlElement(name = "PressureLow")
    protected String pressureLow;
    @XmlElement(name = "PressureRate")
    protected String pressureRate;
    @XmlElement(name = "PressureRateUnit")
    protected String pressureRateUnit;
    @XmlElement(name = "Light")
    protected String light;
    @XmlElement(name = "LightRate")
    protected String lightRate;
    @XmlElement(name = "IndoorTemperature")
    protected String indoorTemperature;
    @XmlElement(name = "IndoorTemperatureRate")
    protected String indoorTemperatureRate;
    @XmlElement(name = "Latitude")
    protected double latitude;
    @XmlElement(name = "Longitude")
    protected double longitude;
    @XmlElement(name = "ObDate")
    protected String obDate;
    @XmlElement(name = "ObDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar obDateTime;
    @XmlElement(name = "RainMonth")
    protected String rainMonth;
    @XmlElement(name = "RainRate")
    protected String rainRate;
    @XmlElement(name = "RainRateMax")
    protected String rainRateMax;
    @XmlElement(name = "RainRateUnit")
    protected String rainRateUnit;
    @XmlElement(name = "RainToday")
    protected String rainToday;
    @XmlElement(name = "RainUnit")
    protected String rainUnit;
    @XmlElement(name = "RainYear")
    protected String rainYear;
    @XmlElement(name = "State")
    protected String state;
    @XmlElement(name = "StationIDRequested")
    protected String stationIDRequested;
    @XmlElement(name = "StationIDReturned")
    protected String stationIDReturned;
    @XmlElement(name = "StationName")
    protected String stationName;
    @XmlElement(name = "StationURL")
    protected String stationURL;
    @XmlElement(name = "Sunrise", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sunrise;
    @XmlElement(name = "Sunset", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sunset;
    @XmlElement(name = "Temperature")
    protected String temperature;
    @XmlElement(name = "TemperatureHigh")
    protected String temperatureHigh;
    @XmlElement(name = "TemperatureLow")
    protected String temperatureLow;
    @XmlElement(name = "TemperatureRate")
    protected String temperatureRate;
    @XmlElement(name = "TemperatureRateUnit")
    protected String temperatureRateUnit;
    @XmlElement(name = "TemperatureUnit")
    protected String temperatureUnit;
    @XmlElement(name = "TimeZone")
    protected String timeZone;
    @XmlElement(name = "TimeZoneOffset")
    protected double timeZoneOffset;
    @XmlElement(name = "WebUrl")
    protected String webUrl;
    @XmlElement(name = "WetBulb")
    protected String wetBulb;
    @XmlElement(name = "WindDirection")
    protected String windDirection;
    @XmlElement(name = "WindDirectionAvg")
    protected String windDirectionAvg;
    @XmlElement(name = "WindDirectionDegrees")
    protected String windDirectionDegrees;
    @XmlElement(name = "WindSpeed")
    protected String windSpeed;
    @XmlElement(name = "WindSpeedAvg")
    protected String windSpeedAvg;
    @XmlElement(name = "WindSpeedUnit")
    protected String windSpeedUnit;
    @XmlElement(name = "ZipCode")
    protected String zipCode;

    
    /**
     * Gets the value of the auxTemperature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuxTemperature() {
        return auxTemperature;
    }

    /**
     * Sets the value of the auxTemperature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuxTemperature(String value) {
        this.auxTemperature = value;
    }

    /**
     * Gets the value of the auxTemperatureRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuxTemperatureRate() {
        return auxTemperatureRate;
    }

    /**
     * Sets the value of the auxTemperatureRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuxTemperatureRate(String value) {
        this.auxTemperatureRate = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the cityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * Sets the value of the cityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityCode(String value) {
        this.cityCode = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the currIcon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrIcon() {
        return currIcon;
    }

    /**
     * Sets the value of the currIcon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrIcon(String value) {
        this.currIcon = value;
    }

    /**
     * Gets the value of the currDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrDesc() {
        return currDesc;
    }

    /**
     * Sets the value of the currDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrDesc(String value) {
        this.currDesc = value;
    }

    /**
     * Gets the value of the dewPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDewPoint() {
        return dewPoint;
    }

    /**
     * Sets the value of the dewPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDewPoint(String value) {
        this.dewPoint = value;
    }

    /**
     * Gets the value of the elevation property.
     * 
     */
    public int getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     */
    public void setElevation(int value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the elevationUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElevationUnit() {
        return elevationUnit;
    }

    /**
     * Sets the value of the elevationUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElevationUnit(String value) {
        this.elevationUnit = value;
    }

    /**
     * Gets the value of the feelsLike property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeelsLike() {
        return feelsLike;
    }

    /**
     * Sets the value of the feelsLike property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeelsLike(String value) {
        this.feelsLike = value;
    }

    /**
     * Gets the value of the gustTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGustTime() {
        return gustTime;
    }

    /**
     * Sets the value of the gustTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGustTime(XMLGregorianCalendar value) {
        this.gustTime = value;
    }

    /**
     * Gets the value of the gustWindSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGustWindSpeed() {
        return gustWindSpeed;
    }

    /**
     * Sets the value of the gustWindSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGustWindSpeed(String value) {
        this.gustWindSpeed = value;
    }

    /**
     * Gets the value of the gustWindSpeedUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGustWindSpeedUnit() {
        return gustWindSpeedUnit;
    }

    /**
     * Sets the value of the gustWindSpeedUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGustWindSpeedUnit(String value) {
        this.gustWindSpeedUnit = value;
    }

    /**
     * Gets the value of the gustWindDirectionString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGustWindDirectionString() {
        return gustWindDirectionString;
    }

    /**
     * Sets the value of the gustWindDirectionString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGustWindDirectionString(String value) {
        this.gustWindDirectionString = value;
    }

    /**
     * Gets the value of the gustWindDirectionDegrees property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGustWindDirectionDegrees() {
        return gustWindDirectionDegrees;
    }

    /**
     * Sets the value of the gustWindDirectionDegrees property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGustWindDirectionDegrees(String value) {
        this.gustWindDirectionDegrees = value;
    }

    /**
     * Gets the value of the humidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * Sets the value of the humidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHumidity(String value) {
        this.humidity = value;
    }

    /**
     * Gets the value of the humidityUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHumidityUnit() {
        return humidityUnit;
    }

    /**
     * Sets the value of the humidityUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHumidityUnit(String value) {
        this.humidityUnit = value;
    }

    /**
     * Gets the value of the humidityHigh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHumidityHigh() {
        return humidityHigh;
    }

    /**
     * Sets the value of the humidityHigh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHumidityHigh(String value) {
        this.humidityHigh = value;
    }

    /**
     * Gets the value of the humidityLow property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHumidityLow() {
        return humidityLow;
    }

    /**
     * Sets the value of the humidityLow property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHumidityLow(String value) {
        this.humidityLow = value;
    }

    /**
     * Gets the value of the humidityRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHumidityRate() {
        return humidityRate;
    }

    /**
     * Sets the value of the humidityRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHumidityRate(String value) {
        this.humidityRate = value;
    }

    /**
     * Gets the value of the inputLocationUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputLocationUrl() {
        return inputLocationUrl;
    }

    /**
     * Sets the value of the inputLocationUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputLocationUrl(String value) {
        this.inputLocationUrl = value;
    }

    /**
     * Gets the value of the moonPhase property.
     * 
     */
    public int getMoonPhase() {
        return moonPhase;
    }

    /**
     * Sets the value of the moonPhase property.
     * 
     */
    public void setMoonPhase(int value) {
        this.moonPhase = value;
    }

    /**
     * Gets the value of the moonPhaseImage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoonPhaseImage() {
        return moonPhaseImage;
    }

    /**
     * Sets the value of the moonPhaseImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoonPhaseImage(String value) {
        this.moonPhaseImage = value;
    }

    /**
     * Gets the value of the pressure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressure() {
        return pressure;
    }

    /**
     * Sets the value of the pressure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressure(String value) {
        this.pressure = value;
    }

    /**
     * Gets the value of the pressureUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressureUnit() {
        return pressureUnit;
    }

    /**
     * Sets the value of the pressureUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressureUnit(String value) {
        this.pressureUnit = value;
    }

    /**
     * Gets the value of the pressureHigh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressureHigh() {
        return pressureHigh;
    }

    /**
     * Sets the value of the pressureHigh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressureHigh(String value) {
        this.pressureHigh = value;
    }

    /**
     * Gets the value of the pressureLow property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressureLow() {
        return pressureLow;
    }

    /**
     * Sets the value of the pressureLow property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressureLow(String value) {
        this.pressureLow = value;
    }

    /**
     * Gets the value of the pressureRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressureRate() {
        return pressureRate;
    }

    /**
     * Sets the value of the pressureRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressureRate(String value) {
        this.pressureRate = value;
    }

    /**
     * Gets the value of the pressureRateUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPressureRateUnit() {
        return pressureRateUnit;
    }

    /**
     * Sets the value of the pressureRateUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPressureRateUnit(String value) {
        this.pressureRateUnit = value;
    }

    /**
     * Gets the value of the light property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLight() {
        return light;
    }

    /**
     * Sets the value of the light property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLight(String value) {
        this.light = value;
    }

    /**
     * Gets the value of the lightRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLightRate() {
        return lightRate;
    }

    /**
     * Sets the value of the lightRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLightRate(String value) {
        this.lightRate = value;
    }

    /**
     * Gets the value of the indoorTemperature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndoorTemperature() {
        return indoorTemperature;
    }

    /**
     * Sets the value of the indoorTemperature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndoorTemperature(String value) {
        this.indoorTemperature = value;
    }

    /**
     * Gets the value of the indoorTemperatureRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndoorTemperatureRate() {
        return indoorTemperatureRate;
    }

    /**
     * Sets the value of the indoorTemperatureRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndoorTemperatureRate(String value) {
        this.indoorTemperatureRate = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     */
    public void setLatitude(double value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     */
    public void setLongitude(double value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the obDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObDate() {
        return obDate;
    }

    /**
     * Sets the value of the obDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObDate(String value) {
        this.obDate = value;
    }

    /**
     * Gets the value of the obDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getObDateTime() {
        return obDateTime;
    }

    /**
     * Sets the value of the obDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setObDateTime(XMLGregorianCalendar value) {
        this.obDateTime = value;
    }

    /**
     * Gets the value of the rainMonth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainMonth() {
        return rainMonth;
    }

    /**
     * Sets the value of the rainMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainMonth(String value) {
        this.rainMonth = value;
    }

    /**
     * Gets the value of the rainRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainRate() {
        return rainRate;
    }

    /**
     * Sets the value of the rainRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainRate(String value) {
        this.rainRate = value;
    }

    /**
     * Gets the value of the rainRateMax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainRateMax() {
        return rainRateMax;
    }

    /**
     * Sets the value of the rainRateMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainRateMax(String value) {
        this.rainRateMax = value;
    }

    /**
     * Gets the value of the rainRateUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainRateUnit() {
        return rainRateUnit;
    }

    /**
     * Sets the value of the rainRateUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainRateUnit(String value) {
        this.rainRateUnit = value;
    }

    /**
     * Gets the value of the rainToday property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainToday() {
        return rainToday;
    }

    /**
     * Sets the value of the rainToday property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainToday(String value) {
        this.rainToday = value;
    }

    /**
     * Gets the value of the rainUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainUnit() {
        return rainUnit;
    }

    /**
     * Sets the value of the rainUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainUnit(String value) {
        this.rainUnit = value;
    }

    /**
     * Gets the value of the rainYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRainYear() {
        return rainYear;
    }

    /**
     * Sets the value of the rainYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRainYear(String value) {
        this.rainYear = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the stationIDRequested property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationIDRequested() {
        return stationIDRequested;
    }

    /**
     * Sets the value of the stationIDRequested property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationIDRequested(String value) {
        this.stationIDRequested = value;
    }

    /**
     * Gets the value of the stationIDReturned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationIDReturned() {
        return stationIDReturned;
    }

    /**
     * Sets the value of the stationIDReturned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationIDReturned(String value) {
        this.stationIDReturned = value;
    }

    /**
     * Gets the value of the stationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Sets the value of the stationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationName(String value) {
        this.stationName = value;
    }

    /**
     * Gets the value of the stationURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationURL() {
        return stationURL;
    }

    /**
     * Sets the value of the stationURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationURL(String value) {
        this.stationURL = value;
    }

    /**
     * Gets the value of the sunrise property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSunrise() {
        return sunrise;
    }

    /**
     * Sets the value of the sunrise property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSunrise(XMLGregorianCalendar value) {
        this.sunrise = value;
    }

    /**
     * Gets the value of the sunset property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSunset() {
        return sunset;
    }

    /**
     * Sets the value of the sunset property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSunset(XMLGregorianCalendar value) {
        this.sunset = value;
    }

    /**
     * Gets the value of the temperature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the value of the temperature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperature(String value) {
        this.temperature = value;
    }

    /**
     * Gets the value of the temperatureHigh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperatureHigh() {
        return temperatureHigh;
    }

    /**
     * Sets the value of the temperatureHigh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperatureHigh(String value) {
        this.temperatureHigh = value;
    }

    /**
     * Gets the value of the temperatureLow property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperatureLow() {
        return temperatureLow;
    }

    /**
     * Sets the value of the temperatureLow property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperatureLow(String value) {
        this.temperatureLow = value;
    }

    /**
     * Gets the value of the temperatureRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperatureRate() {
        return temperatureRate;
    }

    /**
     * Sets the value of the temperatureRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperatureRate(String value) {
        this.temperatureRate = value;
    }

    /**
     * Gets the value of the temperatureRateUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperatureRateUnit() {
        return temperatureRateUnit;
    }

    /**
     * Sets the value of the temperatureRateUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperatureRateUnit(String value) {
        this.temperatureRateUnit = value;
    }

    /**
     * Gets the value of the temperatureUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    /**
     * Sets the value of the temperatureUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemperatureUnit(String value) {
        this.temperatureUnit = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the timeZoneOffset property.
     * 
     */
    public double getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     * Sets the value of the timeZoneOffset property.
     * 
     */
    public void setTimeZoneOffset(double value) {
        this.timeZoneOffset = value;
    }

    /**
     * Gets the value of the webUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Sets the value of the webUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebUrl(String value) {
        this.webUrl = value;
    }

    /**
     * Gets the value of the wetBulb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWetBulb() {
        return wetBulb;
    }

    /**
     * Sets the value of the wetBulb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWetBulb(String value) {
        this.wetBulb = value;
    }

    /**
     * Gets the value of the windDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * Sets the value of the windDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindDirection(String value) {
        this.windDirection = value;
    }

    /**
     * Gets the value of the windDirectionAvg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindDirectionAvg() {
        return windDirectionAvg;
    }

    /**
     * Sets the value of the windDirectionAvg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindDirectionAvg(String value) {
        this.windDirectionAvg = value;
    }

    /**
     * Gets the value of the windDirectionDegrees property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindDirectionDegrees() {
        return windDirectionDegrees;
    }

    /**
     * Sets the value of the windDirectionDegrees property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindDirectionDegrees(String value) {
        this.windDirectionDegrees = value;
    }

    /**
     * Gets the value of the windSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindSpeed() {
        return windSpeed;
    }

    /**
     * Sets the value of the windSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindSpeed(String value) {
        this.windSpeed = value;
    }

    /**
     * Gets the value of the windSpeedAvg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindSpeedAvg() {
        return windSpeedAvg;
    }

    /**
     * Sets the value of the windSpeedAvg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindSpeedAvg(String value) {
        this.windSpeedAvg = value;
    }

    /**
     * Gets the value of the windSpeedUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    /**
     * Sets the value of the windSpeedUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindSpeedUnit(String value) {
        this.windSpeedUnit = value;
    }

    /**
     * Gets the value of the zipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the value of the zipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipCode(String value) {
        this.zipCode = value;
    }

}/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.student11.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
        if (filterGradova == null || filterGradova.trim().isEmpty()) {
            gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet());
        } else {
            gradovi = citiesFacade.filtrirajGradove(odabraneDrzave.keySet(), filterGradova.toUpperCase());
        }

        for (Cities grad : gradovi) {
            String g = grad.getCitiesPK().getState() + " - "
                    + grad.getCitiesPK().getCounty() + " - "
                    + grad.getCitiesPK().getCity();
            popisGradova.put(g, g);
        }
        return popisGradova;
    }

    public void setPopisZipKodova(Map<String, Object> popisZipKodova) {
        this.popisZipKodova = popisZipKodova;
    }

    public List<String> getPopisZipKodovaOdabrano() {
        return popisZipKodovaOdabrano;
    }

    public void setPopisZipKodovaOdabrano(List<String> popisZipKodovaOdabrano) {
        this.popisZipKodovaOdabrano = popisZipKodovaOdabrano;
    }

    public Map<String, Object> getOdabraniZipKodovi() {
        return odabraniZipKodovi;
    }

    public void setOdabraniZipKodovi(Map<String, Object> odabraniZipKodovi) {
        this.odabraniZipKodovi = odabraniZipKodovi;
    }

    public List<String> getOdabraniZipKodoviOdabrano() {
        return odabraniZipKodoviOdabrano;
    }

    public void setOdabraniZipKodoviOdabrano(List<String> odabraniZipKodoviOdabrano) {
        this.odabraniZipKodoviOdabrano = odabraniZipKodoviOdabrano;
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
        List<States> drzave;
        if (filterDrzava == null || filterDrzava.trim().isEmpty()) {
            drzave = statesFacade.findAll();
        } else {
            drzave = statesFacade.filtrirajDrzave(filterDrzava.toUpperCase());
        }


        for (States d : drzave) {
            popisDrzava.put(d.getName(), d.getName());
        }
        return popisDrzava;
    }

    public void setPopisDrzava(Map<String, Object> popisDrzava) {
        this.popisDrzava = popisDrzava;
    }

    public List<String> getPopisDrzavaOdabrano() {
        return popisDrzavaOdabrano;
    }

    public void setPopisDrzavaOdabrano(List<String> popisDrzavaOdabrano) {
        this.popisDrzavaOdabrano = popisDrzavaOdabrano;
    }

    public Map<String, Object> getOdabraneDrzave() {
        return odabraneDrzave;
    }

    public void setOdabraneDrzave(Map<String, Object> odabraneDrzave) {
        this.odabraneDrzave = odabraneDrzave;
    }

    public List<String> getOdabraneDrzaveOdabrano() {
        return odabraneDrzaveOdabrano;
    }

    public void setOdabraneDrzaveOdabrano(List<String> odabraneDrzaveOdabrano) {
        this.odabraneDrzaveOdabrano = odabraneDrzaveOdabrano;
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

    public ArrayList<LiveWeatherData> getPodaci() {
        return podaci;
    }

    public void setPodaci(ArrayList<LiveWeatherData> podaci) {
        this.podaci = podaci;
    }
}