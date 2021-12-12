package s.schedulingsystemvia.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentDialogController implements Initializable
{

  ObservableList<String> cbStartList = FXCollections.observableArrayList("a",
      "b");

  @FXML private ChoiceBox<String> cbClass;
  @FXML private ChoiceBox<String> cbGender;
  @FXML private ChoiceBox<String> cbProgram;
  @FXML private ChoiceBox<String> cbSemester;
  @FXML private DatePicker dpBday;
  @FXML private Label label;
  @FXML private TextField tfEcts;
  @FXML private TextField tfFirst;
  @FXML private TextField tfLast;
  @FXML private TextField tfStudentNo;

  public StudentDialogController()
  {
  }

  @FXML @Override public void initialize(URL url, ResourceBundle resourceBundle)
  {
    ObservableList<String> list1 = FXCollections.observableArrayList();
    list1.addAll("Software Engineering", "Mechanical Engineering",
        "Marketing Management");
    cbProgram.setItems(list1);

    ObservableList<String> list2 = FXCollections.observableArrayList();
    list2.addAll("1", "2", "3", "4", "5", "6", "7");
    cbSemester.setItems(list2);

    ObservableList<String> list3 = FXCollections.observableArrayList();
    list3.addAll("X", "Y", "Z");
    cbClass.setItems(list3);

    ObservableList<String> list4 = FXCollections.observableArrayList();
    list4.addAll("DMA", "SEP", "SDJ", "RWD");
    cbGender.setItems(list4);

    LocalDate date = dpBday.getValue();

  }

  @FXML
  private void ButtonPressed()
  {
    try
    {
      if(tfEcts.getText().trim().isEmpty()|| tfStudentNo.getText().trim().isEmpty()  || tfFirst.getText().trim().isEmpty() || tfLast.getText().trim().isEmpty() || dpBday.getValue() == null || cbProgram.getSelectionModel().isEmpty() || cbSemester.getSelectionModel().isEmpty() || cbClass.getSelectionModel().isEmpty() || cbGender.getSelectionModel().isEmpty()){
        throw new IllegalArgumentException();
      }
      tfStudentNo.getText().toString().length();
      System.out.println(cbProgram.getValue());
      /*Student student = new Student(
          tfFirst.getText(),
          tfLast.getText(),
          tfEcts.getText(),
          cbClass.getValue(),
          cbProgram.getValue(),
          Integer.parseInt(cbSemester.getValue()),
          tfStudentNo.getText(),
          dpBday.getValue(),
          dpBday.getValue(),
          cbGender.getValue()
      );*/
      label.setText("Added");
      //System.out.println(student);
    } catch (IllegalArgumentException e) {
      label.setText("Error");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      label.setText(e.getMessage());
    }

  }
}
