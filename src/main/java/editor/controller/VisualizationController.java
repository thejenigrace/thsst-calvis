package editor.controller;

import configuration.model.engine.CalvisFormattedInstruction;
import editor.model.AssemblyComponent;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Jennica on 07/02/2016.
 */
public class VisualizationController extends AssemblyComponent {

    private static final double animationX = 500;
    private static final double animationY = 130;

    private Tab tab;
    private Group root;
    private int lineBefore;
    private CalvisFormattedInstruction currentInstruction;

    public VisualizationController() {
        this.lineBefore = 0;
        this.tab = new Tab();
        this.tab.setText("Visualization");
        this.root = new Group();
        this.tab.setContent(root);
    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void update(String currentLine, int lineNumber) {
        System.out.println(currentInstruction);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void build() {
//        Group circles = new Group();
//        for ( int i = 0; i < 30; i++ ) {
//            Circle circle = new Circle(30, Color.web("white", 0.05));
//            circle.setStrokeType(StrokeType.OUTSIDE);
//            circle.setStroke(Color.web("blue", 0.16));
//            circle.setStrokeWidth(4);
//            circles.getChildren().add(circle);
//        }

//        root.getChildren().add(circles);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.setAutoReverse(true);

        Rectangle destination = new Rectangle(100, 40, Color.AQUA);
        Rectangle source = new Rectangle(100, 40, Color.FUCHSIA);

        destination.setY(100);
        destination.setX(100);

        root.getChildren().add(destination);
        root.getChildren().add(source);

        Text destinationText = new Text("EAX: 0x1234ABCD");
        destinationText.setX(100);
        destinationText.setY(100);

        root.getChildren().add(destinationText);

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, // set start position at 0
                        new KeyValue(source.translateXProperty(), 300),
                        new KeyValue(source.translateYProperty(), 100)
                ),
                new KeyFrame(new Duration(3000), // set end position at 40s
                        new KeyValue(source.translateXProperty(), 100),
                        new KeyValue(source.translateYProperty(), 100)
                )
        );
//        for ( Node circle : circles.getChildren() ) {
//            timeline.getKeyFrames().addAll(
//                    new KeyFrame(Duration.ZERO, // set start position at 0
//                            new KeyValue(circle.translateXProperty(), random() * animationX),
//                            new KeyValue(circle.translateYProperty(), random() * animationY)
//                    ),
//                    new KeyFrame(new Duration(40000), // set end position at 40s
//                            new KeyValue(circle.translateXProperty(), random() * animationX),
//                            new KeyValue(circle.translateYProperty(), random() * animationY)
//                    )
//            );
//        }
        // play 40s of animation
        timeline.play();
    }

    public void attachCalvisInstruction(CalvisFormattedInstruction CalvisInstruction) {
        this.currentInstruction = CalvisInstruction;
    }

}
