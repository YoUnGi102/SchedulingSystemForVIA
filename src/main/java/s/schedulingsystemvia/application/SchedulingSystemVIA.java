package s.schedulingsystemvia.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class SchedulingSystemVIA extends Application {

    private ViewHandler handler;

    @Override
    public void start(Stage stage) throws Exception {
        handler = new ViewHandler();
        handler.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
