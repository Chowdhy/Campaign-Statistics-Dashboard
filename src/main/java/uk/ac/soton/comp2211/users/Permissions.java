package uk.ac.soton.comp2211.users;

public enum Permissions {
    ADMIN,
    EDITOR,
    VIEWER;

    public static Permissions find(String name) {
        for (Permissions permission : values()) {
            if (permission.name().equalsIgnoreCase(name)) {
                return permission;
            }
        }
        return null;
    }
}
