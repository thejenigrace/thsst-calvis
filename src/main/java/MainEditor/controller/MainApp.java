package MainEditor.controller;

import EnvironmentConfiguration.controller.ConfigurationEnvironmentController;
import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class MainApp extends Application {

    public static Stage primaryStage;
    private FXMLLoader loader;
    private Parent root;
    private ConfigurationEnvironmentController configController;
    
    /**
     * Method implemented by extending the class to Application
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        
        try {
            MainApp.primaryStage = primaryStage;
            MainApp.primaryStage.setTitle("CALVIS Instruction Set Configuration");

            initRootLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the root layout
     */
    public void initRootLayout() {
        try {
            loadPrimaryStageController();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a stage to show CALVIS workspace
     *
     * @param environmentConfigurator
     */
    public void showWorkspace(EnvironmentConfigurator environmentConfigurator) {
        try {
            // Load root layout from fxml file
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("/fxml/workspace.fxml"));
            Parent workspaceLayout = (BorderPane) fxmlLoader.load();

            primaryStage.setScene(new Scene(workspaceLayout));
            primaryStage.setTitle("CALVIS x86-32 Workspace");
//            primaryStage.setMaximized(true);
//            primaryStage.setResizable(true);
            primaryStage.show();

            WorkspaceController workspaceController = fxmlLoader.getController();
            EnvironmentConfigurator environment = environmentConfigurator;
            workspaceController.buildSystem(environment);
            workspaceController.displayDefaultWindows();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the main stage.
     */
    public void hidePrimaryStage() {
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

    public void loadPrimaryStageController() {
        try {
            // Load root layout from fxml file
            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/configurationEnvironment.fxml"));
            root = (BorderPane) loader.load();

            // Give the controller access to the main app
            configController = loader.getController();
            configController.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to run the application.
     *
     * @param args
     */
    public static void main(String[] args) {
//        System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_65");
    	String OS = System.getProperty("os.name").toLowerCase();
        if( OS.contains("win")){
            System.out.println("windows");
            System.setProperty("java.home", "C:\\Program Files\\Java\\jdk" + System.getProperty("java.version"));
        }   
        else if (OS.contains("mac")){
            System.setProperty("java.home", "/Library/Java/JavaVirtualMachines/jdk" + System.getProperty("java.version") +".jdk/Contents/Home");
        }///jdk1.8.0_66.jdk/Contents/Home/jre
        launch(args);
    }

}
