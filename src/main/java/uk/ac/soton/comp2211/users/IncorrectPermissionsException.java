package uk.ac.soton.comp2211.users;

public class IncorrectPermissionsException extends Exception {
    public IncorrectPermissionsException(String errorMessage) {
        super(errorMessage);
    }
}
