/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Konvertiert booleans - True zu "JA" und False zu "nein" -
 *
 * @author adridi
 */
@FacesConverter("booleanJaNeinConverter")
public class BooleanJaNeinConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.trim().isEmpty()) {
            return "";

        } else {

            if (("JA").equals(value)) {
                return true;
            } else {
                return false;
            }
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return "";
        } else {
            boolean booleanWert = (boolean) value;
            if (booleanWert == true) {
                return "JA";
            } else {
                return "nein";
            }
        }

    }

}
