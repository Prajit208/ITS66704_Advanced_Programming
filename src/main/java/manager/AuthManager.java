package manager;


import exception.UserNotFoundException;
import model.Role;
import model.SystemLogs;
import model.User;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;


public class AuthManager {
    private  final PersistenceManager persistance = new PersistenceManager();
    private static final Argon2 argon2 =
            Argon2Factory.create();
    public User registerUser(
            String fullName,
            String username,
            String email,
            String password,
            Role role
    ) {
        try{

            String hashedPassword = hash(password);
            User user = new User(
                    fullName,
                    username,
                    hashedPassword,
                    email,
                    role
            );
            PersistenceManager save = new PersistenceManager();
            // saving the user info
            save.saveUser(user);
            // saving the registration logs
            SystemLogs log =  new SystemLogs(user.getUserID(),"User Registration");
            save.saveSystemLog(log);
            return user;
        } catch (RuntimeException e) {
            return null;
        }
    }

// handles the login
    public User login(String username, String password) {
        PersistenceManager persistence = new PersistenceManager();
        // loops through all the loaded User List and authenticates the user
        for (User user : persistence.loadUsers()) {

            if (user.getUsername().equalsIgnoreCase(username)) {
                if(verify(password, user.getPasswordHash())){
                    // save the logged in log
                    persistence.saveSystemLog(new SystemLogs(user.getUserID(),"Logged In"));
                    return user;
                }
                throw new UserNotFoundException("Invalid Password");

            }
        }

        throw new UserNotFoundException("User not Found");
    }

    // returns a hash password
    public static String hash(String password) {

        return argon2.hash(
                3,       // iterations
                65536,   // memory in KB
                1,       // parallelism
                password.toCharArray()
        );
    }

    // verifies the password and returns a boolean value
    public static boolean verify(
            String password,
            String hash) {

        return argon2.verify(
                hash,
                password.toCharArray()
        );
    }

}