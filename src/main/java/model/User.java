package model;
import java.io.Serializable;
import java.util.UUID;
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userID;
    private String fullName;
    private String username;
    private String passwordHash;
    private String email;
    private Role role;


    // Constructor for creating a NEW user
    public User(String fullName, String username,
                String passwordHash, String email, Role role) {
        this.userID = UUID.randomUUID().toString(); // Automatically generate ID
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    // Constructor for LOADING a user from the CSV
    public User(String userID, String fullName,
                String username, String passwordHash,
                String email, Role role) {
        this.userID = userID; // Use the existing ID from the CSV
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }
    // Minimal constructor for cases needing just identity + role
    // (e.g. building a lightweight "requesting user" for permission checks)
    public User(String userID, Role role) {
        this.userID = userID;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }



    // Getter and Setter for fullName
    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for passwordHash
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for role
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}