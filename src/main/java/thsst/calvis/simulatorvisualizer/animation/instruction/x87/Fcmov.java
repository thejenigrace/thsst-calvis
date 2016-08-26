package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.*;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Jennica on 22/07/2016.
 */
public class Fcmov extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();
        Calculator calculator = new Calculator(registers, memory);

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[1], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[2], width, height);

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

            int desBitSize = registers.getBitSize(tokens[1]);

            String flagsAffected = getConditionReminder(tokens[0].getValue());
            Text detailsText = new Text(X, Y * 2, flagsAffected);
            Text desLabelText = this.createLabelText(X, Y, tokens[1]);
            String desValueString = this.getValueString(tokens[1], registers, memory, desBitSize);
            Text desValueText = new Text(X, Y, desValueString);
            Text srcLabelText = this.createLabelText(X, Y, tokens[2]);
            String srcValueString = this.getValueString(tokens[2], registers, memory, desBitSize);
            Text srcValueText = new Text(X, Y, srcValueString);

            root.getChildren().addAll(detailsText, desLabelText, desValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desValueTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcValueTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            if ( calculator.evaluateCondition(tokens[0].getValue()) ) {
                desValueTransition.setInterpolator(Interpolator.LINEAR);
                desValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                        .add(desRectangle.getLayoutBounds().getWidth() + X)
                        .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                        .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
                desValueTransition.toXProperty().bind(desRectangle.translateXProperty()
                        .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());
            }
            // Destination value static
            else {
                desValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                        .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                        .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
                desValueTransition.toXProperty().bind(desValueTransition.fromXProperty());
                desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());
            }

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcValueTransition.setNode(srcValueText);
            srcValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcValueTransition.fromYProperty().bind(desValueTransition.fromYProperty());
            srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
            srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desValueTransition.play();
            srcValueTransition.play();
        }
    }

    public String getConditionReminder(String condition) {
        switch ( condition.toUpperCase() ) {
            case "B": return "Check if: CF = 1";
            case "NB": return "Check if: CF = 0";
            case "E": return "Check if: ZF = 1";
            case "NE": return "Check if: ZF = 0";
            case "BE": return "Check if: CF = 1 or ZF = 1";
            case "NBE": return "Check if: CF = 0 and ZF = 0";
            case "U": return "Check if: PF = 1";
            case "NU": return "Check if: PF = 0";
            default:
                return "Condition not found";
        }
    }
}
