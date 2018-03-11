/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.controller;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import at.adridi.medienverwaltung.db.DAO;
import at.adridi.medienverwaltung.model.Buecher;
import at.adridi.medienverwaltung.model.Buecherkategorie;
import at.adridi.medienverwaltung.model.Buecherzustand;
import at.adridi.medienverwaltung.model.DatenbankNotizen;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author adridi
 */
@Named(value = "buecherController")
@ViewScoped
public class BuecherController implements Serializable {

    private List<Buecher> buecherListe = new ArrayList<>();
    private List<Buecher> filteredBuecherListe;

    private String tabellenname = "Buecher";
    private List<Buecherkategorie> buecherkategorien = new ArrayList<>();
    private List<Buecherzustand> buecherzustaende = new ArrayList<>();
    private DatenbankNotizen dbnotizEintrag = null;
    private String notiztext;

    private Integer rownumbers = 15;
    private Integer insert_rownumber;

    //ExportColumns - WICHTIG ANZAHL AN SPALTENANZAHL ANPASSEN (ausgenommen Anhang/D Spalte)!!!:
    private List<Boolean> columnList = Arrays.asList(true, true, true, true, true, true, true, true);

    private String buchtitel;
    private String kategorie;
    private String lagerort;
    private String zustand;
    private Integer isbn;
    private Integer jahr;
    private String sprache;
    private String informationen;

    private String datensaetzeAnzahlText;
    private DAO dao;
    private String neuKategorie;
    private String neuBuecherzustand;
    private String deleteID;
    //Cache Liste um gelöschte Datensätze rückgängig zu machen (nur innerhalb einer Session)
    private List<Buecher> deletedBuecherListe = new ArrayList<>();

    private String change_Kategorie;
    private String change_Buecherzustand;
    private Buecherkategorie deleteBuecherkategorie;
    private Buecherzustand deleteBuecherzustand;

    private Buecherkategorie buecherkategorie;
    private Buecherzustand buecherzustand;

  
    public BuecherController() {
        this.dao = new DAO();
    }

    @PostConstruct
    private void init() {
        List<DatenbankNotizen> notizList = dao.getDatenbankNotiz(this.tabellenname);
        if (notizList != null && !notizList.isEmpty()) {
            this.notiztext = notizList.get(0).getNotiztext();
            this.dbnotizEintrag = notizList.get(0);

        }
        this.buecherListe = dao.getAllBuecher();
        this.filteredBuecherListe = new ArrayList<>(this.buecherListe);
        this.buecherzustaende = dao.getAllBuecherzustand();
        this.buecherkategorien = dao.getAllBuecherkategorie();
        this.datensaetzeAnzahlText = ("Insgesamt: " + this.buecherListe.size() + " Datensaetze in der DB gespeichert");

    }

    /**
     * Editor für Zeile aufrufen
     */
    public void editRow(CellEditEvent event) {
        try {
            DataTable tabelle = (DataTable) event.getSource();
            String spaltenname = event.getColumn().getHeaderText();
            this.dao = new DAO();

            Buecher a = this.dao.getSingleBuecher((Integer) tabelle.getRowKey()).get(0);

            if (spaltenname.equals("Titel")) {
                a.setBuchtitel((String) event.getNewValue());
            }
            if (spaltenname.equals("Lagerort")) {
                a.setLagerort((String) event.getNewValue());
            }

            if (spaltenname.equals("Kategorie")) {
                String auswahl = (String) event.getNewValue();
                gefunden:
                for (Buecherkategorie m : this.buecherkategorien) {
                    if (m.getKategoriebezeichnung().equals(auswahl)) {
                        a.setKategorie((String) event.getNewValue());
                        break gefunden;
                    }
                }
            }

            if (spaltenname.equals("Zustand")) {
               String auswahl = (String) event.getNewValue();
                gefunden:
                for (Buecherzustand m : this.buecherzustaende) {
                    if (m.getZustandbezeichnung().equals(auswahl)) {
                        a.setZustand((String) event.getNewValue());
                        break gefunden;
                    }
                }
            }

            if (spaltenname.equals("Sprache")) {
                a.setSprache((String) event.getNewValue());
            }

            if (spaltenname.equals("ISBN")) {
                a.setIsbn((Integer) event.getNewValue());
            }

            if (spaltenname.equals("Informationen")) {
                a.setInformationen((String) event.getNewValue());
            }
            // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "old value: ", "" + event.getNewValue()));

            dao.updateBuecher(a);
            updateData();
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde aktualisiert", ""));

            //DEBUG:
            //   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kategorie: ", kategorie));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler: ", e.toString()));
        }
    }

    public void speichernNotiz() {
        List<DatenbankNotizen> notizList = dao.getDatenbankNotiz(this.tabellenname);
        if (notizList != null && !notizList.isEmpty()) {
            this.dbnotizEintrag = notizList.get(0);
        }

        //Notiz-DB Eintrag für diese Tabelle schon zuvor erstellt wurde
        if (this.dbnotizEintrag != null) {
            //Notiz-Eintrag aktualisieren
            this.dbnotizEintrag.setTabelle(this.tabellenname);
            this.dbnotizEintrag.setDatum(new Date());
            this.dbnotizEintrag.setNotiztext(notiztext);
            this.dao.updateDatenbankNotizen(this.dbnotizEintrag);
        } else {
            //Neuen Notiz-Eintrag erstellen
            DatenbankNotizen n = new DatenbankNotizen();
            n.setTabelle(this.tabellenname);
            n.setDatum(new Date());
            n.setNotiztext(notiztext);
            this.dao.insertDatenbankNotizen(n);
        }
    }

    public void clearNotizen() {

        //Notiz-DB Eintrag für diese Tabelle schon zuvor erstellt wurde
        if (this.notiztext != null) {
            //Notiz-Eintrag als leer speichern
            DatenbankNotizen n = new DatenbankNotizen();
            n.setTabelle(this.tabellenname);
            n.setDatum(new Date());
            n.setNotiztext("");
            this.dao.updateDatenbankNotizen(n);
        } else {
            //Neuen Notiz-Eintrag erstellen und als leer speichern
            DatenbankNotizen n = new DatenbankNotizen();
            n.setTabelle(this.tabellenname);
            n.setDatum(new Date());
            n.setNotiztext("");
            this.dao.insertDatenbankNotizen(n);
        }
    }

    public void preProcessPDF(Object document) {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
    }

    public void updateData() {
        this.dao = new DAO();

        this.buecherListe = dao.getAllBuecher();
        this.filteredBuecherListe = dao.getAllBuecher();
        this.buecherzustaende = dao.getAllBuecherzustand();
        this.buecherkategorien = dao.getAllBuecherkategorie();
        this.datensaetzeAnzahlText = ("Insgesamt: " + this.buecherListe.size() + " Datensaetze in der DB gespeichert");

    }

    public void speichern() {
        Buecher buch = new Buecher();
        buch.setDeleted(false);

        if (this.buchtitel != null) {
            buch.setBuchtitel(buchtitel);
        }
        buch.setLagerort(lagerort);

        if (this.buecherkategorie != null) {
            buch.setKategorie(this.buecherkategorie.getKategoriebezeichnung());
        }

        if (this.buecherzustand != null) {
            buch.setZustand(this.buecherzustand.getZustandbezeichnung());
        }

        buch.setIsbn(isbn);
        if (this.sprache != null) {
        buch.setSprache(this.sprache);
        }
        
        if (this.informationen != null) {
            buch.setInformationen(informationen);
        }
        this.buecherListe.add(buch);
        this.filteredBuecherListe.add(buch);
        dao.insertBuecher(buch);
        updateData();
    }

    public void kategorieSpeichern() {

        if (!this.neuKategorie.isEmpty()) {
            Buecherkategorie ak = new Buecherkategorie();
            ak.setKategoriebezeichnung(this.neuKategorie);
            dao.insertBuecherkategorie(ak);

            updateData();

        }

    }

    public void change_rownumber() {
        this.rownumbers = this.insert_rownumber;
    }

    public void kategorieLoeschen() {

        if (this.deleteBuecherkategorie != null) {

            List<Buecherkategorie> akList = dao.getAllBuecherkategorie();
            List<Buecher> ausgabenList = dao.getAllBuecher();
            boolean kategorieExist = false;

            for (Buecherkategorie a : akList) {
                if ((a.getKategoriebezeichnung().toLowerCase()).equals(this.deleteBuecherkategorie.getKategoriebezeichnung().toLowerCase())) {
                    dao.deleteBuecherkategorie(a);
                    for (Buecher ausgabe : ausgabenList) {
                        if ((ausgabe.getKategorie().toLowerCase()).equals(this.deleteBuecherkategorie.getKategoriebezeichnung().toLowerCase())) {
                            this.buecherListe.remove(ausgabe);
                            this.filteredBuecherListe.remove(ausgabe);
                            ausgabe.setKategorie(this.change_Kategorie);
                            dao.updateBuecher(ausgabe);
                            this.buecherListe.add(ausgabe);
                            this.filteredBuecherListe.add(ausgabe);

                        }
                    }
                }
                if ((a.getKategoriebezeichnung().toLowerCase()).equals(this.change_Kategorie.toLowerCase())) {
                    kategorieExist = true;
                }
            }
            if (!kategorieExist) {
                Buecherkategorie neu = new Buecherkategorie();
                neu.setKategoriebezeichnung(this.change_Kategorie);
                dao.insertBuecherkategorie(neu);
            }
            // System.out.println(" --- DEBUG: " + this.neuAusgabenkategorie);
            //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debug ", " --- DEBUG: " + this.neuAusgabenkategorie));

            updateData();
        }

    }

    public void zustandSpeichern() {

        if (!this.neuBuecherzustand.isEmpty()) {
            Buecherzustand ak = new Buecherzustand();
            ak.setZustandbezeichnung(this.neuBuecherzustand);
            dao.insertBuecherzustand(ak);
            updateData();

        }

    }

    public void zustandLoeschen() {

        if (this.deleteBuecherzustand != null) {

            List<Buecherzustand> akList = dao.getAllBuecherzustand();
            List<Buecher> ausgabenList = dao.getAllBuecher();
            boolean kategorieExist = false;

            for (Buecherzustand a : akList) {
                if ((a.getZustandbezeichnung().toLowerCase()).equals(this.deleteBuecherzustand.getZustandbezeichnung().toLowerCase())) {
                    dao.deleteBuecherzustand(a);
                    for (Buecher ausgabe : ausgabenList) {
                        if ((ausgabe.getZustand().toLowerCase()).equals(this.deleteBuecherzustand.getZustandbezeichnung().toLowerCase())) {
                            this.buecherListe.remove(ausgabe);
                            this.filteredBuecherListe.remove(ausgabe);

                            ausgabe.setZustand(this.change_Buecherzustand);
                            dao.updateBuecher(ausgabe);
                            this.buecherListe.add(ausgabe);
                            this.filteredBuecherListe.add(ausgabe);

                        }
                    }
                }
                if ((a.getZustandbezeichnung().toLowerCase()).equals(this.change_Buecherzustand.toLowerCase())) {
                    kategorieExist = true;
                }
            }
            if (!kategorieExist) {
                Buecherzustand neu = new Buecherzustand();
                neu.setZustandbezeichnung(this.change_Buecherzustand);
                dao.insertBuecherzustand(neu);
            }
            // System.out.println(" --- DEBUG: " + this.neuAusgabenkategorie);
            //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debug ", " --- DEBUG: " + this.neuAusgabenkategorie));

            updateData();
            //         throw new RuntimeException("DEBUG Kategorie: : " + this.neuAusgabenkategorie);

        }

    }

    public void datensatzLoeschen() {
        try {
            if (this.deleteID != null) {
                for (Buecher a : this.getBuecherListe()) {
                    if (a.getBuecher_id().equals(Integer.parseInt(this.deleteID))) {
                        dao.deleteBuecher(a);
                        this.deletedBuecherListe.add(a);
                        this.buecherListe.remove(a);
                        this.filteredBuecherListe.remove(a);
                    }
                }

                updateData();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte Zahl übergeben!", "FEHLER: " + e));
        }

    }

    public void datensatzLoeschenRueckgangigMachen() {

        if (!this.deletedBuecherListe.isEmpty()) {
            for (Buecher a : this.deletedBuecherListe) {
                this.buecherListe.add(a);
                this.filteredBuecherListe.add(a);
                a.setDeleted(false);
                dao.updateBuecher(a);
            }
            updateData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Gelöschte Datensätze wurden wiederhergestellt", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler: Cache ist leer!", "Bitte manuell den Wert der Spalte delete auf false ändern!"));

        }

    }

    //BIETE DIESE IMMER ÜBERPRÜFEN:
    public void scrollTop() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.scrollTo("ueberschriftPanel");
    }

    public void scrollTabelle() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.scrollTo("tabellenAnsicht:listenForm");

    }

    public void scrollHinzufuegen() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.scrollTo("hinzufuegenForm:neuenDatensatzFormular");

    }

    public void scrollKategorieAdd() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.scrollTo("kategorieForm:kategorieAddPanel");

    }

    public void scrollKategorieDel() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.scrollTo("del_kategorieForm:kategorieDelPanel");

    }

    public void onToggle(ToggleEvent e) {
        this.columnList.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public List<Boolean> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Boolean> columnList) {
        this.columnList = columnList;
    }

    public Buecherkategorie getBuecherkategorie() {
        return buecherkategorie;
    }

    public void setBuecherkategorie(Buecherkategorie buecherkategorie) {
        this.buecherkategorie = buecherkategorie;
    }

    public Buecherzustand getBuecherzustand() {
        return buecherzustand;
    }

    public void setBuecherzustand(Buecherzustand buecherzustand) {
        this.buecherzustand = buecherzustand;
    }

    public List<Buecher> getBuecherListe() {
        return buecherListe;
    }

    public String getDeleteID() {
        return deleteID;
    }

    public void setDeleteID(String deleteID) {
        this.deleteID = deleteID;
    }

    public void setBuecherListe(List<Buecher> buecherListe) {
        this.buecherListe = buecherListe;
    }

    public List<Buecher> getFilteredBuecherListe() {
        return filteredBuecherListe;
    }

    public void setFilteredBuecherListe(List<Buecher> filteredBuecherListe) {
        this.filteredBuecherListe = filteredBuecherListe;
    }

    public List<Buecherkategorie> getBuecherkategorien() {
        return buecherkategorien;
    }

    public void setBuecherkategorien(List<Buecherkategorie> buecherkategorien) {
        this.buecherkategorien = buecherkategorien;
    }

    public List<Buecherzustand> getBuecherzustaende() {
        return buecherzustaende;
    }

    public void setBuecherzustaende(List<Buecherzustand> buecherzustaende) {
        this.buecherzustaende = buecherzustaende;
    }

    public String getBuchtitel() {
        return buchtitel;
    }

    public void setBuchtitel(String buchtitel) {
        this.buchtitel = buchtitel;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getLagerort() {
        return lagerort;
    }

    public void setLagerort(String lagerort) {
        this.lagerort = lagerort;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Integer getJahr() {
        return jahr;
    }

    public void setJahr(Integer jahr) {
        this.jahr = jahr;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public String getInformationen() {
        return informationen;
    }

    public void setInformationen(String informationen) {
        this.informationen = informationen;
    }

    public String getNeuKategorie() {
        return neuKategorie;
    }

    public void setNeuKategorie(String neuKategorie) {
        this.neuKategorie = neuKategorie;
    }

    public String getNeuBuecherzustand() {
        return neuBuecherzustand;
    }

    public void setNeuBuecherzustand(String neuBuecherzustand) {
        this.neuBuecherzustand = neuBuecherzustand;
    }

    public String getChange_Kategorie() {
        return change_Kategorie;
    }

    public void setChange_Kategorie(String change_Kategorie) {
        this.change_Kategorie = change_Kategorie;
    }

    public String getChange_Buecherzustand() {
        return change_Buecherzustand;
    }

    public void setChange_Buecherzustand(String change_Buecherzustand) {
        this.change_Buecherzustand = change_Buecherzustand;
    }

    public Buecherkategorie getDeleteBuecherkategorie() {
        return deleteBuecherkategorie;
    }

    public void setDeleteBuecherkategorie(Buecherkategorie deleteBuecherkategorie) {
        this.deleteBuecherkategorie = deleteBuecherkategorie;
    }

    public Buecherzustand getDeleteBuecherzustand() {
        return deleteBuecherzustand;
    }

    public void setDeleteBuecherzustand(Buecherzustand deleteBuecherzustand) {
        this.deleteBuecherzustand = deleteBuecherzustand;
    }

    public Integer getRownumbers() {
        return rownumbers;
    }

    public void setRownumbers(Integer rownumbers) {
        this.rownumbers = rownumbers;
    }

    public Integer getInsert_rownumber() {
        return insert_rownumber;
    }

    public void setInsert_rownumber(Integer insert_rownumber) {
        this.insert_rownumber = insert_rownumber;
    }

    public DatenbankNotizen getDbnotizEintrag() {
        return dbnotizEintrag;
    }

    public void setDbnotizEintrag(DatenbankNotizen dbnotizEintrag) {
        this.dbnotizEintrag = dbnotizEintrag;
    }

    public String getNotiztext() {
        return notiztext;
    }

    public void setNotiztext(String notiztext) {
        this.notiztext = notiztext;
    }

    public String getDatensaetzeAnzahlText() {
        return datensaetzeAnzahlText;
    }

    public void setDatensaetzeAnzahlText(String datensaetzeAnzahlText) {
        this.datensaetzeAnzahlText = datensaetzeAnzahlText;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.buecherListe);
        hash = 29 * hash + Objects.hashCode(this.filteredBuecherListe);
        hash = 29 * hash + Objects.hashCode(this.buecherkategorien);
        hash = 29 * hash + Objects.hashCode(this.buecherzustaende);
        hash = 29 * hash + Objects.hashCode(this.buchtitel);
        hash = 29 * hash + Objects.hashCode(this.kategorie);
        hash = 29 * hash + Objects.hashCode(this.lagerort);
        hash = 29 * hash + Objects.hashCode(this.zustand);
        hash = 29 * hash + Objects.hashCode(this.isbn);
        hash = 29 * hash + Objects.hashCode(this.jahr);
        hash = 29 * hash + Objects.hashCode(this.sprache);
        hash = 29 * hash + Objects.hashCode(this.informationen);
        hash = 29 * hash + Objects.hashCode(this.neuKategorie);
        hash = 29 * hash + Objects.hashCode(this.neuBuecherzustand);
        hash = 29 * hash + Objects.hashCode(this.change_Kategorie);
        hash = 29 * hash + Objects.hashCode(this.change_Buecherzustand);
        hash = 29 * hash + Objects.hashCode(this.deleteBuecherkategorie);
        hash = 29 * hash + Objects.hashCode(this.deleteBuecherzustand);
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
        final BuecherController other = (BuecherController) obj;
        if (!Objects.equals(this.buchtitel, other.buchtitel)) {
            return false;
        }
        if (!Objects.equals(this.kategorie, other.kategorie)) {
            return false;
        }
        if (!Objects.equals(this.lagerort, other.lagerort)) {
            return false;
        }
        if (!Objects.equals(this.zustand, other.zustand)) {
            return false;
        }
        if (!Objects.equals(this.sprache, other.sprache)) {
            return false;
        }
        if (!Objects.equals(this.informationen, other.informationen)) {
            return false;
        }
        if (!Objects.equals(this.neuKategorie, other.neuKategorie)) {
            return false;
        }
        if (!Objects.equals(this.neuBuecherzustand, other.neuBuecherzustand)) {
            return false;
        }
        if (!Objects.equals(this.change_Kategorie, other.change_Kategorie)) {
            return false;
        }
        if (!Objects.equals(this.change_Buecherzustand, other.change_Buecherzustand)) {
            return false;
        }
        if (!Objects.equals(this.buecherListe, other.buecherListe)) {
            return false;
        }
        if (!Objects.equals(this.filteredBuecherListe, other.filteredBuecherListe)) {
            return false;
        }
        if (!Objects.equals(this.buecherkategorien, other.buecherkategorien)) {
            return false;
        }
        if (!Objects.equals(this.buecherzustaende, other.buecherzustaende)) {
            return false;
        }
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        if (!Objects.equals(this.jahr, other.jahr)) {
            return false;
        }
        if (!Objects.equals(this.deleteBuecherkategorie, other.deleteBuecherkategorie)) {
            return false;
        }
        if (!Objects.equals(this.deleteBuecherzustand, other.deleteBuecherzustand)) {
            return false;
        }
        return true;
    }

}
