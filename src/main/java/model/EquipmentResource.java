package model;

import java.util.List;

public class EquipmentResource extends Resource {

    public EquipmentResource(String resourceID, String location, int capacity,
                             boolean availabilityStatus, List<String> bookingRules) {
        super(resourceID, "Equipment", location, capacity, availabilityStatus, bookingRules);
    }

    @Override
    public String getResourceDetails() {
        return "Equipment (" + getResourceID() + ") — " + getLocation();
    }
}