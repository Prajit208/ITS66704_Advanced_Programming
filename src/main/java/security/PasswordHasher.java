package security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    private static final Argon2 argon2 =
            Argon2Factory.create();
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
