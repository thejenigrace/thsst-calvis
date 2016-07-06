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

        Rectangle desRectangle = new Rectangle(120, 60, Color.web("#FCBD6D", 1.0));
        Rectangle srcRectangle = new Rectangle(120, 60, Color.web("#FCBD6D", 1.0));
//        Rectangle srcRectangle = new Rectangle(120, 60, Color.web("#79CFCE", 1.0));

        desRectangle.setX(110);
        desRectangle.setY(100);
        desRectangle.setArcWidth(10);
        desRectangle.setArcHeight(10);

        srcRectangle.setX(desRectangle.xProperty().getValue() +
                desRectangle.widthProperty().getValue() + 100);
        srcRectangle.setY(100);
        srcRectangle.setArcWidth(10);
        srcRectangle.setArcHeight(10);

        root.getChildren().addAll(desRectangle, srcRectangle);

        String desLabel = tokens[0].getValue();
        String srcLabel = tokens[1].getValue();

        Text desLabelText = new Text(desLabel + ": ");
        Text desValueText = new Text(registers.get(desLabel));
        Text srcValueText = new Text(srcLabel + ": " + registers.get(srcLabel));

        desLabelText.setX(100);
        desLabelText.setY(100);

        desValueText.setX(100);
        desValueText.setY(100);

        srcValueText.setX(100);
        srcValueText.setY(100);

        root.getChildren().addAll(desLabelText, desValueText, srcValueText);

        // ANIMATION LOGIC
        TranslateTransition desLabelTransition = new TranslateTransition();
        TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
        TranslateTransition srcTransition = new TranslateTransition();

        // label not moving
        desLabelTransition.setNode(desLabelText);
        desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty().add(20));
        desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 2));
        desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
        desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

        // values moving
        desTransition.setInterpolator(Interpolator.LINEAR);
        desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                .add(srcRectangle.getLayoutBounds().getWidth() + 120));
        desTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
        desTransition.toXProperty().bind(desRectangle.translateXProperty().add(50));
        desTransition.toYProperty().bind(desTransition.fromYProperty());

        // label & value not moving
        srcTransition.setNode(srcValueText);
        srcTransition.fromXProperty().bind(desTransition.fromXProperty());
        srcTransition.fromYProperty().bind(desTransition.fromYProperty());
        srcTransition.toXProperty().bind(srcTransition.fromXProperty());
        srcTransition.toYProperty().bind(srcTransition.fromYProperty());

        // Play 1000 milliseconds of animation
        desLabelTransition.play();
        desTransition.play();
        srcTransition.play();
    }

    private void formatDisplayValue(Token[] tokens) {
        // Check
    }

}

//      timeline.getKeyFrames().addAll(
//                new KeyFrame(Duration.ZERO, // set start position at 0
//                        new KeyValue(srcRectangle.translateXProperty(), 300),
//                        new KeyValue(srcRectangle.translateYProperty(), 100)
//                ),
//                new KeyFrame(new Duration(1000), // set end position at 3s (3000 milliseconds)
//                        new KeyValue(srcRectangle.translateXProperty(), 100),
//                        new KeyValue(srcRectangle.translateYProperty(), 100)
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
