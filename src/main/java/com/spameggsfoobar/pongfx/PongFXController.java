package com.spameggsfoobar.pongfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PongFXController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}