package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import manager.PersistenceManager;
import model.Resource;
import manager.ResourceManager;

import java.io.IOException;
import java.util.List;

public class ResourceListingForm {

    @FXML private ComboBox<String> typeFilter;
    @FXML private TextField locationFilter;
    @FXML private CheckBox availableOnlyCheck;

    @FXML private TableView<Resource> resourceTable;
    @FXML private TableColumn<Resource, String> colResourceId;
    @FXML private TableColumn<Resource, String> colType;
    @FXML private TableColumn<Resource, String> colLocation;
    @FXML private TableColumn<Resource, Integer> colCapacity;
    @FXML private TableColumn<Resource, String> colAvailability;
    @FXML private TableColumn<Resource, String> colRules;
    @FXML private Button btnBook;
    @FXML private Button btnMyBookings;

    private String currentUserRole = "STUDENT";

    private ResourceManager<Resource> resourceManager = new ResourceManager<>();

    @FXML
    public void initialize() {
        typeFilter.getItems().addAll("Study Room", "Lab", "Equipment", "Event Space");

        colResourceId.setCellValueFactory(new PropertyValueFactory<>("resourceID"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        colAvailability.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().isAvailabilityStatus() ? "Available" : "Booked"));

        colRules.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.join(", ", data.getValue().getBookingRules())));

        roleCheck();
        loadInitialResources();
    }

    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
        roleCheck();
    }

    private void roleCheck() {
        if (currentUserRole.equals("STAFF")) {
            btnMyBookings.setText("Manage My Bookings");
        }
    }

    private void loadInitialResources() {
        PersistenceManager persistenceManager = new PersistenceManager();
        List<Resource> savedResources = persistenceManager.loadResources();

        for (Resource r : savedResources) {
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
    private void handleSearch() {
        String type = typeFilter.getValue();
        String location = locationFilter.getText();
        boolean availableOnly = availableOnlyCheck.isSelected();

        List<Resource> results = resourceManager.searchAvailable(type, location, availableOnly, null);
        resourceTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleBookResource() {
        Resource selected = resourceTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            // placeholder: show an alert telling the user to select a row first
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookingForm.fxml"));
            Parent root = loader.load();

            BookingForm controller = loader.getController();
            controller.setResourceId(selected.getResourceID());
            controller.setCurrentUserRole(currentUserRole);

            Stage stage = (Stage) btnBook.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyBookings() {
        // placeholder: BookingManager.getBookingsForUser(currentUserId)
    }

    @FXML
    private void handleBack() {
        // placeholder: return to previous dashboard/screen
    }
}