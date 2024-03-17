package uk.ac.soton.comp2211.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.ArrayList;

public final class Dialogs {
    private static ArrayList<Dialog> dialogs = new ArrayList<>();
    private static Alert previousError = null;
    private Dialogs() {;
    }

    public static void error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
        alert.show();
        dialogs.add(alert);
    }
}
