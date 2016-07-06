package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
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
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // CODE HERE
        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        Rectangle destination = new Rectangle(120, 60, Color.AQUA);
        Rectangle source = new Rectangle(120, 60, Color.FUCHSIA);

        destination.setX(110);
        destination.setY(100);
        destination.setArcWidth(10);
        destination.setArcHeight(10);

        source.setX(destination.xProperty().getValue() +
                destination.widthProperty().getValue() + 100);
        source.setY(100);
        source.setArcWidth(10);
        source.setArcHeight(10);

        root.getChildren().addAll(destination, source);

        String destinationValue = tokens[0].getValue();
        String sourceValue = tokens[1].getValue();
        Text destinationText = new Text(destinationValue + " : " + registers.get(destinationValue));
        Text sourceText = new Text(sourceValue + " : " + registers.get(sourceValue));

        destinationText.setX(100);
        destinationText.setY(100);

        sourceText.setX(100);
        sourceText.setY(100);

        root.getChildren().addAll(destinationText, sourceText);

        // ANIMATION LOGIC
        TranslateTransition desTranslateTransition = new TranslateTransition(new Duration(1000), destinationText);
        TranslateTransition srcTranslateTransition = new TranslateTransition();

        desTranslateTransition.setInterpolator(Interpolator.LINEAR);
        desTranslateTransition.fromXProperty().bind(source.translateXProperty()
                .add(source.getLayoutBounds().getWidth() + 120));
        desTranslateTransition.fromYProperty().bind(source.translateYProperty()
                .add(source.getLayoutBounds().getHeight() / 2));
        desTranslateTransition.toXProperty().bind(destination.translateXProperty().add(20.0));
        desTranslateTransition.toYProperty().bind(desTranslateTransition.fromYProperty());

        srcTranslateTransition.setNode(sourceText);
        srcTranslateTransition.fromXProperty().bind(source.translateXProperty()
                .add(source.getLayoutBounds().getWidth() + 120));
        srcTranslateTransition.fromYProperty().bind(source.translateYProperty()
                .add(source.getLayoutBounds().getHeight() / 2));
        srcTranslateTransition.toXProperty().bind(srcTranslateTransition.fromXProperty());
        srcTranslateTransition.toYProperty().bind(srcTranslateTransition.fromYProperty());

        // Play 1000 milliseconds of animation
        desTranslateTransition.play();
        srcTranslateTransition.play();
    }

    private void formatDisplayValue(Token[] tokens) {
        // Check
    }

}

//      timeline.getKeyFrames().addAll(
//                new KeyFrame(Duration.ZERO, // set start position at 0
//                        new KeyValue(source.translateXProperty(), 300),
//                        new KeyValue(source.translateYProperty(), 100)
//                ),
//                new KeyFrame(new Duration(1000), // set end position at 3s (3000 milliseconds)
//                        new KeyValue(source.translateXProperty(), 100),
//                        new KeyValue(source.translateYProperty(), 100)
//                        // ADD NEW KEY VALUES IF NEEDED HERE
//                )
//        );
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
