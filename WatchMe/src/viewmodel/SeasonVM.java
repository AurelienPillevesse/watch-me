/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Episode;
import model.Season;

/**
 *
 * @author aupilleves
 */
public class SeasonVM implements PropertyChangeListener{
    
    private Season seasonModel;
    private final IntegerProperty number = new SimpleIntegerProperty();
        public int getNumber() {return number.get();}
        public void setNumber(int value) {number.set(value);}
        public IntegerProperty numberProperty() {return number;}
    
    private ObservableList<EpisodeVM> listViewModelEpisodeObs = FXCollections.observableArrayList();
    private final ListProperty<EpisodeVM> listViewModelEpisode = new SimpleListProperty<>(listViewModelEpisodeObs);
        public ObservableList getListViewModelEpisode() {return listViewModelEpisode.get();}
        public void setListViewModelEpisode(ObservableList value) {listViewModelEpisode.set(value);}
        public ListProperty listViewModelEpisodeProperty() {return listViewModelEpisode;}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Season.PROP_NUMBER)) {
            this.number.set((int) evt.getNewValue());
        }
        if(evt.getPropertyName().equals(Season.PROP_LISTMODELEPISODE)) {
            listViewModelEpisodeObs.add(((IndexedPropertyChangeEvent) evt).getIndex(), new EpisodeVM((Episode) evt.getNewValue()));
        }
    }
    
    /**
     * Constructeur
     * @param s instance de Season
     */
    public SeasonVM(Season s) {
        seasonModel = s;
        
        this.number.set(seasonModel.getNumber());
        
        seasonModel.addPropertyChangeListener(this);
        
        this.number.addListener((o, old, newV) -> seasonModel.setNumber((int) newV));
        
        seasonModel.getListModelEpisode().forEach(e -> listViewModelEpisodeObs.add(new EpisodeVM((Episode) e)));
    }
    
    /**
     * Ajouter un épisode à la saison
     * @param number le numéro de l'épisode
     * @param name le nom de l'épisode
     * @param overview la description de l'épisode
     */
    public void addEpisode(int number, String name, String overview) {
        seasonModel.addEpisode(number, name, overview);
    }
    
}
