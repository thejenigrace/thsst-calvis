package editor.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    private AnchorPane anchorPaneSettingsView;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnOk;
    @FXML
    private TreeView<String> treeViewSettings;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            TreeItem<String> appearanceItem = new TreeItem("Appearance");

            TreeItem<String> editorItem = new TreeItem("Editor");
//            editorItem.getChildren().addAll(getEditorItems());

            TreeItem<String> dummyRoot = new TreeItem();
            dummyRoot.getChildren().addAll(editorItem);
            treeViewSettings.setRoot(dummyRoot);
            treeViewSettings.setShowRoot(false);

            treeViewSettings.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<TreeItem<String>>() {
                        @Override
                        public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                            if(newValue == null) {
                                return;
                            } else {
                                try {
                                    changeView(newValue.getValue());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

            btnApply.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeView(String settings) throws Exception {
       if(settings.equals("Editor")) {
           System.out.println("Selected Editor");
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getResource("/fxml/settings_editor.fxml"));
           Parent settingsEditorView = (VBox) loader.load();

           anchorPaneSettingsView.getChildren().add(settingsEditorView);
       }
    }

    public ArrayList<TreeItem<String>> getEditorItems() {
        ArrayList<TreeItem<String>> items = new ArrayList<>();

        TreeItem<String> playItem = new TreeItem("Play Speed");
        TreeItem<String> byteAddressableItem = new TreeItem("Byte Addressable");

        items.add(playItem);
        items.add(byteAddressableItem);

        return items;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleApply(ActionEvent event) {
        btnApply.setDisable(true);
    }
    @FXML
    private void handleOk(ActionEvent event) {
        dialogStage.close();
    }
}
