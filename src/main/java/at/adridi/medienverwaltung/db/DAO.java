/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.medienverwaltung.db;

import at.adridi.medienverwaltung.model.Benutzer;
import at.adridi.medienverwaltung.model.Buecher;
import at.adridi.medienverwaltung.model.Buecherkategorie;
import at.adridi.medienverwaltung.model.Buecherzustand;
import at.adridi.medienverwaltung.model.DatenbankNotizen;
import at.adridi.medienverwaltung.model.MedienMusik;
import at.adridi.medienverwaltung.model.MedienMusikGenre;
import at.adridi.medienverwaltung.model.MedienSoftware;
import at.adridi.medienverwaltung.model.MedienSoftwareBetriebssystem;
import at.adridi.medienverwaltung.model.MedienSoftwareHersteller;
import at.adridi.medienverwaltung.model.MedienVideoclips;
import at.adridi.medienverwaltung.model.MedienVideoclipsSprache;
import at.adridi.medienverwaltung.model.MedienVideos;
import at.adridi.medienverwaltung.model.MedienVideosGenre;
import at.adridi.medienverwaltung.model.MedienVideosSprache;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

/**
 *
 * @author adridi
 */
public class DAO implements AutoCloseable {

    public DAO() {

    }

    public boolean insertDatenbankNotizen(DatenbankNotizen v) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(v);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Notiz wurde gespeichert.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertKryptowaehrungenVorgang: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notiz SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteDatenbankNotizen(DatenbankNotizen b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Notiz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteDatenbankNotiz: " + ex);
            // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Notiz SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }
        return ret;
    }

    public boolean insertBuecher(Buecher b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde hinzugefügt.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertBuecher: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    
    /**
     * Löschen eines Benutzers in der DB
     *
     * @param b das zu löschende Objekt vom Typ Benutzer
     * @return true, wenn erfolgreicher Löschvorgang
     */
    public boolean deleteBenutzer(Benutzer b) {

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;
        try {
            tx = s.beginTransaction();
            s.delete(b);
            tx.commit();
            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteBenutzer: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }
    
    public boolean deleteBuecher(Buecher b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            Buecher bGewaehlt = b;
            bGewaehlt.setDeleted(true);
            s.update(bGewaehlt);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteBuecher: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteBuecherkategorie(Buecherkategorie b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Kategorie wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteBuecherkategorie: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteBuecherzustand(Buecherzustand b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Kategorie wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteBuecherzustand: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertBuecherkategorie(Buecherkategorie o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie Buecherkategorie wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertBuecherkategorie: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertBuecherzustand(Buecherzustand o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie Buecherzustand wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertBuecherzustand: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateBuecher(Buecher b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);

            tx.commit();
            ret = true;

        } catch (HibernateException ex) {
            System.out.println("Fehler in updateBuecher: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateDatenbankNotizen(DatenbankNotizen o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(o);

            tx.commit();
            ret = true;

        } catch (HibernateException ex) {
            System.out.println("Fehler in updateDatenbankNotizen: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }
        return ret;
    }

    /**
     * Anfang: Abfragen von fixierten Listen - "Domains" z.B.: Kategorien
     */
    public List<Buecherkategorie> getAllBuecherkategorie() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM Buecherkategorie order by buecherkategorieid asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllBuecherkategorie: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<Buecherzustand> getAllBuecherzustand() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM Buecherzustand order by zustandid asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllBuecherzustand: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    /**
     * Liefert eine Liste aller Benutzer als List
     *
     *
     */
    public List<Benutzer> getAllBenutzer() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM Benutzer");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllBenutzer: " + e);

            return null;
        } finally {
            s.close();
        }

    }

    /**
     * Liefert eine Liste aller Bücher als List
     *
     *
     */
    public List<Buecher> getAllBuecher() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM Buecher where deleted=false order by buecher_id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllBuecher: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<Buecher> getSingleBuecher(Integer id) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM Buecher where deleted=false and buecher_id=:idWert");
            qu.setInteger("idWert", id);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getSingleBuecher: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<DatenbankNotizen> getDatenbankNotiz(String tabelle) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM DatenbankNotizen where tabelle = :tabellenname");
            qu.setString("tabellenname", tabelle);
            return qu.list();

        } catch (Exception e) {
            //System.out.println("Fehler in getAllKryptowaehrungenNotiz: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienMusik> getAllMedienMusik() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienMusik where deleted=false order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienMusik: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienMusik> getSingleMedienMusik(Integer id) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienMusik where deleted=false and id=:idWert");
            qu.setInteger("idWert", id);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getSingleMedienMusik: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienMusikGenre> getAllMedienMusikGenre() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienMusikGenre order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienMusikGenre: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienSoftware> getAllMedienSoftware() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienSoftware where deleted=false order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienSoftware: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienSoftware> getSingleMedienSoftware(Integer id) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienSoftware where deleted=false and id=:idWert");
            qu.setInteger("idWert", id);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getSingleMedienSoftware: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienSoftwareBetriebssystem> getAllMedienSoftwareBetriebssystem() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienSoftwareBetriebssystem order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienSoftwareBetriebssystem: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienSoftwareHersteller> getAllMedienSoftwareHersteller() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienSoftwareHersteller order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienSoftwareHersteller: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideoclips> getAllMedienVideoclips() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideoclips where deleted=false order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienVideoclips: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideoclips> getSingleMedienVideoclips(Integer id) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideoclips where deleted=false and id=:idWert");
            qu.setInteger("idWert", id);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getSingleMedienVideoclips: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideoclipsSprache> getAllMedienVideoclipsSprache() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideoclipsSprache order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienVideoclipsSprache: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideos> getAllMedienVideos() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideos where deleted=false order by videos_id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienVideos: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideos> getSingleMedienVideos(Integer id) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideos where deleted=false and videos_id=:idWert");
            qu.setInteger("idWert", id);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getSingleMedienVideos: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideosGenre> getAllMedienVideosGenre() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideosGenre order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienVideosGenre: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<MedienVideosSprache> getAllMedienVideosSprache() {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createQuery("FROM MedienVideosSprache order by id asc");
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler in getAllMedienVideosSprache: " + e);

            return null;
        } finally {
            s.close();
        }
    }

    public List<Buecher> customGetAllBuecher(String sqlbefehl) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {

            Query qu = s.createSQLQuery(sqlbefehl);
            return qu.list();

        } catch (Exception e) {
            System.out.println("Fehler im SQL-Befehl:  " + e);
            FacesContext.getCurrentInstance().addMessage("fehlerAnsichtListen", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler im SQL-Befehl! ", e.toString()));
            return null;
        } finally {
            s.close();
        }
    }

    /**
     * Speichern eines Benutzers in die DB
     *
     * @param b zum Speichern fertiges Benutzer-Objekt
     * @return true, wenn erfolgreiche Speicherung
     */
    public boolean insertBenutzer(Benutzer b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in insertBenutzer: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    /**
     * Benutzers in der DB aktualisieren Benutzer loeschen und neuanlegen
     *
     * @param b zum Speichern fertiges Benutzer-Objekt
     * @return true, wenn erfolgreiche Speicherung
     */
    public boolean updateBenutzer(Benutzer b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateBenutzer: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienMusik(MedienMusik b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde hinzugefügt.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienMusik: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienMusikGenre(MedienMusikGenre o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienMusikGenre wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienMusikGenre: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienSoftware(MedienSoftware b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde hinzugefügt.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienSoftware: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienSoftwareBetriebssystem(MedienSoftwareBetriebssystem o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienSoftwareBetriebssystem wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienSoftwareBetriebssystem: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienSoftwareHersteller(MedienSoftwareHersteller o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienSoftwareHersteller wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienSoftwareHersteller: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienVideoclips(MedienVideoclips b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde hinzugefügt.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienVideoclips: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienVideoclipsSprache(MedienVideoclipsSprache o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienVideoclipsSprache wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienVideoclipsSprache: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienVideos(MedienVideos b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde hinzugefügt.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienVideos: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienVideosGenre(MedienVideosGenre o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienVideosGenre wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienVideosGenre: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean insertMedienVideosSprache(MedienVideosSprache o) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.save(o);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Eintrag für die Kategorie MedienVideosSprache wurde erfolgreich gespeichert", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in insertMedienVideosSprache: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienMusik(MedienMusik b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            MedienMusik bGewaehlt = b;
            bGewaehlt.setDeleted(true);
            s.update(bGewaehlt);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienMusik: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienMusikGenre(MedienMusikGenre b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienMusikGenre: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienSoftware(MedienSoftware b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            MedienSoftware bGewaehlt = b;
            bGewaehlt.setDeleted(true);
            s.update(bGewaehlt);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienSoftware: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienSoftwareBetriebssystem(MedienSoftwareBetriebssystem b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienSoftwareBetriebssystem: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienSoftwareHersteller(MedienSoftwareHersteller b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienSoftwareHersteller: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienVideoclips(MedienVideoclips b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            MedienVideoclips bGewaehlt = b;
            bGewaehlt.setDeleted(true);
            s.update(bGewaehlt);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienVideoclips: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienVideoclipsSprache(MedienVideoclipsSprache b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienVideoclipsSprache: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienVideos(MedienVideos b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            MedienVideos bGewaehlt = b;
            bGewaehlt.setDeleted(true);
            s.update(bGewaehlt);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienVideos: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienVideosGenre(MedienVideosGenre b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienVideosGenre: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean deleteMedienVideosSprache(MedienVideosSprache b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.delete(b);

            tx.commit();
            ret = true;
            FacesContext.getCurrentInstance().addMessage("nachrichtGrowl", new FacesMessage(FacesMessage.SEVERITY_INFO, "Datensatz wurde gelöscht.", ""));

        } catch (HibernateException ex) {
            System.out.println("Fehler in deleteMedienVideosSprache: " + ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQL-Fehler! ", ex.toString()));

            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienMusik(MedienMusik b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;

        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienMusik: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienMusikGenre(MedienMusikGenre b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienMusikGenre: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienSoftware(MedienSoftware b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienSoftware: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienSoftwareBetriebssystem(MedienSoftwareBetriebssystem b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in MedienSoftwareBetriebssystem: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienSoftwareHersteller(MedienSoftwareHersteller b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienSoftwareHersteller: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienVideoclips(MedienVideoclips b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;

        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienVideoclips: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienVideoclipsSprache(MedienVideoclipsSprache b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienVideoclipsSprache: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienVideos(MedienVideos b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienVideos: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienVideosGenre(MedienVideosGenre b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienVideosGenre: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    public boolean updateMedienVideosSprache(MedienVideosSprache b) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        DAO dao = new DAO();
        Transaction tx = null;
        boolean ret = false;
        try {

            tx = s.beginTransaction();
            s.update(b);
            tx.commit();

            ret = true;
        } catch (HibernateException ex) {
            System.out.println("Fehler in updateMedienVideosSprache: " + ex);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            s.close();
        }

        return ret;
    }

    @Override
    public void close() {
        HibernateUtil.close();
    }

}
