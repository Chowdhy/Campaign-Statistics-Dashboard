package uk.ac.soton.comp2211.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.users.Credentials;
import uk.ac.soton.comp2211.users.Permissions;
import uk.ac.soton.comp2211.users.User;

public class LoginController {
    private final Credentials credentials;
    private final StringProperty username;
    private final StringProperty password;

    public LoginController() {
        credentials = new Credentials();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
    }

    public User requestLogin() throws Exception {
        String username = this.username.get();
        String password = this.password.get();
        if (credentials.validateCredentials(username, password)) {
            Permissions permissions = credentials.getAuthentication(username);
            User user = new User(username, permissions);
            App.setUser(user);
            return user;
        } else {
            throw new RuntimeException();
        }
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }
}