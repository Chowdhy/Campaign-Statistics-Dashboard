package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.ui.MainWindow;

public class ModifyUserScene extends BaseScene{
    private CheckBox adminCheckBox;
    private CheckBox readWriteCheckBox;
    private CheckBox viewerCheckBox;

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
        var loginLabel = new Label("Modify User");
        var usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        var loginButton = new Button("Modify");
        root.getChildren().add(loginFields);

        root.setPadding(new Insets(200,200,200,200));
        loginFields.setSpacing(10);
        loginFields.setAlignment(Pos.CENTER);
        VBox.setMargin(loginLabel, new Insets(0, 0, 20, 0));
        usernameInput.setFocusTraversable(false);
        loginButton.setFocusTraversable(false);

        var adminHBox = new HBox();
        var adminLabel = new Label("Admin");
        var adminCheckBox = new CheckBox();
        adminHBox.getChildren().addAll(adminCheckBox,adminLabel);
        adminHBox.setSpacing(5);

        var readWriteHBox = new HBox();
        var readWriteLabel = new Label("Read & Write");
        var readWriteCheckBox = new CheckBox();
        readWriteHBox.getChildren().addAll(readWriteCheckBox,readWriteLabel);
        readWriteHBox.setSpacing(5);

        var viewerHBox = new HBox();
        var viewerLabel = new Label("Viewer");
        var viewerCheckBox = new CheckBox();
        viewerHBox.getChildren().addAll(viewerCheckBox,viewerLabel);
        viewerHBox.setSpacing(5);

        var buttonsHBox = new HBox();
        var backButton = new Button("Back");
        buttonsHBox.getChildren().addAll(backButton,loginButton);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        loginFields.getChildren().addAll(loginLabel,adminHBox,readWriteHBox,viewerHBox,usernameInput,buttonsHBox);


        adminCheckBox.setOnAction(e -> {
            if (adminCheckBox.isSelected()) {
                readWriteCheckBox.setSelected(true);
                viewerCheckBox.setSelected(true);
            }
        });


        readWriteCheckBox.setOnAction(e -> {
            if (readWriteCheckBox.isSelected()) {
                viewerCheckBox.setSelected(true);
            }
            else{
                adminCheckBox.setSelected(false);
            }
        });


        viewerCheckBox.setOnAction(e -> {
            if (!viewerCheckBox.isSelected()) {
                readWriteCheckBox.setSelected(false);
                adminCheckBox.setSelected(false);
            }
        });

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
