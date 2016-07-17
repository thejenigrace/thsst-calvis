package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

import thsst.calvis.configuration.model.engine.EFlags;
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

/**
 * Created by Jennica on 10/07/2016.
 */
public class Aad extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();
        EFlags eFlags = registers.getEFlags();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle augendRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle srcRectangle = this.createRectangle(Token.HEX, width, height);
        Rectangle flagRectangle = this.createRectangle(Token.REG, width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            augendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            augendRectangle.setY(Y);
            augendRectangle.setArcWidth(10);
            augendRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth()
                    + augendRectangle.getLayoutBounds().getWidth() + X * 2);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            flagRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth()
                    + augendRectangle.getLayoutBounds().getWidth() + srcRectangle.getLayoutBounds().getWidth() + X * 3);
            flagRectangle.setY(Y);
            flagRectangle.setArcWidth(10);
            flagRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle minusCircle = new Circle(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth()
                    + augendRectangle.getLayoutBounds().getWidth() + 150, 135, 30, Color.web("#798788", 1.0));

            Circle plusCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() +
                    srcRectangle.getLayoutBounds().getWidth() + 250, 135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, augendRectangle, srcRectangle, flagRectangle, equalCircle, minusCircle, plusCircle);


            String flagsAffected = "Flags Affected: PF, ZF, SF";
            Text detailsText = new Text(X, Y*2, flagsAffected);
            Text desLabelText = new Text(X, Y, "AX");
            Text desValueText = new Text(X, Y, "0x" + registers.get("AX"));
            Text augendLabelText = new Text(X, Y, "AH");
            Text augendValueText = new Text(X, Y, "0x" + registers.get("AH"));
            Text srcLabelText, srcValueText;
            if(tokens.length == 1) {
                int size = 8;
                srcLabelText = this.createLabelText(X, Y, tokens[0]);
                srcValueText = this.createValueText(X, Y, tokens[0], registers, memory, size);
            } else {
                srcLabelText = new Text(X, Y, "IMMEDIATE");
                srcValueText = new Text(X, Y, "0x0A");
            }
            Text flagLabelText = new Text(X, Y, "AL");
            Text flagValueText = new Text(X, Y, "0x" + registers.get("AL"));

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            Text minusText = new Text(X, Y, "*");
            minusText.setFont(Font.font(48));
            minusText.setFill(Color.WHITESMOKE);

            Text plusText = new Text(X, Y, "+");
            plusText.setFont(Font.font(48));
            plusText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, minusText, plusText, desLabelText, desValueText,
                    augendLabelText, augendValueText, srcLabelText, srcValueText, flagLabelText, flagValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desValueTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcValueTransition = new TranslateTransition();
            TranslateTransition augendLabelTransition = new TranslateTransition();
            TranslateTransition augendValueTransition = new TranslateTransition();
            TranslateTransition flagLabelTransition = new TranslateTransition();
            TranslateTransition flagValueTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
            TranslateTransition minusTransition = new TranslateTransition();
            TranslateTransition plusTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desValueTransition.setInterpolator(Interpolator.LINEAR);
            desValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desValueTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

            // Equal sign label static
            equalTransition.setNode(equalText);
            equalTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 70 + srcRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            equalTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            equalTransition.toXProperty().bind(equalTransition.fromXProperty());
            equalTransition.toYProperty().bind(equalTransition.fromYProperty());

            // Augend label static
            augendLabelTransition.setNode(augendLabelText);
            augendLabelTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendLabelText.getLayoutBounds().getWidth()) / 2));
            augendLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            augendLabelTransition.toXProperty().bind(augendLabelTransition.fromXProperty());
            augendLabelTransition.toYProperty().bind(augendLabelTransition.fromYProperty());

            // Augend value static
            augendValueTransition.setNode(augendValueText);
            augendValueTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendValueText.getLayoutBounds().getWidth()) / 2));
            augendValueTransition.fromYProperty().bind(augendRectangle.translateYProperty()
                    .add(augendRectangle.getLayoutBounds().getHeight() / 1.5));
            augendValueTransition.toXProperty().bind(augendValueTransition.fromXProperty());
            augendValueTransition.toYProperty().bind(augendValueTransition.fromYProperty());

            // Multiply sign label static
            minusTransition.setNode(minusText);
            minusTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + X + 35));
            minusTransition.fromYProperty().bind(equalTransition.fromYProperty().add(12.5));
            minusTransition.toXProperty().bind(minusTransition.fromXProperty());
            minusTransition.toYProperty().bind(minusTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + X * 2)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcValueTransition.setNode(srcValueText);
            srcValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + X * 2)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcValueTransition.fromYProperty().bind(desValueTransition.fromYProperty());
            srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
            srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

            // Plus sign label static
            plusTransition.setNode(plusText);
            plusTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth())
                    .add(srcRectangle.getLayoutBounds().getWidth() + X * 2 + 35));
            plusTransition.fromYProperty().bind(equalTransition.fromYProperty());
            plusTransition.toXProperty().bind(plusTransition.fromXProperty());
            plusTransition.toYProperty().bind(plusTransition.fromYProperty());

            // Source label static
            flagLabelTransition.setNode(flagLabelText);
            flagLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth())
                    .add(srcRectangle.getLayoutBounds().getWidth() + X * 3)
                    .add((flagRectangle.getLayoutBounds().getWidth() - flagLabelText.getLayoutBounds().getWidth()) / 2));
            flagLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            flagLabelTransition.toXProperty().bind(flagLabelTransition.fromXProperty());
            flagLabelTransition.toYProperty().bind(flagLabelTransition.fromYProperty());

            // Source value static
            flagValueTransition.setNode(flagValueText);
            flagValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth())
                    .add(srcRectangle.getLayoutBounds().getWidth() + X * 3)
                    .add((flagRectangle.getLayoutBounds().getWidth() - flagValueText.getLayoutBounds().getWidth()) / 2));
            flagValueTransition.fromYProperty().bind(desValueTransition.fromYProperty());
            flagValueTransition.toXProperty().bind(flagValueTransition.fromXProperty());
            flagValueTransition.toYProperty().bind(flagValueTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desValueTransition.play();
            equalTransition.play();
            augendLabelTransition.play();
            augendValueTransition.play();
            minusTransition.play();
            srcLabelTransition.play();
            srcValueTransition.play();
            plusTransition.play();
            flagLabelTransition.play();
            flagValueTransition.play();
        }
    }
}
