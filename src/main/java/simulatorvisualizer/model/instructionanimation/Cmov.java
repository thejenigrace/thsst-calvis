package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.*;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Cmov extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
//        tab.setContent(root);
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();
        Calculator calculator = new Calculator(registers, memory);

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
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

            int desSize = registers.getBitSize(tokens[1]);

            String flagsAffected = getConditionReminder(tokens[0].getValue(), registers);
            Text detailsText = new Text(X, Y * 2, flagsAffected);
            Text desLabelText = this.createLabelText(X, Y, tokens[1]);
            Text desValueText = this.createValueText(X, Y, tokens[1], registers, memory, desSize);
            Text srcLabelText = this.createLabelText(X, Y, tokens[2]);
            Text srcValueText = this.createValueText(X, Y, tokens[2], registers, memory, desSize);

            root.getChildren().addAll(detailsText, desLabelText, desValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();

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
                desTransition.setInterpolator(Interpolator.LINEAR);
                desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                        .add(desRectangle.getLayoutBounds().getWidth() + X)
                        .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                        .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
                desTransition.toXProperty().bind(desRectangle.translateXProperty()
                        .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desTransition.toYProperty().bind(desTransition.fromYProperty());
            }
            // Destination value static
            else {
                desTransition.fromXProperty().bind(desRectangle.translateXProperty()
                        .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                        .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
                desTransition.toXProperty().bind(desTransition.fromXProperty());
                desTransition.toYProperty().bind(desTransition.fromYProperty());
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
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desTransition.play();
            srcTransition.play();
        }
    }

    public String getConditionReminder(String condition, RegisterList registers) {
        String con = condition.toUpperCase();
        EFlags flags = registers.getEFlags();
        String CF = flags.getCarryFlag();
        String ZF = flags.getZeroFlag();
        String OF = flags.getOverflowFlag();
        //String AF = flags.getAuxiliaryFlag();
        String PF = flags.getParityFlag();
        String SF = flags.getSignFlag();

        switch ( con ) {
            case "A":
            case "NBE": // fall through
                return "Check if: CF = 0 or ZF = 0";
            case "AE":
            case "NB": // fall through
                return "Check if: CF = 0";
            case "B":
            case "NAE": // fall through
                return "CF = 1";
            case "BE":
            case "NA": // fall through
                return "Check if: CF = 1 or ZF = 1";
            case "G":
            case "NLE": // fall through
                return "Check if: SF == OF or ZF = 0";
            case "GE":
            case "NL": // fall through
                return "Check if: SF == OF";
            case "L":
            case "NGE": // fall through
                return "Check if: SF != OF";
            case "LE":
            case "NG": // fall through
                return "Check if: SF != OF or ZF = 1";
            case "E":
            case "Z": // fall through
                return "Check if: ZF = 1";
            case "NE":
            case "NZ": // fall through
                return "Check if: ZF = 0";
            case "P":
            case "PE": // fall through
                return "Check if: PF = 1";
            case "NP":
            case "PO": // fall through
                return "Check if: PF = 0";
            case "O":
                return "Check if: OF = 1";
            case "NO":
                return "Check if: OF = 0";
            case "C":
                return "Check if: CF = 1";
            case "NC":
                return "Check if: CF = 0";
            case "S":
                return "Check if: SF = 1";
            case "NS":
                return "Check if: SF = 0";
            default:
                return "Condition not found";
        }
    }
}

