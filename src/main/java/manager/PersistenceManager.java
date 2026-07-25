package manager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import model.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceManager {
    private static final String USER_FILE = "data/users.csv";
    public void saveUser(User user) {

        File file = new File(USER_FILE);

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(file, true))) {


            writer.writeNext(new String[]{
                    user.getUserID().toString(),
                    user.getName(),
                    user.getUsername(),
                    user.getPasswordHash(),
                    user.getEmail(),
                    user.getRole().toString()
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // initialize the csv file with two users when the application is lauched
    public void initializeUsersFile() {

        File file = new File(USER_FILE);

        File parentDirectory = file.getParentFile();

        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }


        if (!file.exists() || file.length() == 0) {

            try (CSVWriter writer = new CSVWriter(
                    new FileWriter(file, true))) {


                writer.writeNext(new String[]{
                        "User ID",
                        "Full Name",
                        "Username",
                        "Password Hash",
                        "Email",
                        "Role"
                });


                writer.writeNext(new String[]{
                        UUID.randomUUID().toString(),
                        "System Admin",
                        "admin",
                        AuthManager.hash("admin123"),
                        "admin@gmail.com",
                        "Admin"
                });


                writer.writeNext(new String[]{
                        UUID.randomUUID().toString(),
                        "Main Admin",
                        "admin2",
                        AuthManager.hash("admin456"),
                        "admin2@gmail.com",
                        "Admin"
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Loading the users
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        File file = new File(USER_FILE);

        if (!file.exists()) {
            return users;
        }

        try (CSVReader reader = new CSVReader(new FileReader(file))) {

            String[] line;

            // Skip the header row
            reader.readNext();

            while ((line = reader.readNext()) != null) {

                User user = new User(
                        UUID.fromString(line[0]),     // User ID
                        line[1],                      // Full Name
                        line[2],                      // Username
                        line[3],                      // Password Hash
                        line[4],                      // Email
                        User.Role.valueOf(line[5])    // Role
                );

                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}
