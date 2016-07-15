package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
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
public class Cmp extends CalvisAnimation {

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
        Rectangle minuendRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle subtrahendRectangle = this.createRectangle(tokens[1], width, height);

        if ( minuendRectangle != null && subtrahendRectangle != null ) {
            minuendRectangle.setX(X);
            minuendRectangle.setY(Y);
            minuendRectangle.setArcWidth(10);
            minuendRectangle.setArcHeight(10);

            subtrahendRectangle.setX(minuendRectangle.xProperty().getValue() + minuendRectangle.getLayoutBounds().getWidth() + X);
            subtrahendRectangle.setY(Y);
            subtrahendRectangle.setArcWidth(10);
            subtrahendRectangle.setArcHeight(10);

            Circle minusCircle = new Circle(minuendRectangle.xProperty().getValue() +
                    minuendRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(minuendRectangle, subtrahendRectangle, minusCircle);

            int desSize = 0;
            if ( tokens[0].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[0]);
            else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[1]);
            else
                desSize = memory.getBitSize(tokens[0]);

            String description = "The result is discarded but the status flags are updated according to the results.\n";
            String flagsAffected = "Flags Affected: CF, PF, AF, ZF, SF, OF";
            Text detailsText = new Text(X, Y*2, description + flagsAffected);
            Text minuendLabelText = this.createLabelText(X, Y, tokens[0]);
            Text minuendValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
            Text subtrahendLabelText = this.createLabelText(X, Y, tokens[1]);
            Text subtrahendValueText = this.createValueText(X, Y, tokens[1], registers, memory, desSize);

            Text minusText = new Text(X, Y, "-");
            minusText.setFont(Font.font(48));
            minusText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, subtrahendLabelText, subtrahendValueText,
                    minuendLabelText, minuendValueText, minusText);

            // ANIMATION LOGIC
            TranslateTransition minuendLabelTransition = new TranslateTransition();
            TranslateTransition minuendValueTransition = new TranslateTransition(new Duration(1000), minuendValueText);
            TranslateTransition subtrahendLabelTransition = new TranslateTransition();
            TranslateTransition subtrahendValueTransition = new TranslateTransition();
            TranslateTransition minusTransition = new TranslateTransition();

            // Minuend label static
            minuendLabelTransition.setNode(minuendLabelText);
            minuendLabelTransition.fromXProperty().bind(minuendRectangle.translateXProperty()
                    .add((minuendRectangle.getLayoutBounds().getWidth() - minuendLabelText.getLayoutBounds().getWidth()) / 2));
            minuendLabelTransition.fromYProperty().bind(minuendRectangle.translateYProperty()
                    .add(minuendRectangle.getLayoutBounds().getHeight() / 3));
            minuendLabelTransition.toXProperty().bind(minuendLabelTransition.fromXProperty());
            minuendLabelTransition.toYProperty().bind(minuendLabelTransition.fromYProperty());

            // Minuend value static
            minuendValueTransition.fromXProperty().bind(minuendRectangle.translateXProperty()
                    .add((minuendRectangle.getLayoutBounds().getWidth() - minuendValueText.getLayoutBounds().getWidth()) / 2));
            minuendValueTransition.fromYProperty().bind(minuendRectangle.translateYProperty()
                    .add(minuendRectangle.getLayoutBounds().getHeight() / 1.5));
            minuendValueTransition.toXProperty().bind(minuendValueTransition.fromXProperty());
            minuendValueTransition.toYProperty().bind(minuendValueTransition.fromYProperty());

            // Minus label static
            minusTransition.setNode(minusText);
            minusTransition.fromXProperty().bind(minuendRectangle.translateXProperty()
                    .add(minuendRectangle.getLayoutBounds().getWidth() + 80 + subtrahendRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            minusTransition.fromYProperty().bind(minuendRectangle.translateYProperty()
                    .add(minuendRectangle.getLayoutBounds().getHeight() / 1.5));
            minusTransition.toXProperty().bind(minusTransition.fromXProperty());
            minusTransition.toYProperty().bind(minusTransition.fromYProperty());

            // Subtrahend label static
            subtrahendLabelTransition.setNode(subtrahendLabelText);
            subtrahendLabelTransition.fromXProperty().bind(subtrahendRectangle.translateXProperty()
                    .add(minuendRectangle.getLayoutBounds().getWidth() + X)
                    .add((subtrahendRectangle.getLayoutBounds().getWidth() - subtrahendLabelText.getLayoutBounds().getWidth()) / 2));
            subtrahendLabelTransition.fromYProperty().bind(minuendLabelTransition.fromYProperty());
            subtrahendLabelTransition.toXProperty().bind(subtrahendLabelTransition.fromXProperty());
            subtrahendLabelTransition.toYProperty().bind(subtrahendLabelTransition.fromYProperty());

            // Subtrahend value static
            subtrahendValueTransition.setNode(subtrahendValueText);
            subtrahendValueTransition.fromXProperty().bind(subtrahendRectangle.translateXProperty()
                    .add(minuendRectangle.getLayoutBounds().getWidth() + X)
                    .add((subtrahendRectangle.getLayoutBounds().getWidth() - subtrahendValueText.getLayoutBounds().getWidth()) / 2));
            subtrahendValueTransition.fromYProperty().bind(subtrahendRectangle.translateYProperty()
                    .add(subtrahendRectangle.getLayoutBounds().getHeight() / 1.5));
            subtrahendValueTransition.toXProperty().bind(subtrahendValueTransition.fromXProperty());
            subtrahendValueTransition.toYProperty().bind(subtrahendValueTransition.fromYProperty());
            
            // Play 1000 milliseconds of animation
            minuendLabelTransition.play();
            minuendValueTransition.play();
            subtrahendLabelTransition.play();
            subtrahendValueTransition.play();
            minusTransition.play();
        }
    }
}

