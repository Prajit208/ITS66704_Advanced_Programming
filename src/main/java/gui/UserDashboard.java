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
import model.Booking;
import model.BookingStatus;
import model.Role;
import model.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDashboard {

    @FXML private Label welcomeLabel;
    @FXML private Label statUpcoming;
    @FXML private Label statPending;
    @FXML private Label statTotal;
    @FXML private Button btnManageBookings;

    @FXML private TableView<Booking> recentBookingsTable;
    @FXML private TableColumn<Booking, String> colRecentResource;
    @FXML private TableColumn<Booking, String> colRecentDate;
    @FXML private TableColumn<Booking, String> colRecentTime;
    @FXML private TableColumn<Booking, String> colRecentStatus;

    private final BookingManager bookingManager = new BookingManager();

    private User currentUser;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getName());
        roleCheck();
        loadStats();
        loadRecentBookings();
    }

    private void roleCheck() {
        // explicit both ways — Staff sees it, everyone else (Student) doesn't,
        // regardless of what the FXML default was
        boolean isStaff = currentUser.getRole() == Role.STAFF;
        btnManageBookings.setVisible(isStaff);
        btnManageBookings.setManaged(isStaff);
    }

    @FXML
    public void initialize() {
        setupRecentBookingsTable();
    }

    private void setupRecentBookingsTable() {
        colRecentResource.setCellValueFactory(new PropertyValueFactory<>("resourceID"));

        colRecentDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartTime().format(DATE_FMT)));

        colRecentTime.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartTime().format(TIME_FMT)));

        colRecentStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingStatus().toString()));

        // Color-code the status column based on value
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
        List<Booking> myBookings = bookingManager.getBookingsForUser(currentUser.getUserID());

        long upcoming = myBookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.APPROVED
                        && b.getStartTime().isAfter(LocalDateTime.now()))
                .count();

        long pending = myBookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.PENDING)
                .count();

        long total = myBookings.stream()
                .filter(b -> b.getStartTime().getMonth() == LocalDateTime.now().getMonth()
                        && b.getStartTime().getYear() == LocalDateTime.now().getYear())
                .count();

        statUpcoming.setText(String.valueOf(upcoming));
        statPending.setText(String.valueOf(pending));
        statTotal.setText(String.valueOf(total));
    }

    private void loadRecentBookings() {
        List<Booking> myBookings = bookingManager.getBookingsForUser(currentUser.getUserID());

        ObservableList<Booking> recent = FXCollections.observableArrayList(
                myBookings.stream()
                        .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                        .limit(5)
                        .toList()
        );

        recentBookingsTable.setItems(recent);
    }

    @FXML
    private void handleBrowseResources() {
        navigateToResourceListing();
    }

    @FXML
    private void handleBookNow() {
        navigateToResourceListing(); // same screen — booking starts by picking a resource first
    }

    private void navigateToResourceListing() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResourceListingForm.fxml"));
            Parent root = loader.load();

            ResourceListingForm controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewBookings() {
        navigateToBookingForm();
    }

    @FXML
    private void handleCancelRequest() {
        navigateToBookingForm(); // cancelling happens from the same booking table
    }

    private void navigateToBookingForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookingForm.fxml"));
            Parent root = loader.load();

            BookingForm controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.switchScene(stage, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookingRequests.fxml"));
            Parent root = loader.load();

            BookingRequests controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("Booking Requests");
            SceneNavigator.switchScene(stage, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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