package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPage {

    @FXML
    private void handleGetStarted() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnGetStartedStage();
            stage.setScene(new Scene(root, 500, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // helper to get the current Stage from any node on this screen
    @FXML
    private javafx.scene.control.Button btnGetStarted;

    private javafx.stage.Window btnGetStartedStage() {
        return btnGetStarted.getScene().getWindow();
    }
}