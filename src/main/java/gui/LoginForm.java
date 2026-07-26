package gui;

import exception.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import manager.AuthManager;
import model.User;
import model.Role;


public class LoginForm {
    private final AuthManager auth = new AuthManager();
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;


    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

       try{
           User user = auth.login(username,password);
           System.out.println("Logged in user: " + user.getUsername() + ", role: " + user.getRole());
           errorLabel.setStyle("-fx-text-fill: green;");
           errorLabel.setText("Log in successful");
           // Implementing RBAC
           if (user.getRole() == Role.ADMIN) {
               goToAdminDashboard(user);
           } else {
               goToUserDashboard(user); // covers STUDENT and STAFF, no need for two branches
           }
       }
       catch (UserNotFoundException e){
           errorLabel.setStyle("-fx-text-fill: red;");
           errorLabel.setText(e.getMessage());
           System.out.println("Caught an exception: "+e);
       }

    }
    // Opens the registration screen
    @FXML
    private void handleRegister() {
        try {
            // Adjust the path below if RegisterForm.fxml is inside a subfolder (e.g., "/gui/RegisterForm.fxml")
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 520));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
            if (errorLabel != null) {
                errorLabel.setText("Could not load registration page.");
            }
        }
    }


    @FXML
    private void goToAdminDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboard controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root, 950, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToUserDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDashboard.fxml"));
            Parent root = loader.load();

            UserDashboard controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void goToResourceListing(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResourceListingForm.fxml"));
            Parent root = loader.load();

            ResourceListingForm controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}