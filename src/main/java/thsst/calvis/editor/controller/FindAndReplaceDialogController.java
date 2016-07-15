package thsst.calvis.editor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Jennica on 02/01/2016.
 */
public class FindAndReplaceDialogController {

    @FXML
    private TextField textFieldFind;
    @FXML
    private TextField textFieldReplace;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getFindField() {
        return this.textFieldFind.getText();
    }

    public String getReplaceField() {
        return this.textFieldReplace.getText();
    }

    @FXML
    private void handleCancel(ActionEvent event) throws Exception {
        dialogStage.close();
    }

    @FXML
    private void handleFindAndReplace(ActionEvent event) throws Exception {
        System.out.println("FIND: " + this.getFindField());
        System.out.println("REPLACE: " + this.getReplaceField());

        this.workspaceController.onActionFindAndReplace(this.getFindField(), this.getReplaceField());
    }

}
