package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import manager.PersistenceManager;
import model.SystemLogs;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class SystemLogger implements Initializable {

    private final PersistenceManager persistenceManager = new PersistenceManager();

    @FXML
    private TableView<SystemLogs> logTable;

    @FXML
    private TableColumn<SystemLogs, LocalDateTime> colTimestamp;

    @FXML
    private TableColumn<SystemLogs, String> colUserID;

    @FXML
    private TableColumn<SystemLogs, String> colAction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
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
        // Navigate back to dashboard
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();
            Node node = (Node)actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root,950,600));
            stage.setTitle("Admin Dashboard");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}