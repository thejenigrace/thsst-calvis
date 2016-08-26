package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

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
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Mul extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( Token token : tokens ) {
            
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(Token.REG, width + 40, height);
        Rectangle multiplierRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[0], width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            multiplierRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            multiplierRectangle.setY(Y);
            multiplierRectangle.setArcWidth(10);
            multiplierRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth()
                    + multiplierRectangle.getLayoutBounds().getWidth() + X * 2);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle multiplyCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + multiplierRectangle.getLayoutBounds().getWidth() + 150,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, multiplierRectangle, srcRectangle, equalCircle, multiplyCircle);

            int desSize = 0;
            if ( Objects.equals(tokens[0].getType(), Token.REG) ) {
                desSize = registers.getBitSize(tokens[0]);
            } else if ( Objects.equals(tokens[0].getType(), Token.MEM) )
                desSize = memory.getBitSize(tokens[0]);

            String flagsAffected = "Flags Affected: CF, OF";
            Text detailsText = new Text(X, Y * 2, flagsAffected);
            Text desLabelText, desValueText;
            Text multiplierLabelText, multiplierValueText;
            Text srcLabelText = this.createLabelText(X, Y, tokens[0]);
            Text srcValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);

            String value;
            switch ( desSize ) {
                case 8:
                    desLabelText = new Text(X, Y, "AX");
                    value = "0x" + registers.get("AX");
                    desValueText = new Text(X, Y, value);
                    multiplierLabelText = new Text(X, Y, "AL");
                    value = "0x" + this.finder.getRegister("AL");
                    multiplierValueText = new Text(X, Y, value);
                    break;
                case 16:
                    desLabelText = new Text(X, Y, "DX, AX");
                    value = "0x" + registers.get("DX") + ", 0x" + registers.get("AX");
                    desValueText = new Text(X, Y, value);
                    multiplierLabelText = new Text(X, Y, "AX");
                    value = "0x" + this.finder.getRegister("AX");
                    multiplierValueText = new Text(X, Y, value);
                    break;
                case 32:
                    desLabelText = new Text(X, Y, "EDX, EAX");
                    value = "0x" + registers.get("EDX") + ", 0x" + registers.get("EAX");
                    desValueText = new Text(X, Y, value);
                    multiplierLabelText = new Text(X, Y, "EAX");
                    value = "0x" + this.finder.getRegister("EAX");
                    multiplierValueText = new Text(X, Y, value);
                    break;
                default:
                    desLabelText = new Text();
                    desValueText = new Text();
                    multiplierLabelText = new Text();
                    multiplierValueText = new Text();
            }

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            Text multiplierText = new Text(X, Y, "*");
            multiplierText.setFont(Font.font(48));
            multiplierText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, multiplierText, desLabelText, desValueText,
                    multiplierLabelText, multiplierValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();
            TranslateTransition multiplierLabelTransition = new TranslateTransition();
            TranslateTransition multiplierValueTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
            TranslateTransition multiplierTransition = new TranslateTransition();

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

            // Multiplier label static
            multiplierLabelTransition.setNode(multiplierLabelText);
            multiplierLabelTransition.fromXProperty().bind(multiplierRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((multiplierRectangle.getLayoutBounds().getWidth() - multiplierLabelText.getLayoutBounds().getWidth()) / 2));
            multiplierLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            multiplierLabelTransition.toXProperty().bind(multiplierLabelTransition.fromXProperty());
            multiplierLabelTransition.toYProperty().bind(multiplierLabelTransition.fromYProperty());

            // Multiplier value static
            multiplierValueTransition.setNode(multiplierValueText);
            multiplierValueTransition.fromXProperty().bind(multiplierRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((multiplierRectangle.getLayoutBounds().getWidth() - multiplierValueText.getLayoutBounds().getWidth()) / 2));
            multiplierValueTransition.fromYProperty().bind(multiplierRectangle.translateYProperty()
                    .add(multiplierRectangle.getLayoutBounds().getHeight() / 1.5));
            multiplierValueTransition.toXProperty().bind(multiplierValueTransition.fromXProperty());
            multiplierValueTransition.toYProperty().bind(multiplierValueTransition.fromYProperty());

            // Multiply sign label static
            multiplierTransition.setNode(multiplierText);
            multiplierTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + multiplierRectangle.getLayoutBounds().getWidth() + 40));
            multiplierTransition.fromYProperty().bind(equalTransition.fromYProperty().add(12.5));
            multiplierTransition.toXProperty().bind(multiplierTransition.fromXProperty());
            multiplierTransition.toYProperty().bind(multiplierTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + multiplierRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + multiplierRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desTransition.play();
            equalTransition.play();
            multiplierLabelTransition.play();
            multiplierValueTransition.play();
            multiplierTransition.play();
            srcLabelTransition.play();
            srcTransition.play();
        }
    }
}

