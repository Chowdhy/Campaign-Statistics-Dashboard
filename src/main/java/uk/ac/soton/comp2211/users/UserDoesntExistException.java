package uk.ac.soton.comp2211.users;

public class UserDoesntExistException extends Exception {
    public UserDoesntExistException(String errorMessage) {
        super(errorMessage);
    }
}
