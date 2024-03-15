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
import uk.ac.soton.comp2211.ui.MainWindow;

public class LoginScene extends BaseScene {

    LoginController controller;

    public LoginScene(MainWindow window){
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build(){
        controller = new LoginController();

        root = new StackPane();
        root.setFocusTraversable(false);
        var loginFields = new VBox();
        var loginLabel = new Label("Ad Dashboard Login");
        var usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        var passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        var loginButton = new Button("Login");
        root.getChildren().add(loginFields);
        loginFields.getChildren().addAll(loginLabel,usernameInput,passwordInput,loginButton);
        root.setPadding(new Insets(200,200,200,200));
        loginFields.setSpacing(10);
        loginFields.setAlignment(Pos.CENTER);
        VBox.setMargin(loginLabel, new Insets(0, 0, 20, 0));
        usernameInput.setFocusTraversable(false);
        passwordInput.setFocusTraversable(false);
        loginButton.setFocusTraversable(false);

        usernameInput.textProperty().bindBidirectional(controller.usernameProperty());
        passwordInput.textProperty().bindBidirectional(controller.passwordProperty());




        loginButton.setOnAction(e ->{
            try {
                controller.requestLogin();
                window.loadDashboard();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
    }

    @Override
    public void cleanup(){
    }
}
