package editor.model;

import editor.controller.WorkspaceController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.fxmisc.undo.UndoManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jennica on 02/07/2016.
 */
public class FileEditor {

    private WorkspaceController workspaceController;
    private Tab tab = new Tab();
    private TextEditorPane textEditorPane = new TextEditorPane();

    // 'path' property
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    // 'modified' property
    private final ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper();
    // 'canUndo' property
    private final BooleanProperty canUndo = new SimpleBooleanProperty();
    // 'canRedo' property
    private final BooleanProperty canRedo = new SimpleBooleanProperty();

    public FileEditor(WorkspaceController workspaceController, Path path) {
        System.out.println("Initialize FileEditor!");
        this.workspaceController = workspaceController;

        this.tab.setUserData(this);

        this.path.set(path);
        this.path.addListener((observable, oldPath, newPath) -> updateTab());
        this.modified.addListener((observable, oldPath, newPath) -> updateTab());
        this.updateTab();

        this.tab.setOnSelectionChanged(e -> {
            if ( tab.isSelected() )
                this.activated();
        });
        this.tab.setContent(textEditorPane.getCodeArea());

        this.activated();
    }

    public Tab getTab() {
        return tab;
    }

    public TextEditorPane getTextEditor() {
        return textEditorPane;
    }

    // Path Property
    public Path getPath() {
        return path.get();
    }

    public void setPath(Path path) {
        this.path.set(path);
    }

    public ObjectProperty<Path> pathProperty() {
        return path;
    }

    // Modified Property
    public boolean isModified() {
        return modified.get();
    }

    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified.getReadOnlyProperty();
    }

    // 'canUndo' property
    public BooleanProperty canUndoProperty() { return canUndo; }

    // 'canRedo' property
    public BooleanProperty canRedoProperty() { return canRedo; }

    private void updateTab() {
        Path path = this.path.get();
        this.tab.setText((path != null) ? path.getFileName().toString() : "Untitled");
        this.tab.setTooltip((path != null) ? new Tooltip(path.toString()) : null);
        this.tab.setGraphic(isModified() ? new Text("*") : null);
    }

    private void activated() {
        System.out.println("Activated TextEditorPane!");

//        if( this.tab.getTabPane() == null || !tab.isSelected() )
//            return; // Tab is already closed or no longer active

//        if ( this.tab.getContent() != null ) {
//            this.textEditorPane.requestFocus();
//            return;
//        }

        // Load file and create UI when the tab becomes visible the first time
        this.textEditorPane = new TextEditorPane();
        this.textEditorPane.pathProperty().bind(path);

        this.load();

        // Clear undo history after first load
        this.textEditorPane.getUndoManager().forgetHistory();

        // Bind the text editor undo manager to the properties
        UndoManager undoManager = textEditorPane.getUndoManager();
        this.modified.bind(Bindings.not(undoManager.atMarkedPositionProperty()));
        this.canUndo.bind(undoManager.undoAvailableProperty());
        this.canRedo.bind(undoManager.redoAvailableProperty());

        this.tab.setContent(this.textEditorPane.getCodeArea());
        this.textEditorPane.requestFocus();
    }

    private void load() {
        Path path = this.path.get();
        System.out.println("path = " + path);
        if(path == null)
            return;

        try {
            byte[] bytes = Files.readAllBytes(path);

            String text = new String(bytes);

            System.out.println("load text = " + text);

            this.textEditorPane.setCodeAreaText(text);
            this.textEditorPane.getUndoManager().mark();
        } catch ( IOException ex ) {
            Alert alert = workspaceController.createAlert(Alert.AlertType.ERROR, "Load",
                    "Failed to load ''{0}''.\n\nReason: {1}", path, ex.getMessage());
            alert.showAndWait();
        }
    }

    public boolean save() {
        String text = textEditorPane.getCodeAreaText();

        byte[] bytes = text.getBytes();

        try {
            Files.write(path.get(), bytes);
            this.textEditorPane.getUndoManager().mark();
            return true;
        } catch ( IOException ex ) {
            Alert alert = workspaceController.createAlert(Alert.AlertType.ERROR, "Save",
                    "Failed to save ''{0}''.\n\nReason: {1}", path.get(), ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    private String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
    }

    private void writeFile(String content, File file) {
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
