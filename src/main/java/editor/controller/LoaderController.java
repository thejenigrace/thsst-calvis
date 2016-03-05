package editor.controller;

import editor.MainApp;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jennica on 05/03/2016.
 */
public class LoaderController implements Initializable {

    @FXML
    private ImageView imageViewCalvis;

    @FXML
    private ProgressBar progressBarWorkspace;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(MainApp.class.getResourceAsStream("/img/splash_screen.png"));
            this.imageViewCalvis.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProgressBarWorkspaceProgressProperty(ReadOnlyDoubleProperty progressProperty) {
        this.progressBarWorkspace.progressProperty().bind(progressProperty);
    }
}
