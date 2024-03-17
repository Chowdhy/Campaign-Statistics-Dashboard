package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.ui.MainWindow;

public class AddUserScene extends BaseScene{

    public AddUserScene(MainWindow window){
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build(){
        root = new StackPane();
        root.setFocusTraversable(false);
        var loginFields = new VBox();
        var loginLabel = new Label("Add User");
        var usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        var passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        var buttonsHBox = new HBox();
        var loginButton = new Button("Add");
        var backButton = new Button("Back");
        buttonsHBox.getChildren().addAll(backButton,loginButton);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER);
        root.getChildren().add(loginFields);
        loginFields.getChildren().addAll(loginLabel,usernameInput,passwordInput,buttonsHBox);
        root.setPadding(new Insets(200,200,200,200));
        loginFields.setSpacing(10);
        loginFields.setAlignment(Pos.CENTER);
        VBox.setMargin(loginLabel, new Insets(0, 0, 20, 0));
        usernameInput.setFocusTraversable(false);
        passwordInput.setFocusTraversable(false);
        loginButton.setFocusTraversable(false);
        backButton.setFocusTraversable(false);


        loginButton.setOnAction(e ->{
            window.loadDashboard();
        });

        backButton.setOnAction( e -> {
            window.loadDashboard();
        });
    }

    @Override
    public void cleanup() {

    }
}
