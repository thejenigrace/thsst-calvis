package editor.model;

import editor.MainApp;
import editor.controller.WorkspaceController;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Jennica on 02/07/2016.
 */
public class FileEditorTabPane {

    private WorkspaceController workspaceController;
    private TabPane tabPane;
    private final ReadOnlyObjectWrapper<FileEditor> activeFileEditor = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyBooleanWrapper anyFileEditorModified = new ReadOnlyBooleanWrapper();

    public FileEditorTabPane(WorkspaceController workspaceController, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        System.out.println("Initialize FileEditorTabPane!");
        this.workspaceController = workspaceController;
        this.tabPane = new TabPane();
        this.tabPane.setFocusTraversable(false);
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        this.tabPane.prefWidthProperty().bind(width);
        this.tabPane.prefHeightProperty().bind(height);
        // Update activeFileEditor property
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            activeFileEditor.set((newTab != null) ? (FileEditor) newTab.getUserData() : null);
        });

        // Update anyFileEditorModified property
        ChangeListener<Boolean> modifiedListener = (observable, oldValue, newValue) -> {
            boolean modified = false;
            for ( Tab tab : tabPane.getTabs() ) {
                if ( ((FileEditor) tab.getUserData()).isModified() ) {
                    modified = true;
                    break;
                }
            }
            anyFileEditorModified.set(modified);
        };

        this.tabPane.getTabs().addListener((ListChangeListener<Tab>) c -> {
            while ( c.next() ) {
                if ( c.wasAdded() ) {
                    for ( Tab tab : c.getAddedSubList() )
                        ((FileEditor) tab.getUserData()).modifiedProperty().addListener(modifiedListener);
                } else if ( c.wasRemoved() ) {
                    for ( Tab tab : c.getRemoved() )
                        ((FileEditor) tab.getUserData()).modifiedProperty().removeListener(modifiedListener);
                }
            }

            // changes in the tabs may also change anyFileEditorModified property
            // (e.g. closed modified file)
            modifiedListener.changed(null, null, null);
        });
    }

    public Node getNode() {
        return this.tabPane;
    }

    public TabPane getTabPane() {
        return this.tabPane;
    }

    // 'activeFileEditor' property
    public FileEditor getActiveFileEditor() {
        return activeFileEditor.get();
    }

    public ReadOnlyObjectProperty<FileEditor> activeFileEditorProperty() {
        return activeFileEditor.getReadOnlyProperty();
    }

    // 'anyFileEditorModified' property
    public ReadOnlyBooleanProperty anyFileEditorModifiedProperty() {
        return anyFileEditorModified.getReadOnlyProperty();
    }

    public void newFileEditor() {
        FileEditor fileEditor = createFileEditor(null);
        Tab tab = fileEditor.getTab();
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

        try {
            this.workspaceController.getSysCon().attach(fileEditor.getTextEditor());
            fileEditor.getTextEditor().build();
            this.workspaceController.getSysCon().clear();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private FileEditor createFileEditor(Path path) {
        FileEditor fileEditor = new FileEditor(workspaceController, path);
        fileEditor.getTab().setOnCloseRequest(e -> {
            if(!this.canCloseEditor(fileEditor))
                e.consume();
        });

        return fileEditor;
    }

    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CALVIS Files (*.calvis)", "*.calvis"),
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

//        String lastDirectory = MarkdownWriterFXApp.getState().get("lastDirectory", null);
//        File file = new File((lastDirectory != null) ? lastDirectory : ".");
//        File file = new File();
//        if (!file.isDirectory())
//            file = new File(".");
//        fileChooser.setInitialDirectory(file);
        return fileChooser;
    }

    public boolean saveEditor(FileEditor fileEditor) {
        if (fileEditor == null || !fileEditor.isModified())
            return true;

        if (fileEditor.getPath() == null) {
            this.tabPane.getSelectionModel().select(fileEditor.getTab());

            FileChooser fileChooser = createFileChooser("Save Markdown File");
            File file = fileChooser.showSaveDialog(MainApp.primaryStage);
            if (file == null)
                return false;

//            saveLastDirectory(file);
            fileEditor.setPath(file.toPath());
        }

        return fileEditor.save();
    }

    private boolean canCloseEditor(FileEditor fileEditor) {
        if (!fileEditor.isModified())
            return true;

        Alert alert = this.workspaceController.createAlert(Alert.AlertType.CONFIRMATION, "Close",
                "''{0}'' has been modifiedc. Save changes?", fileEditor.getTab().getText());
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        ButtonType result = alert.showAndWait().get();
        if (result != ButtonType.YES)
            return (result == ButtonType.NO);

        return saveEditor(fileEditor);
    }

    /**
     * MARK --
     */
    public void play() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            this.workspaceController.getSysCon().play(codeArea.getText());
    }

    public void stop() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            this.workspaceController.getSysCon().end();
    }

    public void previous() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            this.workspaceController.getSysCon().previous();
    }

    public void next() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            this.workspaceController.getSysCon().next();
    }

    public void reset() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            this.workspaceController.getSysCon().reset();
    }
}
