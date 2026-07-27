package manager;

import gui.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.SystemLogs;
import model.User;
import model.Role;
import utility.Validator;
import gui.AdminDashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final PersistenceManager persistenceManager = new PersistenceManager();

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<Role> roleBox;


    @FXML
    private TableView<User> userTable;


    @FXML
    private TableColumn<User,String> nameColumn;

    @FXML
    private TableColumn<User,String> usernameColumn;

    @FXML
    private TableColumn<User,String> emailColumn;

    @FXML
    private TableColumn<User,String> roleColumn;


    private User currentUser;

    private ObservableList<User> users =
            FXCollections.observableArrayList();

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

// TODO : This UserManager is a controller file, not logic , should not be in manager, move it to gui folder later
    // getting the all the users
    @FXML
    public void initialize(){

        roleBox.setItems(
                FXCollections.observableArrayList(
                        Role.ADMIN,
                        Role.STAFF,
                        Role.STUDENT
                )
        );

        // configuring the user table columns
        nameColumn.setCellValueFactory(
                data->new SimpleStringProperty(
                        data.getValue().getName()
                )
        );
        usernameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getUsername()
                )
        );


        emailColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );


        roleColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getRole().toString()
                )
        );


        loadUsers();
        //everytime you select an item in the table the input fields gets autmatically filled
        userTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldUser, selectedUser) -> {

                    if(selectedUser != null){

                        fullNameField.setText(
                                selectedUser.getName()
                        );

                        usernameField.setText(
                                selectedUser.getUsername()
                        );

                        emailField.setText(
                                selectedUser.getEmail()
                        );

                        roleBox.setValue(
                                selectedUser.getRole()
                        );
                    }

                });

    }




    private void loadUsers(){

        users.clear();

        List<User> loadedUsers = persistenceManager.loadUsers();
        // load all the users if loaded users is not null
        if(loadedUsers != null){
            users.addAll(loadedUsers);
        }

        userTable.setItems(users);
    }




    @FXML
    public void createUser(){

        String error = Validator.validateCreate(
                fullNameField.getText(),
                usernameField.getText(),
                emailField.getText(),
                roleBox.getValue(),
                new ArrayList<>(users)
        );


        if(error != null){
            // creates an alert object
            Alert alert = new Alert(
                    Alert.AlertType.ERROR
            );

            alert.setContentText(error);
            alert.show();

            return;
        }


        User user = new User(
                fullNameField.getText(),
                usernameField.getText(),
                AuthManager.hash("default123"),
                emailField.getText(),
                roleBox.getValue()
        );


        users.add(user);

        persistenceManager.saveUsers(
                new ArrayList<>(users)
        );
        clear();
    }



    // updates the User Information
    @FXML
    public void updateUser(){

        User selected =
                userTable.getSelectionModel()
                        .getSelectedItem();


        if(selected != null){
            // validates the fields
            String error = Validator.validateUpdate(
                    fullNameField.getText(),
                    usernameField.getText(),
                    emailField.getText(),
                    roleBox.getValue(),
                    new ArrayList<>(users)
            );
            if(error != null){
                // creates an alert object
                Alert alert = new Alert(
                        Alert.AlertType.ERROR
                );

                alert.setContentText(error); // sets the error
                alert.show();// show the error

                return;
            }
            selected.setName(
                    fullNameField.getText()
            );
            selected.setUsername(
                    usernameField.getText()
            );

            selected.setEmail(
                    emailField.getText()
            );

            selected.setRole(
                    roleBox.getValue()
            );


            userTable.refresh();


            persistenceManager.saveUsers(
                    new ArrayList<>(users)
            );

            clear();
        }
    }



    // deletes the user
    @FXML
    public void deleteUser(){

        User selected =
                userTable.getSelectionModel()
                        .getSelectedItem();


        if(selected != null){

            users.remove(selected);

            userTable.refresh();
            persistenceManager.saveUsers(
                    new ArrayList<>(users)
            );
            clear();
        }
    }


    //clears the field
    private void clear(){
        fullNameField.clear();
        usernameField.clear();
        emailField.clear();
        roleBox.setValue(null);

    }



    @FXML
    public void backDashboard(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboard controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Admin Dashboard");
            SceneNavigator.switchScene(stage, root);
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

}