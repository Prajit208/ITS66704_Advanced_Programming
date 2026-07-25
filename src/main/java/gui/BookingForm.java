package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import manager.BookingManager;
import model.Booking;
import model.User;
import model.Role;
import exception.ResourceUnavailableException;
import exception.InvalidBookingDurationException;
import exception.UnauthorizedAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingForm {

    @FXML private TextField resourceIdField;
    @FXML private DatePicker bookingDatePicker;
    @FXML private ComboBox<String> startTimeBox;
    @FXML private ComboBox<String> durationBox;
    @FXML private Button btnConfirmBooking;
    @FXML private CheckBox overrideAvailabilityCheck;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> colBookingId;
    @FXML private TableColumn<Booking, String> colResourceId;
    @FXML private TableColumn<Booking, String> colDate;
    @FXML private TableColumn<Booking, String> colTime;
    @FXML private TableColumn<Booking, String> colStatus;

    @FXML private Label errorLabel;

    @FXML private Button btnCancelBooking;
    @FXML private Button btnModifyBooking;

    private BookingManager bookingManager = new BookingManager();

    // TODO: this should come from AuthManager/passed in via setter once login is real
    private String currentUserRole = "STUDENT";
    private String currentUserID = "user1"; // TODO: replace once login passes real user in
    private String currentUserName = "Test User";
    private String selectedResourceId;

    @FXML
    public void initialize() {
        startTimeBox.getItems().addAll("09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00");
        durationBox.getItems().addAll("1", "2", "3");

        colBookingId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingID()));
        colResourceId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getResourceID()));
        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartTime().toLocalDate().toString()));
        colTime.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStartTime().toLocalTime() + " - " + data.getValue().getEndTime().toLocalTime()));
        colStatus.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingStatus().toString()));

        refreshBookingTable();
    }

    public void setResourceId(String resourceId) {
        this.selectedResourceId = resourceId;
        resourceIdField.setText(resourceId);
        resourceIdField.setEditable(false); // lock it, since it came from a deliberate selection
        resourceIdField.setStyle("-fx-background-color: #F4F6F8;");
    }

    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
        roleCheck();
    }

    private void roleCheck() {
        if (currentUserRole.equals("STAFF")) {
            btnModifyBooking.setVisible(true);
            btnModifyBooking.setManaged(true);
        } else if (currentUserRole.equals("ADMIN")) {
            btnModifyBooking.setVisible(true);
            btnModifyBooking.setManaged(true);
            overrideAvailabilityCheck.setVisible(true);
            overrideAvailabilityCheck.setManaged(true);
        }
    }

    @FXML
    private void handleConfirmBooking() {
        // TODO : Validate resource ID exist
        String resourceId = resourceIdField.getText();
        LocalDate date = bookingDatePicker.getValue();
        String startTimeStr = startTimeBox.getValue();
        String durationStr = durationBox.getValue();

        if (resourceId.isEmpty() || date == null || startTimeStr == null || durationStr == null) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Please fill in all fields");
            return;
        }

        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = start.plusHours(Long.parseLong(durationStr));

        try {
            bookingManager.createBooking(currentUserID, resourceId, start, end, currentUserName);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Booking confirmed");
            refreshBookingTable();
        } catch (ResourceUnavailableException | InvalidBookingDurationException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancelBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Select a booking to cancel first");
            return;
        }

        User requestingUser = new User(currentUserID, Role.valueOf(currentUserRole));

        try {
            bookingManager.cancelBooking(selected.getBookingID(), requestingUser);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Booking cancelled");
            refreshBookingTable();
        } catch (UnauthorizedAccessException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleModifyBooking() {
        // placeholder: staff/admin only, opens edit flow for selected booking
    }

    @FXML
    private void handleBack() {
        // placeholder: return to ResourceListingForm
    }

    private void refreshBookingTable() {
        ObservableList<Booking> userBookings = FXCollections.observableArrayList(
                bookingManager.getBookingsForUser(currentUserID));
        bookingTable.setItems(userBookings);
    }
}