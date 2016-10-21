/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import viewmodel.AllUserVM;
import viewmodel.UserVM;

/**
 * FXML Controller class
 *
 * @author Aurélien
 */
public class FXMLUserWindowController {
    
    private AllUserVM allUserViewModel;
    private static Stage thisStage;
    
    @FXML
    private TextField textFieldName, textFieldLastName;
    
    @FXML
    private ListView<UserVM> listViewUser;
    
    @FXML
    private Button buttonSelectUser;

    @FXML
    public void addUser() {
        allUserViewModel.addUser();
    }
    
    @FXML
    public void selectUser() {
        thisStage = ((Stage) buttonSelectUser.getScene().getWindow());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLSeriesWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("WatchMe");
            stage.getIcons().add(new Image(("/img/icon.png")));
            stage.setScene(new Scene(loader.load()));
            stage.setOnShowing(event -> loader.<FXMLSeriesWindowController>getController().logAs(getCurrentUser()));
            stage.show();
        } catch(Exception e) {
            e.getStackTrace();
        }
        gestionSauvegardeFermetureFenetre();
        thisStage.close();
    }
    
    public void initialize() {
        allUserViewModel = new AllUserVM();
        listViewUser.itemsProperty().bindBidirectional(allUserViewModel.listUserViewModelProperty());
        cellFactoryListViewUser();
        listViewUser();
    }    
    
    private UserVM getCurrentUser() {
        return listViewUser.getSelectionModel().getSelectedItem();
    }
    
    public void gestionSauvegardeFermetureFenetre() {
        try {
            allUserViewModel.save();
        } catch (IOException ex) {
            showAlert(ex.getMessage());
        }
    }
    
    private void showAlert(String message) {
        Alert messageAlerte = new Alert(Alert.AlertType.ERROR);
        messageAlerte.setTitle("Erreur");
        messageAlerte.setContentText(message);
        messageAlerte.setHeaderText(null);
        messageAlerte.showAndWait();
    }

    private void listViewUser() {
        listViewUser.getSelectionModel().selectedItemProperty().addListener((o, old, newV) -> {
            if(old != null) {
                textFieldName.textProperty().unbindBidirectional(old.firstNameProperty());
                textFieldLastName.textProperty().unbindBidirectional(old.lastNameProperty());
            }
            if(newV != null) {
                textFieldName.textProperty().bindBidirectional(newV.firstNameProperty());
                textFieldLastName.textProperty().bindBidirectional(newV.lastNameProperty());
            }
        });
    }

    private void cellFactoryListViewUser() {
        listViewUser.setCellFactory(param -> new ListCell<UserVM>() {
            @Override
            protected void updateItem(UserVM item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty) {
                    textProperty().bind(item.firstNameProperty().concat(" ").concat(item.lastNameProperty()));
                }
                if(empty) {
                    textProperty().unbind();
                }
            }
        });
    }
}
