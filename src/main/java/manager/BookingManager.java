package manager;

import model.Booking;
import model.BookingStatus;
import model.User;
import model.Role;
import exception.ResourceUnavailableException;
import exception.InvalidBookingDurationException;
import exception.UnauthorizedAccessException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.SystemLogs;

public class BookingManager {

    // Define a custom list of Booking . Keeps every booking in memory while the app is running.
    private List<Booking> bookings;
    private  List<SystemLogs> logs;
    private PersistenceManager persistenceManager;

    // Constructor: runs once when a BookingManager object is created.
    public BookingManager() {
        this.persistenceManager = new PersistenceManager();
        // load existing booking
        this.bookings = persistenceManager.loadBookings();
    }

    // Creates a new booking after validating it.
    public Booking createBooking(String userID, String resourceID, LocalDateTime start, LocalDateTime end,String creatorName)
            throws ResourceUnavailableException, InvalidBookingDurationException {

        // Check 1: end time must always be after start time
        // isAfter() compares two LocalDateTime values.
        if (!end.isAfter(start)) { //if end time is not after start time, this returns true and throws error
            throw new InvalidBookingDurationException("End time must be after start time");
        }

        // Check 2: enforce a max booking length check, is subject to change , for now it should not be > 4
        // Duration.between() calculates the time gap between two LocalDateTime values.
        Duration duration = Duration.between(start, end);
        if (duration.toHours() > 4) {// .toHours() convert duration to hours
            throw new InvalidBookingDurationException("Booking cannot exceed 4 hours");
        }

        // Check 3: Booking time overlap check.

        if (hasConflict(resourceID, start, end)) {
            throw new ResourceUnavailableException("This resource is already booked during the requested time");
        }

        // All checks passed, build the actual Booking object.
        String bookingID = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingID, userID, resourceID, start, end, BookingStatus.PENDING, creatorName);

        bookings.add(booking);
        persistenceManager.saveBookings(bookings);

        logs.add(new SystemLogs(userID, "Request Booking"));
        persistenceManager.saveSystemLogs(logs);
        return booking;
    }

    // Cancels a booking, but only if the requester owns it, or is Staff/Admin.
    public void cancelBooking(String bookingID, User requestingUser) throws UnauthorizedAccessException {
        Booking booking = findBookingById(bookingID);

        // Check if this user owns the booking they're trying to cancel.
        boolean isOwner = booking.getUserID().equals(requestingUser.getUserID());

        // Staff and Admin are allowed to cancel bookings that aren't their own too.
        boolean isStaffOrAdmin = requestingUser.getRole() == Role.STAFF || requestingUser.getRole() == Role.ADMIN;

        // If neither condition is true, they're not allowed, throw the custom exception.
        if (!isOwner && !isStaffOrAdmin) {
            throw new UnauthorizedAccessException("You do not have permission to cancel this booking");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        persistenceManager.saveBookings(bookings);

        logs.add(new SystemLogs(requestingUser.getUserID(), "Booking Cancellation"));
        persistenceManager.saveSystemLogs(logs);
    }

    // Only Staff or Admin can approve a booking (per your team's latest decision).
    public void approveBooking(String bookingID, User staffOrAdminUser) throws UnauthorizedAccessException {
        if (staffOrAdminUser.getRole() != Role.STAFF && staffOrAdminUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only Staff or Admin can approve bookings");
        }

        Booking booking = findBookingById(bookingID);
        booking.setBookingStatus(BookingStatus.APPROVED);
        persistenceManager.saveBookings(bookings);

        logs.add(new SystemLogs(staffOrAdminUser.getUserID(), "Approve Booking"));
        persistenceManager.saveSystemLogs(logs);
    }

    // Same pattern as approveBooking, just sets the opposite status.
    public void rejectBooking(String bookingID, User staffOrAdminUser) throws UnauthorizedAccessException {
        if (staffOrAdminUser.getRole() != Role.STAFF && staffOrAdminUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only Staff or Admin can reject bookings");
        }

        Booking booking = findBookingById(bookingID);
        booking.setBookingStatus(BookingStatus.REJECTED);
        persistenceManager.saveBookings(bookings);

        logs.add(new SystemLogs(staffOrAdminUser.getUserID(), "Reject Booking"));
        persistenceManager.saveSystemLogs(logs);

    }

    // Returns every booking that belongs to a specific user.
    // Used for the "View My Bookings" screen.
    public List<Booking> getBookingsForUser(String userID) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUserID().equals(userID)) {
                result.add(booking);
            }
        }
        return result;
    }

    // Returns every booking in the system, used by Admin/Staff to see everything.
    public List<Booking> getAllBookings() {
        return bookings;
    }
    // hasConflict is a custom boolean method
    // This check for resourceID and time conflict in 2 bookings
    // method parameters are resourceId and start and end time
    private boolean hasConflict(String resourceID, LocalDateTime start, LocalDateTime end) {
        for (Booking existing : bookings) { // loops through each bookings

            // Skip bookings for a with different resourceID
            if (!existing.getResourceID().equals(resourceID)) { // if two id do not match , continue and skips to next iteration of for loop
                continue;
            }

            // Skip bookings that are cancelled or rejected, they no longer hold the time slot.
            if (existing.getBookingStatus() == BookingStatus.CANCELLED
                    || existing.getBookingStatus() == BookingStatus.REJECTED) {
                continue;
            }

            // Time overlap check
            // in simple term, if start time of new booking is before end time of existing booking and existing booking's start time is before new booking end time, its overlap
            // If either condition is false, there's no overlap.
            boolean overlaps = start.isBefore(existing.getEndTime()) && existing.getStartTime().isBefore(end);
            if (overlaps) {
                return true;
            }
        }
        return false; // checked everything, nothing overlapped
    }

    // finds a booking by its ID, or fails  if it doesn't exist.
    // Used by cancelBooking, approveBooking, and rejectBooking

    private Booking findBookingById(String bookingID) {
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }
        throw new IllegalArgumentException("No booking found with ID: " + bookingID);
    }
}