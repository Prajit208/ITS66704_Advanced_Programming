package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginForm {

    public void login(Stage primaryStage) {
        // Title
        Label titleLabel = new Label("Login Form");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Username field
        Label usernameLabel = new Label("Username");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Password field
        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Error label, shown if login fails or fields are empty
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                return;
            }

            // Authrntication here AuthManager.login(username, password) gets called here
        });
        // Stack user name label and entry field horizontally
        HBox usernameRow = new HBox(10, usernameLabel, usernameField);
        usernameRow.setAlignment(Pos.CENTER_LEFT);

        HBox passwordRow = new HBox(10, passwordLabel, passwordField);
        passwordRow.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(15, titleLabel, usernameRow, passwordRow, errorLabel, loginButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Campus Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}