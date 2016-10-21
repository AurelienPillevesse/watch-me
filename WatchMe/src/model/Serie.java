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
import javafx.scene.image.Image;

/**
 *
 * @author Aurélien
 */
public class Serie {
    
    private int id;
    private String name;
    private Image poster;
    private String overview;
    private String originalName;
    private List<Season> listSeasonModel;
    
    public static final String PROP_ID = "id";
    public static final String PROP_NAME = "name";
    public static final String PROP_POSTER = "poster";
    public static final String PROP_OVERVIEW = "overview";
    public static final String PROP_ORIGINALNAME = "originalName";
    public static final String PROP_LISTSEASONMODEL = "listSeasonModel";
    
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    /**
     * Obtenir l'id de la série
     * @return l'id de la série
     */
    public int getId() {return id;}
    
    /**
     * Modifier l'id de la série
     * @param id le nouvel id de la série
     */
    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange(PROP_ID, oldId, id);
    }
    
    /**
     * Obtenir le nom de la série
     * @return le nom de la série
     */
    public String getName() {return name;}
    
    /**
     * Modifier le nom de la série
     * @param name le nouveau nom de la série
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
    }
    
    /**
     * Obtenir le poster de la série
     * @return le poster de la série
     */
    public Image getPoster() {return poster;}
    
    /**
     * Modifier le poster de la série
     * @param poster le nouveau poster de la série
     */
    public void setPoster(Image poster) {
        Image oldPoster = this.poster;
        this.poster = poster;
        propertyChangeSupport.firePropertyChange(PROP_POSTER, oldPoster, poster);
    }
    
    /**
     * Obtenir la description de la série
     * @return la description de la série
     */
    public String getOverview() {return overview;}
    
    /**
     * Modifier la description de la série
     * @param overview la nouvelle description de la série
     */
    public void setOverview(String overview) {
        String oldOverview = this.overview;
        this.overview = overview;
        propertyChangeSupport.firePropertyChange(PROP_OVERVIEW, oldOverview, overview);
    }
    
    /**
     * Obtenir le nom original de la série
     * @return le nom original de la série
     */
    public String getOriginalName() {return originalName;}
    
    /**
     * Modifier le nom original de la série
     * @param originalName le nouveau nom original de la série
     */
    public void setOriginalName(String originalName) {
        String oldOriginalName = this.originalName;
        this.originalName = originalName;
        propertyChangeSupport.firePropertyChange(PROP_ORIGINALNAME, oldOriginalName, originalName);
    }
    
    /**
     * Obtenir la liste des séries de la série
     * @return la liste des séries de la série
     */
    public List<Season> getListSeasonModel() {return listSeasonModel;}
    
    /**
     * Modifier la liste des séries de la série
     * @param listSeasonModel la nouvelle liste des saisons de la série
     */
    public void setListSeasonModel(List<Season> listSeasonModel) {
        List<Season> oldListSeasonModel = this.listSeasonModel;
        this.listSeasonModel = listSeasonModel;
        propertyChangeSupport.firePropertyChange(PROP_LISTSEASONMODEL, oldListSeasonModel, listSeasonModel);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Constructeur
     * @param name nom de la série
     * @param overview description de la série
     * @param originalName nom original de la série
     * @param id id unique de la série
     */
    public Serie(String name, String overview, String originalName, int id) {
        this.name = name;
        this.overview = overview;
        this.originalName = originalName;
        this.id = id;
    }
    
    /**
     * Constructeur
     * @param name nom de la série
     * @param overview description de la série
     * @param originalName nom original de la série
     * @param id id unique de la série
     * @param imagePath chemin du poster de la série
     */
    public Serie(String name, String overview, String originalName, int id, String imagePath) {
        this(name, overview, originalName, id);
        this.listSeasonModel = new ArrayList<>();
        this.poster = new Image("https://image.tmdb.org/t/p/w500" + imagePath);
    }
    
    /**
     * Constructeur
     * @param name nom de la série
     * @param overview description de la série
     * @param originalName nom original de la série
     * @param id id unique de la série
     * @param listSeason liste des saisons de la série
     * @param image image du poster de la série
     */
    public Serie(String name, String overview, String originalName, int id, List<Season> listSeason, Image image) {
        this(name, overview, originalName, id);
        this.listSeasonModel = listSeason;
        this.poster = image;
    }
    
    /**
     * Ajouter une saison à la série
     * @param s une saison
     */
    public void addSeason(Season s) {
        listSeasonModel.add(s);
        propertyChangeSupport.fireIndexedPropertyChange(PROP_LISTSEASONMODEL, listSeasonModel.size()-1, null, s);
    }
}
