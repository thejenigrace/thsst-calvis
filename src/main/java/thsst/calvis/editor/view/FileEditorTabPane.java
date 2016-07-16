package thsst.calvis.editor.view;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import thsst.calvis.MainApp;
import thsst.calvis.editor.controller.WorkspaceController;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 02/07/2016.
 */
public class FileEditorTabPane {

    private WorkspaceController workspaceController;
    private TabPane tabPane;
    private final ReadOnlyObjectWrapper<FileEditorTab> activeFileEditor = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyBooleanWrapper anyFileEditorModified = new ReadOnlyBooleanWrapper();

    private HashMap<Integer, int[]> findHighlightRanges;
    private int currentFindRangeIndex;

    public FileEditorTabPane(WorkspaceController workspaceController, ReadOnlyDoubleProperty paneWidth, ReadOnlyDoubleProperty paneHeight) {
        this.workspaceController = workspaceController;
        this.tabPane = new TabPane();
        this.tabPane.setFocusTraversable(false);
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        this.tabPane.prefWidthProperty().bind(paneWidth);
        this.tabPane.prefHeightProperty().bind(paneHeight);
//        this.tabPane.getStylesheets().add("others-tab-pane.css");
        // Update activeFileEditor property
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            activeFileEditor.set((newTab != null) ? (FileEditorTab) newTab.getUserData() : null);
        });

        // Update anyFileEditorModified property
        ChangeListener<Boolean> modifiedListener = (observable, oldValue, newValue) -> {
            boolean modified = false;
            for ( Tab tab : tabPane.getTabs() ) {
                if ( ((FileEditorTab) tab.getUserData()).isModified() ) {
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
                        ((FileEditorTab) tab.getUserData()).modifiedProperty().addListener(modifiedListener);
                } else if ( c.wasRemoved() ) {
                    for ( Tab tab : c.getRemoved() )
                        ((FileEditorTab) tab.getUserData()).modifiedProperty().removeListener(modifiedListener);
                }
            }

            // changes in the tabs may also change anyFileEditorModified property
            // (e.g. closed modified file)
            modifiedListener.changed(null, null, null);
        });

        // re-open files
//        restoreState();
    }

    public TabPane getTabPane() {
        return this.tabPane;
    }

    // 'activeFileEditor' property
    public FileEditorTab getActiveFileEditor() {
        return activeFileEditor.get();
    }

    public ReadOnlyObjectProperty<FileEditorTab> activeFileEditorProperty() {
        return activeFileEditor.getReadOnlyProperty();
    }

    // 'anyFileEditorModified' property
    public ReadOnlyBooleanProperty anyFileEditorModifiedProperty() {
        return anyFileEditorModified.getReadOnlyProperty();
    }

    public void newFileEditor() {
        FileEditorTab fileEditorTab = this.createFileEditor(null);
        Tab tab = fileEditorTab.getTab();
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);

        try {
            this.workspaceController.getSysCon().attach(fileEditorTab.getTextEditor());
            fileEditorTab.getTextEditor().build();
            this.workspaceController.getSysCon().clear();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private FileEditorTab createFileEditor(Path path) {
        FileEditorTab fileEditorTab = new FileEditorTab(path);
        fileEditorTab.getTab().setOnCloseRequest(e -> {
            if ( !this.canCloseFileEditor(fileEditorTab) )
                e.consume();
        });

        return fileEditorTab;
    }

    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        // Set extension filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CALVIS Files (*.txt, *.calvis)", "*.txt", "*.calvis")
//                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

//        String lastDirectory = MainApp.getState().get("lastDirectory", null);
//        File file = new File((lastDirectory != null) ? lastDirectory : ".");
//        if ( !file.isDirectory() )
//            file = new File(".");

        File file = new File(".");
        fileChooser.setInitialDirectory(file);
        return fileChooser;
    }

    public FileEditorTab[] openFileEditor() {
        FileChooser fileChooser = createFileChooser("Open CALVIS File");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(MainApp.primaryStage);
        if ( selectedFiles == null )
            return null;

//        saveLastDirectory(selectedFiles.get(0));
        return openFileEditors(selectedFiles, 0);
    }

    private FileEditorTab[] openFileEditors(List<File> files, int activeIndex) {
        // close single unmodified "Untitled" tab
        if ( this.tabPane.getTabs().size() == 1 ) {
            FileEditorTab fileEditorTab = (FileEditorTab) this.tabPane.getTabs().get(0).getUserData();
            if ( fileEditorTab.getPath() == null && !fileEditorTab.isModified() )
                this.closeFileEditor(fileEditorTab, false);
        }

        FileEditorTab[] fileEditorTabs = new FileEditorTab[files.size()];
        for ( int i = 0; i < files.size(); i++ ) {
            Path path = files.get(i).toPath();

            // check whether file is already opened
            FileEditorTab fileEditorTab = this.findFileEditor(path);
            if ( fileEditorTab == null ) {
                System.out.println("Already Open!");
                fileEditorTab = this.createFileEditor(path);

                this.tabPane.getTabs().add(fileEditorTab.getTab());
            }

            try {
                this.workspaceController.getSysCon().attach(fileEditorTab.getTextEditor());
                fileEditorTab.getTextEditor().build();
                this.workspaceController.getSysCon().clear();
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            // select first file
            if ( i == activeIndex )
                this.tabPane.getSelectionModel().select(fileEditorTab.getTab());

            fileEditorTabs[i] = fileEditorTab;
        }
        return fileEditorTabs;
    }

    public boolean saveFileEditor(FileEditorTab fileEditorTab) {
        if ( fileEditorTab == null || !fileEditorTab.isModified() )
            return true;

        if ( fileEditorTab.getPath() == null ) {
            this.tabPane.getSelectionModel().select(fileEditorTab.getTab());

            FileChooser fileChooser = createFileChooser("Save CALVIS File");
            File file = fileChooser.showSaveDialog(MainApp.primaryStage);
            if ( file == null )
                return false;

//            this.saveLastDirectory(file);
            fileEditorTab.setPath(file.toPath());
        }

        return fileEditorTab.save();
    }

//    private void saveLastDirectory(File file) {
//        MainApp.getState().put("lastDirectory", file.getParent());
//    }

    public boolean saveAllFileEditors() {
        FileEditorTab[] allEditors = this.getAllFileEditors();

        boolean success = true;
        for ( FileEditorTab fileEditorTab : allEditors ) {
            if ( !saveFileEditor(fileEditorTab) )
                success = false;
        }

        return success;
    }

    public boolean saveAsFileEditor(FileEditorTab fileEditorTab) {
        this.tabPane.getSelectionModel().select(fileEditorTab.getTab());

        FileChooser fileChooser =  createFileChooser("Save As CALVIS File");
        // Show save file dialog
        File file = fileChooser.showSaveDialog(MainApp.primaryStage);

        if ( file == null )
           return false;

//        this.saveLastDirectory(file);
        fileEditorTab.setPath(file.toPath());

        return fileEditorTab.save();
    }

    public boolean canCloseFileEditor(FileEditorTab fileEditorTab) {
        if ( !fileEditorTab.isModified() )
            return true;

        Alert alert = MainApp.createAlert(Alert.AlertType.CONFIRMATION, "Close",
                "''{0}'' has been modifiedc. Save changes?", fileEditorTab.getTab().getText());
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        ButtonType result = alert.showAndWait().get();
        if ( result != ButtonType.YES )
            return (result == ButtonType.NO);

        return saveFileEditor(fileEditorTab);
    }

    public boolean closeFileEditor(FileEditorTab fileEditorTab, boolean isSave) {
        if ( fileEditorTab == null )
            return true;

        Tab tab = fileEditorTab.getTab();

        if ( isSave ) {
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
        FileEditorTab[] allEditors = this.getAllFileEditors();
        FileEditorTab activeEditor = activeFileEditor.get();

        // try to save active tab first because in case the user decides to cancel,
        // then it stays active
        if ( activeEditor != null && !canCloseFileEditor(activeEditor) )
            return false;

        // save modified tabs
        for ( int i = 0; i < allEditors.length; i++ ) {
            FileEditorTab fileEditorTab = allEditors[i];
            if ( fileEditorTab == activeEditor )
                continue;

            if ( fileEditorTab.isModified() ) {
                // activate the modified tab to make its modified content visible to the user
                tabPane.getSelectionModel().select(i);

                if ( !canCloseFileEditor(fileEditorTab) )
                    return false;
            }
        }

        // close all tabs
        for ( FileEditorTab fileEditorTab : allEditors ) {
            if ( !closeFileEditor(fileEditorTab, false) )
                return false;
        }

//        saveState(allEditors, activeEditor);

        return tabPane.getTabs().isEmpty();
    }

    private FileEditorTab[] getAllFileEditors() {
        ObservableList<Tab> tabs = tabPane.getTabs();
        FileEditorTab[] allEditors = new FileEditorTab[tabs.size()];
        for ( int i = 0; i < tabs.size(); i++ )
            allEditors[i] = (FileEditorTab) tabs.get(i).getUserData();
        return allEditors;
    }

    private FileEditorTab findFileEditor(Path path) {
        for ( Tab tab : tabPane.getTabs() ) {
            FileEditorTab fileEditorTab = (FileEditorTab) tab.getUserData();
            if ( path.equals(fileEditorTab.getPath()) )
                return fileEditorTab;
        }
        return null;
    }

    /**
     * MARK -- SIMULATION METHODS
     */

    public void simulationAction(String action) {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        if ( codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") )
            switch ( action ) {
                case "PLAY": this.workspaceController.getSysCon().play(codeArea.getText());
                    break;
                case "PAUSE": this.workspaceController.getSysCon().pause();
                    break;
                case "STOP": this.workspaceController.getSysCon().end();
                    break;
                case "PREVIOUS": this.workspaceController.getSysCon().previous();
                    break;
                case "NEXT": this.workspaceController.getSysCon().next();
                    break;
                case "RESET": this.workspaceController.getSysCon().reset();
                    break;
                default: System.out.println("None");
            }
    }

    public void disableCodeArea(boolean flag) {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.setDisable(flag);
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

    public void onActionFind(String find) {
        Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
        if ( !find.isEmpty() && tab != null ) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            String find_pattern = "\\b(" + find + ")\\b";
            Pattern pattern = Pattern.compile("(?<FIND>" + find_pattern + ")");
//            System.out.println("PATTERN: " + pattern.toString());

            Matcher matcher = pattern.matcher(codeArea.getText());

            findHighlightRanges = new HashMap<>();
            int c = 0;
            while ( matcher.find() ) {
//                System.out.println("matcher.group(\"FIND\"): " + matcher.group("FIND"));
//                System.out.println("matcher.end() " + matcher.end());
//                System.out.println("matcher.start() " + matcher.start());

                int[] arrRange = new int[2];
                arrRange[0] = matcher.start();
                arrRange[1] = matcher.end();

                this.findHighlightRanges.put(c, arrRange);
                c++;
            }

            if ( c > 0 ) {
                this.highlightsSelected(findHighlightRanges);
                this.workspaceController.disableFindButton(false);
            } else {
                this.workspaceController.disableFindButton(true);
            }
        } else {
            this.workspaceController.disableFindButton(true);
        }
    }

    public void highlightsSelected(HashMap<Integer, int[]> findHighlightRanges) {
        CodeArea codeArea = (CodeArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();

        this.findHighlightRanges = findHighlightRanges;
        if ( findHighlightRanges.size() != 0 ) {
            currentFindRangeIndex = 0;
            int[] range = findHighlightRanges.get(0);
            codeArea.selectRange(range[0], range[1]);
        }
    }

    public void onActionFindMoveUpward() {
        try {
            Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
            if ( tab != null ) {
                CodeArea codeArea = (CodeArea) tab.getContent();
                int[] range;
                if ( findHighlightRanges.size() > 0 ) {
//                    System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
                    if ( currentFindRangeIndex > 0 ) {
                        currentFindRangeIndex--;
//                        System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                        range = findHighlightRanges.get(currentFindRangeIndex);
                        codeArea.selectRange(range[0], range[1]);
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void onActionFindMoveDownward() {
        try {
            Tab tab = this.tabPane.getSelectionModel().getSelectedItem();
            if ( tab != null ) {
                CodeArea codeArea = (CodeArea) tab.getContent();
                int[] range;
                if ( findHighlightRanges.size() > 1 ) {
//                    System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
//                    System.out.println("findHiglightRanges.size() = " + findHighlightRanges.size());

                    if ( currentFindRangeIndex < findHighlightRanges.size() - 1 ) {
                        currentFindRangeIndex++;
//                        System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
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
//        System.out.println("BTW find: " + find);
//        System.out.println("BTW replace: " + replace);

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

//            System.out.println("count: " + c);
            m.appendTail(sb);
//            System.out.println("sb: " + sb);
            codeArea.replaceText(sb.toString());
        }
    }
}
