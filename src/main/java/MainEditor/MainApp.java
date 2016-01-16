package MainEditor;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import MainEditor.controller.ConfigurationController;
import MainEditor.controller.WorkspaceController;
import SimulatorVisualizer.controller.SimulationEngine;
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

    /**
     * Method implemented by extending the class to Application.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CALVIS Instruction Set Configuration");

        initRootLayout();
    }

    /**
     * Initialize the root layout
     * @throws IOException
     */
    public void initRootLayout() throws IOException {
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/configuration.fxml"));
        Parent root = (BorderPane) loader.load();

        // Give the controller access to the main app
        ConfigurationController configController = loader.getController();
        configController.setMainApp(this);

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Opens a stage to show CALVIS workspace.
     * @throws IOException
     */
    public void showWorkspace() throws IOException {
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/workspace.fxml"));
        Parent workspaceLayout = (BorderPane) loader.load();

        WorkspaceController workspaceController = loader.getController();

        primaryStage.setScene(new Scene(workspaceLayout));
        primaryStage.setTitle("CALVIS x86-32 Workspace");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
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

    /**
     * Main method to run the application.
     * @param args
     */
    public static void main(String[] args) {
        EnvironmentConfigurator envCon = new EnvironmentConfigurator();

        SimulationEngine simEng = new SimulationEngine(envCon.getRegisters(), envCon.getMemory());

//        EditorController ediCon = new EditorController(envCon.getParser(), simEng);

        launch(args);
    }
}
