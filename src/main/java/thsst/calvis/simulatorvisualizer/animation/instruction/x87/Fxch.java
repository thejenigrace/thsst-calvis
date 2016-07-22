package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Fxch extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
//        tab.setContent(root);
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
//            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        String value1;
        String value0 = finder.getRegister("ST0");
        if( tokens.length > 0 ) {
            value1 = finder.getRegister(tokens[0].getValue());
        }
        else {
            value1 = finder.getRegister("ST1");
        }


        // CODE HERE
        int width = 250;
        int height = 70;
        Token token = new Token(Token.REG, "ST0");
        Rectangle desRectangle = this.createRectangle(token, width, height);
        Rectangle srcRectangle;
        if( tokens.length > 0 ) {
            srcRectangle = this.createRectangle(tokens[0], width, height);
        }
        else {
            srcRectangle = this.createRectangle(token, width, height);
        }


        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(100);
            desRectangle.setY(100);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + 100);
            srcRectangle.setY(100);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            root.getChildren().addAll(desRectangle, srcRectangle);

            Text desLabelText = this.createLabelText(token);
            Text desValueText = new Text(value1);
            Text srcLabelText;
            if( tokens.length > 0 ) {
                srcLabelText = this.createLabelText(tokens[0]);
            }
            else {
                srcLabelText = new Text("ST1");
            }
            Text srcValueText = new Text(value0);

            desLabelText.setX(100);
            desLabelText.setY(100);

            desValueText.setX(100);
            desValueText.setY(100);

            srcLabelText.setX(100);
            srcLabelText.setY(100);

            srcValueText.setX(100);
            srcValueText.setY(100);

            Text labelFlags = new Text("Affected flags:\n" +
                    "C1 = 0\n" +
                    "C0, C2, C3 = undefined");
            labelFlags.setX(X);
            labelFlags.setY(Y + 85);

            root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText, labelFlags);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition(new Duration(1000), srcValueText);

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desTransition.setInterpolator(Interpolator.LINEAR);
            desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value moving
            srcTransition.setInterpolator(Interpolator.LINEAR);
            srcTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            srcTransition.toXProperty().bind(srcRectangle.translateXProperty()
                    .add(srcRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desTransition.play();
            srcTransition.play();
        }
    }
}

