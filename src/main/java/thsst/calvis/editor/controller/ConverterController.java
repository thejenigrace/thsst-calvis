package thsst.calvis.editor.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jennica on 19/07/2016.
 */
public class ConverterController implements Initializable {

    @FXML
    private TextField textFieldInput;
    @FXML
    private TextField textFieldOutput;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textFieldOutput.setEditable(false);
    }

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
