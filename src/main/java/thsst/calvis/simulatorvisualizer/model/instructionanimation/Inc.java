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

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Inc extends CalvisAnimation {

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
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle augendRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(Token.HEX, width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(100);
            desRectangle.setY(100);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            augendRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + 100);
            augendRectangle.setY(100);
            augendRectangle.setArcWidth(10);
            augendRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + 200);
            srcRectangle.setY(100);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle equalCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 30, Color.web("#798788", 1.0));

            Circle plusCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + augendRectangle.getLayoutBounds().getWidth() + 150,
                    135, 30, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, augendRectangle, srcRectangle, equalCircle, plusCircle);

            int desSize = 0;
            if ( tokens[0].getType() == Token.REG )
                desSize = registers.getBitSize(tokens[0]);
            else if ( tokens[0].getType() == Token.MEM )
                desSize = memory.getBitSize(tokens[0]);

            String flagsAffected = "Flags Affected: PF, AF, ZF, SF, OF";
            Text detailsText = new Text(100, 200, flagsAffected);
            Text desLabelText = this.createLabelText(X, Y, tokens[0]);
            Text desValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
            Text augendLabelText = this.createLabelText(X, Y, tokens[0]);
            Text augendValueText = this.createValueTextUsingFinder(X, Y, tokens[0], desSize);
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

            Text plusText = new Text(X, Y, "+");
            plusText.setFont(Font.font(48));
            plusText.setFill(Color.WHITESMOKE);

            Text equalText = new Text(X, Y, "=");
            equalText.setFont(Font.font(48));
            equalText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, equalText, plusText, desLabelText, desValueText,
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

            // Augend label static
            augendLabelTransition.setNode(augendLabelText);
            augendLabelTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendLabelText.getLayoutBounds().getWidth()) / 2));
            augendLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            augendLabelTransition.toXProperty().bind(augendLabelTransition.fromXProperty());
            augendLabelTransition.toYProperty().bind(augendLabelTransition.fromYProperty());

            // Augend value static
            augendTransition.setNode(augendValueText);
            augendTransition.fromXProperty().bind(augendRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((augendRectangle.getLayoutBounds().getWidth() - augendValueText.getLayoutBounds().getWidth()) / 2));
            augendTransition.fromYProperty().bind(augendRectangle.translateYProperty()
                    .add(augendRectangle.getLayoutBounds().getHeight() / 1.5));
            augendTransition.toXProperty().bind(augendTransition.fromXProperty());
            augendTransition.toYProperty().bind(augendTransition.fromYProperty());

            // Plus sign label static
            plusTransition.setNode(plusText);
            plusTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + augendRectangle.getLayoutBounds().getWidth() + 35));
            plusTransition.fromYProperty().bind(equalTransition.fromYProperty());
            plusTransition.toXProperty().bind(plusTransition.fromXProperty());
            plusTransition.toYProperty().bind(plusTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + augendRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100 + augendRectangle.getLayoutBounds().getWidth() + 100)
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

