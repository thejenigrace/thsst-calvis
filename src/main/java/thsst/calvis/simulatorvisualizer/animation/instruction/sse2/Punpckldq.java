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
public class Punpckldq extends CalvisAnimation {

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

        int desSize = registers.getBitSize(tokens[0]);

        // CODE HERE
        if( desSize == 64) {
            int width = 100;
            int height = 30;
            Rectangle des1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            Rectangle src1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            des1.setX(X);
            des1.setY(Y);
            des2.setX(X + 110);
            des2.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 110);
            src2.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 100);
            res2.setX(X + 110);
            res2.setY(Y + 100);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, src1, src2, res1, res2, label1, label2, label3);

            Text textBit1;
            Text textBit2;

            Text textBit9;
            Text textBit10;

            Text textBit17;
            Text textBit18;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 8));
            textBit2 = new Text(X + 110, Y + 10, "" + value0.substring(8));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 8));
            textBit10 = new Text(X + 110, Y + 60, "" + value1.substring(8));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 8));
            textBit18 = new Text(X + 110, Y + 110, "" + result.substring(8));

            this.root.getChildren().addAll(textBit1, textBit2,
                    textBit9, textBit10,
                    textBit17, textBit18);

            TranslateTransition bit7Transition = new TranslateTransition();
            TranslateTransition bit6Transition = new TranslateTransition();

            TranslateTransition bit8Transition = new TranslateTransition();
            TranslateTransition bit9Transition = new TranslateTransition();

            TranslateTransition bit16Transition = new TranslateTransition();
            TranslateTransition bit17Transition = new TranslateTransition();

            bit7Transition.setNode(textBit1);
            bit7Transition.fromXProperty().bind(des1.translateXProperty()
                    .add(des1.getLayoutBounds().getWidth() - 100 )
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() -100 )
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());



            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 100 )
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 100 )
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());



            bit16Transition.setNode(textBit17);
            bit16Transition.fromXProperty().bind(res1.translateXProperty()
                    .add(res1.getLayoutBounds().getWidth() - 100)
                    .add((res1.getLayoutBounds().getWidth() - textBit17.getLayoutBounds().getWidth()) / 2));
            bit16Transition.fromYProperty().bind(res1.translateYProperty()
                    .add(res1.getLayoutBounds().getHeight() / 3));
            bit16Transition.toXProperty().bind(bit16Transition.fromXProperty());
            bit16Transition.toYProperty().bind(bit16Transition.fromYProperty());

            bit17Transition.setNode(textBit18);
            bit17Transition.fromXProperty().bind(res2.translateXProperty()
                    .add(res2.getLayoutBounds().getWidth() -100)
                    .add((res2.getLayoutBounds().getWidth() - textBit18.getLayoutBounds().getWidth()) / 2));
            bit17Transition.fromYProperty().bind(res2.translateYProperty()
                    .add(res2.getLayoutBounds().getHeight() / 3));
            bit17Transition.toXProperty().bind(bit17Transition.fromXProperty());
            bit17Transition.toYProperty().bind(bit17Transition.fromYProperty());



            bit17Transition.play();
            bit16Transition.play();
            bit9Transition.play();
            bit8Transition.play();
            bit7Transition.play();
            bit6Transition.play();
        }
        else {
            int width = 100;
            int height = 30;
            Rectangle des1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des3 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            Rectangle src1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src4 = new Rectangle(width, height, Color.web("#3498db", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            des1.setX(X);
            des1.setY(Y);
            des2.setX(X + 110);
            des2.setY(Y);
            des3.setX(X + 220);
            des3.setY(Y);
            des4.setX(X + 330);
            des4.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 110);
            src2.setY(Y + 50);
            src3.setX(X + 220);
            src3.setY(Y + 50);
            src4.setX(X + 330);
            src4.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 100);
            res2.setX(X + 110);
            res2.setY(Y + 100);
            res3.setX(X + 220);
            res3.setY(Y + 100);
            res4.setX(X + 330);
            res4.setY(Y + 100);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, des3, des4,
                    src1, src2, src3, src4,
                    res1, res2, res3, res4,
                    label1, label2, label3);

            Text textBit1;
            Text textBit2;
            Text textBit3;
            Text textBit4;

            Text textBit9;
            Text textBit10;
            Text textBit11;
            Text textBit12;

            Text textBit17;
            Text textBit18;
            Text textBit19;
            Text textBit20;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 8));
            textBit2 = new Text(X + 110, Y + 10, "" + value0.substring(8, 16));
            textBit3 = new Text(X + 220, Y + 10, "" + value0.substring(16, 24));
            textBit4 = new Text(X + 330, Y + 10, "" + value0.substring(24));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 8));
            textBit10 = new Text(X + 110, Y + 60, "" + value1.substring(8, 16));
            textBit11 = new Text(X + 220, Y + 60, "" + value1.substring(16, 24));
            textBit12 = new Text(X + 330, Y + 60, "" + value1.substring(24));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 8));
            textBit18 = new Text(X + 110, Y + 110, "" + result.substring(8, 16));
            textBit19 = new Text(X + 220, Y + 110, "" + result.substring(16, 24));
            textBit20 = new Text(X + 330, Y + 110, "" + result.substring(24));

            this.root.getChildren().addAll(textBit1, textBit2, textBit3, textBit4,
                    textBit9, textBit10, textBit11, textBit12,
                    textBit17, textBit18, textBit19, textBit20);

            TranslateTransition bit7Transition = new TranslateTransition();
            TranslateTransition bit6Transition = new TranslateTransition();
            TranslateTransition bit5Transition = new TranslateTransition();
            TranslateTransition bit4Transition = new TranslateTransition();

            TranslateTransition bit8Transition = new TranslateTransition();
            TranslateTransition bit9Transition = new TranslateTransition();
            TranslateTransition bit10Transition = new TranslateTransition();
            TranslateTransition bit11Transition = new TranslateTransition();

            TranslateTransition bit16Transition = new TranslateTransition();
            TranslateTransition bit17Transition = new TranslateTransition();
            TranslateTransition bit18Transition = new TranslateTransition();
            TranslateTransition bit19Transition = new TranslateTransition();

            bit7Transition.setNode(textBit1);
            bit7Transition.fromXProperty().bind(des1.translateXProperty()
                    .add(des1.getLayoutBounds().getWidth() - 100)
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() - 100)
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());

            bit5Transition.setNode(textBit3);
            bit5Transition.fromXProperty().bind(des3.translateXProperty()
                    .add(des3.getLayoutBounds().getWidth() - 100)
                    .add((des3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
            bit5Transition.fromYProperty().bind(des3.translateYProperty()
                    .add(des3.getLayoutBounds().getHeight() / 3));
            bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
            bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

            bit4Transition.setNode(textBit4);
            bit4Transition.fromXProperty().bind(des4.translateXProperty()
                    .add(des4.getLayoutBounds().getWidth() - 100)
                    .add((des4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
            bit4Transition.fromYProperty().bind(des4.translateYProperty()
                    .add(des4.getLayoutBounds().getHeight() / 3));
            bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
            bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());




            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 100)
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 100)
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

            bit10Transition.setNode(textBit11);
            bit10Transition.fromXProperty().bind(src3.translateXProperty()
                    .add(src3.getLayoutBounds().getWidth() - 100)
                    .add((src3.getLayoutBounds().getWidth() - textBit11.getLayoutBounds().getWidth()) / 2));
            bit10Transition.fromYProperty().bind(src3.translateYProperty()
                    .add(src3.getLayoutBounds().getHeight() / 3));
            bit10Transition.toXProperty().bind(bit10Transition.fromXProperty());
            bit10Transition.toYProperty().bind(bit10Transition.fromYProperty());

            bit11Transition.setNode(textBit12);
            bit11Transition.fromXProperty().bind(src4.translateXProperty()
                    .add(src4.getLayoutBounds().getWidth() - 100)
                    .add((src4.getLayoutBounds().getWidth() - textBit12.getLayoutBounds().getWidth()) / 2));
            bit11Transition.fromYProperty().bind(src4.translateYProperty()
                    .add(src4.getLayoutBounds().getHeight() / 3));
            bit11Transition.toXProperty().bind(bit11Transition.fromXProperty());
            bit11Transition.toYProperty().bind(bit11Transition.fromYProperty());



            bit16Transition.setNode(textBit17);
            bit16Transition.fromXProperty().bind(res1.translateXProperty()
                    .add(res1.getLayoutBounds().getWidth() - 100)
                    .add((res1.getLayoutBounds().getWidth() - textBit17.getLayoutBounds().getWidth()) / 2));
            bit16Transition.fromYProperty().bind(res1.translateYProperty()
                    .add(res1.getLayoutBounds().getHeight() / 3));
            bit16Transition.toXProperty().bind(bit16Transition.fromXProperty());
            bit16Transition.toYProperty().bind(bit16Transition.fromYProperty());

            bit17Transition.setNode(textBit18);
            bit17Transition.fromXProperty().bind(res2.translateXProperty()
                    .add(res2.getLayoutBounds().getWidth() - 100)
                    .add((res2.getLayoutBounds().getWidth() - textBit18.getLayoutBounds().getWidth()) / 2));
            bit17Transition.fromYProperty().bind(res2.translateYProperty()
                    .add(res2.getLayoutBounds().getHeight() / 3));
            bit17Transition.toXProperty().bind(bit17Transition.fromXProperty());
            bit17Transition.toYProperty().bind(bit17Transition.fromYProperty());

            bit18Transition.setNode(textBit19);
            bit18Transition.fromXProperty().bind(res3.translateXProperty()
                    .add(res3.getLayoutBounds().getWidth() - 100)
                    .add((res3.getLayoutBounds().getWidth() - textBit19.getLayoutBounds().getWidth()) / 2));
            bit18Transition.fromYProperty().bind(res3.translateYProperty()
                    .add(res3.getLayoutBounds().getHeight() / 3));
            bit18Transition.toXProperty().bind(bit18Transition.fromXProperty());
            bit18Transition.toYProperty().bind(bit18Transition.fromYProperty());

            bit19Transition.setNode(textBit20);
            bit19Transition.fromXProperty().bind(res4.translateXProperty()
                    .add(res4.getLayoutBounds().getWidth() - 100)
                    .add((res4.getLayoutBounds().getWidth() - textBit20.getLayoutBounds().getWidth()) / 2));
            bit19Transition.fromYProperty().bind(res4.translateYProperty()
                    .add(res4.getLayoutBounds().getHeight() / 3));
            bit19Transition.toXProperty().bind(bit19Transition.fromXProperty());
            bit19Transition.toYProperty().bind(bit19Transition.fromYProperty());


            bit19Transition.play();
            bit18Transition.play();
            bit17Transition.play();
            bit16Transition.play();

            bit11Transition.play();
            bit10Transition.play();
            bit9Transition.play();
            bit8Transition.play();

            bit7Transition.play();
            bit6Transition.play();
            bit5Transition.play();
            bit4Transition.play();
        }

    }
}
