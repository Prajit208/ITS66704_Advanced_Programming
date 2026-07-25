package service;

import com.opencsv.CSVWriter;

import model.User;
import security.PasswordHasher;
import utility.Validator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserService {
    private static final String USER_FILE = "data/users.csv";
    public User registerUser(
            String fullName,
            String username,
            String email,
            String password,
            User.Role role
    ) {
        try{

        String hashedPassword = PasswordHasher.hash(password);

        User user = new User(
                fullName,
                username,
                hashedPassword,
                email,
                role
        );

        saveUserToCSV(user);
        return user;
        } catch (RuntimeException e) {
            return null;
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
                        "Full Name",
                        "Username",
                        "Password Hash",
                        "Email",
                        "Role"
                });


                writer.writeNext(new String[]{
                        "System Admin",
                        "admin",
                        PasswordHasher.hash("admin123"),
                        "admin@gmail.com",
                        "Admin"
                });


                writer.writeNext(new String[]{
                        "Main Admin",
                        "admin2",
                        PasswordHasher.hash("admin456"),
                        "admin2@gmail.com",
                        "Admin"
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveUserToCSV(User user) {

        File file = new File(USER_FILE);

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(file, true))) {


            writer.writeNext(new String[]{
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
}