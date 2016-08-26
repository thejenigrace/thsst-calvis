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

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Dec extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
//        tab.setContent(root);
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle minuendRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(Token.HEX, width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(100);
            desRectangle.setY(100);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            minuendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + 100);
            minuendRectangle.setY(100);
            minuendRectangle.setArcWidth(10);
            minuendRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + minuendRectangle.getLayoutBounds().getWidth() + 200);
            srcRectangle.setY(100);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle minusCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + minuendRectangle.getLayoutBounds().getWidth() + 150,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, minuendRectangle, srcRectangle, equalCircle, minusCircle);

            int desSize = 0;
            if ( tokens[0].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[0]);
            else if ( tokens[0].getType() == Token.MEM )
                desSize = memory.getBitSize(tokens[0]);
            
            String flagsAffected = "Flags Affected: PF, AF, ZF, SF, OF";
            Text detailsText = new Text(100, 200, flagsAffected);
            Text desLabelText = this.createLabelText(X, Y, tokens[0]);
            Text desValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
            Text minuendLabelText = this.createLabelText(X, Y, tokens[0]);
            Text minuendValueText = this.createValueTextUsingFinder(X, Y, tokens[0], desSize);
            Text srcLabelText = new Text(X, Y, "IMMEDIATE");
            Text srcValueText;
            switch ( desSize ) {
                case 8:
                    srcValueText = new Text(X, Y, "0x01");
                    break;
                case 16:
                    srcValueText = new Text(X, Y, "0x0001");
                    break;
                case 32:
                    srcValueText = new Text(X, Y, "0x00000001");
                    break;
                default:
                    srcValueText = new Text(X, Y, "0x01");
            }

            Text minusText = new Text(X, Y, "-");
            minusText.setFont(Font.font(48));
            minusText.setFill(Color.WHITESMOKE);

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, minusText, desLabelText, desValueText,
                    minuendLabelText, minuendValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();
            TranslateTransition minuendLabelTransition = new TranslateTransition();
            TranslateTransition minuendTransition = new TranslateTransition();
            TranslateTransition equalTransition = new TranslateTransition();
            TranslateTransition minusTransition = new TranslateTransition();

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
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Equal sign label static
            equalTransition.setNode(equalText);
            equalTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 70 + srcRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            equalTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            equalTransition.toXProperty().bind(equalTransition.fromXProperty());
            equalTransition.toYProperty().bind(equalTransition.fromYProperty());

            // Minuend label static
            minuendLabelTransition.setNode(minuendLabelText);
            minuendLabelTransition.fromXProperty().bind(minuendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((minuendRectangle.getLayoutBounds().getWidth() - minuendLabelText.getLayoutBounds().getWidth()) / 2));
            minuendLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            minuendLabelTransition.toXProperty().bind(minuendLabelTransition.fromXProperty());
            minuendLabelTransition.toYProperty().bind(minuendLabelTransition.fromYProperty());

            // minuend value static
            minuendTransition.setNode(minuendValueText);
            minuendTransition.fromXProperty().bind(minuendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((minuendRectangle.getLayoutBounds().getWidth() - minuendValueText.getLayoutBounds().getWidth()) / 2));
            minuendTransition.fromYProperty().bind(minuendRectangle.translateYProperty()
                    .add(minuendRectangle.getLayoutBounds().getHeight() / 1.5));
            minuendTransition.toXProperty().bind(minuendTransition.fromXProperty());
            minuendTransition.toYProperty().bind(minuendTransition.fromYProperty());

            // Minus sign label static
            minusTransition.setNode(minusText);
            minusTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + minuendRectangle.getLayoutBounds().getWidth() + 40));
            minusTransition.fromYProperty().bind(equalTransition.fromYProperty());
            minusTransition.toXProperty().bind(minusTransition.fromXProperty());
            minusTransition.toYProperty().bind(minusTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + minuendRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + minuendRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desTransition.play();
            equalTransition.play();
            minuendLabelTransition.play();
            minuendTransition.play();
            minusTransition.play();
            srcLabelTransition.play();
            srcTransition.play();
        }
    }
}

