package thsst.calvis.simulatorvisualizer.animation.instruction.sse2;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Comisd extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        int width = 300;
        int height = 70;
        Rectangle firstOperandRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle secondOperandRectangle = this.createRectangle(tokens[1], width, height);

        if ( firstOperandRectangle != null && secondOperandRectangle != null ) {
            firstOperandRectangle.setX(X);
            firstOperandRectangle.setY(Y);
            firstOperandRectangle.setArcWidth(10);
            firstOperandRectangle.setArcHeight(10);

            secondOperandRectangle.setX(firstOperandRectangle.xProperty().getValue() + firstOperandRectangle.getLayoutBounds().getWidth() + X);
            secondOperandRectangle.setY(Y);
            secondOperandRectangle.setArcWidth(10);
            secondOperandRectangle.setArcHeight(10);

            Circle minusCircle = new Circle(firstOperandRectangle.xProperty().getValue() +
                    firstOperandRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(firstOperandRectangle, secondOperandRectangle, minusCircle);

            int desBitSize = 64;

            String description = "Flags Affected <- S[63:0]   OPERAND   D[63:0]\n" +
                    "The result is discarded but the status flags are updated according to the results.\n";
            String flagsAffected = "Flags Affected: ZF, PF, CF";
            Text detailsText = new Text(X, Y * 2, description + flagsAffected);
            Text firstOperandLabelText = this.createLabelText(X, Y, tokens[0]);
            String firstOperandString = this.getSubLowerHexValueString(tokens[0], registers, memory, desBitSize, desBitSize/4);
            Text firstOperandValueText = new Text(X, Y, firstOperandString);
            Text secondOperandLabelText = this.createLabelText(X, Y, tokens[1]);
            String secondOperandString = this.getSubLowerHexValueString(tokens[1], registers, memory, desBitSize, desBitSize/4);
            Text secondOperandValueText = new Text(X, Y, secondOperandString);

            Text operandText = new Text(X, Y, getOperandString(registers.getEFlags().getZeroFlag(),
                    registers.getEFlags().getParityFlag(), registers.getEFlags().getCarryFlag()));
            operandText.setFont(Font.font(48));
            operandText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, secondOperandLabelText, secondOperandValueText,
                    firstOperandLabelText, firstOperandValueText, operandText);

            // ANIMATION LOGIC
            TranslateTransition firstOperandLabelTransition = new TranslateTransition();
            TranslateTransition firstOperandTextTransition = new TranslateTransition(new Duration(1000), firstOperandValueText);
            TranslateTransition secondOperandLabelTransition = new TranslateTransition();
            TranslateTransition secondOperandValueTransition = new TranslateTransition();
            TranslateTransition operandTransition = new TranslateTransition();

            // FIRST OPERAND LABEL -- STATIC
            firstOperandLabelTransition.setNode(firstOperandLabelText);
            firstOperandLabelTransition.fromXProperty().bind(firstOperandRectangle.translateXProperty()
                    .add((firstOperandRectangle.getLayoutBounds().getWidth()
                            - firstOperandLabelText.getLayoutBounds().getWidth()) / 2));
            firstOperandLabelTransition.fromYProperty().bind(firstOperandRectangle.translateYProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getHeight() / 3));
            firstOperandLabelTransition.toXProperty().bind(firstOperandLabelTransition.fromXProperty());
            firstOperandLabelTransition.toYProperty().bind(firstOperandLabelTransition.fromYProperty());

            // FIRST OPERAND VALUE -- STATIC
            firstOperandTextTransition.fromXProperty().bind(firstOperandRectangle.translateXProperty()
                    .add((firstOperandRectangle.getLayoutBounds().getWidth()
                            - firstOperandValueText.getLayoutBounds().getWidth()) / 2));
            firstOperandTextTransition.fromYProperty().bind(firstOperandRectangle.translateYProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getHeight() / 1.5));
            firstOperandTextTransition.toXProperty().bind(firstOperandTextTransition.fromXProperty());
            firstOperandTextTransition.toYProperty().bind(firstOperandTextTransition.fromYProperty());

            // OPERAND -- STATIC
            operandTransition.setNode(operandText);
            operandTransition.fromXProperty().bind(firstOperandRectangle.translateXProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getWidth() + 70
                            + firstOperandRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            operandTransition.fromYProperty().bind(firstOperandRectangle.translateYProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getHeight() / 1.5));
            operandTransition.toXProperty().bind(operandTransition.fromXProperty());
            operandTransition.toYProperty().bind(operandTransition.fromYProperty());

            // SECOND OPERAND LABEL -- STATIC
            secondOperandLabelTransition.setNode(secondOperandLabelText);
            secondOperandLabelTransition.fromXProperty().bind(firstOperandRectangle.translateXProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getWidth() + X)
                    .add((firstOperandRectangle.getLayoutBounds().getWidth()
                            - secondOperandLabelText.getLayoutBounds().getWidth()) / 2));
            secondOperandLabelTransition.fromYProperty().bind(firstOperandLabelTransition.fromYProperty());
            secondOperandLabelTransition.toXProperty().bind(secondOperandLabelTransition.fromXProperty());
            secondOperandLabelTransition.toYProperty().bind(secondOperandLabelTransition.fromYProperty());

            // SECOND OPERAND VALUE -- STATIC
            secondOperandValueTransition.setNode(secondOperandValueText);
            secondOperandValueTransition.fromXProperty().bind(firstOperandRectangle.translateXProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getWidth() + X)
                    .add((firstOperandRectangle.getLayoutBounds().getWidth()
                            - secondOperandValueText.getLayoutBounds().getWidth()) / 2));
            secondOperandValueTransition.fromYProperty().bind(firstOperandRectangle.translateYProperty()
                    .add(firstOperandRectangle.getLayoutBounds().getHeight() / 1.5));
            secondOperandValueTransition.toXProperty().bind(secondOperandValueTransition.fromXProperty());
            secondOperandValueTransition.toYProperty().bind(secondOperandValueTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            firstOperandLabelTransition.play();
            firstOperandTextTransition.play();
            secondOperandLabelTransition.play();
            secondOperandValueTransition.play();
            operandTransition.play();
        }
    }

    private String getOperandString(String zeroFlag, String parityFlag, String carryFlag) {
        if ( zeroFlag.equals("1") && parityFlag.equals("1") && carryFlag.equals("1") ) {
            return "unordered";
        } else if ( zeroFlag.equals("1") && parityFlag.equals("0") && carryFlag.equals("0") ) {
            return "=";
        } else if ( zeroFlag.equals("0") && parityFlag.equals("0") && carryFlag.equals("1") ) {
            return "<";
        } else if ( zeroFlag.equals("0") && parityFlag.equals("0") && carryFlag.equals("0") ) {
            return ">";
        }
        return "";
    }
}

