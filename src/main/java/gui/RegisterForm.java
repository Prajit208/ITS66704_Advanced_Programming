package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.AuthManager;
import model.User;
import model.Role;
import utility.Validator;
import java.util.ArrayList;
import java.util.List;

import manager.PersistenceManager;

import static model.Role.STUDENT;


public class RegisterForm {
    private List<User> users = new ArrayList<>();
    private final PersistenceManager load =  new PersistenceManager();
    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Hyperlink btnBack;

    private final AuthManager register = new AuthManager();

    // adds new user to the application data if it is valid
    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();



//        Getting the validation message

        String validationMessage = Validator.validateRegistration(
                fullName,
                username,
                email,
                password,
                confirmPassword,
                users
        );


//        Return null if there validation msg is not null
        if (validationMessage != null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(validationMessage);
            return;
        }

        User user = register.registerUser(
                fullName,
                username,
                email,
                password,
                STUDENT);

        if (user != null) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Registration successful");
        try {
            // redirection to login page logic
            //Gets the log in form fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginForm.fxml"));
            //reads and loads the fxml file
            Parent root = loader.load();
            // returns the scene that contains btnBack and  the Window displaying the scene  and
            // casts the window into Scene
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root ,500, 500));
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Registration failed");
            return;

    }
    // used to go back to log in if the user already has the account
    @FXML
    private void handleBack() {
        try {
            //Gets the log in form fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginForm.fxml"));
            //reads and loads the fxml file
            Parent root = loader.load();
            // returns the scene that contains btnBack and  the Window displaying the scene  and
            // casts the window into Scene
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root ,500, 500));
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        System.out.println("RegisterForm initialized");
        users = load.loadUsers();
        System.out.println("Number of users: " + users.size());
    }
}