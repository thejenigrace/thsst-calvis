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
public class Fxam extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(this.root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        int width = 160;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle srcRectangle = this.createRectangle(Token.STRING, width, height);

        desRectangle.setX(X);
        desRectangle.setY(Y);
        desRectangle.setArcWidth(10);
        desRectangle.setArcHeight(10);

        srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
        srcRectangle.setY(Y);
        srcRectangle.setArcWidth(10);
        srcRectangle.setArcHeight(10);

        Circle operandCircle = new Circle(desRectangle.xProperty().getValue() +
                desRectangle.getLayoutBounds().getWidth() + 50,
                135, 35, Color.web("#798788", 1.0));

        this.root.getChildren().addAll(desRectangle, srcRectangle, operandCircle);

        String description = "The C1 flag is set to the sign of the value in ST0, regardless of whether " +
                "the register is empty or full";
        Text detailsText = new Text(X, Y * 2, description);
        Text desLabelText = new Text(X, Y, "ST0");
        Text desValueText = new Text(X, Y, registers.get("ST0"));
        Text srcLabelText = new Text(X, Y, "CLASS");
        char C3 = registers.x87().status().getFlag("C3");
        char C2 = registers.x87().status().getFlag("C2");
        char C0 = registers.x87().status().getFlag("C0");
        Text srcValueText = new Text(X, Y, this.getConditionCodeClass(C3, C2, C0));

        Text operandText = new Text(X, Y, "->");
        operandText.setFont(Font.font(48));
        operandText.setFill(Color.WHITESMOKE);

        this.root.getChildren().addAll(detailsText, srcLabelText, srcValueText,
                desLabelText, desValueText, operandText);

        // ANIMATION LOGIC
        TranslateTransition desLabelTransition = new TranslateTransition();
        TranslateTransition desValueTransition = new TranslateTransition(new Duration(1000), desValueText);
        TranslateTransition srcLabelTransition = new TranslateTransition();
        TranslateTransition srcValueTransition = new TranslateTransition();
        TranslateTransition operandTransition = new TranslateTransition();

        // DESTINATION LABEL -- STATIC
        desLabelTransition.setNode(desLabelText);
        desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
        desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 3));
        desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
        desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

        // DESTINATION VALUE -- STATIC
        desValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
        desValueTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
        desValueTransition.toXProperty().bind(desValueTransition.fromXProperty());
        desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

        // OPERAND LABEL STATIC
        operandTransition.setNode(operandText);
        operandTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add(desRectangle.getLayoutBounds().getWidth() + 50 + srcRectangle.getLayoutBounds().getWidth())
                .divide(2));
        operandTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
        operandTransition.toXProperty().bind(operandTransition.fromXProperty());
        operandTransition.toYProperty().bind(operandTransition.fromYProperty());

        // CLASS LABEL -- STATIC
        srcLabelTransition.setNode(srcLabelText);
        srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                .add(desRectangle.getLayoutBounds().getWidth() + X)
                .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
        srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
        srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
        srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

        // CLASS VALUE -- STATIC
        srcValueTransition.setNode(srcValueText);
        srcValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                .add(desRectangle.getLayoutBounds().getWidth() + X)
                .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
        srcValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
        srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
        srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

        // Play 1000 milliseconds of animation
        desLabelTransition.play();
        desValueTransition.play();
        srcLabelTransition.play();
        srcValueTransition.play();
        operandTransition.play();
    }

    private String getConditionCodeClass(char C3, char C2, char C0) {
        String conditionCodeFormat = "C3=" + C3 + "; C2=" + C2 + "; C0=" + C0;
        String conditionCode = "" + C3 + C2 + C0;
         switch ( conditionCode ) {
             case "000": return "Unsupported\n" + conditionCodeFormat;
             case "001": return "NaN\n" + conditionCodeFormat;
             case "010": return "Normal Finite Number\n" + conditionCodeFormat;
             case "011": return "Infinity\n" + conditionCodeFormat ;
             case "100": return "Zero\n"+ conditionCodeFormat;
             case "101": return "Empty\n" + conditionCodeFormat;
             case "110": return "Denormal Number\n" + conditionCodeFormat;
             default: return "";
         }
    }
}
