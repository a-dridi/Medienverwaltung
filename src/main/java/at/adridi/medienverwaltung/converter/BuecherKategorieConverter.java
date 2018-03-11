/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.converter;

import at.adridi.medienverwaltung.db.DAO;
import at.adridi.medienverwaltung.model.Buecherkategorie;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author adridi
 */
@FacesConverter("buecherKategorieConverter")
public class BuecherKategorieConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.trim().isEmpty()) {
            return "";

        } else {

            DAO dao = new DAO();
            List<Buecherkategorie> liste = dao.getAllBuecherkategorie();
            Buecherkategorie kategorieAusgewaehlt = null;

            for (Buecherkategorie a : liste) {
                if ((a.getKategoriebezeichnung()).equals(value)) {
                    kategorieAusgewaehlt = a;
                }
            }
            return kategorieAusgewaehlt;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return "";
        } else {

            return value.toString();
        }

    }

}
