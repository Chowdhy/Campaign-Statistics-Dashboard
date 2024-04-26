package uk.ac.soton.comp2211.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import uk.ac.soton.comp2211.scene.UserManagementScene;
import uk.ac.soton.comp2211.ui.Dialogs;
import uk.ac.soton.comp2211.users.*;

import java.util.HashMap;
import java.util.Map;

public class UserManagementController {
    private final Credentials credentials;
    private final StringProperty selectedUser = new SimpleStringProperty();
    private final UserManagementScene scene;
    private Map<String, Permissions> userList;

    public UserManagementController(UserManagementScene scene) {
        credentials = new Credentials();
        this.scene = scene;
    }

    public boolean isAlreadyUser(String username) {
        return credentials.isAlreadyUser(username);
    }

    public void createUser(String username, String password, Permissions permissions) {
        try {
            var success = credentials.addUser(username, password, permissions);

            updateUserList();

            scene.promptSuccessfulCreate("Successfully created user");
        } catch (UserAlreadyExistsException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }
    }

    public void deleteSelectedUser() {
        try {
            var success = credentials.deleteUser(selectedUser.get());

            selectedUser.set(null);

            updateUserList();
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }
    }

    public void updateSelectedPassword(String password) {
        try {
            credentials.resetPassword(selectedUser.get(), password);

            updateSelectedUser();
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }
    }

    public void updateSelectedPermissions(Permissions permissions) {
        try {
            credentials.updatePermissions(selectedUser.get(), permissions);

            updateUserList();

            updateSelectedUser();
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }
    }

    public void updateUserList() {
        userList = credentials.getUserList();

        scene.populateUserList(userList);
    }

    public boolean containsSpecial(String password) {
        return credentials.containsSpecial(password);
    }

    public boolean containsCapitals(String password) {
        return credentials.containsCapitals(password);
    }

    public boolean containsLowers(String password) {
        return credentials.containsLowers(password);
    }

    public boolean containsNumbers(String password) {
        return credentials.containsNumbers(password);
    }

    public boolean correctLength(String password) {
        return credentials.correctLength(password);
    }

    public void updateSelectedUser() {
        scene.updateSelectedUser("Succesfully updated user");
    }

    public StringProperty selectedUserProperty() {
        return selectedUser;
    }
}
