package s.schedulingsystemvia.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import s.schedulingsystemvia.application.ViewHandler;
import s.schedulingsystemvia.generator.Lesson;
import s.schedulingsystemvia.generator.TimeTable;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.generator.Class;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ResourceBundle;

import static s.schedulingsystemvia.generator.TimeTable.ZONE_ID;

public class TimeTableViewController implements Initializable {

    // TODO TEACHERS CANNOT HAVE MULTIPLE LESSONS AT THE SAME TIME

    // TODO GENERATION FOR MULTIPLE CLASSES
    // TODO EDITING LESSONS
    // TODO ADDING LESSONS
    // TODO LEFT AND RIGHT ARROW ICONS

    @FXML
    private GridPane gridPane;

    @FXML
    private ChoiceBox<String> programme;

    @FXML
    private ChoiceBox<Integer> semester;

    @FXML
    private ChoiceBox<Class> aClass;

    @FXML
    private Label weekLabel;

    private Class currentClass;
    private int currentWeek;
    private int currentYear;

    private TimeTable timeTable;

    ViewHandler handler;

    public TimeTableViewController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentWeek = ZonedDateTime.of(LocalDateTime.now(), ZONE_ID).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        currentYear = LocalDate.now().getYear();

        Database database = Database.getInstance();

        //TimeTable table = timeTableMap.get(database.getClass("Software Technology Engineering", 1, "Z"));
        //generator.spreadTimeTable(table);

        programme.setItems(database.getProgrammesList());
        programme.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(semester.getValue() != null){
                aClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
            }
        });

        semester.setItems(Database.SEMESTER_LIST);
        semester.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(programme.getValue() != null){
                aClass.setItems(database.getClassesList(programme.getValue(), semester.getValue()));
            }
        });

        aClass.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            timeTable = database.getTimeTable(newVal);
            timeTable.resetGridPane(gridPane, currentWeek, currentYear);
        });

    }

    public void setWeek(int week){
        weekLabel.setText("Week " + week);
        timeTable.resetGridPane(gridPane, currentWeek, currentYear);
    }

    public static Lesson getNewNode(){
        /*LocalDateTime start = LocalDateTime.of(2021, 12, 1, 8, 20);
        LocalDateTime end = LocalDateTime.of(2021, 12, 1, 11, 45);
        Teacher teacher = new Teacher("Stephen Andersen", "932322");
        Lesson node = new Lesson(new Classroom("C05.16", 0), start, end, new Course("Responsive Web Design", "RWD", 5, new Teacher[]{new Teacher("Line", "2355")}));
        node.setColor(Color.YELLOW);
        return node;*/
        return null;
    }

    @FXML
    private void leftArrow(MouseEvent mouseEvent){
        if(currentWeek > 1){
            currentWeek--;
        }else {
            currentWeek = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(currentYear-1, 12, 31), LocalTime.of(0,0)), ZONE_ID).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            currentYear--;
        }
        setWeek(currentWeek);

    }

    public void rightArrow(MouseEvent mouseEvent) {
        int weekMax = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(currentYear-1, 12, 31), LocalTime.of(0,0)), ZONE_ID).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        if(currentWeek < weekMax){
            currentWeek++;
        }else {
            currentWeek = 1;
            currentYear++;
        }
        setWeek(currentWeek);
    }

    public void setViewHandler(ViewHandler handler){
        this.handler = handler;
    }

    public void setLessonListView(){
        handler.openView(ViewHandler.LESSON_LIST_VIEW_PATH);
    }

    public void setStudentListView(){
        handler.openView(ViewHandler.STUDENT_LIST_VIEW_PATH);
    }
}