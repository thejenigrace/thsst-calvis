package editor.model;

import editor.MainApp;
import editor.controller.WorkspaceController;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 02/07/2016.
 */
public class FileEditorTabPane {

    private WorkspaceController workspaceController;
    private TabPane tabPane;
    private final ReadOnlyObjectWrapper<FileEditor> activeFileEditor = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyBooleanWrapper anyFileEditorModified = new ReadOnlyBooleanWrapper();

    private HashMap<Integer, int[]> findHighlightRanges;
    private int currentFindRangeIndex;

    public FileEditorTabPane(WorkspaceController workspaceController, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
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

        // re-open files
        restoreState();
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
            if ( !this.canCloseFileEditor(fileEditor) )
                e.consume();
        });

        return fileEditor;
    }

    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        // Set extension filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CALVIS Files (*.txt, *.calvis)", "*.txt", "*.calvis"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String lastDirectory = MainApp.getState().get("lastDirectory", null);
        File file = new File((lastDirectory != null) ? lastDirectory : ".");
        if ( !file.isDirectory() )
            file = new File(".");
        fileChooser.setInitialDirectory(file);
        return fileChooser;
    }

    public FileEditor[] openFileEditor() {
        FileChooser fileChooser = createFileChooser("Open CALVIS File");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(MainApp.primaryStage);
        if ( selectedFiles == null )
            return null;

//        saveLastDirectory(selectedFiles.get(0));
        return openFileEditors(selectedFiles, 0);
    }

    private FileEditor[] openFileEditors(List<File> files, int activeIndex) {
        // close single unmodified "Untitled" tab
        if ( this.tabPane.getTabs().size() == 1 ) {
            FileEditor fileEditor = (FileEditor) this.tabPane.getTabs().get(0).getUserData();
            if ( fileEditor.getPath() == null && !fileEditor.isModified() )
                this.closeFileEditor(fileEditor, false);
        }

        FileEditor[] fileEditors = new FileEditor[files.size()];
        for ( int i = 0; i < files.size(); i++ ) {
            Path path = files.get(i).toPath();

            // check whether file is already opened
            FileEditor fileEditor = findEditor(path);
            if ( fileEditor == null ) {
                fileEditor = createFileEditor(path);

                this.tabPane.getTabs().add(fileEditor.getTab());
            }

            try {
                this.workspaceController.getSysCon().attach(fileEditor.getTextEditor());
                fileEditor.getTextEditor().build();
                this.workspaceController.getSysCon().clear();
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            // select first file
            if ( i == activeIndex )
                this.tabPane.getSelectionModel().select(fileEditor.getTab());

            fileEditors[i] = fileEditor;
        }
        return fileEditors;
    }

    public boolean saveFileEditor(FileEditor fileEditor) {
        if ( fileEditor == null || !fileEditor.isModified() )
            return true;

        if ( fileEditor.getPath() == null ) {
            this.tabPane.getSelectionModel().select(fileEditor.getTab());

            FileChooser fileChooser = createFileChooser("Save CALVIS File");
            File file = fileChooser.showSaveDialog(MainApp.primaryStage);
            if ( file == null )
                return false;

            this.saveLastDirectory(file);
            fileEditor.setPath(file.toPath());
        }

        return fileEditor.save();
    }

    private void saveLastDirectory(File file) {
        MainApp.getState().put("lastDirectory", file.getParent());
    }

    public boolean saveAllFileEditors() {
        FileEditor[] allEditors = getAllEditors();

        boolean success = true;
        for ( FileEditor fileEditor : allEditors ) {
            if ( !saveFileEditor(fileEditor) )
                success = false;
        }

        return success;
    }

    public boolean saveAsFileEditor(FileEditor fileEditor) {
        this.tabPane.getSelectionModel().select(fileEditor.getTab());

        FileChooser fileChooser =  createFileChooser("Save CALVIS File");
        // Show save file dialog
        File file = fileChooser.showSaveDialog(MainApp.primaryStage);

        if ( file == null )
           return false;

        this.saveLastDirectory(file);
        fileEditor.setPath(file.toPath());

        return fileEditor.save();
    }

    private void writeFile(String content, File file) {
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch ( IOException ex ) {
            Logger.getLogger(WorkspaceController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public boolean canCloseFileEditor(FileEditor fileEditor) {
        if ( !fileEditor.isModified() )
            return true;

        Alert alert = this.workspaceController.createAlert(Alert.AlertType.CONFIRMATION, "Close",
                "''{0}'' has been modifiedc. Save changes?", fileEditor.getTab().getText());
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        ButtonType result = alert.showAndWait().get();
        if ( result != ButtonType.YES )
            return (result == ButtonType.NO);

        return saveFileEditor(fileEditor);
    }

    public boolean closeFileEditor(FileEditor fileEditor, boolean save) {
        if ( fileEditor == null )
            return true;

        Tab tab = fileEditor.getTab();

        if ( save ) {
            Event event = new Event(tab, tab, Tab.TAB_CLOSE_REQUEST_EVENT);
            Event.fireEvent(tab, event);
            if ( event.isConsumed() )
                return false;
        }

        tabPane.getTabs().remove(tab);
        if ( tab.getOnClosed() != null )
            Event.fireEvent(tab, new Event(Tab.CLOSED_EVENT));

        return true;
    }

    public boolean closeAllFileEditors() {
        FileEditor[] allEditors = getAllEditors();
        FileEditor activeEditor = activeFileEditor.get();

        // try to save active tab first because in case the user decides to cancel,
        // then it stays active
        if ( activeEditor != null && !canCloseFileEditor(activeEditor) )
            return false;

        // save modified tabs
        for ( int i = 0; i < allEditors.length; i++ ) {
            FileEditor fileEditor = allEditors[i];
            if ( fileEditor == activeEditor )
                continue;

            if ( fileEditor.isModified() ) {
                // activate the modified tab to make its modified content visible to the user
                tabPane.getSelectionModel().select(i);

                if ( !canCloseFileEditor(fileEditor) )
                    return false;
            }
        }

        // close all tabs
        for ( FileEditor fileEditor : allEditors ) {
            if ( !closeFileEditor(fileEditor, false) )
                return false;
        }

        saveState(allEditors, activeEditor);

        return tabPane.getTabs().isEmpty();
    }

    private FileEditor[] getAllEditors() {
        ObservableList<Tab> tabs = tabPane.getTabs();
        FileEditor[] allEditors = new FileEditor[tabs.size()];
        for ( int i = 0; i < tabs.size(); i++ )
            allEditors[i] = (FileEditor) tabs.get(i).getUserData();
        return allEditors;
    }

    private FileEditor findEditor(Path path) {
        for ( Tab tab : tabPane.getTabs() ) {
            FileEditor fileEditor = (FileEditor) tab.getUserData();
            if ( path.equals(fileEditor.getPath()) )
                return fileEditor;
        }
        return null;
    }

    private void restoreState() {
        Preferences state = MainApp.getState();
        String[] fileNames = Utility.getPrefsStrings(state, "file");
        String activeFileName = state.get("activeFile", null);

        int activeIndex = 0;
        ArrayList<File> files = new ArrayList<>(fileNames.length);
        for ( String fileName : fileNames ) {
            File file = new File(fileName);
            if ( file.exists() ) {
                files.add(file);

                if ( fileName.equals(activeFileName) )
                    activeIndex = files.size() - 1;
            }
        }

        if ( files.isEmpty() ) {
            this.newFileEditor();
            return;
        }

        this.openFileEditors(files, activeIndex);
    }

    private void saveState(FileEditor[] allEditors, FileEditor activeEditor) {
        ArrayList<String> fileNames = new ArrayList<>(allEditors.length);
        for ( FileEditor fileEditor : allEditors ) {
            if ( fileEditor.getPath() != null )
                fileNames.add(fileEditor.getPath().toString());
        }

        Preferences state = MainApp.getState();
        Utility.putPrefsStrings(state, "file", fileNames.toArray(new String[fileNames.size()]));
        if ( activeEditor != null && activeEditor.getPath() != null )
            state.put("activeFile", activeEditor.getPath().toString());
        else
            state.remove("activeFile");
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

    public void disableCodeArea(boolean flag) {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.setDisable(flag);
    }

    public void undo() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.getUndoManager().undo();
    }

    public void redo() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.getUndoManager().redo();
    }

    public void cut() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.cut();
    }

    public void copy() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.copy();
    }

    public void paste() {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.paste();
    }

    public void formatCode(String codeBlock) {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        String[] arr = this.workspaceController.getSysCon().getKeywordBuilder().getInstructionKeywords();
        String expression = String.join("|", arr);
        String pat = "[^\\S\\n]+(?=(([a-zA-Z_][a-zA-Z\\d_]*:\\s*)?\\b(" + expression + ")\\b))";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(codeBlock);
        String replacedCodeAreaText = matcher.replaceAll("\r\n");
        replacedCodeAreaText = replacedCodeAreaText.replaceAll("(?!.*\")\\s*,\\s*", ", ");
        replacedCodeAreaText = replacedCodeAreaText.replaceAll("(?!.*\")\\s*:\\s*", ": ");
        codeArea.replaceText(replacedCodeAreaText);
        codeArea.redo();
    }

    public void onActionFind(HashMap<Integer, int[]> findHighlightRanges) {
        System.out.println("Launch onActionFind!");
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        this.findHighlightRanges = findHighlightRanges;
        if ( findHighlightRanges.size() != 0 ) {
            currentFindRangeIndex = 0;
            int[] range = findHighlightRanges.get(0);
            codeArea.selectRange(range[0], range[1]);
        }
    }

    public void findTextFieldChange(String newValue) {
        Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
        if ( !newValue.isEmpty() && tab != null ) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            String find_pattern = "\\b(" + newValue + ")\\b";
            Pattern pattern = Pattern.compile("(?<FIND>" + find_pattern + ")");
            System.out.println("PATTERN: " + pattern.toString());

            Matcher matcher = pattern.matcher(codeArea.getText());

            findHighlightRanges = new HashMap<>();
            int c = 0;
            while ( matcher.find() ) {
                System.out.println("matcher.group(\"FIND\"): " + matcher.group("FIND"));
                System.out.println("matcher.end() " + matcher.end());
                System.out.println("matcher.start() " + matcher.start());

                int[] arrRange = new int[2];
                arrRange[0] = matcher.start();
                arrRange[1] = matcher.end();

                this.findHighlightRanges.put(c, arrRange);
                c++;
            }

            if ( c > 0 ) {
                onActionFind(findHighlightRanges);
                this.workspaceController.disableFindButton(false);
            } else {
                this.workspaceController.disableFindButton(true);
            }
        } else {
            this.workspaceController.disableFindButton(true);
        }
    }

    public void onFindUp() {
        try {
            Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
            if ( tab != null ) {
                CodeArea codeArea = (CodeArea) tab.getContent();
                int[] range;
                if ( findHighlightRanges.size() > 0 ) {
                    System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
                    if ( currentFindRangeIndex > 0 ) {
                        currentFindRangeIndex--;
                        System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                        range = findHighlightRanges.get(currentFindRangeIndex);
                        codeArea.selectRange(range[0], range[1]);
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void onFindDown() {
        try {
            Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
            if ( tab != null ) {
                CodeArea codeArea = (CodeArea) tab.getContent();
                int[] range;
                if ( findHighlightRanges.size() > 1 ) {
                    System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
                    System.out.println("findHiglightRanges.size() = " + findHighlightRanges.size());

                    if ( currentFindRangeIndex < findHighlightRanges.size() - 1 ) {
                        currentFindRangeIndex++;
                        System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                        range = findHighlightRanges.get(currentFindRangeIndex);
                        codeArea.selectRange(range[0], range[1]);
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void onActionFindAndReplace(String find, String replace) {
        System.out.println("BTW find: " + find);
        System.out.println("BTW replace: " + replace);

        Tab tab = this.tabPane.getSelectionModel().getSelectedItem();

        if ( tab != null ) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            String text = codeArea.getText();
            Pattern p = Pattern.compile(find);
            Matcher m = p.matcher(text);

            StringBuffer sb = new StringBuffer();
            int c = 0;
            while ( m.find() ) {
                m.appendReplacement(sb, replace);
                c++;
            }

            System.out.println("count: " + c);
            m.appendTail(sb);
            System.out.println("sb: " + sb);
            codeArea.replaceText(sb.toString());
        }
    }
}
