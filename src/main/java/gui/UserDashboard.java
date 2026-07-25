package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserDashboard {

    @FXML private Label welcomeLabel;
    @FXML private Label statUpcoming;
    @FXML private Label statPending;
    @FXML private Label statTotal;
    @FXML private Button btnManageBookings;

    // TODO: replace with real role from AuthManager once login is wired up
    private String currentUserRole = "STAFF";

    @FXML
    public void initialize() {
        if (currentUserRole.equals("STAFF")) {
            btnManageBookings.setVisible(true);
            btnManageBookings.setManaged(true);
        }
        // placeholder: load stats and recent bookings here
    }

    @FXML
    private void handleBrowseResources() {
        // placeholder: navigate to ResourceListingForm
    }

    @FXML
    private void handleBookNow() {
        // placeholder: navigate to BookingForm
    }

    @FXML
    private void handleViewBookings() {
        // placeholder: BookingManager.getBookingsForUser(currentUserId)
    }

    @FXML
    private void handleCancelRequest() {
        // placeholder: BookingManager.cancelBooking(bookingId, currentUser)
    }

    @FXML
    private void handleManageBookings() {
        // placeholder: navigate to a screen listing all Student bookings
        // pending approval, with Approve/Reject buttons per row
        // calls BookingManager.approveBooking(bookingId, currentUser)
        // or BookingManager.rejectBooking(bookingId, currentUser)
    }

    @FXML
    private void handleLogout() {
        // placeholder: navigate back to LoginForm
    }
}