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

import manager.BookingManager;
import model.Booking;
import model.BookingStatus;
import model.Role;
import model.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingRequests{

    private final BookingManager bookingManager = new BookingManager();

    @FXML private TableView<Booking> requestsTable;
    @FXML private TableColumn<Booking, String> colBookingId;
    @FXML private TableColumn<Booking, String> colResourceId;
    @FXML private TableColumn<Booking, String> colRequestedBy;
    @FXML private TableColumn<Booking, String> colDate;
    @FXML private TableColumn<Booking, String> colTime;
    @FXML private TableColumn<Booking, String> colStatus;

    private User currentUser;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        colBookingId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("bookingID"));
        colResourceId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("resourceID"));
        colRequestedBy.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("creatorName"));

        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartTime().format(DATE_FMT)));

        colTime.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStartTime().format(TIME_FMT) + " - " +
                                data.getValue().getEndTime().format(TIME_FMT)));

        colStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingStatus().toString()));

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        List<Booking> all = bookingManager.getAllBookings();
        ObservableList<Booking> pending = FXCollections.observableArrayList();
        for (Booking b : all) {
            if (b.getBookingStatus() == BookingStatus.PENDING) {
                pending.add(b);
            }
        }
        requestsTable.setItems(pending);
    }

    @FXML
    private void handleApprove() {
        if (currentUser == null) {
            showError("User session is missing.");
            return;
        }

        Booking selected = requestsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a booking first.");
            return;
        }

        try {
            bookingManager.approveBooking(
                    selected.getBookingID(),
                    currentUser
            );

            loadPendingRequests();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleReject() {
        if (currentUser == null) {
            showError("User session is missing.");
            return;
        }

        Booking selected = requestsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a booking first.");
            return;
        }

        try {
            bookingManager.rejectBooking(
                    selected.getBookingID(),
                    currentUser
            );

            loadPendingRequests();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void handleBack() {
        if (currentUser == null) {
            showError("User session is missing.");
            return;
        }

        try {
            String fxmlPath = currentUser.getRole() == Role.ADMIN
                    ? "/AdminDashboard.fxml"
                    : "/UserDashboard.fxml";

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxmlPath)
            );

            Parent root = loader.load();

            if (currentUser.getRole() == Role.ADMIN) {
                AdminDashboard controller = loader.getController();
                controller.setCurrentUser(currentUser);
            } else {
                UserDashboard controller = loader.getController();
                controller.setCurrentUser(currentUser);
            }

            Stage stage = (Stage) requestsTable.getScene().getWindow();

            stage.setTitle(
                    currentUser.getRole() == Role.ADMIN
                            ? "Admin Dashboard"
                            : "User Dashboard"
            );

            stage.setScene(new Scene(root, 950, 600));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}