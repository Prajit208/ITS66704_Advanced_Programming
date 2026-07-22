
package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LandingPage {
    public void show(Stage primaryStage) {
        // Main Text
        Label title = new Label("Campus Resource Booking System");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        // Sub Text
        Label subtitle = new Label("Book study rooms, labs, and other resources");
        // Login redirect button or get started button that redirects to login or register page
        Button getStartedButton = new Button("Get Started");
        getStartedButton.setOnAction(e -> {
            LoginForm loginform= new LoginForm();
            // access login method in LoginForm class
            loginform.login(primaryStage);
        });

        VBox root = new VBox(20, title, subtitle, getStartedButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 50px;");

        primaryStage.setTitle("Campus Booking System");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }
}