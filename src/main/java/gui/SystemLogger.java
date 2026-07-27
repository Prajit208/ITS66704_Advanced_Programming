package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import manager.PersistenceManager;
import model.SystemLogs;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SystemLogger implements Initializable {

    private final PersistenceManager persistenceManager = new PersistenceManager();
    private static final DateTimeFormatter LOG_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private User currentUser;

    @FXML
    private TableView<SystemLogs> logTable;

    @FXML
    private TableColumn<SystemLogs, String> colTimestamp;

    @FXML
    private TableColumn<SystemLogs, String> colUserID;

    @FXML
    private TableColumn<SystemLogs, String> colAction;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTimestamp.setCellValueFactory(data -> {
            LocalDateTime time = data.getValue().getTimestamp();
            return new SimpleStringProperty(time != null ? time.format(LOG_FMT) : "");
        });

        colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        loadLogs();
    }

    private void loadLogs() {
        ObservableList<SystemLogs> logs = FXCollections.observableArrayList(
                persistenceManager.loadSystemLogs()
        );
        logTable.setItems(logs);
    }

    @FXML
    private void refreshLogs() {
        loadLogs();
    }

    @FXML
    private void backDashboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();

            AdminDashboard controller = loader.getController();
            if (currentUser != null) {
                controller.setCurrentUser(currentUser);
            }

            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            // Uses SceneNavigator instead of hardcoded 950x600 fixed dimensions
            SceneNavigator.switchScene(stage, root);
            stage.setTitle("Admin Dashboard");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}