package thsst.calvis.simulatorvisualizer.model.instructionanimation;

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
public class Shufps extends CalvisAnimation {

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


        String imm = calculator.hexToBinaryString(tokens[2].getValue(), 8);

        // CODE HERE
        int width = 140;
        int height = 50;
        Rectangle des1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
        Rectangle des2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
        Rectangle des3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
        Rectangle des4 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
//        Rectangle src1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
//        Rectangle src2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
//        Rectangle src3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
//        Rectangle src4 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));

        Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        Rectangle res2 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        Rectangle res3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
        Rectangle res4 = new Rectangle(width, height, Color.web("#3498db", 1.0));

        des1.setX(X);
        des1.setY(Y);
        des2.setX(X + 150);
        des2.setY(Y);
        des3.setX(X + 300);
        des3.setY(Y);
        des4.setX(X + 450);
        des4.setY(Y);
        res1.setX(X);
        res1.setY(Y + 151);
        res2.setX(X + 150);
        res2.setY(Y + 151);
        res3.setX(X + 300);
        res3.setY(Y + 151);
        res4.setX(X + 450);
        res4.setY(Y + 151);

        Text label1 = new Text("Value of the immediate");
        Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

        String text = "*\n00 - get the value of bit 0 to 31\n" +
                "01 - get the value of bit 32 to 63\n" +
                "10 - get the value of bit 64 to 95\n" +
                "11 - get the value of bit 96 to 127";
        Text label4 = new Text(text);

        label1.setX(X);
        label1.setY(Y);
        label3.setX(X);
        label3.setY(Y + 150);
        label4.setX(X);
        label4.setY(Y + 70);
        this.root.getChildren().addAll(des1, des2, des3, des4, res1, res2, res3, res4, label1, label3, label4);

        Text textBit7 = new Text(X, Y + 10, "Bit 6 and 7 = " + imm.substring(0, 2) + "\n     from SRC*");
        Text textBit6 = new Text(X + 150, Y + 10, "Bit 4 and 5 = " + imm.substring(2, 4) + "\n     from SRC*");
        Text textBit5 = new Text(X + 300, Y + 10, "Bit 2 and 3 = " + imm.substring(4, 6) + "\n     from DES*");
        Text textBit4 = new Text(X + 450, Y + 10, "Bit 0 and 1 = " + imm.substring(6) + "\n     from DES*");
        Text text0 = new Text(X, Y + 165, "" + result.substring(0, 8));
        Text text1 = new Text(X + 150, Y + 165, "" + result.substring(8, 16));
        Text text2 = new Text(X + 300, Y + 165, "" + result.substring(16, 24));
        Text text3 = new Text(X + 450, Y + 165, "" + result.substring(24));

        this.root.getChildren().addAll(textBit7, textBit6, textBit5, textBit4, text0, text1, text2, text3);

        TranslateTransition bit11Transition = new TranslateTransition();
        TranslateTransition bit10Transition = new TranslateTransition();
        TranslateTransition bit9Transition = new TranslateTransition();
        TranslateTransition bit8Transition = new TranslateTransition();
        TranslateTransition bit7Transition = new TranslateTransition();
        TranslateTransition bit6Transition = new TranslateTransition();
        TranslateTransition bit5Transition = new TranslateTransition();
        TranslateTransition bit4Transition = new TranslateTransition();
        TranslateTransition bit3Transition = new TranslateTransition();
        TranslateTransition bit2Transition = new TranslateTransition();
        TranslateTransition bit1Transition = new TranslateTransition();
        TranslateTransition bit0Transition = new TranslateTransition();

        bit7Transition.setNode(textBit7);
        bit7Transition.fromXProperty().bind(des1.translateXProperty()
                .add(des1.getLayoutBounds().getWidth() - 140)
                .add((des1.getLayoutBounds().getWidth() - textBit7.getLayoutBounds().getWidth()) / 2));
        bit7Transition.fromYProperty().bind(des1.translateYProperty()
                .add(des1.getLayoutBounds().getHeight() / 3));
        bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

        bit6Transition.setNode(textBit6);
        bit6Transition.fromXProperty().bind(des2.translateXProperty()
                .add(des2.getLayoutBounds().getWidth() - 140)
                .add((des2.getLayoutBounds().getWidth() - textBit6.getLayoutBounds().getWidth()) / 2));
        bit6Transition.fromYProperty().bind(des2.translateYProperty()
                .add(des2.getLayoutBounds().getHeight() / 3));
        bit7Transition.toXProperty().bind(bit6Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit6Transition.fromYProperty());

        bit5Transition.setNode(textBit5);
        bit5Transition.fromXProperty().bind(des3.translateXProperty()
                .add(des3.getLayoutBounds().getWidth() - 140)
                .add((des3.getLayoutBounds().getWidth() - textBit5.getLayoutBounds().getWidth()) / 2));
        bit5Transition.fromYProperty().bind(des3.translateYProperty()
                .add(des3.getLayoutBounds().getHeight() / 3));
        bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
        bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

        bit4Transition.setNode(textBit4);
        bit4Transition.fromXProperty().bind(des4.translateXProperty()
                .add(des4.getLayoutBounds().getWidth() - 140)
                .add((des4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
        bit4Transition.fromYProperty().bind(des4.translateYProperty()
                .add(des4.getLayoutBounds().getHeight() / 3));
        bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
        bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());

//        bit3Transition.setNode(textBit3);
//        bit3Transition.fromXProperty().bind(src1.translateXProperty()
//                .add(src1.getLayoutBounds().getWidth() - 140)
//                .add((src1.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
//        bit3Transition.fromYProperty().bind(src1.translateYProperty()
//                .add(src1.getLayoutBounds().getHeight() / 3));
//        bit3Transition.toXProperty().bind(bit3Transition.fromXProperty());
//        bit3Transition.toYProperty().bind(bit3Transition.fromYProperty());
//
//        bit2Transition.setNode(textBit2);
//        bit2Transition.fromXProperty().bind(src2.translateXProperty()
//                .add(src2.getLayoutBounds().getWidth() - 140)
//                .add((src2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
//        bit2Transition.fromYProperty().bind(src2.translateYProperty()
//                .add(src2.getLayoutBounds().getHeight() / 3));
//        bit2Transition.toXProperty().bind(bit2Transition.fromXProperty());
//        bit2Transition.toYProperty().bind(bit2Transition.fromYProperty());
//
//        bit1Transition.setNode(textBit1);
//        bit1Transition.fromXProperty().bind(src3.translateXProperty()
//                .add(src3.getLayoutBounds().getWidth() - 140)
//                .add((src3.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
//        bit1Transition.fromYProperty().bind(src3.translateYProperty()
//                .add(src3.getLayoutBounds().getHeight() / 3));
//        bit1Transition.toXProperty().bind(bit1Transition.fromXProperty());
//        bit1Transition.toYProperty().bind(bit1Transition.fromYProperty());
//
//        bit0Transition.setNode(textBit0);
//        bit0Transition.fromXProperty().bind(src4.translateXProperty()
//                .add(src4.getLayoutBounds().getWidth() - 140)
//                .add((src4.getLayoutBounds().getWidth() - textBit0.getLayoutBounds().getWidth()) / 2));
//        bit0Transition.fromYProperty().bind(src4.translateYProperty()
//                .add(src4.getLayoutBounds().getHeight() / 3));
//        bit0Transition.toXProperty().bind(bit0Transition.fromXProperty());
//        bit0Transition.toYProperty().bind(bit0Transition.fromYProperty());

        bit8Transition.setNode(text0);
        bit8Transition.fromXProperty().bind(res1.translateXProperty()
                .add(res1.getLayoutBounds().getWidth() - 140)
                .add((res1.getLayoutBounds().getWidth() - text0.getLayoutBounds().getWidth()) / 2));
        bit8Transition.fromYProperty().bind(res1.translateYProperty()
                .add(res1.getLayoutBounds().getHeight() / 3));
        bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
        bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

        bit9Transition.setNode(text1);
        bit9Transition.fromXProperty().bind(res2.translateXProperty()
                .add(res2.getLayoutBounds().getWidth() - 140)
                .add((res2.getLayoutBounds().getWidth() - text1.getLayoutBounds().getWidth()) / 2));
        bit9Transition.fromYProperty().bind(res2.translateYProperty()
                .add(res2.getLayoutBounds().getHeight() / 3));
        bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
        bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

        bit10Transition.setNode(text2);
        bit10Transition.fromXProperty().bind(res3.translateXProperty()
                .add(res3.getLayoutBounds().getWidth() - 140)
                .add((res3.getLayoutBounds().getWidth() - text2.getLayoutBounds().getWidth()) / 2));
        bit10Transition.fromYProperty().bind(res3.translateYProperty()
                .add(res3.getLayoutBounds().getHeight() / 3));
        bit10Transition.toXProperty().bind(bit10Transition.fromXProperty());
        bit10Transition.toYProperty().bind(bit10Transition.fromYProperty());

        bit11Transition.setNode(text3);
        bit11Transition.fromXProperty().bind(res4.translateXProperty()
                .add(res4.getLayoutBounds().getWidth() - 140)
                .add((res4.getLayoutBounds().getWidth() - text3.getLayoutBounds().getWidth()) / 2));
        bit11Transition.fromYProperty().bind(res4.translateYProperty()
                .add(res4.getLayoutBounds().getHeight() / 3));
        bit11Transition.toXProperty().bind(bit11Transition.fromXProperty());
        bit11Transition.toYProperty().bind(bit11Transition.fromYProperty());

        bit11Transition.play();
        bit10Transition.play();
        bit9Transition.play();
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
