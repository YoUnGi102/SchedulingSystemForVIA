module s.schedulingsystemvia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports s.schedulingsystemvia.generator;
    opens s.schedulingsystemvia.generator to javafx.fxml;
    exports s.schedulingsystemvia.controllers;
    opens s.schedulingsystemvia.controllers to javafx.fxml;
    exports s.schedulingsystemvia.application;
    opens s.schedulingsystemvia.application to javafx.fxml;
    exports s.schedulingsystemvia.database;
    opens s.schedulingsystemvia.database to javafx.fxml;
}