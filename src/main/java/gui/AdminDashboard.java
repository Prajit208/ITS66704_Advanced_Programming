package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import manager.BookingManager;
import manager.PersistenceManager;
import manager.UserManager;
import model.Booking;
import model.BookingStatus;
import model.Resource;
import model.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboard {

    @FXML private Label welcomeLabel;

    @FXML private Label statTotalUsers;
    @FXML private Label statPendingBookings;
    @FXML private Label statTotalBookings;
    @FXML private Label statTotalResources;
    @FXML private Label statAvailableResources;

    @FXML private TableView<Booking> recentBookingsTable;
    @FXML private TableColumn<Booking, String> colRecentResource;
    @FXML private TableColumn<Booking, String> colRecentRequestedBy;
    @FXML private TableColumn<Booking, String> colRecentDate;
    @FXML private TableColumn<Booking, String> colRecentStatus;
    @FXML private TableColumn<Booking, String> colRecentTime;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final BookingManager bookingManager = new BookingManager();
    private final PersistenceManager persistenceManager = new PersistenceManager();

    private User currentUser;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getName());
        loadStats();
        loadRecentBookings();
    }

    @FXML
    public void initialize() {
        setupRecentBookingsTable();
    }

    private void setupRecentBookingsTable() {
        colRecentRequestedBy.setCellValueFactory(new PropertyValueFactory<>("creatorName"));

        colRecentDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartTime().format(DATE_FMT)));
        colRecentTime.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStartTime().format(TIME_FMT) + " - " +
                                data.getValue().getEndTime().format(TIME_FMT)));
        colRecentStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingStatus().toString()));

        colRecentStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(status);
                switch (status) {
                    case "APPROVED" -> setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                    case "PENDING" -> setStyle("-fx-text-fill: #F9A825; -fx-font-weight: bold;");
                    case "REJECTED" -> setStyle("-fx-text-fill: #E53935; -fx-font-weight: bold;");
                    case "CANCELLED" -> setStyle("-fx-text-fill: #9E9E9E; -fx-font-weight: bold;");
                    default -> setStyle("");
                }
            }
        });
    }

    private void loadStats() {
        List<User> allUsers = persistenceManager.loadUsers();
        List<Resource> allResources = persistenceManager.loadResources();
        List<Booking> allBookings = bookingManager.getAllBookings();

        long pendingCount = allBookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.PENDING)
                .count();

        long availableCount = allResources.stream()
                .filter(Resource::isAvailabilityStatus)
                .count();

        statTotalUsers.setText(String.valueOf(allUsers.size()));
        statPendingBookings.setText(String.valueOf(pendingCount));
        statTotalBookings.setText(String.valueOf(allBookings.size()));
        statTotalResources.setText(String.valueOf(allResources.size()));
        statAvailableResources.setText(String.valueOf(availableCount));
    }

    private void loadRecentBookings() {
        List<Booking> allBookings = bookingManager.getAllBookings();
        List<Resource> allResources = persistenceManager.loadResources();

        Map<String, String> resourceTypeById = new HashMap<>();
        for (Resource resource : allResources) {
            resourceTypeById.put(resource.getResourceID(), resource.getType());
        }

        ObservableList<Booking> recent = FXCollections.observableArrayList(
                allBookings.stream()
                        .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                        .limit(5)
                        .toList()
        );

        recentBookingsTable.setItems(recent);

        colRecentResource.setCellValueFactory(data -> {
            String resId = data.getValue().getResourceID();
            String type = resourceTypeById.get(resId);

            // If type exists, combine as "Type (ID)", otherwise fallback to just the ID
            String displayString = (type != null) ? type + " (" + resId + ")" : resId;
            return new SimpleStringProperty(displayString);
        });
    }

    @FXML
    private void handleSearchResources() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResourceListingForm.fxml"));
            Parent root = loader.load();

            ResourceListingForm controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
            stage.setTitle("Browse Resources");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageResources() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageResourcesForm.fxml"));
            Parent root = loader.load();

            ResourceManagement controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
            stage.setTitle("Manage Resources");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookingRequests.fxml"));
            Parent root = loader.load();

            BookingRequests controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);

            stage.setTitle("Booking Requests");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManager.fxml"));
            Parent root = loader.load();

            UserManager controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
            stage.setTitle("Manage Users");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewLogs() {
        // show system log contents
        // calls SystemLogger.getLogs()
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}