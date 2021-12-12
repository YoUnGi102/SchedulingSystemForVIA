package s.schedulingsystemvia.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import s.schedulingsystemvia.ViewHandler;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.models.LessonViewModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LessonListViewController implements Initializable {

  @FXML private TableView<LessonViewModel> lessonListTable;

  @FXML private TableColumn<LessonViewModel, String> classroomColumn;
  @FXML private TableColumn<LessonViewModel, String> startTimeColumn;
  @FXML private TableColumn<LessonViewModel, String> endTimeColumn;
  @FXML private TableColumn<LessonViewModel, String> courseColumn;
  @FXML private TableColumn<LessonViewModel, String> teachersColumn;

  @FXML private Label errorLabel;
  @FXML private TextField search;

  private Database database;

  public LessonListViewController(){}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    database = Database.getInstance();

    classroomColumn.setCellValueFactory((cellData) -> cellData.getValue().classroomProperty().asString());
    startTimeColumn.setCellValueFactory((cellData) -> cellData.getValue().startTimeProperty().asString());
    endTimeColumn.setCellValueFactory((cellData) -> cellData.getValue().endTimeProperty().asString());
    courseColumn.setCellValueFactory((cellData) -> cellData.getValue().courseProperty().asString());

    reset();

  }

  public void reset(){
    lessonListTable.setItems(database.getLessonViewModelList());
    errorLabel.setText("");
    //viewModel.update();
  }

  @FXML private void searchButton(){
    /*viewModel.searchCourses(Integer.valueOf(search.getText()));
    courseListTable.setItems(viewModel.getSearchBar());
    if(courseListTable.getItems().size() < 0){
      reset();
      errorLabel.setText("No course with thid ID");
    }*/
  }

  @FXML
  private void resetButton(){
    reset();
  }

  @FXML
  private void addCourseButton(){
    Parent root = null;
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(ViewHandler.class.getResource(ViewHandler.LESSON_DIALOG_VIEW_PATH));
      root = loader.load();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if(root != null){

      Stage stage = new Stage();
      stage.setResizable(false);
      stage.setScene(new Scene(root, 720, 480));

      stage.showAndWait();
    }
  }

  @FXML
  private void removeCourseButton(){
    errorLabel.setText("");

    boolean remove = confirmation();
    if(remove)
    {
      for (LessonViewModel model : lessonListTable.getSelectionModel().getSelectedItems()) {
        database.removeLesson(model.getLesson());
        lessonListTable.getItems().remove(model);
      }
    }

  }

  private boolean confirmation() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("DELETE COURSE: Confirmation");
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }

  public void onLesson(ActionEvent actionEvent) {
  }

  public void onCustomizeCourse(ActionEvent actionEvent) {
  }

  /*

  @FXML private void onCustomizeCourse(){
    int index = courseListTable.getSelectionModel().getSelectedIndex();
    if(index > -1 && index < courseListTable.getItems().size())
    {
      state.setSelectCourse(index);
      viewHandler.openView("CustomizeCourse");
      state.setSelectCourse(courseListTable.getSelectionModel().getSelectedIndex());
    }
    else errorLabel.setText("No course selected.");
  }

  @FXML private void onLesson(){
    int index = courseListTable.getSelectionModel().getSelectedIndex();
    if(index > -1 && index < courseListTable.getItems().size())
    {
      state.setSelectCourse(index);
      viewHandler.openView("Lesson");
    }
    else errorLabel.setText("We couldn't find the course.");
  }


   */
}
