

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.PersistenceManager;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PersistenceManager init = new PersistenceManager();
        init.initializeUsersFile();

        Parent root = FXMLLoader.load(getClass().getResource("/LandingPage.fxml"));

        primaryStage.setTitle("Campus Booking System");
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(950);
        primaryStage.setScene(new Scene(root, 950, 650));
        primaryStage.show();

        Platform.runLater(() -> primaryStage.setMaximized(true));
    }

    public static void main(String[] args) {
        launch(args);
    }
}