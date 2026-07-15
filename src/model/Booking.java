package model;

import java.time.LocalDateTime;

public class Booking{
    private String bookingID;
    private String userID;
    private String resourceID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus bookingStatus;
    private String creatorName;
    // initialize with constructor
    public Booking(String bookingID,String userID, String resourceID,
                   LocalDateTime startTime, LocalDateTime endTime,
                   BookingStatus bookingStatus,String creatorName){
        this.bookingID = bookingID;
        this.userID = userID;
        this.resourceID = resourceID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingStatus = bookingStatus;
        this.creatorName = creatorName;
    }

    // define getter method so that other classes can read without modifying the existing data

    public String getBookingID() {
        return bookingID;
    }

    public String getUserID() {
        return userID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public String getCreatorName() {
        return creatorName;
    }

    // Make a setter method for booking status as its the only one that changes over time
    public void setBookingStatus(BookingStatus bookingStatus){
        this.bookingStatus=bookingStatus;
    }
}