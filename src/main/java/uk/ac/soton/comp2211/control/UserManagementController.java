package uk.ac.soton.comp2211.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import uk.ac.soton.comp2211.scene.UserManagementScene;
import uk.ac.soton.comp2211.ui.Dialogs;
import uk.ac.soton.comp2211.users.*;

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
        } catch (UserAlreadyExistsException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }

        updateUserList();
    }

    public void deleteSelectedUser() {
        try {
            var success = credentials.deleteUser(selectedUser.get());
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }

        updateUserList();
    }

    public void updateSelectedPassword(String password) {
        try {
            credentials.resetPassword(selectedUser.get(), password);
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }
    }

    public void updateSelectedPermissions(Permissions permissions) {
        try {
            credentials.updatePermissions(selectedUser.get(), permissions);
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            Dialogs.error(e);
        }

        userList = credentials.getUserList();

        scene.updateSelectedUser(credentials.getPermissions(selectedUser.get()));
    }

    public void updateUserList() {
        userList = credentials.getUserList();
        scene.populateUserList(userList.keySet());
    }

    public void updateSelectedUser() {
        scene.updateSelectedUser(userList.get(selectedUser.get()));
    }

    public StringProperty selectedUserProperty() {
        return selectedUser;
    }
}
