package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Fistp extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
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
        int width = 300;
        int height = 30;
        Rectangle st0 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        Rectangle st1 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st2 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st3 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st4 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st5 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st6 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st7 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));
        Rectangle st8 = new Rectangle(width, height, Color.web("#2ecc71", 1.0));

        st0.setX(X);
        st0.setY(Y);
        st1.setX(X);
        st1.setY(Y + 50);
        st2.setX(X);
        st2.setY(Y + 90);
        st3.setX(X);
        st3.setY(Y + 130);
        st4.setX(X);
        st4.setY(Y + 170);
        st5.setX(X);
        st5.setY(Y + 210);
        st6.setX(X);
        st6.setY(Y + 250);
        st7.setX(X);
        st7.setY(Y + 290);
        st8.setX(X);
        st8.setY(Y + 330);

        Text label = new Text("Convert the value in the ST(0) register to a signed integer and store the result in " + tokens[0].getValue() + ". Register stack is popped.");
        label.setX(X);
        label.setY(Y);

        Text label0 = new Text("Value of ST0: " );
        Text label1 = new Text("ST0");
        Text label2 = new Text("ST1");
        Text label3 = new Text("ST2");
        Text label4 = new Text("ST3");
        Text label5 = new Text("ST4");
        Text label6 = new Text("ST5");
        Text label7 = new Text("ST6");
        Text label8 = new Text("ST7");

        label0.setX(X);
        label0.setY(Y + 20);
        label1.setX(X);
        label1.setY(Y + 70);
        label2.setX(X);
        label2.setY(Y + 110);
        label3.setX(X);
        label3.setY(Y + 150);
        label4.setX(X);
        label4.setY(Y + 190);
        label5.setX(X);
        label5.setY(Y + 230);
        label6.setX(X);
        label6.setY(Y + 270);
        label7.setX(X);
        label7.setY(Y + 310);
        label8.setX(X);
        label8.setY(Y + 350);

        this.root.getChildren().addAll(st0, st1, st2, st3, st4, st5, st6, st7, st8,
                label, label0, label1, label2, label3, label4, label5, label6, label7, label8);

        Text textBit0a = new Text(X, Y + 60, "" + registers.get("ST0"));
        Text textBit0 = new Text(X, Y + 100, "" + registers.get("ST0"));
        Text textBit1 = new Text(X, Y + 140, "" + registers.get("ST1"));
        Text textBit2 = new Text(X, Y + 180, "" + registers.get("ST2"));
        Text textBit3 = new Text(X, Y + 220, "" + registers.get("ST3"));
        Text textBit4 = new Text(X, Y + 260, "" + registers.get("ST4"));
        Text textBit5 = new Text(X, Y + 300, "" + registers.get("ST5"));
        Text textBit6 = new Text(X, Y + 340, "" + registers.get("ST6"));
        Text textBit7 = new Text(X, Y + 380, "" + registers.get("ST7"));

        this.root.getChildren().addAll(textBit0, textBit1, textBit2, textBit3, textBit4, textBit5, textBit6, textBit7, textBit0a);

        TranslateTransition bit8Transition = new TranslateTransition(new Duration(1000), textBit0a);
        TranslateTransition bit7Transition = new TranslateTransition(new Duration(1000), textBit7);
        TranslateTransition bit6Transition = new TranslateTransition(new Duration(1000), textBit6);
        TranslateTransition bit5Transition = new TranslateTransition(new Duration(1000), textBit5);
        TranslateTransition bit4Transition = new TranslateTransition(new Duration(1000), textBit4);
        TranslateTransition bit3Transition = new TranslateTransition(new Duration(1000), textBit3);
        TranslateTransition bit2Transition = new TranslateTransition(new Duration(1000), textBit2);
        TranslateTransition bit1Transition = new TranslateTransition(new Duration(1000), textBit1);
        TranslateTransition bit0Transition = new TranslateTransition(new Duration(1000), textBit0);

        bit8Transition.setNode(textBit0a);
        bit8Transition.fromXProperty().bind(st0.translateXProperty()
                .add(st7.getLayoutBounds().getWidth() - 290)
                .add((st0.getLayoutBounds().getWidth() - textBit0a.getLayoutBounds().getWidth()) / 2));
        bit8Transition.fromYProperty().bind(st0.translateYProperty()
                .add(st0.getLayoutBounds().getHeight() / 3));
        bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
        bit8Transition.toYProperty().bind(bit8Transition.fromYProperty()
                                    .add(-50));

        bit7Transition.setInterpolator(Interpolator.LINEAR);
        bit7Transition.fromXProperty().bind(st7.translateXProperty()
                .add(st7.getLayoutBounds().getWidth() - 300)
                .add((st7.getLayoutBounds().getWidth() - textBit7.getLayoutBounds().getWidth()) / 2));
        bit7Transition.fromYProperty().bind(st7.translateYProperty()
                .add(st7.getLayoutBounds().getHeight() / 3));

        bit6Transition.setInterpolator(Interpolator.LINEAR);
        bit6Transition.fromXProperty().bind(st6.translateXProperty()
                .add(st6.getLayoutBounds().getWidth() - 300)
                .add((st6.getLayoutBounds().getWidth() - textBit6.getLayoutBounds().getWidth()) / 2));
        bit6Transition.fromYProperty().bind(st6.translateYProperty()
                .add(st6.getLayoutBounds().getHeight() / 3));

        bit5Transition.setInterpolator(Interpolator.LINEAR);
        bit5Transition.fromXProperty().bind(st5.translateXProperty()
                .add(st5.getLayoutBounds().getWidth() - 300)
                .add((st5.getLayoutBounds().getWidth() - textBit5.getLayoutBounds().getWidth()) / 2));
        bit5Transition.fromYProperty().bind(st5.translateYProperty()
                .add(st5.getLayoutBounds().getHeight() / 3));

        bit4Transition.setInterpolator(Interpolator.LINEAR);
        bit4Transition.fromXProperty().bind(st4.translateXProperty()
                .add(st4.getLayoutBounds().getWidth() - 300)
                .add((st4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
        bit4Transition.fromYProperty().bind(st4.translateYProperty()
                .add(st4.getLayoutBounds().getHeight() / 3));

        bit3Transition.setInterpolator(Interpolator.LINEAR);
        bit3Transition.fromXProperty().bind(st3.translateXProperty()
                .add(st3.getLayoutBounds().getWidth() - 300)
                .add((st3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
        bit3Transition.fromYProperty().bind(st3.translateYProperty()
                .add(st3.getLayoutBounds().getHeight() / 3));

        bit2Transition.setInterpolator(Interpolator.LINEAR);
        bit2Transition.fromXProperty().bind(st2.translateXProperty()
                .add(st2.getLayoutBounds().getWidth() - 300)
                .add((st2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
        bit2Transition.fromYProperty().bind(st2.translateYProperty()
                .add(st2.getLayoutBounds().getHeight() / 3));

        bit1Transition.setInterpolator(Interpolator.LINEAR);
        bit1Transition.fromXProperty().bind(st1.translateXProperty()
                .add(st1.getLayoutBounds().getWidth() - 300)
                .add((st1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
        bit1Transition.fromYProperty().bind(st1.translateYProperty()
                .add(st1.getLayoutBounds().getHeight() / 3));


        bit0Transition.setInterpolator(Interpolator.LINEAR);
        bit0Transition.fromXProperty().bind(st0.translateXProperty()
                .add(st0.getLayoutBounds().getWidth() - 300)
                .add((st0.getLayoutBounds().getWidth() - textBit0.getLayoutBounds().getWidth()) / 2));
        bit0Transition.fromYProperty().bind(st0.translateYProperty()
                .add(st0.getLayoutBounds().getHeight() / 3));

        bit8Transition.play();
        bit7Transition.play();
        bit6Transition.play();
        bit5Transition.play();
        bit4Transition.play();
        bit3Transition.play();
        bit2Transition.play();
        bit1Transition.play();
        bit0Transition.play();

        bit7Transition.stop();
        bit6Transition.stop();
        bit5Transition.stop();
        bit4Transition.stop();
        bit3Transition.stop();
        bit2Transition.stop();
        bit1Transition.stop();
        bit0Transition.stop();

        bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit7Transition.fromYProperty()
                .add(-40));

        bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
        bit6Transition.toYProperty().bind(bit6Transition.fromYProperty()
                .add(-40));

        bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
        bit5Transition.toYProperty().bind(bit5Transition.fromYProperty()
                .add(-40));

        bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
        bit4Transition.toYProperty().bind(bit4Transition.fromYProperty()
                .add(-40));

        bit3Transition.toXProperty().bind(bit3Transition.fromXProperty());
        bit3Transition.toYProperty().bind(bit3Transition.fromYProperty()
                .add(-40));

        bit2Transition.toXProperty().bind(bit2Transition.fromXProperty());
        bit2Transition.toYProperty().bind(bit2Transition.fromYProperty()
                .add(-40));

        bit1Transition.toXProperty().bind(bit1Transition.fromXProperty());
        bit1Transition.toYProperty().bind(bit1Transition.fromYProperty()
                .add(-40));

        bit0Transition.toXProperty().bind(bit0Transition.fromXProperty());
        bit0Transition.toYProperty().bind(bit0Transition.fromYProperty()
                .add(-40));

        bit8Transition.play();
        bit7Transition.play();
        bit6Transition.play();
        bit5Transition.play();
        bit4Transition.play();
        bit3Transition.play();
        bit2Transition.play();
        bit1Transition.play();
        bit0Transition.play();

    }
}
