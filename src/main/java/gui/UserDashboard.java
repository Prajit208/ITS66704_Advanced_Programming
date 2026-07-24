package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserDashboard {

    @FXML private Label welcomeLabel;
    @FXML private Button btnManageBookings;
    @FXML private Label statUpcoming;
    @FXML private Label statPending;
    @FXML private Label statTotal;

    // TODO: this should come from AuthManager once login is real
    private String currentUserRole = "STUDENT";

    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
        roleCheck();
    }

    private void roleCheck() {
        welcomeLabel.setText("Welcome, " + currentUserRole.charAt(0)
                + currentUserRole.substring(1).toLowerCase());

        if (currentUserRole.equals("STAFF")) {
            btnManageBookings.setVisible(true);
            btnManageBookings.setManaged(true);
        }

        // placeholder: BookingManager.getBookingsForUser(currentUserId) once real,
        // then set statUpcoming/statPending/statTotal from actual counts
    }

    @FXML
    private void handleBrowseResources() {
        // placeholder: navigate to ResourceListingForm
    }

    @FXML
    private void handleBookNow() {
        // placeholder: navigate to ResourceListingForm (or BookingForm directly)
    }

    @FXML
    private void handleViewBookings() {
        // placeholder: navigate to BookingForm
    }

    @FXML
    private void handleCancelRequest() {
        // placeholder: navigate to BookingForm
    }

    @FXML
    private void handleManageBookings() {
        // placeholder: navigate to BookingForm (staff view)
    }

    @FXML
    private void handleLogout() {
        // placeholder: navigate to LoginForm
    }
}