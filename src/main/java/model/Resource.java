package model;

import java.util.List;

public abstract class Resource {
    private String resourceID;
    private String type;
    private String location;
    private int capacity;
    private boolean availabilityStatus;
    private List<String> bookingRules;

    public Resource(String resourceID, String type, String location,
                    int capacity, boolean availabilityStatus, List<String> bookingRules) {
        this.resourceID = resourceID;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
        this.bookingRules = bookingRules;
    }

    public String getResourceID() { return resourceID; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public boolean isAvailabilityStatus() { return availabilityStatus; }
    public List<String> getBookingRules() { return bookingRules; }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    // Each subtype must implement this itself — no shared implementation here
    public abstract String getResourceDetails();
}