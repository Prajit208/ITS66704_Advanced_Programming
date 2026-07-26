package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import model.*;
import manager.PersistenceManager;
import manager.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ResourceManagement {

    private final PersistenceManager persistenceManager = new PersistenceManager();
    private final ResourceManager<Resource> resourceManager = new ResourceManager<>();

    @FXML private TextField resourceIdField;
    @FXML private ComboBox<String> typeBox;
    @FXML private TextField locationField;
    @FXML private TextField capacityField;
    @FXML private TextField rulesField;
    @FXML private CheckBox availableCheck;

    @FXML private TableView<Resource> resourceTable;
    @FXML private TableColumn<Resource, String> colResourceId;
    @FXML private TableColumn<Resource, String> colType;
    @FXML private TableColumn<Resource, String> colLocation;
    @FXML private TableColumn<Resource, Integer> colCapacity;
    @FXML private TableColumn<Resource, String> colAvailability;
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    @FXML
    public void initialize() {
        typeBox.setItems(FXCollections.observableArrayList(
                "Study Room", "Lab", "Equipment", "Event Space"));

        colResourceId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("resourceID"));
        colType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("type"));
        colLocation.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("location"));
        colCapacity.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("capacity"));

        colAvailability.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isAvailabilityStatus() ? "Available" : "Booked"));

        loadResources();

        resourceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldR, selected) -> {
            if (selected != null) {
                resourceIdField.setText(selected.getResourceID());
                resourceIdField.setEditable(false);
                typeBox.setValue(selected.getType());
                locationField.setText(selected.getLocation());
                capacityField.setText(String.valueOf(selected.getCapacity()));
                rulesField.setText(String.join(", ", selected.getBookingRules()));
                availableCheck.setSelected(selected.isAvailabilityStatus());
            }
        });
    }

    private void loadResources() {
        List<Resource> saved = persistenceManager.loadResources();
        for (Resource r : saved) {
            resourceManager.addResource(r);
        }
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<Resource> items = FXCollections.observableArrayList(
                resourceManager.searchAvailable(null, null, false, null));
        resourceTable.setItems(items);
    }

    @FXML
    public void addResource() {
        String type = typeBox.getValue();
        String location = locationField.getText();
        String rulesText = rulesField.getText();
        List<String> rules = rulesText == null || rulesText.isBlank()
                ? new ArrayList<>()
                : Arrays.asList(rulesText.split("\\s*,\\s*"));
        boolean available = availableCheck.isSelected();

        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            showError("Capacity must be a number");
            return;
        }

        if (type == null || location == null || location.isBlank()) {
            showError("Type and Location are required");
            return;
        }

        String resourceID = "R" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Resource resource = switch (type) {
            case "Study Room" -> new StudyRoom(resourceID, location, capacity, available, rules);
            case "Lab" -> new Lab(resourceID, location, capacity, available, rules, new ArrayList<>());
            case "Equipment" -> new EquipmentResource(resourceID, location, capacity, available, rules);
            case "Event Space" -> new EventSpace(resourceID, location, capacity, available, rules);
            default -> null;
        };

        if (resource == null) {
            showError("Please select a valid type");
            return;
        }

        resourceManager.addResource(resource);
        persistenceManager.saveResources(resourceManager.searchAvailable(null, null, false, null));
        refreshTable();
        clear();
    }

    @FXML
    public void removeResource() {
        Resource selected = resourceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            resourceManager.removeResource(selected.getResourceID());
            persistenceManager.saveResources(resourceManager.searchAvailable(null, null, false, null));
            refreshTable();
            clear();
        }
    }

    private void clear() {
        resourceIdField.clear();
        resourceIdField.setEditable(true);
        typeBox.setValue(null);
        locationField.clear();
        capacityField.clear();
        rulesField.clear();
        availableCheck.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void backDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/AdminDashboard.fxml")
            );

            Parent root = loader.load();

            AdminDashboard controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) resourceIdField.getScene().getWindow();

            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root, 950, 600));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}