package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Booking;
import model.BookingStatus;

import java.time.LocalDateTime;

public class BookingForm {

    @FXML private TextField resourceIdField;
    @FXML private DatePicker bookingDatePicker;
    @FXML private ComboBox<String> startTimeBox;
    @FXML private ComboBox<String> durationBox;
    @FXML private CheckBox overrideAvailabilityCheck;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> colBookingId;
    @FXML private TableColumn<Booking, String> colResourceId;
    @FXML private TableColumn<Booking, String> colDate;
    @FXML private TableColumn<Booking, String> colTime;
    @FXML private TableColumn<Booking, String> colStatus;

    @FXML private Button btnCancelBooking;
    @FXML private Button btnModifyBooking;

    // TODO: this should come from AuthManager/passed in via setter once login is real
    private String currentUserRole = "STUDENT";
    private String selectedResourceId; // set via setResourceId() when navigating from ResourceListingForm

    @FXML
    public void initialize() {
        startTimeBox.getItems().addAll("09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00");
        durationBox.getItems().addAll("1", "2", "3");

        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colResourceId.setCellValueFactory(new PropertyValueFactory<>("resourceId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadDummyBookings();
    }

    // Called from ResourceListingForm when navigating here with a resource already picked
    public void setResourceId(String resourceId) {
        this.selectedResourceId = resourceId;
        resourceIdField.setText(resourceId);
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
        // STUDENT: defaults stay as-is, only Confirm + Cancel visible
    }

    private void loadDummyBookings() {
        ObservableList<Booking> dummyBookings = FXCollections.observableArrayList(
                new Booking("BK001", "U001", "R001",
                        LocalDateTime.of(2026, 7, 25, 10, 0),
                        LocalDateTime.of(2026, 7, 25, 12, 0),
                        BookingStatus.APPROVED, "John Student"),
                new Booking("BK002", "U002", "R003",
                        LocalDateTime.of(2026, 7, 26, 14, 0),
                        LocalDateTime.of(2026, 7, 26, 15, 0),
                        BookingStatus.PENDING, "Jane Staff")
        );
        bookingTable.setItems(dummyBookings);
    }

    @FXML
    private void handleConfirmBooking() {
        // placeholder: BookingManager.createBooking(selectedResourceId, date, startTime, duration, currentUserId)
    }

    @FXML
    private void handleCancelBooking() {
        // placeholder: get selected row from bookingTable, BookingManager.cancelBooking(bookingId)
    }

    @FXML
    private void handleModifyBooking() {
        // placeholder: staff/admin only, opens edit flow for selected booking
    }

    @FXML
    private void handleBack() {
        // placeholder: return to ResourceListingForm
    }
}