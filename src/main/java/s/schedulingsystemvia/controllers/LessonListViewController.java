package s.schedulingsystemvia.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import s.schedulingsystemvia.application.ViewHandler;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.generator.Class;
import s.schedulingsystemvia.generator.Classroom;
import s.schedulingsystemvia.generator.Course;
import s.schedulingsystemvia.generator.Teacher;
import s.schedulingsystemvia.models.LessonViewModel;
import s.schedulingsystemvia.models.StudentViewModel;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class LessonListViewController implements Initializable {

  // TODO FIX LOCALDATETIME DISPLAYING FORMAT
  // TODO FIX TEACHER/S DISPLAYING FORMAT
  // TODO SAVING EDITED LESSON
  // TODO ADDING A NEW LESSON

  @FXML private TableView<LessonViewModel> lessonListTable;

  @FXML private TableColumn<LessonViewModel, String> programmeColumn;
  @FXML private TableColumn<LessonViewModel, Integer> semesterColumn;
  @FXML private TableColumn<LessonViewModel, Class> classColumn;
  @FXML private TableColumn<LessonViewModel, Course> courseColumn;
  @FXML private TableColumn<LessonViewModel, Classroom> classroomColumn;
  @FXML private TableColumn<LessonViewModel, LocalDateTime> startTimeColumn;
  @FXML private TableColumn<LessonViewModel, LocalDateTime> endTimeColumn;
  @FXML private TableColumn<LessonViewModel, Teacher[]> teacherColumn;

  @FXML
  private ChoiceBox<String> programmeSearch;
  @FXML
  private ChoiceBox<Integer> semesterSearch;

  @FXML
  private ChoiceBox<Class> classSearch;

  @FXML
  private TextField courseSearch;

  @FXML
  private DatePicker startDateSearch, endDateSearch;

  @FXML
  private Button search;

  @FXML private Label errorLabel;

  private Database database;

  public LessonListViewController(){}

  ViewHandler handler;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    database = Database.getInstance();

    programmeSearch.setItems(database.getProgrammesList());
    programmeSearch.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
      if(semesterSearch.getValue() != null){
        classSearch.setItems(database.getClassesList(programmeSearch.getValue(), semesterSearch.getValue()));
      }
    });

    semesterSearch.setItems(Database.SEMESTER_LIST);
    semesterSearch.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
      if(programmeSearch.getValue() != null){
        classSearch.setItems(database.getClassesList(programmeSearch.getValue(), semesterSearch.getValue()));
      }
    });

    programmeColumn.setCellValueFactory((cellData) -> cellData.getValue().programmeProperty());
    semesterColumn.setCellValueFactory((cellData) -> cellData.getValue().semesterProperty().asObject());
    classColumn.setCellValueFactory((cellData) -> cellData.getValue().studentClassProperty());
    courseColumn.setCellValueFactory((cellData) -> cellData.getValue().courseProperty());
    classroomColumn.setCellValueFactory((cellData) -> cellData.getValue().classroomProperty());
    startTimeColumn.setCellValueFactory((cellData) -> cellData.getValue().startTimeProperty());
    endTimeColumn.setCellValueFactory((cellData) -> cellData.getValue().endTimeProperty());
    teacherColumn.setCellValueFactory((cellData) -> cellData.getValue().teachersProperty());

    reset();

  }

  public void reset(){
    lessonListTable.setItems(database.getLessonViewModelList());
    errorLabel.setText("");
  }

  @FXML
  private void search(){

    lessonListTable.setItems(
        database.getSearchedLessons(
            programmeSearch.getValue(),
            (semesterSearch.getValue() == null)? 0 : semesterSearch.getValue(),
            classSearch.getValue(),
            courseSearch.getText(),
            (startDateSearch.getValue() != null) ? LocalDateTime.of(startDateSearch.getValue(), LocalTime.of(0,0)) : null,
            (endDateSearch.getValue() != null) ? LocalDateTime.of(endDateSearch.getValue(), LocalTime.of(23,59)) : null
    ));

  }

  public void setTimeTableView(){
    handler.openView(ViewHandler.TIME_TABLE_VIEW_PATH);
  }
  public void setStudentListView(){
    handler.openView(ViewHandler.STUDENT_LIST_VIEW_PATH);
  }

  @FXML

  private void addLesson(){
    Parent root = handler.loadSimpleGUIView(ViewHandler.LESSON_DIALOG_VIEW_PATH, null);

    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 720, 480));

    stage.showAndWait();
    stage.setOnCloseRequest(e -> {
      lessonListTable.refresh();
    });

  }
  @FXML
  private void removeLesson(){
    errorLabel.setText("");

    boolean remove = confirmation();
    if(remove)
    {
      for (LessonViewModel model : lessonListTable.getSelectionModel().getSelectedItems()) {
        database.removeLesson(model.getLesson());
        lessonListTable.getItems().remove(model);
        lessonListTable.refresh();
      }
    }

  }
  @FXML
  private void editLesson(){

    LessonViewModel lesson = lessonListTable.getSelectionModel().getSelectedItem();
    if(lesson.getLesson() == null)
      return;

    Parent root = handler.loadSimpleGUIView(ViewHandler.LESSON_DIALOG_VIEW_PATH, lesson.getLesson());

    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 720, 480));

    stage.showAndWait();

    System.out.println("HEY UP");
    lesson.reset();
    lessonListTable.refresh();

  }

  private boolean confirmation() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("DELETE COURSE: Confirmation");
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }

  public void setViewHandler(ViewHandler handler){
    this.handler = handler;
  }

}
