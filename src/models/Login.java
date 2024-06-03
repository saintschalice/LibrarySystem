package models;

public class Login {
    private final String username;
    private final String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean authenticate() {
        // Add your authentication logic here
        // Return true if authentication is successful, false otherwise
        return false; // Replace with your authentication logic
    }

    // Add getters for username and password

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
