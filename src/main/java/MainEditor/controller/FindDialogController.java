package MainEditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 28/12/2015.
 */
public class FindDialogController implements Initializable {

    @FXML
    private TextField textFieldFind;
    @FXML
    private Button btnUp;
    @FXML
    private Button btnDown;

    private WorkspaceController workspaceController;
    private Stage dialogStage;
    private String code;

    private HashMap<Integer, int[]> findHighlightRanges;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnUp.setVisible(false);
        btnDown.setVisible(false);
    }

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFindField() {
        return textFieldFind.getText();
    }

    @FXML
    private void handleUp(ActionEvent event) {
        workspaceController.onActionUp();
    }

    @FXML void handleDown(ActionEvent event) {
        workspaceController.onActionDown();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleFind(ActionEvent event) {
        System.out.println("FIND: " + getFindField());
        System.out.println("CODE: " + code);
        String find_pattern = "\\b(" + getFindField() + ")\\b";
        Pattern pattern = Pattern.compile("(?<FIND>" + find_pattern + ")");
        System.out.println("PATTERN: " + pattern.toString());

        Matcher matcher = pattern.matcher(code);

        this.findHighlightRanges = new HashMap<>();
        int c = 0;
        while(matcher.find()) {
            System.out.println("matcher.group(\"FIND\"): " + matcher.group("FIND"));

            System.out.println("matcher.end() " + matcher.end());
            System.out.println("matcher.start() " + matcher.start());

            int[] arrRange = new int[2];
            arrRange[0] = matcher.start();
            arrRange[1] = matcher.end();

            findHighlightRanges.put(c, arrRange);

            c++;
        }

        if(c > 0) {
            workspaceController.onActionFind(findHighlightRanges);
            btnUp.setVisible(true);
            btnDown.setVisible(true);
        }
    }
}
