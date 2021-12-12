package s.schedulingsystemvia.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import s.schedulingsystemvia.ViewHandler;
import s.schedulingsystemvia.database.Database;
import s.schedulingsystemvia.models.StudentViewModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentListViewController implements Initializable {

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
  private Label errorLabel;

  private Database database;

  public StudentListViewController()
  {
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    database = Database.getInstance();

    nameColumn.setCellValueFactory((cellData) -> cellData.getValue().nameProperty());
    VIANumberColumn.setCellValueFactory((cellData) -> cellData.getValue().VIANumberProperty());
    birthdayColumn.setCellValueFactory((cellData) -> cellData.getValue().birthdayProperty());
    programmeColumn.setCellValueFactory((cellData) -> cellData.getValue().programmeProperty());
    semesterColumn.setCellValueFactory((cellData) -> cellData.getValue().semesterProperty().asObject());
    classColumn.setCellValueFactory((cellData) -> cellData.getValue().studentClassProperty());

    reset();
  }

  public void reset() {
    errorLabel.setText("");
    studentListTable.setItems(database.getStudentViewModelList());
  }
  @FXML private void addStudentButtonPressed()
  {
  }

  public void removeStudentButtonPressed() {
    errorLabel.setText("");

    boolean remove = confirmation();
    if (remove){
      studentListTable.getSelectionModel().getSelectedItems().forEach(e -> {
        database.removeStudent(e.getStudent());
      });
    }
  }

  private boolean confirmation() {
    int index = studentListTable.getSelectionModel().getSelectedIndex();
    StudentViewModel selectedItem = studentListTable.getItems().get(index);
    if (index < 0 || index >= studentListTable.getItems().size())
    {
      return false;
    }
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(
        "Removing student");
    Optional<ButtonType> result = alert.showAndWait();
    return ((result.isPresent()) && (result.get() == ButtonType.OK));
  }

}