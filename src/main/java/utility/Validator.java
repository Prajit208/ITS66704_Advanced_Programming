package utility;

import model.User;

import java.util.List;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,20}$");


    public static String validateRegistration(
            String fullName,
            String username,
            String email,
            String password,
            String confirmPassword,
            List<User> users
            ) {

        if (isEmpty(fullName)) {
            return "Name is required";
        }

        if (!isValidUsername(username)) {
            return "Username must be 3-20 characters";
        }

        if (usernameExists(username, users)) {
            return "Username already exists";
        }

        if (emailExists(email, users)) {
            return "Email already exists";
        }

        if (!isValidEmail(email)) {
            return "Invalid email format";
        }

        if (!isValidPassword(password)) {
            return "Password must be at least 8 characters";
        }

        if (!passwordsMatch(password, confirmPassword)) {
            return "Passwords do not match";
        }

        return null; // no errors
    }


    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }


    public static boolean isValidEmail(String email) {
        return email != null &&
                EMAIL_PATTERN.matcher(email).matches();
    }


    public static boolean isValidUsername(String username) {
        return username != null &&
                USERNAME_PATTERN.matcher(username).matches();
    }


    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }


    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null &&
                password.equals(confirmPassword);
    }
// Checks whether the username is already taken
    public static boolean usernameExists(String username, List<User> users) {
        if (users == null) {
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                return true;
            }
        }
        return false;
    }
// Checks if the same email exists
    public static boolean emailExists(String email, List<User> users) {
        if (users == null) {
            return false;
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }
}