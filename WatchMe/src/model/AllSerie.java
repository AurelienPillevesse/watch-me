/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Aurélien
 */
public class AllSerie {
    
    private List<Serie> listModelSerie;

    public static final String PROP_LISTMODELTV = "listModelTv";

    /**
     * Obtenir la liste des séries
     * @return la liste des séries
     */
    public List<Serie> getListModelSerie() {
        return Collections.unmodifiableList(listModelSerie);
    }

    /**
     * Modifier la liste des séries
     * @param listModelSerie la série des séries
     */
    public void setListModelSerie(List<Serie> listModelSerie) {
        List<Serie> oldListModelSerie = this.listModelSerie;
        this.listModelSerie = listModelSerie;
        propertyChangeSupport.firePropertyChange(PROP_LISTMODELTV, oldListModelSerie, listModelSerie);
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
     */
    public AllSerie() {
        listModelSerie = new ArrayList<>();
    }

    /**
     * Ajouter une série
     * @param name le nom de la série
     * @param overview la description de la série
     * @param originalName la nom original de la série
     * @param id l'id de la série
     * @param posterPath le chemin de l'url du poster de la série
     */
    public void addSerie(String name, String overview, String originalName, int id, String posterPath) {
        Serie t = new Serie(name, overview, originalName, id, posterPath);
        listModelSerie.add(t);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTMODELTV, listModelSerie.size()-1, null, t);
    }

    /**
     * Savoir si le nom d'une série fait parti de la liste des séries
     * @param name nom de la série
     * @return true si le nom de la série en fait parti sinon false
     */
    public boolean contains(String name) {
        for (int i = 0; i < listModelSerie.size(); i++) {
            if(listModelSerie.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
