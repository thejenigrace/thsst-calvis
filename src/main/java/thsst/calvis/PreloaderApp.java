package thsst.calvis;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by Jennica on 05/03/2016.
 */
public class PreloaderApp extends Preloader {

    private Stage primaryStage;
    private boolean isEmbedded = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // embedded stage has preset size
        this.isEmbedded = (primaryStage.getWidth() > 0);

        this.primaryStage = primaryStage;
        Pane pane = new Pane();

        ImageView imageView = new ImageView();
        Image image = new Image(MainApp.class.getResourceAsStream("/img/splash_screen.png"));
        imageView.setFitWidth(600);
        imageView.setFitHeight(300);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);

        pane.getChildren().add(imageView);

        this.primaryStage.setScene(new Scene(pane));
        this.primaryStage.initStyle(StageStyle.UTILITY);
        this.primaryStage.setWidth(600);
        this.primaryStage.setHeight(320);
        this.primaryStage.centerOnScreen();
        this.primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            if (isEmbedded && primaryStage.isShowing()) {
                // fade out, hide stage at the end of animation
                FadeTransition ft = new FadeTransition(Duration.millis(3000), primaryStage.getScene().getRoot());
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                final Stage s = primaryStage;
                EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        s.hide();
                    }
                };
                ft.setOnFinished(eh);
                ft.play();
            } else {
                primaryStage.hide();
            }
        }
    }
}
