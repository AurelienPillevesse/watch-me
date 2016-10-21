/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AllUser;
import model.User;

/**
 *
 * @author Aurélien
 */
public class AllUserVM implements PropertyChangeListener{

    private AllUser allUserModel;
    
    private ObservableList<UserVM> listUserViewModelObs = FXCollections.observableArrayList();
    private final ListProperty<UserVM> listUserViewModel = new SimpleListProperty<>(listUserViewModelObs);
        public ObservableList getListUserViewModel() {return listUserViewModel.get();}
        public void setListUserViewModel(ObservableList value) {listUserViewModel.set(value);}
        public ListProperty listUserViewModelProperty() {return listUserViewModel;}
    
    /**
     * Ajouter un utilisateur au modèle
     */
    public void addUser() {
        allUserModel.addUser();
    }
    
    /**
     * Obtenir la liste des utilisateurs du modèle
     * @return 
     */
    public List<User> getListUser() {
        return allUserModel.getListModelUser();
    }
    
    /**
     * Constructeur
     */
    public AllUserVM() {
        allUserModel = new AllUser();
        allUserModel.addPropertyChangeListener(this);
        allUserModel.getListModelUser().forEach(e -> listUserViewModelObs.add(new UserVM((User) e)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(AllUser.PROP_LISTMODELUSER)) {
            listUserViewModelObs.add(((IndexedPropertyChangeEvent)evt).getIndex(), new UserVM((User) evt.getNewValue()));
        }
    }

    /**
     * Sauvegarder les utilisateurs
     * @throws IOException 
     */
    public void save() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.data"))) {
            oos.writeObject(allUserModel);
        }
    }
    
}
