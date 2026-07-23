package model;

import java.util.List;

public class Lab extends Resource {
    private List<String> equipmentIncluded;

    public Lab(String resourceID, String location, int capacity,
               boolean availabilityStatus, List<String> bookingRules,
               List<String> equipmentIncluded) {
        super(resourceID, "Lab", location, capacity, availabilityStatus, bookingRules);
        this.equipmentIncluded = equipmentIncluded;
    }

    public List<String> getEquipmentIncluded() { return equipmentIncluded; }

    @Override
    public String getResourceDetails() {
        return "Lab (" + getResourceID() + ") — " + getLocation() +
                ", equipment: " + equipmentIncluded;
    }
}