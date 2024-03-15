package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.ui.MainWindow;

public class ModifyUserScene extends BaseScene{

    public ModifyUserScene(MainWindow window){
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
        var loginLabel = new Label("Ad Dashboard Login");
        var usernameInput = new TextField();
        usernameInput.setPromptText("Username");;
        var loginButton = new Button("Login");
        root.getChildren().add(loginFields);
        loginFields.getChildren().addAll(loginLabel,usernameInput,loginButton);
        root.setPadding(new Insets(200,200,200,200));
        loginFields.setSpacing(10);
        loginFields.setAlignment(Pos.CENTER);
        VBox.setMargin(loginLabel, new Insets(0, 0, 20, 0));
        usernameInput.setFocusTraversable(false);
        loginButton.setFocusTraversable(false);


        loginButton.setOnAction(e ->{
            window.loadDashboard();
            System.out.println("Done");
        });
    }

    @Override
    public void cleanup() {

    }
}
