package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboard {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void handleSearchResources() {
        //load ResourceListingForm.fxml here
    }

    @FXML
    private void handleManageResources() {

    }

    @FXML
    private void handleViewBookings() {
        // show list of all bookings
        // calls BookingManager.getAllBookings()
    }

    @FXML
    private void handleManageUsers() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManager.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("Manage Users");
            stage.setScene(new Scene(root, 800, 500));
        }
        catch (IOException e){
            e.printStackTrace();
        }
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