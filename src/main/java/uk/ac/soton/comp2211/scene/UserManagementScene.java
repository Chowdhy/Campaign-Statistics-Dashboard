package uk.ac.soton.comp2211.scene;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.control.UserManagementController;
import uk.ac.soton.comp2211.ui.UserWindow;
import uk.ac.soton.comp2211.users.InvalidPasswordException;
import uk.ac.soton.comp2211.users.Permissions;

import java.util.Map;
import java.util.Optional;

public class UserManagementScene extends UserScene {
    UserManagementController controller;
    VBox userListBox;
    VBox userOptionsBox;
    ComboBox<String> permissionsBox;
    ToggleGroup userToggleGroup;
    ToggleButton addUserButton;
    Label selectedUserLabel;
    Button deleteUserButton;
    Label errorLabel;
    Label createErrorLabel;
    PasswordField newPassword;
    PasswordField confirmPassword;
    TextField username;
    PasswordField newUserPassword;
    PasswordField confirmNewUserPassword;
    ComboBox<String> choiceBox;

    public UserManagementScene(UserWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
        controller.updateUserList();
    }

    @Override
    public void build() {
        controller = window.getController();

        root = new BorderPane();
        root.getStyleClass().add("white");

        // UI left side

        VBox leftSide = new VBox();
        leftSide.setSpacing(10);
        leftSide.setAlignment(Pos.CENTER);

        ScrollPane scroller = new ScrollPane();
        scroller.getStyleClass().add("scroller");

        userListBox = new VBox();
        userListBox.setSpacing(5);
        scroller.setContent(userListBox);
        scroller.setFitToWidth(true);
        VBox.setVgrow(scroller, Priority.ALWAYS);

        addUserButton = new ToggleButton("Create new user");
        addUserButton.getStyleClass().add("fill-button");

        var exportButton = new ToggleButton("Export Logs");
        exportButton.getStyleClass().add("fill-button");

        leftSide.getChildren().addAll(scroller, addUserButton, exportButton);

        exportButton.setOnAction(e -> {
            window.loadExportLogsScene();
        });

        // UI right side

        userOptionsBox = new VBox();
        userOptionsBox.getStyleClass().add("white");
        userOptionsBox.setSpacing(10);
        userOptionsBox.setPrefWidth((double) window.getWidth() /2);
        userOptionsBox.setAlignment(Pos.CENTER);

        selectedUserLabel = new Label();
        selectedUserLabel.getStyleClass().add("selected-user");
        selectedUserLabel.textProperty().bind(Bindings.concat("Modify user: ", controller.selectedUserProperty()));

        permissionsBox = new ComboBox<>();

        permissionsBox.getItems().addAll("Viewer", "Editor", "Admin");
        permissionsBox.setPromptText("Select new permission level");

        Button updateUserButton = new Button("Update user");
        updateUserButton.getStyleClass().add("outline-button");
        deleteUserButton = new Button("Delete user");
        deleteUserButton.getStyleClass().add("outline-button");

        updateUserButton.setMaxWidth(125);
        deleteUserButton.setMaxWidth(125);

        newPassword = new PasswordField();
        newPassword.setPromptText("New password");

        confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");

        errorLabel = new Label();

        userOptionsBox.getChildren().addAll(selectedUserLabel, newPassword, confirmPassword, permissionsBox, errorLabel, updateUserButton, deleteUserButton);

        // --

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(10);
        detailsBox.getStyleClass().add("white");

        Label title = new Label("Create new user");
        title.getStyleClass().add("selected-user");

        username = new TextField();
        username.setPromptText("Username");

        newUserPassword = new PasswordField();
        newUserPassword.setPromptText("Password");

        confirmNewUserPassword = new PasswordField();
        confirmNewUserPassword.setPromptText("Confirm password");

        choiceBox = new ComboBox<>();
        choiceBox.getItems().addAll("Viewer", "Editor", "Admin");
        choiceBox.setPromptText("Select permission level");

        createErrorLabel = new Label();
        detailsBox.setAlignment(Pos.CENTER);

        Button createButton = new Button("Create");
        createButton.getStyleClass().add("outline-button");

        detailsBox.getChildren().addAll(title, username, newUserPassword, confirmNewUserPassword, choiceBox, createErrorLabel, createButton);

        detailsBox.setPrefWidth((double) window.getWidth()/2);

        createButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (newUserPassword.getText() == null || newUserPassword.getText().isEmpty()) {
                createErrorLabel.setStyle("-fx-text-fill: red");
                createErrorLabel.setText("Cannot have empty password");
                event.consume();
            } else if ((!newUserPassword.getText().equals(confirmNewUserPassword.getText()))) {
                createErrorLabel.setStyle("-fx-text-fill: red");
                createErrorLabel.setText("Passwords are not the same");
                event.consume();
            } else {
                try {
                    controller.isValidPassword(newUserPassword.getText());
                } catch (InvalidPasswordException e) {
                    createErrorLabel.setStyle("-fx-text-fill: red");
                    createErrorLabel.setText(e.getMessage());
                    event.consume();
                }
            }
            if (choiceBox.getValue() == null) {
                createErrorLabel.setStyle("-fx-text-fill: red");
                createErrorLabel.setText("Must select permission level");
                event.consume();
            }
            if (username.getText() == null || username.getText().isEmpty()) {
                createErrorLabel.setStyle("-fx-text-fill: red");
                createErrorLabel.setText("Cannot have empty username");
                event.consume();
            } else if (controller.isAlreadyUser(username.getText())) {
                createErrorLabel.setStyle("-fx-text-fill: red");
                createErrorLabel.setText("Username is already taken");
                event.consume();
            }
        });

        createButton.setOnAction(e -> controller.createUser(username.getText(), newUserPassword.getText(), Permissions.find(choiceBox.getValue())));

        // BorderPane setting

        ((BorderPane) root).setCenter(leftSide);
        ((BorderPane) root).setRight(userOptionsBox);

        // UI Control (touch if you dare)

        userToggleGroup = new ToggleGroup();

        addUserButton.setOnAction(e -> {
            createErrorLabel.setText(null);
            if (addUserButton.isSelected()) {
                controller.selectedUserProperty().set(null);
                userOptionsBox.setDisable(true);
                ((BorderPane) root).setRight(detailsBox);
            } else {
                ((BorderPane) root).setRight(userOptionsBox);
            }
        });
        userToggleGroup.getToggles().add(addUserButton);

        updateUserButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (permissionsBox.getValue() == null && (newPassword.getText() == null || newPassword.getText().isEmpty())) {
                errorLabel.setStyle("-fx-text-fill: red");
                errorLabel.setText("No attributes to update");
                event.consume();
            } else if (!(newPassword.getText() == null || newPassword.getText().isEmpty())) {
                if ((!newPassword.getText().equals(confirmPassword.getText()))) {
                    errorLabel.setStyle("-fx-text-fill: red");
                    errorLabel.setText("Passwords are not the same");
                    event.consume();
                } else {
                    try {
                        controller.isValidPassword(newPassword.getText());
                    } catch (InvalidPasswordException e) {
                        errorLabel.setStyle("-fx-text-fill: red");
                        errorLabel.setText(e.getMessage());
                        event.consume();
                    }
                }
            }
        });

        updateUserButton.setOnAction(e -> {
            errorLabel.setText(null);

            if (permissionsBox.getValue() != null) {
                controller.updateSelectedPermissions(Permissions.find(permissionsBox.getValue()));
            }
            if (!newPassword.getText().isEmpty()) {
                controller.updateSelectedPassword(newPassword.getText());
            }
        });

        deleteUserButton.setOnAction(e -> {
            errorLabel.setText(null);

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

    public void resetPermissionsBox(ComboBox<String> choiceBox) {
        choiceBox.getSelectionModel().clearSelection();
        choiceBox.setValue(null);
        choiceBox.setEditable(true);
        choiceBox.setEditable(false);
    }

    public void add (String username, Permissions permissions) {
        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER);
        userBox.setSpacing(20);
        ToggleButton button = new ToggleButton(username);
        button.getStyleClass().add("admin-button");
        Label permissionLabel = new Label(permissions.name());
        permissionLabel.getStyleClass().add("admin-title");

        userBox.getChildren().addAll(permissionLabel, button);
        button.setMaxWidth(100000);

        button.setOnAction(e -> {
            resetPermissionsBox(permissionsBox);

            errorLabel.setText(null);

            if (button.isSelected()) {
                ((BorderPane) root).setRight(userOptionsBox);

                Platform.runLater(() -> {
                    controller.selectedUserProperty().set(button.getText());
                    updateOptionsBox();
                });
            } else {
                userOptionsBox.setDisable(true);

                togglePermissions(true);

                Platform.runLater(() -> controller.selectedUserProperty().set(null));
            }
        });

        button.setPrefWidth(140);
        permissionLabel.setPrefWidth(50);
        userToggleGroup.getToggles().add(button);
        userListBox.getChildren().add(userBox);
    }

    public void populateUserList(Map<String, Permissions> users) {
        userOptionsBox.setDisable(true);
        userToggleGroup.getToggles().clear();
        userToggleGroup.getToggles().add(addUserButton);
        userListBox.getChildren().clear();

        for (Map.Entry<String, Permissions> entry : users.entrySet()) {
            var username = entry.getKey();
            var permissions = entry.getValue();

            if (permissions.equals(Permissions.ADMIN)) add(username, permissions);
        }

        for (Map.Entry<String, Permissions> entry : users.entrySet()) {
            var username = entry.getKey();
            var permissions = entry.getValue();

            if (permissions.equals(Permissions.EDITOR)) add(username, permissions);
        }

        for (Map.Entry<String, Permissions> entry : users.entrySet()) {
            var username = entry.getKey();
            var permissions = entry.getValue();

            if (permissions.equals(Permissions.VIEWER)) add(username, permissions);
        }

        if (controller.selectedUserProperty().get() != null) {
            updateOptionsBox();
        }
    }

    public void updateOptionsBox() {
        errorLabel.setText(null);
        newPassword.setText(null);
        confirmPassword.setText(null);

        if (controller.selectedUserProperty().get().equalsIgnoreCase(App.getUser().getUsername())) {
            togglePermissions(true);
            deleteUserButton.setDisable(true);
        } else {
            togglePermissions(false);
            deleteUserButton.setDisable(false);
        }
        userOptionsBox.setDisable(false);
    }

    public void togglePermissions(boolean enabled) {
        permissionsBox.setDisable(enabled);
    }

    public void updateSelectedUser(String message) {
        resetPermissionsBox(permissionsBox);
        updateOptionsBox();
        errorLabel.setStyle("-fx-text-fill: green");
        errorLabel.setText(message);
    }

    public void promptSuccessfulCreate(String message) {
        createErrorLabel.setStyle("-fx-text-fill: green");
        createErrorLabel.setText(message);
        resetPermissionsBox(choiceBox);
        username.setText(null);
        newUserPassword.setText(null);
        confirmNewUserPassword.setText(null);
    }
}
