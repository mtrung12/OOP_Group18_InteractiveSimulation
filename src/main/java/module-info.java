module view.interactivesimulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens view.interactivesimulation to javafx.fxml;
    exports view.interactivesimulation;
}