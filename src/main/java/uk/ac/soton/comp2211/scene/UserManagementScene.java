package uk.ac.soton.comp2211.scene;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.control.UserManagementController;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.users.Permissions;

import java.util.Optional;
import java.util.Set;

public class UserManagementScene extends BaseScene {
    UserManagementController controller;
    VBox userListBox;
    VBox userOptionsBox;
    VBox permissionsBox;
    ToggleGroup userToggleGroup;
    Label selectedUserLabel;
    RadioButton viewerButton;
    RadioButton editorButton;
    RadioButton adminButton;
    Button deleteUserButton;

    public UserManagementScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
        controller.updateUserList();
    }

    @Override
    public void build() {
        controller = new UserManagementController(this);

        root = new BorderPane();

        // UI left side

        VBox leftSide = new VBox();

        ScrollPane scroller = new ScrollPane();

        userListBox = new VBox();
        scroller.setContent(userListBox);
        VBox.setVgrow(scroller, Priority.ALWAYS);
        scroller.setFitToWidth(true);

        Button addUserButton = new Button("Create new user");

        leftSide.getChildren().addAll(scroller, addUserButton);

        // UI right side

        userOptionsBox = new VBox();
        userOptionsBox.setPrefWidth((double) window.getWidth() /2);

        selectedUserLabel = new Label();
        selectedUserLabel.textProperty().bind(Bindings.concat("Currently selected: ", controller.selectedUserProperty()));

        permissionsBox = new VBox();

        viewerButton = new RadioButton("Viewer");
        editorButton = new RadioButton("Editor");
        adminButton = new RadioButton("Admin");

        permissionsBox.getChildren().addAll(viewerButton, editorButton, adminButton);

        Button changePasswordButton = new Button("Change password");
        deleteUserButton = new Button("Delete user");

        userOptionsBox.getChildren().addAll(selectedUserLabel, permissionsBox, changePasswordButton, deleteUserButton);

        // BorderPane setting

        ((BorderPane) root).setCenter(leftSide);
        ((BorderPane) root).setRight(userOptionsBox);

        // UI Control (touch if you dare)

        userToggleGroup = new ToggleGroup();

        addUserButton.setOnAction(e -> {
            Dialog<Triple<String, String, Permissions>> dialog = new Dialog<>();

            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            VBox detailsBox = new VBox();

            TextField username = new TextField();
            username.setPromptText("Username");

            PasswordField newPassword = new PasswordField();
            newPassword.setPromptText("Password");

            PasswordField confirmPassword = new PasswordField();
            confirmPassword.setPromptText("Confirm password");

            VBox permissions = new VBox();

            ToggleGroup selectedPermissionGroup = new ToggleGroup();
            RadioButton adminPermission = new RadioButton("Admin");
            RadioButton editorPermission = new RadioButton("Editor");
            RadioButton viewerPermission = new RadioButton("Viewer");

            permissions.getChildren().addAll(adminPermission, editorPermission, viewerPermission);
            selectedPermissionGroup.getToggles().addAll(adminPermission, editorPermission, viewerPermission);

            Label errorLabel = new Label();

            detailsBox.getChildren().addAll(username, newPassword, confirmPassword, permissions, errorLabel);

            dialog.getDialogPane().setContent(detailsBox);

            Button changeButton = (Button) dialog.getDialogPane().lookupButton(createButtonType);
            changeButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (newPassword.getText().isEmpty()) {
                    errorLabel.setText("Cannot have empty password");
                    event.consume();
                }
                if ((!newPassword.getText().equals(confirmPassword.getText()))) {
                    errorLabel.setText("Passwords are not the same");
                    event.consume();
                }
                if (selectedPermissionGroup.getSelectedToggle() == null) {
                    errorLabel.setText("Must select permission level");
                    event.consume();
                }
                if (username.getText().isEmpty()) {
                    errorLabel.setText("Cannot have empty username");
                    event.consume();
                }
                if (controller.isAlreadyUser(username.getText())) {
                    errorLabel.setText("Username is already taken");
                    event.consume();
                }
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    return new ImmutableTriple<>(username.getText(), newPassword.getText(), Permissions.find(((RadioButton) selectedPermissionGroup.getSelectedToggle()).getText()));
                }
                return null;
            });

            Optional<Triple<String, String, Permissions>> newUserDetails = dialog.showAndWait();

            if (newUserDetails.isPresent()) {
                var userDetailsTriple = newUserDetails.get();

                Platform.runLater(() -> controller.createUser(userDetailsTriple.getLeft(), userDetailsTriple.getMiddle(), userDetailsTriple.getRight()));
            }
        });

        viewerButton.setOnAction(e -> {
            togglePermissions(true);
            Platform.runLater(() -> controller.updateSelectedPermissions(Permissions.VIEWER));
        });

        editorButton.setOnAction(e -> {
            togglePermissions(true);
            Platform.runLater(() -> controller.updateSelectedPermissions(Permissions.EDITOR));
        });

        adminButton.setOnAction(e -> {
            togglePermissions(true);
            Platform.runLater(() -> controller.updateSelectedPermissions(Permissions.ADMIN));
        });

        ToggleGroup permissionsGroup = new ToggleGroup();
        permissionsGroup.getToggles().addAll(viewerButton, editorButton, adminButton);

        changePasswordButton.setOnAction(e -> {
            Dialog<String> dialog = new Dialog<>();

            ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

            VBox passwords = new VBox();

            PasswordField newPassword = new PasswordField();
            newPassword.setPromptText("New password");

            PasswordField confirmPassword = new PasswordField();
            confirmPassword.setPromptText("Confirm new password");

            Label errorLabel = new Label();

            passwords.getChildren().addAll(newPassword, confirmPassword, errorLabel);

            dialog.getDialogPane().setContent(passwords);

            Button changeButton = (Button) dialog.getDialogPane().lookupButton(changeButtonType);
            changeButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (newPassword.getText().isEmpty()) {
                    errorLabel.setText("Cannot have empty password");
                    event.consume();
                }
                if ((!newPassword.getText().equals(confirmPassword.getText()))) {
                    errorLabel.setText("Passwords are not the same");
                    event.consume();
                }
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == changeButtonType) {
                    return newPassword.getText();
                }
                return null;
            });

            Optional<String> password = dialog.showAndWait();

            password.ifPresent(s -> Platform.runLater(() -> controller.updateSelectedPassword(s)));
        });

        deleteUserButton.setOnAction(e -> {
            Dialog<Boolean> dialog = new Dialog<>();

            VBox dialogContent = new VBox();

            Label prompt1 = new Label("Are you sure you want to delete user '" + controller.selectedUserProperty().get() + "'?");
            Label prompt2 = new Label("This action cannot be undone.");

            dialogContent.getChildren().addAll(prompt1, prompt2);

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.YES);

            Optional<Boolean> choice = dialog.showAndWait();

            if (choice.isPresent() && choice.get().equals(true)) {
                Platform.runLater(() -> controller.deleteSelectedUser());
            }
        });
    }

    @Override
    public void cleanup() {

    }

    public void populateUserList(Set<String> users) {
        userOptionsBox.setDisable(true);
        userToggleGroup.getToggles().clear();
        userListBox.getChildren().clear();

        for (String username : users) {
            ToggleButton button = new ToggleButton(username);
            button.setMaxWidth(100000);

            button.setOnAction(e -> {
                if (button.isSelected()) {
                    userOptionsBox.setDisable(false);

                    Platform.runLater(() -> {
                        controller.selectedUserProperty().set(button.getText());
                        controller.updateSelectedUser();
                    });
                } else {
                    userOptionsBox.setDisable(true);

                    adminButton.setSelected(false);
                    editorButton.setSelected(false);
                    viewerButton.setSelected(false);

                    Platform.runLater(() -> controller.selectedUserProperty().set(null));
                }
            });

            userToggleGroup.getToggles().add(button);
            userListBox.getChildren().add(button);
        }
    }

    public void togglePermissions(boolean enabled) {
        viewerButton.setDisable(enabled);
        editorButton.setDisable(enabled);
        adminButton.setDisable(enabled);
    }

    public void updateSelectedUser(Permissions permissions) {
        if (controller.selectedUserProperty().get().equalsIgnoreCase(App.getUser().getUsername())) {
            togglePermissions(true);
            deleteUserButton.setDisable(true);
        } else {
            togglePermissions(false);
            deleteUserButton.setDisable(false);
        }

        if (permissions.equals(Permissions.ADMIN)) {
            adminButton.setSelected(true);
        } else if (permissions.equals(Permissions.EDITOR)) {
            editorButton.setSelected(true);
        } else if (permissions.equals(Permissions.VIEWER)) {
            viewerButton.setSelected(true);
        }
        permissionsBox.setDisable(false);
    }
}
