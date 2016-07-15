package thsst.calvis;

import javafx.scene.control.Alert;
import thsst.calvis.configuration.controller.ConfigurationEnvironmentController;
import thsst.calvis.configuration.controller.ConfiguratorEnvironment;
import thsst.calvis.editor.controller.WorkspaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.text.MessageFormat;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class MainApp extends Application {

    public static Stage primaryStage;
    private FXMLLoader loader;
    private Parent root;
    private ConfigurationEnvironmentController configController;

    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * Method implemented by extending the class to Application
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            MainApp.primaryStage = primaryStage;
            MainApp.primaryStage.setTitle("CALVIS Configuration");

            initRootLayout();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the root layout
     */
    public void initRootLayout() {
        try {
            loadConfigurationEnvironmentScene();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a stage to show CALVIS workspace
     *
     * @param configuratorEnvironment
     */
    public static void showWorkspace(ConfiguratorEnvironment configuratorEnvironment) {
        try {
            // Load root layout from fxml file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("/fxml/workspace.fxml"));
            Parent workspaceLayout = (BorderPane) fxmlLoader.load();

            primaryStage.setScene(new Scene(workspaceLayout));
            primaryStage.setTitle("CALVIS Workspace");
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);
            primaryStage.show();

            WorkspaceController workspaceController = fxmlLoader.getController();
            ConfiguratorEnvironment environment = configuratorEnvironment;
            workspaceController.buildSystem(environment);
            workspaceController.displayDefaultWindows();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the main stage.
     */
    public static void hidePrimaryStage() {
        MainApp.primaryStage.hide();
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return MainApp.primaryStage;
    }

    public void loadConfigurationEnvironmentScene() {
        try {
            // Load root layout from fxml file
            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/configuratio_environment.fxml"));
            root = (BorderPane) loader.load();

            // Give the controller access to the main app
            configController = loader.getController();
            configController.setMainApp(this);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static Alert createAlert(Alert.AlertType alertType, String title, String contentTextFormat,
                                    Object... contentTextArgs) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(MessageFormat.format(contentTextFormat, contentTextArgs));
        alert.initOwner(primaryStage);
        return alert;
    }

    /**
     * Main method to run the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        String OS = System.getProperty("os.name").toLowerCase();
        if ( OS.contains("win") ) {
//            System.out.println("windows");
//            System.out.println("C:\\Program Files\\Java\\jdk" + System.getProperty("java.version"));
            System.setProperty("java.home", "C:\\Program Files\\Java\\jdk" + System.getProperty("java.version"));
        } else if ( OS.contains("mac") ) {
            System.setProperty("java.home", "/Library/Java/JavaVirtualMachines/jdk" + System.getProperty("java.version") + ".jdk/Contents/Home/");
        }

        try {
            launch(args);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
