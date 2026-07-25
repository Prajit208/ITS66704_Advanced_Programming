package manager;

import model.Resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager<T extends Resource> {

    private List<T> resources;

    public ResourceManager() {
        this.resources = new ArrayList<>();
    }

    // Optionally load an existing list at startup (e.g. from PersistenceManager)
    public ResourceManager(List<T> resources) {
        this.resources = resources;
    }

    public void addResource(T resource) {
        resources.add(resource);
    }

    public void removeResource(String resourceID) {
        resources.removeIf(r -> r.getResourceID().equals(resourceID));
    }

    public T getResourceById(String resourceID) {
        for (T resource : resources) {
            if (resource.getResourceID().equals(resourceID)) {
                return resource;
            }
        }
        return null; // consider throwing a custom exception here instead, if you want stricter error handling
    }

    public List<T> searchAvailable(String type, String location, boolean availableOnly, LocalDateTime timeSlot) {
        List<T> results = new ArrayList<>();

        for (T resource : resources) {
            boolean matches = true;

            if (type != null && !type.isEmpty() && !resource.getType().equalsIgnoreCase(type)) {
                matches = false;
            }

            if (location != null && !location.isEmpty() && !resource.getLocation().equalsIgnoreCase(location)) {
                matches = false;
            }

            if (availableOnly && !resource.isAvailabilityStatus()) {
                matches = false;
            }

            // timeSlot isn't checked against anything yet since Resource itself
            // doesn't track bookings — that check actually belongs to BookingManager's
            // hasConflict(), not here. This parameter exists per spec but resource-level
            // availability is really just the boolean flag until a booking is made.

            if (matches) {
                results.add(resource);
            }
        }

        return results;
    }

    public void updateAvailability(String resourceID, boolean status) {
        T resource = getResourceById(resourceID);
        if (resource != null) {
            resource.setAvailabilityStatus(status);
        }
    }
}