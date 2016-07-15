package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

import java.util.Objects;

/**
 * Created by Jennica on 10/07/2016.
 */
public class Div extends CalvisAnimation {
    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( Token token : tokens ) {
            System.out.println(token + " : " + token.getClass());
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(Token.REG, width + 40, height);
        Rectangle dividendRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[0], width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            dividendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            dividendRectangle.setY(Y);
            dividendRectangle.setArcWidth(10);
            dividendRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth()
                    + dividendRectangle.getLayoutBounds().getWidth() + X * 2);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle multiplyCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + dividendRectangle.getLayoutBounds().getWidth() + 150,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, dividendRectangle, srcRectangle, equalCircle, multiplyCircle);

            int desSize = 0;
            if ( Objects.equals(tokens[0].getType(), Token.REG) ) {
                desSize = registers.getBitSize(tokens[0]);
            } else if ( Objects.equals(tokens[0].getType(), Token.MEM) )
                desSize = memory.getBitSize(tokens[0]);

            String flagsAffected = "Flags Affected: All flags are undefined after execution.";
            Text detailsText = new Text(X, Y * 2, flagsAffected);
            Text desLabelText, desValueText;
            Text dividendLabelText, dividendValueText;
            Text srcLabelText = this.createLabelText(X, Y, tokens[0]);
            Text srcValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);

            String value;
            switch ( desSize ) {
                case 8:
                    desLabelText = new Text(X, Y, "AX");
                    value = "0x" + registers.get("AX");
                    desValueText = new Text(X, Y, value);
                    dividendLabelText = new Text(X, Y, "AL");
                    value = "0x" + this.finder.getRegister("AL");
                    dividendValueText = new Text(X, Y, value);
                    break;
                case 16:
                    desLabelText = new Text(X, Y, "DX, AX");
                    value = "0x" + registers.get("DX") + ", 0x" + registers.get("AX");
                    desValueText = new Text(X, Y, value);
                    dividendLabelText = new Text(X, Y, "AX");
                    value = "0x" + this.finder.getRegister("AX");
                    dividendValueText = new Text(X, Y, value);
                    break;
                case 32:
                    desLabelText = new Text(X, Y, "EDX, EAX");
                    value = "0x" + registers.get("EDX") + ", 0x" + registers.get("EAX");
                    desValueText = new Text(X, Y, value);
                    dividendLabelText = new Text(X, Y, "EAX");
                    value = "0x" + this.finder.getRegister("EAX");
                    dividendValueText = new Text(X, Y, value);
                    break;
                default:
                    desLabelText = new Text();
                    desValueText = new Text();
                    dividendLabelText = new Text();
                    dividendValueText = new Text();
            }

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            Text divideText = new Text(X, Y, "/");
            divideText.setFont(Font.font(48));
            divideText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, divideText, desLabelText, desValueText,
                    dividendLabelText, dividendValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();
            TranslateTransition dividendLabelTransition = new TranslateTransition();
            TranslateTransition dividendValueTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
            TranslateTransition divideTransition = new TranslateTransition();

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
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Equal sign label static
            equalTransition.setNode(equalText);
            equalTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110 + srcRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            equalTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            equalTransition.toXProperty().bind(equalTransition.fromXProperty());
            equalTransition.toYProperty().bind(equalTransition.fromYProperty());

            // Dividend label static
            dividendLabelTransition.setNode(dividendLabelText);
            dividendLabelTransition.fromXProperty().bind(dividendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((dividendRectangle.getLayoutBounds().getWidth() - dividendLabelText.getLayoutBounds().getWidth()) / 2));
            dividendLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            dividendLabelTransition.toXProperty().bind(dividendLabelTransition.fromXProperty());
            dividendLabelTransition.toYProperty().bind(dividendLabelTransition.fromYProperty());

            // Dividend value static
            dividendValueTransition.setNode(dividendValueText);
            dividendValueTransition.fromXProperty().bind(dividendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((dividendRectangle.getLayoutBounds().getWidth() - dividendValueText.getLayoutBounds().getWidth()) / 2));
            dividendValueTransition.fromYProperty().bind(dividendRectangle.translateYProperty()
                    .add(dividendRectangle.getLayoutBounds().getHeight() / 1.5));
            dividendValueTransition.toXProperty().bind(dividendValueTransition.fromXProperty());
            dividendValueTransition.toYProperty().bind(dividendValueTransition.fromYProperty());

            // Divide sign label static
            divideTransition.setNode(divideText);
            divideTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + dividendRectangle.getLayoutBounds().getWidth() + 40));
            divideTransition.fromYProperty().bind(equalTransition.fromYProperty());
            divideTransition.toXProperty().bind(divideTransition.fromXProperty());
            divideTransition.toYProperty().bind(divideTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + dividendRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + dividendRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desTransition.play();
            equalTransition.play();
            dividendLabelTransition.play();
            dividendValueTransition.play();
            divideTransition.play();
            srcLabelTransition.play();
            srcTransition.play();
        }
    }
}
