/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Serie;
import model.AllSerie;

/**
 *
 * @author Aurélien
 */
public class AllSerieVM implements PropertyChangeListener{

    private AllSerie modelAllSerie;
    private static AllSerieVM instance;
    
    private ObservableList<SerieVM> listViewModelSerieObservable = FXCollections.observableArrayList();
    private final ListProperty<SerieVM> listViewModelSerie = new SimpleListProperty<>(listViewModelSerieObservable);
        public ObservableList getListViewModelSerie() {return listViewModelSerie.get();}
        public void setListViewModelSerie(ObservableList value) {listViewModelSerie.set(value);}
        public ListProperty listViewModelSerieProperty() {return listViewModelSerie;}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(AllSerie.PROP_LISTMODELTV)) {
            Platform.runLater(() -> listViewModelSerieObservable.add(((IndexedPropertyChangeEvent)evt).getIndex(), new SerieVM((Serie)evt.getNewValue())));
        }
    }
    
    /**
     * Constructeur
     */
    public AllSerieVM() {
        modelAllSerie = new AllSerie();
        
        modelAllSerie.addPropertyChangeListener(this);
        
        modelAllSerie.getListModelSerie().forEach(e -> listViewModelSerie.add(new SerieVM(e)));
    }
    
    /**
     * Ajouter une série au modèle
     * @param name nom de la série
     * @param overview description de la série
     * @param originalName nom original de la série
     * @param id id de la série
     * @param posterPath chemin du poster de la série
     */
    public void addSerie(String name, String overview, String originalName, int id, String posterPath) {
        modelAllSerie.addSerie(name, overview, originalName, id, posterPath);
    }
    
    /**
     * Retourne l'instance d'une SerieVM si elle existe déjà
     * @param serie une série
     * @return une SerieVM si une est construite cette série sinon null
     */
    public SerieVM getExistingSerieVM(Serie serie) {
        for (int i = 0; i < listViewModelSerieObservable.size(); i++) {
            if(listViewModelSerieObservable.get(i).getName().equals(serie.getName())) {
                return listViewModelSerieObservable.get(i);
            }
        }
        return null;
    }
    
    /**
     * Obtenir l'instance d'AllSerieVM si il y en a une sinon la créer et la retourner
     * @return l'instance d'AllSerieVM
     */
    public static AllSerieVM getInstance() {
        if(instance == null) {
            instance = new AllSerieVM();
        }
        return instance;
    }

    /**
     * Savoir si le modèle des séries contient une série
     * @param name nom de la série 
     * @return true si elle le contient sinon false
     */
    public boolean contains(String name) {
        return modelAllSerie.contains(name);
    }
}
