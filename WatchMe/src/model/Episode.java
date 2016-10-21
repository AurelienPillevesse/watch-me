/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Aurélien
 */
public class Episode {
    
    private int number;
    private String title;
    private String overview;
    private static long uniqueId = 0;

    public static final String PROP_NUMBER = "number";
    public static final String PROP_TITLE = "title";
    public static final String PROP_OVERVIEW = "overview";
    public static final String PROP_UNIQUEID = "uniqueId";

    /**
     * Obtenir le numéro de l'épisode
     * @return le numéro de l'épisode
     */
    public int getNumber() {return number;}
    
    /**
     * Modifier le numéro de l'épisode
     * @param number le nouveau numéro de l'épisode
     */
    public void setNumber(int number) {
        int oldNumber = this.number;
        this.number = number;
        propertyChangeSupport.firePropertyChange(PROP_NUMBER, oldNumber, number);
    }
    
    /**
     * Obtenir le titre de l'épisode
     * @return le titre de l'épisode
     */
    public String getTitle() {return title;}
    
    /**
     * Modifier le titre de l'épisode
     * @param title le nouveau titre de l'épisode
     */
    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        propertyChangeSupport.firePropertyChange(PROP_TITLE, oldTitle, title);
    }
    
    /**
     * Obtenir la description de l'épisode
     * @return la description de l'épisode
     */
    public String getOverview() {return overview;}
    
    /**
     * Modifier la description de l'épisode
     * @param overview la nouvelle description de l'épisode
     */
    public void setOverview(String overview) {
        String oldOverview = this.overview;
        this.overview = overview;
        propertyChangeSupport.firePropertyChange(PROP_OVERVIEW, oldOverview, overview);
    }
    
    /**
     * Obtenir l'id unique de l'épisode
     * @return l'id unique de l'épisode
     */
    public long getUniqueId() {return uniqueId;}
    
    /**
     * Modifier l'id unique de l'épisode
     * @param uniqueId le nouvel id unique de l'épisode
     */
    public void setUniqueId(long uniqueId) {
        long oldUniqueId = this.uniqueId;
        this.uniqueId = uniqueId;
        propertyChangeSupport.firePropertyChange(PROP_UNIQUEID, oldUniqueId, uniqueId);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Constructeur
     * @param number le numéro de l'épisode
     * @param title le titre de l'épisode
     * @param overview la description de l'épisode
     */
    public Episode(int number, String title, String overview) {
        this.number = number;
        this.title = title;
        this.overview = overview;
        this.uniqueId++;
    }

}
