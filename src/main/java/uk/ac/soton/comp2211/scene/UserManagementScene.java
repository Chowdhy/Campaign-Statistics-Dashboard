package uk.ac.soton.comp2211.scene;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import uk.ac.soton.comp2211.ui.MainWindow;

import java.util.ArrayList;
import java.util.Optional;

public class UserManagementScene extends BaseScene {
    VBox userListBox;
    VBox userOptionsBox;
    ToggleGroup userToggleGroup;
    Label selectedUserLabel;

    public UserManagementScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        root = new BorderPane();

        VBox leftSide = new VBox();

        ScrollPane scroller = new ScrollPane();
        leftSide.getChildren().add(scroller);

        userListBox = new VBox();
        scroller.setContent(userListBox);
        VBox.setVgrow(scroller, Priority.ALWAYS);

        Button addUserButton = new Button("Create new user");
        leftSide.getChildren().add(addUserButton);

        ((BorderPane) root).setLeft(leftSide);
        userToggleGroup = new ToggleGroup();

        userOptionsBox = new VBox();

        selectedUserLabel = new Label();

        ToggleGroup permissionsGroup = new ToggleGroup();

        VBox permissionsBox = new VBox();

        RadioButton viewerButton = new RadioButton("Viewer");
        RadioButton editorButton = new RadioButton("Editor");
        RadioButton adminButton = new RadioButton("Admin");

        permissionsGroup.getToggles().addAll(viewerButton, editorButton, adminButton);
        permissionsBox.getChildren().addAll(viewerButton, editorButton, adminButton);

        Button changePasswordButton = new Button("Change password");
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

            if (password.isPresent()) {

            }
        });

        Button deleteUserButton = new Button("Delete user");
        deleteUserButton.setOnAction(e -> {
            Dialog<Boolean> dialog = new Dialog<>();

            VBox dialogContent = new VBox();

            Label prompt1 = new Label("Are you sure you want to delete user " + "?");
            Label prompt2 = new Label("This action cannot be undone.");

            dialogContent.getChildren().addAll(prompt1, prompt2);

            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.YES);

            Optional<Boolean> choice = dialog.showAndWait();

            if (choice.isPresent() && choice.get().equals(true)) {

            }
        });

        userOptionsBox.getChildren().addAll(selectedUserLabel, permissionsBox, changePasswordButton, deleteUserButton);

        ((BorderPane) root).setRight(userOptionsBox);
    }

    @Override
    public void cleanup() {

    }

    public void populateUserList(ArrayList<String> users) {
        userOptionsBox.setDisable(true);
        userToggleGroup.getToggles().clear();
        userListBox.getChildren().clear();

        for (String username : users) {
            ToggleButton button = new ToggleButton(username);

            button.setOnAction(e -> {
                if (button.isSelected()) {
                    userOptionsBox.setDisable(false);
                    selectedUserLabel.setText("Currently selected: " + button.getText());
                } else {
                    selectedUserLabel.setText("No user selected");
                    userOptionsBox.setDisable(true);
                }
            });

            userToggleGroup.getToggles().add(button);
            userListBox.getChildren().add(button);
        }
    }
}
