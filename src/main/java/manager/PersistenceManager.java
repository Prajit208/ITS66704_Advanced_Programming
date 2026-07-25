package manager;

import model.Booking;
import model.Resource;
import model.Role;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {

    private static final String USERS_FILE = "data/users.dat";
    private static final String RESOURCES_FILE = "data/resources.dat";
    private static final String BOOKINGS_FILE = "data/bookings.dat";

    // ---------- USERS ----------

    public void saveUser(User user) {
        List<User> users = loadUsers();
        users.add(user);
        saveUsers(users);
    }
    public void saveUsers(List<User> users) {
        saveToFile(USERS_FILE, users);
    }
    @SuppressWarnings("unchecked")
    public List<User> loadUsers() {
        return (List<User>) loadFromFile(USERS_FILE);
    }

    // ---------- RESOURCES ----------

    public void saveResources(List<Resource> resources) {
        saveToFile(RESOURCES_FILE, resources);
    }

    @SuppressWarnings("unchecked")
    public List<Resource> loadResources() {
        return (List<Resource>) loadFromFile(RESOURCES_FILE);
    }

    // ---------- BOOKINGS ----------

    public void saveBookings(List<Booking> bookings) {
        saveToFile(BOOKINGS_FILE, bookings);
    }

    @SuppressWarnings("unchecked")
    public List<Booking> loadBookings() {
        return (List<Booking>) loadFromFile(BOOKINGS_FILE);
    }

    // ---------- SHARED HELPERS ----------
    // Both save methods above just call this one generic method underneath,
    // avoids repeating the same file-writing code three separate times.

    private void saveToFile(String filePath, List<?> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeUsersFile() {
        File file = new File(USERS_FILE);
        file.getParentFile().mkdirs(); // ensure data/ folder exists

        if (!file.exists()) {
            List<User> users = new ArrayList<>();

            users.add(new User(
                    "System Administrator",
                    "admin1",
                    AuthManager.hash("admin1234"),      // Ideally store a hashed password
                    "admin1@system.com",
                    Role.ADMIN
            ));

            users.add(new User(
                    "Backup Administrator",
                    "admin2",
                    AuthManager.hash("admin456"),      // Ideally store a hashed password
                    "admin2@system.com",
                    Role.ADMIN
            ));

            saveUsers(users);// creates users.dat with two users
        }
    }
    private List<?> loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // first run, nothing saved yet
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<?>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}