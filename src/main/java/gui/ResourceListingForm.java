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

import model.Resource;
import model.StudyRoom;
import model.Lab;
import model.EquipmentResource;
import model.EventSpace;

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
        loadDummyData();
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

    @FXML
    private void handleSearch() {
        String type = typeFilter.getValue();
        String location = locationFilter.getText();
        boolean availableOnly = availableOnlyCheck.isSelected();

        // placeholder: ResourceManager.searchAvailable(type, location, availableOnly, null)
        // then populate resourceTable with the results
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
            stage.setScene(new Scene(root, 800, 500));
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

    private void loadDummyData() {
        ObservableList<Resource> dummyResources = FXCollections.observableArrayList(
                new StudyRoom("R001", "Library 2F", 4, true, List.of("Max 2hr booking")),
                new Lab("R002", "CS Building B12", 20, false, List.of("Requires staff approval"), List.of("PCs", "Projector")),
                new EquipmentResource("R003", "Media Lab", 1, true, List.of("Return within 24hr")),
                new EventSpace("R004", "Main Hall", 100, true, List.of("Book 1 week ahead"))
        );
        resourceTable.setItems(dummyResources);
    }
}