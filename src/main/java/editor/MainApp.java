package editor;

import configuration.controller.ConfigurationEnvironmentController;
import configuration.controller.ConfiguratorEnvironment;
import editor.controller.WorkspaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.math.BigInteger;


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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        String myString = "BF800000";
        int a = 0;
        Long i = Long.parseLong("BFC00000", 16);
        Float f = Float.intBitsToFloat(i.intValue());
        System.out.println(f);
//        System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_65");
        //System.out.println(new BigInteger("5", 16).subtract(new BigInteger("22", 16)).toString(16));
        System.out.println(new BigInteger("FF", 16).multiply(new BigInteger("F", 16)).toString(16));
        String OS = System.getProperty("os.name").toLowerCase();
        if( OS.contains("win")){
            System.out.println("windows");
            System.out.println("C:\\Program Files\\Java\\jdk" + System.getProperty("java.version"));
            System.setProperty("java.home", "C:\\Program Files\\Java\\jdk" + System.getProperty("java.version"));
        }   
        else if (OS.contains("mac")){
            System.setProperty("java.home", "/Library/Java/JavaVirtualMachines/jdk" + System.getProperty("java.version") +".jdk/Contents/Home");
        }///jdk1.8.0_66.jdk/Contents/Home/jre
        String x = "FFFFEEEEHHHHGGGG";
        System.out.println(x.substring(x.length() - 1));
//        LauncherImpl.launchApplication(MainApp.class, PreloaderApp.class, args);
        launch(args);
    }

}
