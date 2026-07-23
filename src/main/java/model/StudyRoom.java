package model;

import java.util.List;

public class StudyRoom extends Resource {

    public StudyRoom(String resourceID, String location, int capacity,
                     boolean availabilityStatus, List<String> bookingRules) {
        super(resourceID, "Study Room", location, capacity, availabilityStatus, bookingRules);
    }

    @Override
    public String getResourceDetails() {
        return "Study Room (" + getResourceID() + ") — " + getLocation() +
                ", capacity " + getCapacity();
    }
}