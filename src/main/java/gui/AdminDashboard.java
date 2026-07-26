package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import manager.UserManager;
import model.User;

import java.io.IOException;

public class AdminDashboard {

    @FXML
    private Label welcomeLabel;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getName());
    }


    @FXML
    private void handleSearchResources() {
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
    private void handleManageResources() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ManageResourcesForm.fxml")
            );

            Parent root = loader.load();

            ResourceManagement controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("Manage Resources");
            stage.setScene(new Scene(root, 800, 500));

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
            stage.setTitle("Booking Requests");
            stage.setScene(new Scene(root, 950, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageUsers() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManager.fxml")); // confirm exact filename
            Parent root = loader.load();

            UserManager controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("Manage Users");
            stage.setScene(new Scene(root, 950, 600));
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