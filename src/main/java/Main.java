

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.BookingManager;
import java.time.LocalDateTime;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ResourceListingForm.fxml"));
        primaryStage.setTitle("Campus Booking System");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


public static void main(String[] args){
    launch(args);
//    BookingManager bookingManager = new BookingManager();
//
//try {// change
//        bookingManager.createBooking("user1", "resource1",
//                LocalDateTime.of(2026, 8, 1, 13, 0),
//                LocalDateTime.of(2026, 8, 1, 14, 0),
//                "Test User");
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//
//System.out.println("Bookings right now: " + bookingManager.getAllBookings().size());
//
}
}