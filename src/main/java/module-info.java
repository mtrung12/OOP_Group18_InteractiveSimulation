module view.interactivesimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    opens view.interactivesimulation to javafx.fxml;
    opens controller to javafx.fxml;
    exports view.interactivesimulation to javafx.graphics;
    exports controller;
}