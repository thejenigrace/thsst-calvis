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

/**
 * Created by Jennica on 10/07/2016.
 */
public class Aaa extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

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

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            augendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            augendRectangle.setY(Y);
            augendRectangle.setArcWidth(10);
            augendRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + X * 2);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle plusCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + 150,
                    135, 30, Color.web("#798788", 1.0));

            String flagsAffected = "Flags Affected: CF, AF";
            Text detailsText = new Text(X, Y*2, flagsAffected);
            Text desLabelText = new Text(X, Y, "AX");
            Text desValueText = new Text(X, Y, "0x" + registers.get("AX"));
            Text augendLabelText = new Text(X, Y, "AX");
            Text augendValueText = new Text(X, Y, "0x" + registers.get("AX"));
            Text srcLabelText = new Text(X, Y, "IMMEDIATE");
            String value = "0x0106";

            Text srcValueText = new Text(X, Y, value);

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            Text plusText = new Text(X, Y, "+");
            plusText.setFont(Font.font(48));
            plusText.setFill(Color.WHITESMOKE);

            // If oldValue of AX == newValue of AX
            boolean equal = false;
            if(equal)
                this.root.getChildren().addAll(desRectangle, desLabelText, desValueText);
            else
                this.root.getChildren().addAll(desRectangle, augendRectangle, srcRectangle, equalCircle, plusCircle,
                        detailsText, equalText, plusText, desLabelText, desValueText,
                        augendLabelText, augendValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();
            TranslateTransition augendLabelTransition = new TranslateTransition();
            TranslateTransition augendTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
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
            if(equal) {
                desTransition.fromXProperty().bind(desRectangle.translateXProperty()
                        .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
                desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                        .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
                desTransition.toXProperty().bind(desTransition.fromXProperty());
                desTransition.toYProperty().bind(desTransition.fromYProperty());
            } else {
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
            augendTransition.setNode(augendValueText);
            augendTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendValueText.getLayoutBounds().getWidth()) / 2));
            augendTransition.fromYProperty().bind(augendRectangle.translateYProperty()
                    .add(augendRectangle.getLayoutBounds().getHeight() / 1.5));
            augendTransition.toXProperty().bind(augendTransition.fromXProperty());
            augendTransition.toYProperty().bind(augendTransition.fromYProperty());

            // Plus sign label static
            plusTransition.setNode(plusText);
            plusTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + augendRectangle.getLayoutBounds().getWidth() + 35));
            plusTransition.fromYProperty().bind(equalTransition.fromYProperty());
            plusTransition.toXProperty().bind(plusTransition.fromXProperty());
            plusTransition.toYProperty().bind(plusTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + augendRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X + augendRectangle.getLayoutBounds().getWidth() + X)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desTransition.play();
            equalTransition.play();
            augendLabelTransition.play();
            augendTransition.play();
            plusTransition.play();
            srcLabelTransition.play();
            srcTransition.play();
        }
    }
}
