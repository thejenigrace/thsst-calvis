package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Xadd extends CalvisAnimation {

    @Override
    public void animate(Tab tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);
        Rectangle resRectangle = this.createRectangle(tokens[0], width, height);
        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(110);
            desRectangle.setY(100);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + 100);
            srcRectangle.setY(100);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            resRectangle.setX(srcRectangle.xProperty().getValue() + srcRectangle.widthProperty().getValue() +
                     + 120);
            resRectangle.setY(100);
            resRectangle.setArcWidth(10);
            resRectangle.setArcHeight(10);

            root.getChildren().addAll(desRectangle, srcRectangle, resRectangle);

            int desSize = 0;
            if ( tokens[0].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[0]);
            else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[1]);
            else
                desSize = memory.getBitSize(tokens[0]);


            Text desLabelText = this.createLabelText(tokens[0]);
            Text resLabelText = this.createLabelText(tokens[0]);
            Text resValueText = this.createValueText(tokens[0], registers, memory, desSize);
            Text srcLabelText = this.createLabelText(tokens[1]);
            Text srcValueText = this.createValueText(tokens[1], registers, memory, desSize);
            Calculator c = new Calculator(registers, memory);
            BigInteger numberDes = new BigInteger(resValueText.getText(), 16).subtract(new BigInteger(srcValueText.getText(), 16));
            Text desValueText = new Text(c.hexZeroExtend(numberDes.toString(16).toUpperCase(), desSize/4));

            Text plusText = new Text(100, 125, "+");
            plusText.setFont(Font.font(48));
            plusText.setFill(Color.WHITESMOKE);

            Text equalText = new Text(100, 125, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            desLabelText.setX(100);
            desLabelText.setY(100);

            desValueText.setX(100);
            desValueText.setY(100);

            srcLabelText.setX(100);
            srcLabelText.setY(100);

            resLabelText.setX(100);
            resLabelText.setY(100);

            srcValueText.setX(100);
            srcValueText.setY(100);

            resValueText.setX(100);
            resValueText.setY(120);

            Circle plusCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() * 3 + 20,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText, plusCircle, plusText, equalCircle, equalText, resLabelText, resValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition(new Duration(1000), srcValueText);
            TranslateTransition resLabelTransition = new TranslateTransition();
            TranslateTransition resTransition = new TranslateTransition(new Duration(1000), resValueText);
            TranslateTransition plusSignLabelTransition = new TranslateTransition();
            TranslateTransition equalLabelTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desTransition.setInterpolator(Interpolator.LINEAR);
            desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            //asdasdasdasdasdasd
            // Destination label static
            resLabelTransition.setNode(resLabelText);
            resLabelTransition.fromXProperty().bind(resRectangle.translateXProperty()
                    .add(10 + (4 * resRectangle.getLayoutBounds().getWidth())));
            resLabelTransition.fromYProperty().bind(resRectangle.translateYProperty()
                    .add(resRectangle.getLayoutBounds().getHeight() / 3));
            resLabelTransition.toXProperty().bind(resLabelTransition.fromXProperty());
            resLabelTransition.toYProperty().bind(resLabelTransition.fromYProperty());

            // Destination value moving
            resTransition.setNode(resValueText);
            resTransition.fromXProperty().bind(resRectangle.translateXProperty()
                    .add(10 + (3.9 * resRectangle.getLayoutBounds().getWidth())));
            resTransition.fromYProperty().bind(resRectangle.translateYProperty()
                    .add(resRectangle.getLayoutBounds().getHeight() / 3));
            resTransition.toXProperty().bind(resTransition.fromXProperty());
            resTransition.toYProperty().bind(resTransition.fromYProperty());


            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // tanginang yan ang raming pota para sa plus sign
            plusSignLabelTransition.setNode(plusText);
            plusSignLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth())
                    .add((srcRectangle.getLayoutBounds().getWidth() - 97)));
            plusSignLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            plusSignLabelTransition.toXProperty().bind(plusSignLabelTransition.fromXProperty());
            plusSignLabelTransition.toYProperty().bind(plusSignLabelTransition.fromYProperty());

            // puta yan ang raming pota para sa plus sign
            equalLabelTransition.setNode(equalText);
            equalLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth())
                    .add((desRectangle.getLayoutBounds().getWidth() + 155)));
            equalLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            equalLabelTransition.toXProperty().bind(equalLabelTransition.fromXProperty());
            equalLabelTransition.toYProperty().bind(equalLabelTransition.fromYProperty());

            // Source value Moving na
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() - 130)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));

            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            System.out.println(srcValueText.getLayoutBounds().getWidth() + " width");
            srcTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add(130 + 110 + 11 + (desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));

            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            plusSignLabelTransition.play();
            equalLabelTransition.play();
            resLabelTransition.play();
            desTransition.play();
            srcTransition.play();
            resTransition.play();
        }
    }
}

