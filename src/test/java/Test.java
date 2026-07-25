import manager.BookingManager;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        BookingManager bookingManager = new BookingManager();

        try {// change the hour to try multiple times
            bookingManager.createBooking("user1", "resource1",
                    LocalDateTime.of(2026, 8, 1, 14, 0),
                    LocalDateTime.of(2026, 8, 1, 16, 0),
                    "Test User");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Bookings right now: " + bookingManager.getAllBookings().size());
    }
}