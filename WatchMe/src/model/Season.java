/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aurélien
 */
public class Season {
    
    private int number;
    private List<Episode> listModelEpisode;

    public static final String PROP_NUMBER = "number";
    public static final String PROP_LISTMODELEPISODE = "listModelEpisode";

    /**
     * Obtenir la liste des épisodes de la saison
     * @return la liste des épisodes de la saison
     */
    public List<Episode> getListModelEpisode() {return listModelEpisode;}
    
    /**
     * Modifier la liste des épisodes de la saison
     * @param listModelEpisode la nouvelle liste des épisodes de la saison
     */
    public void setListModelEpisode(List<Episode> listModelEpisode) {
        List<Episode> oldListModelEpisode = this.listModelEpisode;
        this.listModelEpisode = listModelEpisode;
        propertyChangeSupport.firePropertyChange(PROP_LISTMODELEPISODE, oldListModelEpisode, listModelEpisode);
    }
    
    /**
     * Obtenir le numéro de la saison
     * @return le numéro de la saison
     */
    public int getNumber() {return number;}
    
    /**
     * Modifier le numéro de la saison
     * @param number le nouveau numéro de la saison
     */
    public void setNumber(int number) {
        int oldNumber = this.number;
        this.number = number;
        propertyChangeSupport.firePropertyChange(PROP_NUMBER, oldNumber, number);
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
     * @param number le numéro de la saison
     */
    public Season(int number) {
        this.number = number;
        listModelEpisode = new ArrayList<>();
    }
    
    /**
     * Ajouter un épisode à la saison
     * @param number le numéro de l'épisode
     * @param name le nom de l'épisode
     * @param overview la description de l'épisode
     */
    public void addEpisode(int number, String name, String overview) {
        Episode e = new Episode(number, name, overview);
        listModelEpisode.add(e);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTMODELEPISODE, listModelEpisode.size()-1, null, e);
    }
    
}
