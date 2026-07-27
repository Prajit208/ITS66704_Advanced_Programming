package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPage {
    @FXML
    private javafx.scene.control.Button btnGetStarted;

    @FXML
    private void handleGetStarted() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnGetStarted.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}