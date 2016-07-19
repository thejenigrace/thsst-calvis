package thsst.calvis.simulatorvisualizer.animation.instruction.sse2;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Shufpd extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        Calculator calculator = new Calculator(registers, memory);

        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        String value0 = finder.getRegister(tokens[0].getValue());
        String value1 = "";
        String result = "";
        String l = "";
        if ( tokens[1].getType() == Token.REG ) {
            value1 = registers.get(tokens[1].getValue());
            l = tokens[1].getValue();
        }
        else if ( tokens[1].getType() == Token.MEM ) {
            try {
                value1 = memory.read(tokens[1], registers.getBitSize(tokens[0]));
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }

            l = "[" + tokens[1].getValue() + "]";
        }

        if ( tokens[0].getType() == Token.REG )
            result = registers.get(tokens[0].getValue());

        // CODE HERE
        int width = 290;
        int height = 30;

        String i = calculator.hexToBinaryString(tokens[2].getValue(), 8);
        String i1 = i.charAt(7) + "";
        String i0 = i.charAt(6) + "";

        Rectangle des1;
        Rectangle des3;
        Rectangle src1;
        Rectangle src3;

        if( i1.equals("0") ) {
            des1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            des3 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
        } else {
            des1 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            des3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
        }

        if( i0.equals("0") ) {
            src1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            src3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        } else {
            src1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            src3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
        }

        Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        Rectangle res2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

        des1.setX(X);
        des1.setY(Y);
        des3.setX(X + 300);
        des3.setY(Y);
        src1.setX(X);
        src1.setY(Y + 51);
        src3.setX(X + 300);
        src3.setY(Y + 51);
        res1.setX(X);
        res1.setY(Y + 101);
        res2.setX(X + 300);
        res2.setY(Y + 101);

        Text label1 = new Text(tokens[0].getValue());
        Text label2 = new Text(l);

        Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

        String text = "Bit 0 selects which value is moved from the destination operand to the result and bit 1 selects which value is moved from the source operand to the result.\n" +
                "If the value of the bit is 0, get the lower half. Else, get the upper half.";
        Text label4 = new Text(text);

        label1.setX(X);
        label1.setY(Y);
        label2.setX(X);
        label2.setY(Y + 50);
        label3.setX(X);
        label3.setY(Y + 100);
        label4.setX(X);
        label4.setY(Y + 150);
        this.root.getChildren().addAll(des1, des3, src1, src3, res1, res2, label1, label2, label3, label4);

        Text textBit7 = new Text(X, Y + 10, "" + value0.substring(0, 16));
        Text textBit5 = new Text(X + 300, Y + 10, "" + value0.substring(16));
        Text textBit3 = new Text(X, Y + 60, "" + value1.substring(0, 16));
        Text textBit1 = new Text(X + 300, Y + 60, "" + value1.substring(16));
        Text text0 = new Text(X, Y + 110, "" + result.substring(0, 16));
        Text text2 = new Text(X + 300, Y + 110, "" + result.substring(16));

        this.root.getChildren().addAll(textBit7, textBit5, textBit3, textBit1, text0, text2);

        TranslateTransition bit9Transition = new TranslateTransition();
        TranslateTransition bit8Transition = new TranslateTransition();
        TranslateTransition bit7Transition = new TranslateTransition();
        TranslateTransition bit5Transition = new TranslateTransition();
        TranslateTransition bit3Transition = new TranslateTransition();
        TranslateTransition bit1Transition = new TranslateTransition();

        bit7Transition.setNode(textBit7);
        bit7Transition.fromXProperty().bind(des1.translateXProperty()
                .add(des1.getLayoutBounds().getWidth() - 290)
                .add((des1.getLayoutBounds().getWidth() - textBit7.getLayoutBounds().getWidth()) / 2));
        bit7Transition.fromYProperty().bind(des1.translateYProperty()
                .add(des1.getLayoutBounds().getHeight() / 3));
        bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

        bit5Transition.setNode(textBit5);
        bit5Transition.fromXProperty().bind(des3.translateXProperty()
                .add(des3.getLayoutBounds().getWidth() - 290)
                .add((des3.getLayoutBounds().getWidth() - textBit5.getLayoutBounds().getWidth()) / 2));
        bit5Transition.fromYProperty().bind(des3.translateYProperty()
                .add(des3.getLayoutBounds().getHeight() / 3));
        bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
        bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

        bit3Transition.setNode(textBit3);
        bit3Transition.fromXProperty().bind(src1.translateXProperty()
                .add(src1.getLayoutBounds().getWidth() - 290)
                .add((src1.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
        bit3Transition.fromYProperty().bind(src1.translateYProperty()
                .add(src1.getLayoutBounds().getHeight() / 3));
        bit3Transition.toXProperty().bind(bit3Transition.fromXProperty());
        bit3Transition.toYProperty().bind(bit3Transition.fromYProperty());

        bit1Transition.setNode(textBit1);
        bit1Transition.fromXProperty().bind(src3.translateXProperty()
                .add(src3.getLayoutBounds().getWidth() - 290)
                .add((src3.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
        bit1Transition.fromYProperty().bind(src3.translateYProperty()
                .add(src3.getLayoutBounds().getHeight() / 3));
        bit1Transition.toXProperty().bind(bit1Transition.fromXProperty());
        bit1Transition.toYProperty().bind(bit1Transition.fromYProperty());

        bit8Transition.setNode(text0);
        bit8Transition.fromXProperty().bind(res1.translateXProperty()
                .add(res1.getLayoutBounds().getWidth() - 290)
                .add((res1.getLayoutBounds().getWidth() - text0.getLayoutBounds().getWidth()) / 2));
        bit8Transition.fromYProperty().bind(res1.translateYProperty()
                .add(res1.getLayoutBounds().getHeight() / 3));
        bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
        bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

        bit9Transition.setNode(text2);
        bit9Transition.fromXProperty().bind(res2.translateXProperty()
                .add(res2.getLayoutBounds().getWidth() - 290)
                .add((res2.getLayoutBounds().getWidth() - text2.getLayoutBounds().getWidth()) / 2));
        bit9Transition.fromYProperty().bind(res2.translateYProperty()
                .add(res2.getLayoutBounds().getHeight() / 3));
        bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
        bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());


        bit9Transition.play();
        bit8Transition.play();
        bit7Transition.play();
        bit5Transition.play();
        bit3Transition.play();
        bit1Transition.play();
    }
}
