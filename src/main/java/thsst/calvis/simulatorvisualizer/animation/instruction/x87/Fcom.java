package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

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
 * Created by Jennica on 23/07/2016.
 */
public class Fcom extends CalvisAnimation {

    private int type;

    public Fcom(int type) {
        super();
        this.type = type;
    }

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
        Rectangle firstOperandRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle secondOperandRectangle;
        if(tokens.length > 0)
            secondOperandRectangle = this.createRectangle(tokens[0].getType(), width, height);
        else
            secondOperandRectangle = this.createRectangle(Token.REG, width, height);

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

            this.root.getChildren().addAll(firstOperandRectangle, secondOperandRectangle, minusCircle);

            int desBitSize = registers.getBitSize("ST0");
            char C3 = registers.x87().status().getFlag("C3");
            char C2 = registers.x87().status().getFlag("C2");
            char C0 = registers.x87().status().getFlag("C0");

            String conditionCodeFormat = "Flags Affected: C3=" + C3 + "; C2=" + C2 + "; C0=" + C0 + "\n";
            Text detailsText = new Text(X, Y * 2, conditionCodeFormat + this.getDescriptionString());
            Text firstOperandLabelText = new Text(X, Y, "ST0");
            Text firstOperandValueText = new Text(X, Y, this.finder.getRegister("ST0"));
            Text secondOperandLabelText, secondOperandValueText;
            if (tokens.length > 0) {
                secondOperandLabelText = this.createLabelText(X, Y, tokens[0]);
                secondOperandValueText = this.createValueTextUsingFinderNotHex(X, Y, tokens[0], desBitSize);
            } else {
                secondOperandLabelText = new Text(X, Y, "ST1");
                secondOperandValueText = new Text(X, Y, this.finder.getRegister("ST1"));
            }

            Text operandText = new Text(X, Y, getOperandString(C3, C2, C0));
            operandText.setFont(Font.font(40));
            operandText.setFill(Color.WHITESMOKE);

            this.root.getChildren().addAll(detailsText, secondOperandLabelText, secondOperandValueText,
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

    private String getDescriptionString() {
        String flagsNote = "Flags not set if unmasked invalid-arithmetic-operand (#IA) exception is generated.";
        switch ( this.type ) {
            case 0: return flagsNote;
            case 1: return "Afterwards, the register stack is popped.\n" + flagsNote;
            case 2: return "Afterwards, the register stack is popped twice.\n" + flagsNote;
            default: return "";
        }
    }

    private String getOperandString(char C3, char C2, char C0) {
        String conditionCode = "" + C3 + C2 + C0;
        switch ( conditionCode ) {
            case "000":
                return ">";
            case "001":
                return "<";
            case "100":
                return "=";
            case "111":
                return "Unordered";
            default:
                return "";
        }
    }
}
