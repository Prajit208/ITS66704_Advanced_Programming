package gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {
    public static void switchScene(Stage stage, Parent newRoot) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(newRoot));
        } else {
            stage.getScene().setRoot(newRoot);
        }
    }
}