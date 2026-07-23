package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class RegisterForm {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button btnBack;

    // adds new user to the application data if it is valid
    @FXML
    private void handleRegister() {
        // Registration logic here
    }
    // used to go back to log in if the user already has the account
    @FXML
    private void handleBack() {
        try {
            //Gets the log in form fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            //reads and loads the fxml file
            Parent root = loader.load();
            // returns the scene that contains btnBack and  the Window displaying the scene  and
            // casts the window into Scene
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Student", "Staff");
    }
}