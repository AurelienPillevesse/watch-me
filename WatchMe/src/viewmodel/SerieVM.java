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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import model.Season;
import model.Serie;

/**
 *
 * @author Aurélien
 */
public class SerieVM implements PropertyChangeListener {
    
    private Serie modelTv;
    public Serie getSerie() {return modelTv;}
    
    private final StringProperty name = new SimpleStringProperty();
        public String getName() {return name.get();}
        public void setName(String value) {name.set(value);}
        public StringProperty nameProperty() {return name;}
    
    private final StringProperty overview = new SimpleStringProperty();
        public String getOverview() {return overview.get();}
        public void setOverview(String value) {overview.set(value);}
        public StringProperty overviewProperty() {return overview;}
    
    private final StringProperty originalName = new SimpleStringProperty();
        public String getOriginalName() {return originalName.get();}
        public void setOriginalName(String value) {originalName.set(value);}
        public StringProperty originalNameProperty() {return originalName;}
    
    private final IntegerProperty id = new SimpleIntegerProperty();
        public int getId() {return id.get();}
        public void setId(int value) {id.set(value);}
        public IntegerProperty idProperty() {return id;}
        
    private final ObjectProperty<Image> poster = new SimpleObjectProperty<>();
        public Image getPoster() {return poster.get();}
        public void setPoster(Image value) {poster.set(value);}
        public ObjectProperty posterProperty() {return poster;}
    
    private ObservableList<SeasonVM> listViewModelSeasonObs = FXCollections.observableArrayList();
    private final ListProperty<SeasonVM> listViewModelSeason = new SimpleListProperty<>(listViewModelSeasonObs);
        public ObservableList getListViewModelSeason() {return listViewModelSeason.get();}
        public void setListViewModelSeason(ObservableList value) {listViewModelSeason.set(value);}
        public ListProperty listViewModelSeasonProperty() {return listViewModelSeason;}    
    
    /**
     * Constructeur
     * @param t instance de Serie
     */
    public SerieVM(Serie t) {
        modelTv = new Serie(t.getName(), t.getOverview(), t.getOriginalName(), t.getId(), t.getListSeasonModel(), t.getPoster());
        
        this.id.set(modelTv.getId());
        this.name.set(modelTv.getName());
        this.overview.set(modelTv.getOverview());
        this.originalName.set(modelTv.getOriginalName());
        this.poster.set(modelTv.getPoster());
        
        modelTv.addPropertyChangeListener(this);
        
        this.id.addListener((o, old, newV) -> modelTv.setId((int) newV));
        this.name.addListener((o, old, newV) -> modelTv.setName(newV));
        this.overview.addListener((o, old, newV) -> modelTv.setOverview(newV));
        this.originalName.addListener((o, old, newV) -> modelTv.setOriginalName(newV));
        this.poster.addListener((o, old, newV) -> modelTv.setPoster(newV));
        
        modelTv.getListSeasonModel().forEach(e -> listViewModelSeasonObs.add(new SeasonVM((Season) e)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Serie.PROP_NAME)) {
            this.name.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(Serie.PROP_OVERVIEW)) {
            this.overview.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(Serie.PROP_ORIGINALNAME)) {
            this.originalName.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(Serie.PROP_ID)) {
            this.id.set((int) evt.getNewValue());
        }
        if(evt.getPropertyName().equals(Serie.PROP_LISTSEASONMODEL)) {
            listViewModelSeasonObs.add(((IndexedPropertyChangeEvent)evt).getIndex(), new SeasonVM((Season) evt.getNewValue()));
        }
        if(evt.getPropertyName().equals(Serie.PROP_POSTER)) {
            this.poster.set((Image) evt.getNewValue());
        }
    }
    
    /**
     * Ajouter une saison à la série
     * @param number le numéro de la saison
     */
    public void addSeason(int number) {
        modelTv.addSeason(new Season(number));
    }
}
