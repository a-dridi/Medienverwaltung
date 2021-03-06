/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.converter;

import at.adridi.medienverwaltung.db.DAO;
import at.adridi.medienverwaltung.model.MedienVideosSprache;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author adridi
 */
@FacesConverter("medienVideosSpracheConverter")
public class MedienVideosSpracheConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.trim().isEmpty()) {
            return "";

        } else {

            DAO dao = new DAO();
            List<MedienVideosSprache> liste = dao.getAllMedienVideosSprache();
            MedienVideosSprache kategorieAusgewaehlt = null;

            for (MedienVideosSprache a : liste) {
                if ((a.getBezeichnung()).equals(value)) {
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
