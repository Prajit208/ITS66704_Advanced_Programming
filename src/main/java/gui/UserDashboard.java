package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import model.Role;
import model.User;

import java.io.IOException;

public class UserDashboard {

    @FXML private Label welcomeLabel;
    @FXML private Label statUpcoming;
    @FXML private Label statPending;
    @FXML private Label statTotal;
    @FXML private Button btnManageBookings;

    private User currentUser;

    public void setCurrentUser(User user) {
        System.out.println("UserDashboard.setCurrentUser called with: " + (user == null ? "NULL" : user.getUsername()));
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getName());
        roleCheck();
    }

    private void roleCheck() {
        if (currentUser.getRole() == Role.STAFF) {
            btnManageBookings.setVisible(true);
            btnManageBookings.setManaged(true);
        }
        // placeholder: load stats and recent bookings here
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
            stage.setScene(new Scene(root, 900, 600));
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
            stage.setScene(new Scene(root, 900, 600));
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
            stage.setScene(new Scene(root, 950, 600));
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
            stage.setScene(new Scene(root, 700, 620));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}