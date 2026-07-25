package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.PersistenceManager;
import model.User;
import java.util.ArrayList;

import java.io.IOException;

public class LoginForm {
    private ArrayList<User> users = new ArrayList<>();
    private final PersistenceManager load =  new PersistenceManager();
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        System.out.println("LoginForm initialized");
        users = load.loadUsers();
        System.out.println("Number of users: " + users.size());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        // Authentication method call here

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

// temporary access button to view admin dashboard
    @FXML
    private void handleTempAdminAccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // temporary access button to view what student would see
    @FXML
    private void handleDevStudent() {
        goToResourceListing("STUDENT");
    }
    // temporary access button to view what student would see
    @FXML
    private void handleDevStaff() {
        goToResourceListing("STAFF");
    }

    private void goToResourceListing(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResourceListingForm.fxml"));
            Parent root = loader.load();

            ResourceListingForm controller = loader.getController();
            controller.setCurrentUserRole(role);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}