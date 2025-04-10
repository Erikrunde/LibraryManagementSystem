package org.example.libarymgmtsys;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
/**
 * I didn't use this at all
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}