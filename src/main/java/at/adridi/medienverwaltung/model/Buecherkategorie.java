/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author adridi
 */
@Entity
public class Buecherkategorie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int buecherkategorieid;
    
    @Column(unique = true)
    private String kategoriebezeichnung;

    public int getBuecherkategorieid() {
        return buecherkategorieid;
    }

    public void setBuecherkategorieid(int buecherkategorieid) {
        this.buecherkategorieid = buecherkategorieid;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.kategoriebezeichnung);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Buecherkategorie other = (Buecherkategorie) obj;
        if (!Objects.equals(this.kategoriebezeichnung, other.kategoriebezeichnung)) {
            return false;
        }
        return true;
    }

    public String getKategoriebezeichnung() {
        return kategoriebezeichnung;
    }

    public void setKategoriebezeichnung(String kategoriebezeichnung) {
        this.kategoriebezeichnung = kategoriebezeichnung;
    }

    @Override
    public String toString() {
        return kategoriebezeichnung;
    }

}
