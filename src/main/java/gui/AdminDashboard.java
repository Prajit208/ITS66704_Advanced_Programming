package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboard {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void handleSearchResources() {
        //load ResourceListingForm.fxml here
    }

    @FXML
    private void handleManageResources() {
        // popen resource add/edit/remove screen
        // calls ResourceManager.addResource(), removeResource(), etc.
    }

    @FXML
    private void handleViewBookings() {
        // show list of all bookings
        // calls BookingManager.getAllBookings()
    }

    @FXML
    private void handleManageUsers() {
        // open user account management screen
        // calls UserManager.createAccount(), editAccount(), etc.
    }

    @FXML
    private void handleViewLogs() {
        //show system log contents
        // calls SystemLogger.getLogs()
    }

    @FXML
    private void handleLogout() {
        // return to LoginForm.fxml
    }
}