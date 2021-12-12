package s.schedulingsystemvia;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import s.schedulingsystemvia.database.Database;

public class ViewHandler {

    // TODO - FINISH VIEW HANDLER AND CONNECT IT TO CONTROLLERS
    // TODO - SAVING TIMETABLES AND STUDENTS INTO XML DATABASE
    // TODO - CONNECTING LESSON DIALOG WINDOW TO TIME TABLE
    //

    public static final String STUDENT_DIALOG_VIEW_PATH = "student_dialog.fxml";
    public static final String LESSON_DIALOG_VIEW_PATH = "lesson_dialog.fxml";

    public static final String TIME_TABLE_VIEW_PATH = "time_table_view.fxml";
    public static final String STUDENT_LIST_VIEW_PATH = "student_list_view.fxml";
    public static final String LESSON_LIST_VIEW_PATH = "lesson_list_view.fxml";

    private Scene currentScene;
    private Stage primaryStage;

    private Database database;

    public ViewHandler() {
        currentScene = new Scene(new Region());
        database = Database.getInstance();
        database.saveAll();
    }

    public void openView(String path) {
        Region root = loadSimpleGUIView(path);
        currentScene.setRoot(root);

        //TODO root.setUserData();

        primaryStage.setTitle((root.getUserData() != null) ? "" : (String) root.getUserData());
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void start(Stage stage) {
        Scene scene = new Scene(loadSimpleGUIView(STUDENT_LIST_VIEW_PATH), 1080, 720);
        stage.setResizable(false);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    public void close(){
        primaryStage.close();
    }

    public Region loadSimpleGUIView(String fxmlFile) {
        Region root = null;
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            root = loader.load();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return root;
    }

}