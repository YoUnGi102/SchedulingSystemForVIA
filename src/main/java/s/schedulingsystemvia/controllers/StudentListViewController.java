package s.schedulingsystemvia.controllers;

import javafx.event.ActionEvent;
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
import s.schedulingsystemvia.generator.Student;
import s.schedulingsystemvia.models.LessonViewModel;
import s.schedulingsystemvia.models.StudentViewModel;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentListViewController implements Initializable {

  @FXML
  private Menu timeTableItem, studentListItem, lessonListItem;

  @FXML
  private TableView<StudentViewModel> studentListTable;

  @FXML
  private TableColumn<StudentViewModel, String> nameColumn;

  @FXML
  private TableColumn<StudentViewModel, String> VIANumberColumn;

  @FXML
  private TableColumn<StudentViewModel, LocalDate> birthdayColumn;

  @FXML
  private TableColumn<StudentViewModel, String> programmeColumn;

  @FXML
  private TableColumn<StudentViewModel, Integer> semesterColumn;

  @FXML
  private TableColumn<StudentViewModel, String> classColumn;

  @FXML
  private ChoiceBox<String> programmeSearch;

  @FXML
  private ChoiceBox<Integer> semesterSearch;

  @FXML
  private ChoiceBox<Class> classSearch;

  @FXML
  private TextField nameSearch, numberSearch;

  @FXML
  private Label errorLabel;

  private Database database;

  private ViewHandler handler;

  public StudentListViewController() {}

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

    nameColumn.setCellValueFactory((cellData) -> cellData.getValue().nameProperty());
    VIANumberColumn.setCellValueFactory((cellData) -> cellData.getValue().VIANumberProperty());
    birthdayColumn.setCellValueFactory((cellData) -> cellData.getValue().birthdayProperty());
    programmeColumn.setCellValueFactory((cellData) -> cellData.getValue().programmeProperty());
    semesterColumn.setCellValueFactory((cellData) -> cellData.getValue().semesterProperty().asObject());
    classColumn.setCellValueFactory((cellData) -> cellData.getValue().studentClassProperty());

    reset();
  }

  public void setViewHandler(ViewHandler handler){
    this.handler = handler;
  }

  public void setTimeTableView() {
    handler.openView(ViewHandler.TIME_TABLE_VIEW_PATH);
  }

  public void setLessonListView() {
    handler.openView(ViewHandler.LESSON_LIST_VIEW_PATH);
  }

  public void reset() {
    errorLabel.setText("");
    studentListTable.setItems(database.getStudentViewModelList());
  }

  @FXML
  private void addStudent() {

    Parent root = handler.loadSimpleGUIView(ViewHandler.STUDENT_DIALOG_VIEW_PATH, null);

    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 720, 480));

    stage.showAndWait();
    studentListTable.setItems(database.getStudentViewModelList());
    studentListTable.refresh();

  }
  public void removeStudent() {
    errorLabel.setText("");

    boolean remove = confirmation();
    if(remove) {

      StudentViewModel model = studentListTable.getSelectionModel().getSelectedItem();
      studentListTable.getItems().remove(model);
      database.removeStudent(model.getStudent());
      studentListTable.setItems(database.getStudentViewModelList());

    }
  }
  private boolean confirmation() {
    int index = studentListTable.getSelectionModel().getSelectedIndex();
    StudentViewModel selectedItem = studentListTable.getItems().get(index);
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(
        "Removing student");
    Optional<ButtonType> result = alert.showAndWait();
    return ((result.isPresent()) && (result.get() == ButtonType.OK));
  }
  public void editStudent(ActionEvent actionEvent) {

    StudentViewModel student = studentListTable.getSelectionModel().getSelectedItem();
    if(student == null)
      return;

    Parent root = handler.loadSimpleGUIView(ViewHandler.STUDENT_DIALOG_VIEW_PATH, student.getStudent());

    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setScene(new Scene(root, 720, 480));

    stage.showAndWait();

    student.reset();
    studentListTable.refresh();

  }

  @FXML
  private void search(){

    studentListTable.setItems(database.getSearchedStudents(
            nameSearch.getText(),
            numberSearch.getText(),
            programmeSearch.getValue(),
            semesterSearch.getValue(),
            classSearch.getValue()
    ));
    studentListTable.refresh();

  }


}