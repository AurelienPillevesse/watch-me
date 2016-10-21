/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Serie;
import model.User;

/**
 *
 * @author Aurélien
 */
public class UserVM implements PropertyChangeListener{
    
    private User userModel;

    private final StringProperty firstName = new SimpleStringProperty();
        public String getFirstName() {return firstName.get();}
        public void setFirstName(String value) {firstName.set(value);}
        public StringProperty firstNameProperty() {return firstName;}
    
    private final StringProperty lastName = new SimpleStringProperty();
        public String getLastName() {return lastName.get();}
        public void setLastName(String value) {lastName.set(value);}
        public StringProperty lastNameProperty() {return lastName;}
    
    private ObservableList<SerieVM> listViewModelFavSerieObs = FXCollections.observableArrayList();
    private final ListProperty<SerieVM> listViewModelFavSerie = new SimpleListProperty<>(listViewModelFavSerieObs);
        public ObservableList getListViewModelFavSerie() {return listViewModelFavSerie.get();}
        public void setListViewModelFavSerie(ObservableList value) {listViewModelFavSerie.set(value);}
        public ListProperty listViewModelFavSerieProperty() {return listViewModelFavSerie;}
    
    private ObservableList<Long> listViewModelSawEpisodeObs = FXCollections.observableArrayList();
    private final ListProperty<Long> listViewModelSawEpisode = new SimpleListProperty<>(listViewModelSawEpisodeObs);
        public ObservableList getListViewModelSawEpisode() {return listViewModelSawEpisode.get();}
        public void setListViewModelSawEpisode(ObservableList value) {listViewModelSawEpisode.set(value);}
        public ListProperty listViewModelSawEpisodeProperty() {return listViewModelSawEpisode;}
    
    /**
     * Constructeur
     * @param u instance de User
     */
    public UserVM(User u) {
        userModel = u;
        
        this.firstName.set(userModel.getFirstName());
        this.lastName.set(userModel.getLastName());
        
        userModel.addPropertyChangeListener(this);
        
        this.firstName.addListener((o, old, newV) -> userModel.setFirstName(newV));
        this.lastName.addListener((o, old, newV) -> userModel.setLastName(newV));
        
        userModel.getListFavSerie().forEach(e -> listViewModelFavSerieObs.add(new SerieVM(e)));
        userModel.getListSawEpisode().forEach(e -> listViewModelSawEpisodeObs.add(e));
    }
        
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(User.PROP_FIRSTNAME)) {
            this.firstName.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(User.PROP_LASTNAME)) {
            this.lastName.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(User.PROP_LISTFAVTV)) {
            if(evt.getOldValue() == null && evt.getNewValue() == null) {
                listViewModelFavSerieObs.remove(((IndexedPropertyChangeEvent)evt).getIndex());
            }
            if(evt.getNewValue() != null) {
                listViewModelFavSerieObs.add(((IndexedPropertyChangeEvent)evt).getIndex(), AllSerieVM.getInstance().getExistingSerieVM((Serie) evt.getNewValue()));
            }
        }
        if(evt.getPropertyName().equals(User.PROP_LISTSAWEPISODE)) {
            listViewModelSawEpisodeObs.add(((IndexedPropertyChangeEvent)evt).getIndex(), (long) evt.getNewValue());
        }
    }
    
    /**
     * Ajouter une série favorite
     * @param tvFav instance de SerieVM
     */
    public void addFavTv(SerieVM tvFav) {
        userModel.addFavSerie(tvFav);
    }
    
    /**
     * Enlever une série favorite
     * @param tvFav instance de SerieVM
     */
    public void removeFavTv(SerieVM tvFav) {
        userModel.removeFavSerie(tvFav.getName());
    }

    /**
     * Verifier si le nom d'une série fait parti des séries préférées de l'utilisateur
     * @param serie instance de SerieVM
     * @return true si le nom en fait parti sinon false
     */
    public boolean checkList(SerieVM serie) {
        return userModel.checkList(serie.getName(), false);
    }

    /**
     * Ajouter un épisode vu
     * @param uniqueId l'id unique d'un épisode
     */
    public void addSawEpisode(long uniqueId) {
        userModel.addSawEpisode(uniqueId);
    }

    /**
     * Vérifier si un épisode fait parti de la liste des épisodes vus
     * @param uniqueId l'id unique d'un épisode
     * @return true si oui sinon false
     */
    public boolean isSaw(long uniqueId) {
        return userModel.isSaw(uniqueId);
    }

    /**
     * Enlever un épisode de la liste des épisodes vus
     * @param uniqueId l'id unique d'un épisode
     */
    public void removeSawEpisode(long uniqueId) {
        userModel.removeSawEpisode(uniqueId);
    }
}
