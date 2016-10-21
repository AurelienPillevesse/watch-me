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

/**
 *
 * @author Aurélien
 */
public class AllUser implements Serializable{
    
    private List<User> listModelUser;

    public static final String PROP_LISTMODELUSER = "listModelUser";

    /**
     * Obtenir la liste des utilisateurs
     * @return la liste des utilisateurs
     */
    public List<User> getListModelUser() {return Collections.unmodifiableList(listModelUser);}
    
    /**
     * Modifier la liste des utilisateurs
     * @param listModelUser la nouvelle liste d'utilisateurs à affecter
     */
    public void setListModelUser(List<User> listModelUser) {
        List<User> oldListModelUser = this.listModelUser;
        this.listModelUser = listModelUser;
        propertyChangeSupport.firePropertyChange(PROP_LISTMODELUSER, oldListModelUser, listModelUser);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Ajouter un utilisateur
     */
    public void addUser() {
        User u = new User();
        listModelUser.add(u);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTMODELUSER, listModelUser.size()-1, null, u);
    }
    
    /**
     * Constructeur
     */
    public AllUser() {
        listModelUser = new ArrayList<>();
    }
}
