package model;

import java.util.List;

public class EventSpace extends Resource {

    public EventSpace(String resourceID, String location, int capacity,
                      boolean availabilityStatus, List<String> bookingRules) {
        super(resourceID, "Event Space", location, capacity, availabilityStatus, bookingRules);
    }

    @Override
    public String getResourceDetails() {
        return "Event Space (" + getResourceID() + ") — " + getLocation() +
                ", capacity " + getCapacity();
    }
}