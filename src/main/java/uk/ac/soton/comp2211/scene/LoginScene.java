package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.control.LoginController;
import uk.ac.soton.comp2211.ui.Dialogs;
import uk.ac.soton.comp2211.ui.MainWindow;

public class LoginScene extends MainScene {

    public LoginScene(MainWindow window){
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build(){
        LoginController controller = new LoginController();

        root = new StackPane();
        root.setFocusTraversable(false);
        var loginFields = new VBox();
        loginFields.getStyleClass().add("form-container");
        var loginLabel = new Label("Ad Dashboard Login");
        loginLabel.getStyleClass().add("form-title");
        var usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.textProperty().bindBidirectional(controller.usernameProperty());
        var passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.textProperty().bindBidirectional(controller.passwordProperty());
        var loginButton = new Button("Log in");
        loginButton.getStyleClass().add("fill-button");
        root.getChildren().add(loginFields);
        var incorrectPrompt = new Label();
        incorrectPrompt.setStyle("-fx-text-fill: red");
        loginFields.getChildren().addAll(loginLabel,usernameInput,passwordInput,loginButton, incorrectPrompt);
        root.setPadding(new Insets(225,500,225,500));
        loginFields.setSpacing(10);
        loginFields.setAlignment(Pos.CENTER);
        VBox.setMargin(loginLabel, new Insets(0, 0, 10, 0));
        VBox.setMargin(loginButton, new Insets(10, 0, 0, 0));

        loginButton.setOnAction(e -> {
            try {
                if (controller.requestLogin() != null) {
                    System.out.println("Successfully logged in!");
                    window.loadDashboard();
                    System.out.println("Done");
                } else {
                    incorrectPrompt.setText("Incorrect username or password");
                    System.out.println("Unsuccessful login attempt.");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void cleanup(){
    }
}
