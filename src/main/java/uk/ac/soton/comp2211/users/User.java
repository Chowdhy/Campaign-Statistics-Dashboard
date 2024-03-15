package uk.ac.soton.comp2211.users;

public class User {
    private final String username;
    private final Permissions permissions;

    public User(String username, Permissions permissions) {
        this.username = username;
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public Permissions getPermissions() {
        return permissions;
    }
}