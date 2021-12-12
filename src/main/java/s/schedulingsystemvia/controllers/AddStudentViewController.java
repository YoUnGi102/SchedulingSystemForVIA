package s.schedulingsystemvia.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import s.schedulingsystemvia.ViewHandler;
import s.schedulingsystemvia.andrei.Student;

public class AddStudentViewController
{
  @FXML private TextField nameTextField;
  @FXML private TextField viaNumberTextField;
  @FXML private TextField programmeTextField;
  @FXML private Label errorLabel;

  private Region root;
  private ViewHandler viewHandler;

  public AddStudentViewController()
  {
  }

  public void init(ViewHandler viewHandler, Region root)
  {
    this.root = root;
    this.viewHandler = viewHandler;
    reset();
  }

  public void reset()
  {
    this.nameTextField.setText("");
    this.viaNumberTextField.setText("");
    this.programmeTextField.setText("");
    this.errorLabel.setText("");
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void addStudentSubmitButton()
  {
    errorLabel.setText("");
    try
    {
     Student student = new Student(nameTextField.getText(),Integer.parseInt(
         viaNumberTextField.getText()), programmeTextField.getText());
      viewHandler.openView("list");
    }
    catch (Exception e)
    {
      errorLabel.setText("illegal" + e.getMessage());
    }
  }
  @FXML private void cancelButton()

  {
    viewHandler.openView("list");
  }

}
