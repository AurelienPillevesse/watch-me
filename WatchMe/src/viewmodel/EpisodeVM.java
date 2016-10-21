/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Episode;

/**
 *
 * @author aupilleves
 */
public class EpisodeVM implements PropertyChangeListener{
    
    private Episode episodeModelView;
    private final IntegerProperty number = new SimpleIntegerProperty();
        public int getNumber() {return number.get();}
        public void setNumber(int value) {number.set(value);}
        public IntegerProperty numberProperty() {return number;}
    
    private final StringProperty title = new SimpleStringProperty();
        public String getTitle() {return title.get();}
        public void setTitle(String value) {title.set(value);}
        public StringProperty titleProperty() {return title;}
    
    private final StringProperty overview = new SimpleStringProperty();
        public String getOverview() {return overview.get();}
        public void setOverview(String value) {overview.set(value);}
        public StringProperty overviewProperty() {return overview;}
    
    private final LongProperty uniqueId = new SimpleLongProperty();
        public long getUniqueId() {return uniqueId.get();}
        public void setUniqueId(long value) {uniqueId.set(value);}
        public LongProperty uniqueIdProperty() {return uniqueId;}
    
    /**
     * Constructeur
     * @param e un épisode
     */
    public EpisodeVM(Episode e) {
        episodeModelView = e;
        
        this.number.set(e.getNumber());
        this.title.set(e.getTitle());
        this.overview.set(e.getOverview());
        this.uniqueId.set(e.getUniqueId());
        
        episodeModelView.addPropertyChangeListener(this);
        
        this.number.addListener((o, old, newV) -> episodeModelView.setNumber((int) newV));
        this.title.addListener((o, old, newV) -> episodeModelView.setTitle(newV));
        this.overview.addListener((o, old, newV) -> episodeModelView.setOverview(newV));
        this.uniqueId.addListener((o, old, newV) -> episodeModelView.setUniqueId((long) newV));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Episode.PROP_NUMBER)) {
            this.number.set((int) evt.getNewValue());
        }
        if(evt.getPropertyName().equals(Episode.PROP_TITLE)) {
            this.title.set(evt.getNewValue().toString());
        }
        if(evt.getPropertyName().equals(Episode.PROP_UNIQUEID)) {
            this.uniqueId.set((long) evt.getNewValue());
        }
        if(evt.getPropertyName().equals(Episode.PROP_OVERVIEW)) {
            this.overview.set(evt.getNewValue().toString());
        }
    }
}
