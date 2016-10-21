/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package controller;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import request.Request;
import viewmodel.AllSerieVM;
import viewmodel.EpisodeVM;
import viewmodel.SeasonVM;
import viewmodel.SerieVM;
import viewmodel.UserVM;

/**
 * FXML Controller class
 *
 * @author Aurélien
 */
public class FXMLSeriesWindowController {
    
    private AllSerieVM viewModelAllSerie;
    private Request request;
    private UserVM u;
    private Task t;
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem sawContextMenu = new MenuItem("Marquer comme épisode vu");
    private MenuItem notSawContextMenu = new MenuItem("Marquer comme épisode non vu");
    
    @FXML
    private ListView<SerieVM> listView;
    
    @FXML
    private ListView<SeasonVM> listViewSeason;
    
    @FXML
    private ListView<EpisodeVM> listViewEpisode;
    
    @FXML
    private Label labelName, labelOriginalName, labelLogAs, labelOverview, labelOverviewEpisode, labelNameEpisode, labelNumberEpisode;
    
    @FXML
    private CheckBox checkFav;
    
    @FXML
    private Button buttonMostPopular;
    
    @FXML
    private ChoiceBox choiceBoxMenu;
    
    @FXML
    private ImageView imageViewPoster;
    
    @FXML
    private ProgressBar progress;
    
    public void initialize() {
        viewModelAllSerie = AllSerieVM.getInstance();
        request = new Request();
      
        checkFav.setDisable(true);
        gestionContextMenu();
        choiceBoxMenu.getSelectionModel().select(0);
        miseEnPlaceDesListes();
        clickButtonMostPopular();
    }
    
    private void choiceBoxMenuSelectedItemListener() {
        choiceBoxMenu.getSelectionModel().selectedItemProperty().addListener((o, old, newV) -> {
            if(newV != null) {
                if(newV.equals("Mes séries favorites")) {
                    choiceBoxMenuSelectedItemListenerSeriesFavorites();
                }
                if(newV.equals("Séries populaires")) {
                    choiceBoxMenuSelectedItemListenerSeriesPopulaires();
                }
            }
        });
    }
    
    private void choiceBoxMenuSelectedItemListenerSeriesFavorites() {
        listView.itemsProperty().unbind();
        listViewSeason.itemsProperty().unbind();
        listViewEpisode.itemsProperty().unbind();
        listView.itemsProperty().bind(u.listViewModelFavSerieProperty());
    }
    
    private void choiceBoxMenuSelectedItemListenerSeriesPopulaires() {
        listView.itemsProperty().unbind();
        listViewSeason.itemsProperty().unbind();
        listViewEpisode.itemsProperty().unbind();
        listView.itemsProperty().bind(viewModelAllSerie.listViewModelSerieProperty());
    }
    
    private void cellFactoryListViewSerie() {
        listView.setCellFactory(param -> {
            return new ListCell<SerieVM>() {
                
                @Override
                protected void updateItem(SerieVM item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty) {
                        textProperty().bind(item.nameProperty());
                    }
                    if(empty) {
                        textProperty().unbind();
                        setText("");
                    }
                }
            };
        });
    }
    
    private void cellFactoryListViewSeason() {
        listViewSeason.setCellFactory(param -> {
            return new ListCell<SeasonVM>() {
                
                @Override
                protected void updateItem(SeasonVM item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty) {
                        textProperty().bind(Bindings.concat("Saison ").concat(item.numberProperty()));
                    }
                    if(empty) {
                        textProperty().unbind();
                        setText("");
                    }
                }
                
            };
        });
    }
    
    private void unbindLabelOverviewEpisode() {
        labelOverviewEpisode.textProperty().unbind();
        labelOverviewEpisode.setText("");
    }
    
    private void cellFactoryListViewEpisode() {
        listViewEpisode.setCellFactory(param -> {
            return new ListCell<EpisodeVM>() {
                
                @Override
                protected void updateItem(EpisodeVM item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty) {
                        cellFactoryNotEmptyListViewEpisode(item, this);
                    }
                    if(empty) {
                        textProperty().unbind();
                        setText("");
                    }
                }
            };
        });
    }
    
    private void cellFactoryNotEmptyListViewEpisode(EpisodeVM item, ListCell<EpisodeVM> aThis) {
        aThis.textProperty().bind(item.titleProperty().concat(" - Episode ").concat(item.numberProperty()));
        gestionStyleClassSawEpisode(item, aThis);
    }
    
    private void gestionStyleClassSawEpisode(EpisodeVM item, ListCell<EpisodeVM> cell) {
        if(u.isSaw(item.getUniqueId()) && !cell.getStyleClass().contains("sawEpisode")) {
            cell.getStyleClass().add("sawEpisode");
        } else if(cell.getStyleClass().contains("sawEpisode") && !u.isSaw(item.getUniqueId())) {
            cell.getStyleClass().remove("sawEpisode");
        }
    }
    
    private void gestionCheckBox(SerieVM tv) {
        if(u.checkList(tv)) {
            checkFav.setSelected(true);
        } else {
            checkFav.setSelected(false);
        }
        checkFav.setOnMouseClicked(event -> {
            if(checkFav.isSelected()) {
                u.addFavTv(tv);
            }
            if(!checkFav.isSelected()) {
                u.removeFavTv(tv);
            }
        });
    }
    
    private void gestionContextMenu() {
        contextMenu.getItems().add(sawContextMenu);
        contextMenu.getItems().add(notSawContextMenu);
        
        listViewEpisode.setContextMenu(contextMenu);
        sawContextMenu.setOnAction(e -> {
            sawClick();
        });
        notSawContextMenu.setOnAction(e -> {
            notSawClick();
        });
    }
    
    private void sawClick() {
        if(listViewEpisode.getSelectionModel().getSelectedIndex() == -1) {
            displayAlertEpisode();
        } else {
            u.addSawEpisode(listViewEpisode.getSelectionModel().getSelectedItem().getUniqueId());
            cellFactoryListViewEpisode();
        }
    }
    
    private void notSawClick() {
        if(listViewEpisode.getSelectionModel().getSelectedIndex() == -1) {
            displayAlertEpisode();
        } else {
            u.removeSawEpisode(listViewEpisode.getSelectionModel().getSelectedItem().getUniqueId());
            cellFactoryListViewEpisode();
        }
    }
    
    private void displayAlertEpisode() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Vous devez sélectionner un épisode dans la liste pour effectuer cette action.");
        alert.show();
    }
    
    @FXML
    public void contextMenuRequest() {
        if(u.isSaw(listViewEpisode.getSelectionModel().getSelectedItem().getUniqueId())) {
            contextMenu.getItems().get(0).setDisable(true);
            contextMenu.getItems().get(1).setDisable(false);
        } else {
            contextMenu.getItems().get(0).setDisable(false);
            contextMenu.getItems().get(1).setDisable(true);
        }
    }

    private void unbindLabelNameEpisode() {
        labelNameEpisode.textProperty().unbind();
        labelNameEpisode.setText("");
    }

    private void unbindImageViewPoster() {
        imageViewPoster.imageProperty().unbind();
        imageViewPoster.setImage(new Image("/img/default-img.png"));
    }

    private void unbindListViewEpisode() {
        listViewEpisode.itemsProperty().unbind();
        listViewEpisode.setItems(null);
    }

    private void unbindListViewSeason() {
        listViewSeason.itemsProperty().unbind();
        listViewSeason.setItems(null);
    }

    private void unbindLabelNumberEpisode() {
        labelNumberEpisode.textProperty().unbind();
        labelNumberEpisode.setText("");
    }

    private void unbindLabelOverview() {
        labelOverview.textProperty().unbind();
        labelOverview.setText("");
    }

    private void unbindLabelName() {
        labelName.textProperty().unbind();
        labelName.setText("");
    }

    private void unbindLabelOriginalName() {
        labelOriginalName.textProperty().unbind();
        labelOriginalName.setText("");
    }

    private void unbindListView() {
        checkFav.setDisable(true);
        unbindEpisode();
        unbindListViewEpisode();
        unbindListViewSeason();
        unbindImageViewPoster();
        unbindLabelOverview();
        unbindLabelName();
        unbindLabelOriginalName();
    }
    
    private void unbindEpisode() {
        unbindLabelOverviewEpisode();
        unbindLabelNameEpisode();
        unbindLabelNumberEpisode();
    }

    private void clickButtonMostPopular() {
        buttonMostPopular.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                progress.setVisible(true);
                buttonMostPopular.setDisable(true);
                listView.setDisable(true);
                tryCatchUrl();
            }
        });
    }
    
    private void tryCatchUrl() {
        try {
            t = request.getApiInformationPopular(viewModelAllSerie, listView, progress);
            progress.progressProperty().bind(t.progressProperty());
            new Thread(t).start();
        } catch (Exception e) {
            exceptionLoading();
            listView.setDisable(false);
        }
    }
    
    private void exceptionLoading() {
        showAlert("Erreur pendant le chargement");
        progress.setVisible(false);
        buttonMostPopular.setDisable(false);
    }

    private void bindListView(SerieVM newV) {
        checkFav.setDisable(false);
        listViewSeason.itemsProperty().bind(newV.listViewModelSeasonProperty());
        labelName.textProperty().bind(newV.nameProperty());
        labelOriginalName.textProperty().bind(newV.originalNameProperty());
        labelOverview.textProperty().bind(newV.overviewProperty());
        imageViewPoster.imageProperty().bind(newV.posterProperty());
        gestionCheckBox(newV);
    }

    private void listenerSelectedItemListViewSeason() {
        listViewSeason.getSelectionModel().selectedItemProperty().addListener((o, old, newV) -> {
            if(old != null) {
                unbindListViewEpisode();
                unbindLabelOverviewEpisode();
                unbindLabelNameEpisode();
                unbindLabelNumberEpisode();
            }
            if(newV != null) {
                listViewEpisode.itemsProperty().bind(newV.listViewModelEpisodeProperty());
            }
        });
    }

    private void listenerSelectedItemListViewEpisode() {
        listViewEpisode.getSelectionModel().selectedItemProperty().addListener((o, old, newV) -> {
            if(old != null) {
                labelOverviewEpisode.textProperty().unbind();
                labelNameEpisode.textProperty().unbind();
                labelNumberEpisode.textProperty().unbind();
            }
            if(newV != null) {
                labelOverviewEpisode.textProperty().bind(newV.overviewProperty());
                labelNameEpisode.textProperty().bind(newV.titleProperty());
                labelNumberEpisode.textProperty().bind(Bindings.concat("Episode ").concat(newV.numberProperty()));
            }
        });
    }

    private void listenerSelectedItemListView() {
        listView.getSelectionModel().selectedItemProperty().addListener((o, old, newV) -> {
            if(old != null) {
                unbindListView();
            }
            if(newV != null) {
                if(newV.getListViewModelSeason().isEmpty()) {
                    try {
                        request.getApiInformationSpecified(newV.getId(), newV);
                    } catch (Exception e) {
                        showAlert("Erreur pendant le chargement");
                    }
                }
                bindListView(newV);
            }
        });
    }

    private void miseEnPlaceDesListes() {
        listView.itemsProperty().bind(viewModelAllSerie.listViewModelSerieProperty());
        listenerSelectedItemListView();
        listenerSelectedItemListViewSeason();
        listenerSelectedItemListViewEpisode();
        choiceBoxMenuSelectedItemListener();
        cellFactoryListViewSerie();
        cellFactoryListViewSeason();
        cellFactoryListViewEpisode();
    }
    
    public void logAs(UserVM u) {
        this.u = u;
        labelLogAs.setText("Connecté en tant que : " + u.getFirstName());
    }
    
    private void showAlert(String message) {
        Alert messageAlerte = new Alert(Alert.AlertType.ERROR);
        messageAlerte.setTitle("Erreur");
        messageAlerte.setContentText(message);
        messageAlerte.setHeaderText(null);
        messageAlerte.showAndWait();
    }
}
