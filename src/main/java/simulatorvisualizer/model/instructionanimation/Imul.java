package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;

import java.util.Objects;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Imul extends CalvisAnimation {

    private RegisterList registers;
    private Memory memory;
    private Token[] tokens;

    private int width = 140;
    private int height = 70;
    private int equalPosition;

    private Rectangle desRectangle, multiplierRectangle, srcRectangle;

    private Text desLabelText, desValueText;
    private Text multiplierLabelText, multiplierValueText;
    private Text srcLabelText, srcValueText;

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        this.registers = this.currentInstruction.getRegisters();
        this.memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        this.tokens = this.currentInstruction.getParameterTokens();
        for ( Token token : this.tokens ) {
            System.out.println(token + " : " + token.getClass());
        }

        // CODE HERE
        switch ( this.tokens.length ) {
            case 1:
                this.imulOneOperand();
                break;
            case 2:
                this.imulTwoOperand();
                break;
            case 3:
                this.imulThreeOperand();
                break;
            default:
                System.out.println("None!");
        }

        System.out.println("Continue");

        this.desRectangle.setX(X);
        this.desRectangle.setY(Y);
        this.desRectangle.setArcWidth(10);
        this.desRectangle.setArcHeight(10);

        this.multiplierRectangle.setX(this.desRectangle.xProperty().getValue() + this.desRectangle.getLayoutBounds().getWidth() + X);
        this.multiplierRectangle.setY(Y);
        this.multiplierRectangle.setArcWidth(10);
        this.multiplierRectangle.setArcHeight(10);

        this.srcRectangle.setX(this.desRectangle.xProperty().getValue() + this.desRectangle.getLayoutBounds().getWidth()
                + this.multiplierRectangle.getLayoutBounds().getWidth() + X * 2);
        this.srcRectangle.setY(Y);
        this.srcRectangle.setArcWidth(10);
        this.srcRectangle.setArcHeight(10);

        Circle equalCircle = new Circle(this.desRectangle.xProperty().getValue() +
                this.desRectangle.getLayoutBounds().getWidth() + 50,
                135, 30, Color.web("#798788", 1.0));

        Circle multiplyCircle = new Circle(this.desRectangle.xProperty().getValue() +
                this.desRectangle.getLayoutBounds().getWidth() + this.multiplierRectangle.getLayoutBounds().getWidth() + 150,
                135, 30, Color.web("#798788", 1.0));

        this.root.getChildren().addAll(this.desRectangle, this.multiplierRectangle, this.srcRectangle, equalCircle, multiplyCircle);

        String flagsAffected = "Flags Affected: CF, OF";
        Text detailsText = new Text(X, Y * 2, flagsAffected);

        Text equalText = new Text(X, Y, "=");
        equalText.setFont(Font.font(48));
        equalText.setFill(Color.WHITESMOKE);

        Text multiplierText = new Text(X, Y, "*");
        multiplierText.setFont(Font.font(48));
        multiplierText.setFill(Color.WHITESMOKE);

        this.root.getChildren().addAll(detailsText, equalText, multiplierText, desLabelText, desValueText,
                multiplierLabelText, multiplierValueText, srcLabelText, srcValueText);

        // ANIMATION LOGIC
        TranslateTransition desLabelTransition = new TranslateTransition();
        TranslateTransition desTransition = new TranslateTransition(new Duration(1000), this.desValueText);
        TranslateTransition srcLabelTransition = new TranslateTransition();
        TranslateTransition srcTransition = new TranslateTransition();
        TranslateTransition multiplierLabelTransition = new TranslateTransition();
        TranslateTransition multiplierValueTransition = new TranslateTransition();
        TranslateTransition equalTransition = new TranslateTransition();
        TranslateTransition multiplierTransition = new TranslateTransition();

        // Destination label static
        desLabelTransition.setNode(this.desLabelText);
        desLabelTransition.fromXProperty().bind(this.desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
        desLabelTransition.fromYProperty().bind(this.desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 3));
        desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
        desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

        // Destination value moving
        desTransition.setInterpolator(Interpolator.LINEAR);
        desTransition.fromXProperty().bind(this.srcRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X)
                .add((this.srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
        desTransition.fromYProperty().bind(this.srcRectangle.translateYProperty()
                .add(this.srcRectangle.getLayoutBounds().getHeight() / 1.5));
        desTransition.toXProperty().bind(this.desRectangle.translateXProperty()
                .add((this.desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
        desTransition.toYProperty().bind(desTransition.fromYProperty());

        // Equal sign label static
        equalTransition.setNode(equalText);
        equalTransition.fromXProperty().bind(this.desRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + this.equalPosition + this.srcRectangle.getLayoutBounds().getWidth())
                .divide(2));
        equalTransition.fromYProperty().bind(this.desRectangle.translateYProperty()
                .add(this.desRectangle.getLayoutBounds().getHeight() / 1.5));
        equalTransition.toXProperty().bind(equalTransition.fromXProperty());
        equalTransition.toYProperty().bind(equalTransition.fromYProperty());

        // Multiplier label static
        multiplierLabelTransition.setNode(this.multiplierLabelText);
        multiplierLabelTransition.fromXProperty().bind(this.multiplierRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X)
                .add((this.multiplierRectangle.getLayoutBounds().getWidth() - multiplierLabelText.getLayoutBounds().getWidth()) / 2));
        multiplierLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
        multiplierLabelTransition.toXProperty().bind(multiplierLabelTransition.fromXProperty());
        multiplierLabelTransition.toYProperty().bind(multiplierLabelTransition.fromYProperty());

        // Multiplier value static
        multiplierValueTransition.setNode(this.multiplierValueText);
        multiplierValueTransition.fromXProperty().bind(this.multiplierRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X)
                .add((this.multiplierRectangle.getLayoutBounds().getWidth() - multiplierValueText.getLayoutBounds().getWidth()) / 2));
        multiplierValueTransition.fromYProperty().bind(this.multiplierRectangle.translateYProperty()
                .add(this.multiplierRectangle.getLayoutBounds().getHeight() / 1.5));
        multiplierValueTransition.toXProperty().bind(multiplierValueTransition.fromXProperty());
        multiplierValueTransition.toYProperty().bind(multiplierValueTransition.fromYProperty());

        // Multiply sign label static
        multiplierTransition.setNode(multiplierText);
        multiplierTransition.fromXProperty().bind(this.desRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X + this.multiplierRectangle.getLayoutBounds().getWidth() + 40));
        multiplierTransition.fromYProperty().bind(equalTransition.fromYProperty().add(12.5));
        multiplierTransition.toXProperty().bind(multiplierTransition.fromXProperty());
        multiplierTransition.toYProperty().bind(multiplierTransition.fromYProperty());

        // Source label static
        srcLabelTransition.setNode(this.srcLabelText);
        srcLabelTransition.fromXProperty().bind(this.srcRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X + this.multiplierRectangle.getLayoutBounds().getWidth() + X)
                .add((this.srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
        srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
        srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
        srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

        // Source value static
        srcTransition.setNode(this.srcValueText);
        srcTransition.fromXProperty().bind(this.srcRectangle.translateXProperty()
                .add(this.desRectangle.getLayoutBounds().getWidth() + X + this.multiplierRectangle.getLayoutBounds().getWidth() + X)
                .add((this.srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
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

    private void imulOneOperand() {
        System.out.println("ONE OPERAND!");
        this.equalPosition = 110;
        this.desRectangle = this.createRectangle(Token.REG, this.width + 40, this.height);
        this.multiplierRectangle = this.createRectangle(Token.REG, this.width, this.height);
        this.srcRectangle = this.createRectangle(this.tokens[0], this.width, this.height);

        int desSize = 0;
        if ( Objects.equals(this.tokens[0].getType(), Token.REG) ) {
            desSize = registers.getBitSize(this.tokens[0]);
        } else if ( Objects.equals(this.tokens[0].getType(), Token.MEM) )
            desSize = memory.getBitSize(this.tokens[0]);

        this.srcLabelText = this.createLabelText(X, Y, this.tokens[0]);
        this.srcValueText = this.createValueText(X, Y, this.tokens[0], this.registers, this.memory, desSize);

        String value;
        switch ( desSize ) {
            case 8:
                this.desLabelText = new Text(X, Y, "AX");
                value = "0x" + registers.get("AX");
                this.desValueText = new Text(X, Y, value);
                this.multiplierLabelText = new Text(X, Y, "AL");
                value = "0x" + registers.get("AL");
                this.multiplierValueText = new Text(X, Y, value);
                break;
            case 16:
                this.desLabelText = new Text(X, Y, "DX, AX");
                value = "0x" + registers.get("DX") + ", 0x" + registers.get("AX");
                this.desValueText = new Text(X, Y, value);
                this.multiplierLabelText = new Text(X, Y, "AX");
                value = "0x" + registers.get("AX");
                this.multiplierValueText = new Text(X, Y, value);
                break;
            case 32:
                this.desLabelText = new Text(X, Y, "EDX, EAX");
                value = "0x" + registers.get("EDX") + ", 0x" + registers.get("EAX");
                this.desValueText = new Text(X, Y, value);
                this.multiplierLabelText = new Text(X, Y, "EAX");
                value = "0x" + registers.get("EAX");
                this.multiplierValueText = new Text(X, Y, value);
                break;
            default:
                this.desLabelText = new Text();
                this.desValueText = new Text();
                this.multiplierLabelText = new Text();
                this.multiplierValueText = new Text();
        }
    }


    private void imulTwoOperand() {
        System.out.println("TWO OPERAND!");
        this.equalPosition = 70;
        this.desRectangle = this.createRectangle(this.tokens[0], this.width, this.height);
        this.multiplierRectangle = this.createRectangle(this.tokens[0], this.width, this.height);
        this.srcRectangle = this.createRectangle(this.tokens[1], this.width, this.height);

        int desSize = 0;
        if ( tokens[0].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[0]);
        else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[1]);
        else
            desSize = memory.getBitSize(tokens[0]);

        this.desLabelText = this.createLabelText(X, Y, tokens[0]);
        this.desValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
        this.multiplierLabelText = this.createLabelText(X, Y, tokens[0]);
        this.multiplierValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
        this.srcLabelText = this.createLabelText(X, Y, tokens[1]);
        this.srcValueText = this.createValueText(X, Y, tokens[1], registers, memory, desSize);
    }

    private void imulThreeOperand() {
        System.out.println("THREE OPERAND!");
        this.equalPosition = 70;
        this.desRectangle = this.createRectangle(this.tokens[0], this.width, this.height);
        this.multiplierRectangle = this.createRectangle(this.tokens[1], this.width, this.height);
        this.srcRectangle = this.createRectangle(this.tokens[2], this.width, this.height);

        int desSize = registers.getBitSize(tokens[0]);
        int multiplierSize = 0;
        if ( Objects.equals(this.tokens[1].getType(), Token.REG) ) {
            multiplierSize = registers.getBitSize(this.tokens[1]);
        } else if ( Objects.equals(this.tokens[1].getType(), Token.MEM) )
            multiplierSize = memory.getBitSize(this.tokens[1]);

        this.desLabelText = this.createLabelText(X, Y, tokens[0]);
        this.desValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
        this.multiplierLabelText = this.createLabelText(X, Y, tokens[1]);
        this.multiplierValueText = this.createValueText(X, Y, tokens[1], registers, memory, multiplierSize);
        this.srcLabelText = this.createLabelText(X, Y, tokens[2]);
        this.srcValueText = new Text(X, Y, tokens[2].getValue());
    }
}

