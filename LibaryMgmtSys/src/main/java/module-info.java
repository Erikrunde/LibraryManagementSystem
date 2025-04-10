module org.example.libarymgmtsys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.libarymgmtsys to javafx.fxml;
    exports org.example.libarymgmtsys;
}