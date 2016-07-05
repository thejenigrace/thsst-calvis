package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Mov extends CalvisAnimation {

    @Override
    public void animate(Tab tab) {
        root.getChildren().clear();
        tab.setContent(root);
        timeline = new Timeline();

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // CODE HERE

        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
             System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        Rectangle destination = new Rectangle(100, 40, Color.AQUA);
        Rectangle source = new Rectangle(100, 40, Color.FUCHSIA);

        destination.setY(100);
        destination.setX(100);

        root.getChildren().add(destination);
        root.getChildren().add(source);

        String destinationValue = tokens[0].getValue();
        Text destinationText = new Text(destinationValue + " : " + registers.get(destinationValue));
        destinationText.setX(100);
        destinationText.setY(100);

        root.getChildren().add(destinationText);

        // ANIMATION LOGIC
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, // set start position at 0
                        new KeyValue(source.translateXProperty(), 300),
                        new KeyValue(source.translateYProperty(), 100)
                ),
                new KeyFrame(new Duration(1000), // set end position at 3s (3000 milliseconds)
                        new KeyValue(source.translateXProperty(), 100),
                        new KeyValue(source.translateYProperty(), 100)
                        // ADD NEW KEY VALUES IF NEEDED HERE
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
}
