package editor;

import configuration.controller.ConfigurationEnvironmentController;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Jennica on 01/03/2016.
 */
public class PreloaderApp extends Preloader {

    private Stage preloaderStage;
    private FXMLLoader loader;
    private Parent root;
    private ConfigurationEnvironmentController configController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        this.preloaderStage.setTitle("CALVIS Configuration");

        initRootLayout();

//        VBox loading = new VBox(20);
//        loading.setMaxWidth(Region.USE_PREF_SIZE);
//        loading.setMaxHeight(Region.USE_PREF_SIZE);
//        loading.getChildren().add(new ProgressBar());
//        loading.getChildren().add(new Label("Please wait..."));
//
//        BorderPane root = new BorderPane(loading);
//        Scene scene = new Scene(root);
//
//        primaryStage.setWidth(800);
//        primaryStage.setHeight(600);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    /**
     * Initialize the root layout
     */
    public void initRootLayout() {
        try {
            loadPrimaryStageController();
            preloaderStage.setScene(new Scene(root));
            preloaderStage.setResizable(false);
            preloaderStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPrimaryStageController() {
        try {
            // Load root layout from fxml file
            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/configurationEnvironment.fxml"));
            root = (BorderPane) loader.load();

            // Give the controller access to the main app
            configController = loader.getController();
//            configController.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
//        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
//            preloaderStage.hide();
//        }
    }

    public void hidePreloaderStage() {
        this.preloaderStage.hide();
    }
}
