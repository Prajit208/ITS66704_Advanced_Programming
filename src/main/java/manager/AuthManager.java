package manager;


import exception.UserNotFoundException;
import model.Role;
import model.User;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;


public class AuthManager {
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
            save.saveUser(user);
            return user;
        } catch (RuntimeException e) {
            return null;
        }
    }

// handles the login
    public User login(String username, String password) {
        PersistenceManager persistence = new PersistenceManager();

        for (User user : persistence.loadUsers()) {

            if (user.getUsername().equalsIgnoreCase(username)) {
                if(verify(password, user.getPasswordHash())){
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


    public static boolean verify(
            String password,
            String hash) {

        return argon2.verify(
                hash,
                password.toCharArray()
        );
    }

}