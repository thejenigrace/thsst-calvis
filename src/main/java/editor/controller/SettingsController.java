package editor.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jennica on 28/02/2016.
 */

@SuppressWarnings("unchecked")
public class SettingsController implements Initializable {

    @FXML
    private TreeView treeViewSettings;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem appearanceItem = new TreeItem("Appearance");

        TreeItem editorItem = new TreeItem("Editor");
        editorItem.getChildren().addAll(getEditorItems());

        TreeItem dummyRoot = new TreeItem();
        dummyRoot.getChildren().addAll(editorItem);
        treeViewSettings.setRoot(dummyRoot);
        treeViewSettings.setShowRoot(false);

//        treeViewSettings.
    }

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public ArrayList<TreeItem> getEditorItems() {
        ArrayList<TreeItem> items = new ArrayList<>();

        TreeItem playItem = new TreeItem("Play Speed");
        TreeItem byteAddressableItem = new TreeItem("Byte Addressable");

        items.add(playItem);
        items.add(byteAddressableItem);

        return items;
    }
}
