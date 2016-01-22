package MainEditor;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import MainEditor.controller.WorkspaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class MainApp extends Application {

    public static Stage primaryStage;
//    private FXMLLoader loader;
//    private Parent root;
    // private EnvironmentBuilder configController;

    /**
     * Method implemented by extending the class to Application.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CALVIS Environment Configuration");
        initRootLayout();
    }

    /**
     * Initialize the root layout
     * @throws IOException
     */
    public void initRootLayout() throws IOException{
        loadPrimaryStageController();
    }

    public void loadPrimaryStageController() throws IOException {
        EnvironmentConfigurator ec = new EnvironmentConfigurator();
        ec.setMainApp(this);
        ec.begin();

        Parent root = ec.getParent();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

//        Give the controller access to the main app
//        configController = (EnvironmentBuilder)  loader.getController();
//        configController.setMainApp(this);

    }

    /**
     * Opens a stage to show CALVIS workspace.
     * @throws IOException
     */
    public void showWorkspace() throws IOException {
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MainAppEditor/workspace.fxml"));
        Parent workspaceLayout = (BorderPane) loader.load();

        WorkspaceController workspaceController = loader.getController();

        primaryStage.setScene(new Scene(workspaceLayout));
        primaryStage.setTitle("CALVIS x86-32 Workspace");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

       // workspaceController.setEngine(buildEnvironment());
        workspaceController.displayDefaultWindows();

    }

    /**
     * Hide the main stage.
     */
    public void hidePrimaryStage() {
        this.primaryStage.hide();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

//
//    public EnvironmentBuilder getStageController(){
//        return configController;
//    }

//    private SystemController buildEnvironment(){
//        EnvironmentConfigurator envCon = new
//                EnvironmentConfigurator(configController.getConfigurationFilePath());
//        SimulationEngine simEng = new
//                SimulationEngine(envCon.getRegisters(), envCon.getMemory());
//        SystemController ediCon = new
//                SystemController(envCon.getParser(), simEng);
//
//        return ediCon;
//    }

    /**
     * Main method to run the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
