module com.spameggsfoobar.pongfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.spameggsfoobar.pongfx to javafx.fxml;
    exports com.spameggsfoobar.pongfx;
}