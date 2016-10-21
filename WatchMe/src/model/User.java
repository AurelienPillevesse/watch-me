/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import viewmodel.SerieVM;

/**
 *
 * @author Aurélien
 */
public class User implements Serializable{
    
    private String firstName;
    private String lastName;
    private List<Serie> listFavSerie;
    private List<Long> listSawEpisode;

    public static final String PROP_FIRSTNAME = "firstName";
    public static final String PROP_LASTNAME = "lastName";
    public static final String PROP_LISTFAVTV = "listFavTv";
    public static final String PROP_LISTSAWEPISODE = "listSawEpisode";

    /**
     * Obtenir le prénom de l'utilisateur
     * @return le prénom de l'utilisateur
     */
    public String getFirstName() {return firstName;}
    
    /**
     * Modifier le prénom de l'utilisateur
     * @param firstName le nouveau prénom de l'utilisateur
     */
    public void setFirstName(String firstName) {
        String oldFirstName = this.firstName;
        this.firstName = firstName;
        propertyChangeSupport.firePropertyChange(PROP_FIRSTNAME, oldFirstName, firstName);
    }
    /**
     * Obtenir le nom de famille de l'utilisateur
     * @return le nom de famille de l'utilisateur
     */
    public String getLastName() {return lastName;}
    
    /**
     * Modifier le nom de famille de l'utilisateur
     * @param lastName le nouveau nom de famille de l'utilisateur
     */
    public void setLastName(String lastName) {
        String oldLastName = this.lastName;
        this.lastName = lastName;
        propertyChangeSupport.firePropertyChange(PROP_LASTNAME, oldLastName, lastName);
    }
    
    /**
     * Obtenir la liste des séries préférées de l'utilisateur
     * @return la liste des séries préférées de l'utilisateur
     */
    public List<Serie> getListFavSerie() {return Collections.unmodifiableList(listFavSerie);}
    
    /**
     * Modifier la liste des séries favorites de l'utilisateur
     * @param listFavSerie la nouvelle liste des séries favorites de l'utilisateur
     */
    public void setListFavSerie(List<Serie> listFavSerie) {
        List<Serie> oldListFavSerie = this.listFavSerie;
        this.listFavSerie = listFavSerie;
        propertyChangeSupport.firePropertyChange(PROP_LISTFAVTV, oldListFavSerie, listFavSerie);
    }
    
    /**
     * Obtenir la liste des identifiants uniques des épisodes vus par l'utilsateur
     * @return la liste des identifiants uniques des épisodes vus par l'utilsateur
     */
    public List<Long> getListSawEpisode() {return listSawEpisode;}
    
    /**
     * Modifier la liste des épisodes vus par l'utilisateur
     * @param listSawEpisode la nouvelle liste des épisodes vus par l'utilisateur
     */
    public void setListSawEpisode(List<Long> listSawEpisode) {
        List<Long> oldListSawEpisode = this.listSawEpisode;
        this.listSawEpisode = listSawEpisode;
        propertyChangeSupport.firePropertyChange(PROP_LISTSAWEPISODE, oldListSawEpisode, listSawEpisode);
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
    public User() {
        this.firstName = "--- Prénom ---";
        this.lastName = "--- Nom ---";
        this.listFavSerie = new ArrayList<>();
        this.listSawEpisode = new ArrayList<>();
    }
    
    /**
     * Ajouter une série aux séries favorites de l'utilisateur
     * @param tvFav une instance de SerieVM
     */
    public void addFavSerie(SerieVM tvFav) {
        for (int i = 0; i < listFavSerie.size(); i++) {
            if(tvFav.getName().equals(listFavSerie.get(i).getName())) {
                return;
            }
        }
        Serie s = tvFav.getSerie();
        listFavSerie.add(s);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTFAVTV, listFavSerie.size()-1, null, s);
    }

    /**
     * Retirer une série aux séries favorites de l'utilisateur
     * @param name le nom de la série
     */
    public void removeFavSerie(String name) {
        checkList(name, true);
    }
    
    /**
     * Verifier si le nom d'une série fait parti des séries préférées de l'utilisateur
     * @param name nom de la série
     * @param remove true si le nom en fait parti, pour le supprimer sinon false
     * @return true si le nom en fait parti sinon false
     */
    public boolean checkList(String name, boolean remove) {
        for (int i = 0; i < listFavSerie.size(); i++) {
            if(name.equals(listFavSerie.get(i).getName())) {
                if(remove) {
                    listFavSerie.remove(i);
                    propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTFAVTV, i, null, null);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Ajouter un épisode vu à l'utlisateur
     * @param uniqueId l'identifiant unique de l'épisode
     */
    public void addSawEpisode(long uniqueId) {
        for (int i = 0; i < listSawEpisode.size(); i++) {
            if(listSawEpisode.get(i) == uniqueId) {
                return;
            }
        }
        listSawEpisode.add(uniqueId);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTSAWEPISODE, listSawEpisode.size()-1, null, uniqueId);
    }

    /**
     * Vérifier si un épisode à été vu par l'utilisateur
     * @param uniqueId l'identifiant unique de l'épisode
     * @return true si oui sinon false
     */
    public boolean isSaw(long uniqueId) {
        return listSawEpisode.contains(uniqueId);
    }

    /**
     * Retirer un épisode vu par l'utilisateur
     * @param uniqueId l'identifiant unique d'un épisode
     */
    public void removeSawEpisode(long uniqueId) {
        for (int i = 0; i < listSawEpisode.size(); i++) {
            if(listSawEpisode.get(i) == uniqueId) {
                listSawEpisode.remove(i);
                return;
            }
        }
    }
}