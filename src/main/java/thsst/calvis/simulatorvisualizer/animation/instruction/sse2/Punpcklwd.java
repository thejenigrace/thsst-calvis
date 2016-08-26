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
public class Punpcklwd extends CalvisAnimation {

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
            int width = 60;
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
            des2.setX(X + 70);
            des2.setY(Y);
            des3.setX(X + 140);
            des3.setY(Y);
            des4.setX(X + 210);
            des4.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 70);
            src2.setY(Y + 50);
            src3.setX(X + 140);
            src3.setY(Y + 50);
            src4.setX(X + 210);
            src4.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 100);
            res2.setX(X + 70);
            res2.setY(Y + 100);
            res3.setX(X + 140);
            res3.setY(Y + 100);
            res4.setX(X + 210);
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
            this.root.getChildren().addAll(des1, des2, des3, des4, src1, src2, src3, src4, res1, res2, res3, res4, label1, label2, label3);

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

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 4));
            textBit2 = new Text(X + 70, Y + 10, "" + value0.substring(4, 8));
            textBit3 = new Text(X + 140, Y + 10, "" + value0.substring(8, 12));
            textBit4 = new Text(X + 210, Y + 10, "" + value0.substring(12));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 4));
            textBit10 = new Text(X + 70, Y + 60, "" + value1.substring(4, 8));
            textBit11 = new Text(X + 140, Y + 60, "" + value1.substring(8, 12));
            textBit12 = new Text(X + 210, Y + 60, "" + value1.substring(12));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 4));
            textBit18 = new Text(X + 70, Y + 110, "" + result.substring(4, 8));
            textBit19 = new Text(X + 140, Y + 110, "" + result.substring(8, 12));
            textBit20 = new Text(X + 210, Y + 110, "" + result.substring(12));

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
                    .add(des1.getLayoutBounds().getWidth() - 60)
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() - 60)
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());

            bit5Transition.setNode(textBit3);
            bit5Transition.fromXProperty().bind(des3.translateXProperty()
                    .add(des3.getLayoutBounds().getWidth() - 60)
                    .add((des3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
            bit5Transition.fromYProperty().bind(des3.translateYProperty()
                    .add(des3.getLayoutBounds().getHeight() / 3));
            bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
            bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

            bit4Transition.setNode(textBit4);
            bit4Transition.fromXProperty().bind(des4.translateXProperty()
                    .add(des4.getLayoutBounds().getWidth() - 60)
                    .add((des4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
            bit4Transition.fromYProperty().bind(des4.translateYProperty()
                    .add(des4.getLayoutBounds().getHeight() / 3));
            bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
            bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());



            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 60)
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 60)
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

            bit10Transition.setNode(textBit11);
            bit10Transition.fromXProperty().bind(src3.translateXProperty()
                    .add(src3.getLayoutBounds().getWidth() - 60)
                    .add((src3.getLayoutBounds().getWidth() - textBit11.getLayoutBounds().getWidth()) / 2));
            bit10Transition.fromYProperty().bind(src3.translateYProperty()
                    .add(src3.getLayoutBounds().getHeight() / 3));
            bit10Transition.toXProperty().bind(bit10Transition.fromXProperty());
            bit10Transition.toYProperty().bind(bit10Transition.fromYProperty());

            bit11Transition.setNode(textBit12);
            bit11Transition.fromXProperty().bind(src4.translateXProperty()
                    .add(src4.getLayoutBounds().getWidth() - 60)
                    .add((src4.getLayoutBounds().getWidth() - textBit12.getLayoutBounds().getWidth()) / 2));
            bit11Transition.fromYProperty().bind(src4.translateYProperty()
                    .add(src4.getLayoutBounds().getHeight() / 3));
            bit11Transition.toXProperty().bind(bit11Transition.fromXProperty());
            bit11Transition.toYProperty().bind(bit11Transition.fromYProperty());


            bit16Transition.setNode(textBit17);
            bit16Transition.fromXProperty().bind(res1.translateXProperty()
                    .add(res1.getLayoutBounds().getWidth() - 60)
                    .add((res1.getLayoutBounds().getWidth() - textBit17.getLayoutBounds().getWidth()) / 2));
            bit16Transition.fromYProperty().bind(res1.translateYProperty()
                    .add(res1.getLayoutBounds().getHeight() / 3));
            bit16Transition.toXProperty().bind(bit16Transition.fromXProperty());
            bit16Transition.toYProperty().bind(bit16Transition.fromYProperty());

            bit17Transition.setNode(textBit18);
            bit17Transition.fromXProperty().bind(res2.translateXProperty()
                    .add(res2.getLayoutBounds().getWidth() - 60)
                    .add((res2.getLayoutBounds().getWidth() - textBit18.getLayoutBounds().getWidth()) / 2));
            bit17Transition.fromYProperty().bind(res2.translateYProperty()
                    .add(res2.getLayoutBounds().getHeight() / 3));
            bit17Transition.toXProperty().bind(bit17Transition.fromXProperty());
            bit17Transition.toYProperty().bind(bit17Transition.fromYProperty());

            bit18Transition.setNode(textBit19);
            bit18Transition.fromXProperty().bind(res3.translateXProperty()
                    .add(res3.getLayoutBounds().getWidth() - 60)
                    .add((res3.getLayoutBounds().getWidth() - textBit19.getLayoutBounds().getWidth()) / 2));
            bit18Transition.fromYProperty().bind(res3.translateYProperty()
                    .add(res3.getLayoutBounds().getHeight() / 3));
            bit18Transition.toXProperty().bind(bit18Transition.fromXProperty());
            bit18Transition.toYProperty().bind(bit18Transition.fromYProperty());

            bit19Transition.setNode(textBit20);
            bit19Transition.fromXProperty().bind(res4.translateXProperty()
                    .add(res4.getLayoutBounds().getWidth() - 60)
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
        else {
            int width = 60;
            int height = 30;
            Rectangle des1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des4 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des5 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des6 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des7 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des8 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            Rectangle src1 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src2 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src3 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src4 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src5 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src6 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src7 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src8 = new Rectangle(width, height, Color.web("#3498db", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res5 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res6 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res7 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res8 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

            des1.setX(X);
            des1.setY(Y);
            des2.setX(X + 70);
            des2.setY(Y);
            des3.setX(X + 140);
            des3.setY(Y);
            des4.setX(X + 210);
            des4.setY(Y);
            des5.setX(X + 280);
            des5.setY(Y);
            des6.setX(X + 350);
            des6.setY(Y);
            des7.setX(X + 420);
            des7.setY(Y);
            des8.setX(X + 490);
            des8.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 70);
            src2.setY(Y + 50);
            src3.setX(X + 140);
            src3.setY(Y + 50);
            src4.setX(X + 210);
            src4.setY(Y + 50);
            src5.setX(X + 280);
            src5.setY(Y + 50);
            src6.setX(X + 350);
            src6.setY(Y + 50);
            src7.setX(X + 420);
            src7.setY(Y + 50);
            src8.setX(X + 490);
            src8.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 100);
            res2.setX(X + 70);
            res2.setY(Y + 100);
            res3.setX(X + 140);
            res3.setY(Y + 100);
            res4.setX(X + 210);
            res4.setY(Y + 100);
            res5.setX(X + 280);
            res5.setY(Y + 100);
            res6.setX(X + 350);
            res6.setY(Y + 100);
            res7.setX(X + 420);
            res7.setY(Y + 100);
            res8.setX(X + 490);
            res8.setY(Y + 100);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, des3, des4, des5, des6, des7, des8,
                    src1, src2, src3, src4, src5, src6, src7, src8,
                    res1, res2, res3, res4, res5, res6, res7, res8,
                    label1, label2, label3);

            Text textBit1;
            Text textBit2;
            Text textBit3;
            Text textBit4;
            Text textBit5;
            Text textBit6;
            Text textBit7;
            Text textBit8;

            Text textBit9;
            Text textBit10;
            Text textBit11;
            Text textBit12;
            Text textBit13;
            Text textBit14;
            Text textBit15;
            Text textBit16;

            Text textBit17;
            Text textBit18;
            Text textBit19;
            Text textBit20;
            Text textBit21;
            Text textBit22;
            Text textBit23;
            Text textBit24;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 4));
            textBit2 = new Text(X + 70, Y + 10, "" + value0.substring(4, 8));
            textBit3 = new Text(X + 140, Y + 10, "" + value0.substring(8, 12));
            textBit4 = new Text(X + 210, Y + 10, "" + value0.substring(12, 16));
            textBit5 = new Text(X + 280, Y + 10, "" + value0.substring(16, 20));
            textBit6 = new Text(X + 350, Y + 10, "" + value0.substring(20, 24));
            textBit7 = new Text(X + 420, Y + 10, "" + value0.substring(24, 28));
            textBit8 = new Text(X + 490, Y + 10, "" + value0.substring(28));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 4));
            textBit10 = new Text(X + 70, Y + 60, "" + value1.substring(4, 8));
            textBit11 = new Text(X + 140, Y + 60, "" + value1.substring(8, 12));
            textBit12 = new Text(X + 210, Y + 60, "" + value1.substring(12, 16));
            textBit13 = new Text(X + 280, Y + 60, "" + value1.substring(16, 20));
            textBit14 = new Text(X + 350, Y + 60, "" + value1.substring(20, 24));
            textBit15 = new Text(X + 420, Y + 60, "" + value1.substring(24, 28));
            textBit16 = new Text(X + 490, Y + 60, "" + value1.substring(28));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 4));
            textBit18 = new Text(X + 70, Y + 110, "" + result.substring(4, 8));
            textBit19 = new Text(X + 140, Y + 110, "" + result.substring(8, 12));
            textBit20 = new Text(X + 210, Y + 110, "" + result.substring(12, 16));
            textBit21 = new Text(X + 280, Y + 110, "" + result.substring(16, 20));
            textBit22 = new Text(X + 350, Y + 110, "" + result.substring(20, 24));
            textBit23 = new Text(X + 420, Y + 110, "" + result.substring(24, 28));
            textBit24 = new Text(X + 490, Y + 110, "" + result.substring(28));

            this.root.getChildren().addAll(textBit1, textBit2, textBit3, textBit4, textBit5, textBit6, textBit7, textBit8,
                    textBit9, textBit10, textBit11, textBit12, textBit13, textBit14, textBit15, textBit16,
                    textBit17, textBit18, textBit19, textBit20, textBit21, textBit22, textBit23, textBit24);

            TranslateTransition bit7Transition = new TranslateTransition();
            TranslateTransition bit6Transition = new TranslateTransition();
            TranslateTransition bit5Transition = new TranslateTransition();
            TranslateTransition bit4Transition = new TranslateTransition();
            TranslateTransition bit3Transition = new TranslateTransition();
            TranslateTransition bit2Transition = new TranslateTransition();
            TranslateTransition bit1Transition = new TranslateTransition();
            TranslateTransition bit0Transition = new TranslateTransition();

            TranslateTransition bit8Transition = new TranslateTransition();
            TranslateTransition bit9Transition = new TranslateTransition();
            TranslateTransition bit10Transition = new TranslateTransition();
            TranslateTransition bit11Transition = new TranslateTransition();
            TranslateTransition bit12Transition = new TranslateTransition();
            TranslateTransition bit13Transition = new TranslateTransition();
            TranslateTransition bit14Transition = new TranslateTransition();
            TranslateTransition bit15Transition = new TranslateTransition();

            TranslateTransition bit16Transition = new TranslateTransition();
            TranslateTransition bit17Transition = new TranslateTransition();
            TranslateTransition bit18Transition = new TranslateTransition();
            TranslateTransition bit19Transition = new TranslateTransition();
            TranslateTransition bit20Transition = new TranslateTransition();
            TranslateTransition bit21Transition = new TranslateTransition();
            TranslateTransition bit22Transition = new TranslateTransition();
            TranslateTransition bit23Transition = new TranslateTransition();

            bit7Transition.setNode(textBit1);
            bit7Transition.fromXProperty().bind(des1.translateXProperty()
                    .add(des1.getLayoutBounds().getWidth() - 60)
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() - 60)
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());

            bit5Transition.setNode(textBit3);
            bit5Transition.fromXProperty().bind(des3.translateXProperty()
                    .add(des3.getLayoutBounds().getWidth() - 60)
                    .add((des3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
            bit5Transition.fromYProperty().bind(des3.translateYProperty()
                    .add(des3.getLayoutBounds().getHeight() / 3));
            bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
            bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

            bit4Transition.setNode(textBit4);
            bit4Transition.fromXProperty().bind(des4.translateXProperty()
                    .add(des4.getLayoutBounds().getWidth() - 60)
                    .add((des4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
            bit4Transition.fromYProperty().bind(des4.translateYProperty()
                    .add(des4.getLayoutBounds().getHeight() / 3));
            bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
            bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());

            bit3Transition.setNode(textBit5);
            bit3Transition.fromXProperty().bind(des5.translateXProperty()
                    .add(des5.getLayoutBounds().getWidth() - 60)
                    .add((des5.getLayoutBounds().getWidth() - textBit5.getLayoutBounds().getWidth()) / 2));
            bit3Transition.fromYProperty().bind(des5.translateYProperty()
                    .add(des5.getLayoutBounds().getHeight() / 3));
            bit3Transition.toXProperty().bind(bit3Transition.fromXProperty());
            bit3Transition.toYProperty().bind(bit3Transition.fromYProperty());

            bit2Transition.setNode(textBit6);
            bit2Transition.fromXProperty().bind(des6.translateXProperty()
                    .add(des6.getLayoutBounds().getWidth() - 60)
                    .add((des6.getLayoutBounds().getWidth() - textBit6.getLayoutBounds().getWidth()) / 2));
            bit2Transition.fromYProperty().bind(des6.translateYProperty()
                    .add(des6.getLayoutBounds().getHeight() / 3));
            bit2Transition.toXProperty().bind(bit2Transition.fromXProperty());
            bit2Transition.toYProperty().bind(bit2Transition.fromYProperty());

            bit1Transition.setNode(textBit7);
            bit1Transition.fromXProperty().bind(des7.translateXProperty()
                    .add(des7.getLayoutBounds().getWidth() - 60)
                    .add((des7.getLayoutBounds().getWidth() - textBit7.getLayoutBounds().getWidth()) / 2));
            bit1Transition.fromYProperty().bind(des7.translateYProperty()
                    .add(des7.getLayoutBounds().getHeight() / 3));
            bit1Transition.toXProperty().bind(bit1Transition.fromXProperty());
            bit1Transition.toYProperty().bind(bit1Transition.fromYProperty());

            bit0Transition.setNode(textBit8);
            bit0Transition.fromXProperty().bind(des8.translateXProperty()
                    .add(des8.getLayoutBounds().getWidth() - 60)
                    .add((des8.getLayoutBounds().getWidth() - textBit8.getLayoutBounds().getWidth()) / 2));
            bit0Transition.fromYProperty().bind(des8.translateYProperty()
                    .add(des8.getLayoutBounds().getHeight() / 3));
            bit0Transition.toXProperty().bind(bit0Transition.fromXProperty());
            bit0Transition.toYProperty().bind(bit0Transition.fromYProperty());




            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 60)
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 60)
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

            bit10Transition.setNode(textBit11);
            bit10Transition.fromXProperty().bind(src3.translateXProperty()
                    .add(src3.getLayoutBounds().getWidth() - 60)
                    .add((src3.getLayoutBounds().getWidth() - textBit11.getLayoutBounds().getWidth()) / 2));
            bit10Transition.fromYProperty().bind(src3.translateYProperty()
                    .add(src3.getLayoutBounds().getHeight() / 3));
            bit10Transition.toXProperty().bind(bit10Transition.fromXProperty());
            bit10Transition.toYProperty().bind(bit10Transition.fromYProperty());

            bit11Transition.setNode(textBit12);
            bit11Transition.fromXProperty().bind(src4.translateXProperty()
                    .add(src4.getLayoutBounds().getWidth() - 60)
                    .add((src4.getLayoutBounds().getWidth() - textBit12.getLayoutBounds().getWidth()) / 2));
            bit11Transition.fromYProperty().bind(src4.translateYProperty()
                    .add(src4.getLayoutBounds().getHeight() / 3));
            bit11Transition.toXProperty().bind(bit11Transition.fromXProperty());
            bit11Transition.toYProperty().bind(bit11Transition.fromYProperty());

            bit12Transition.setNode(textBit13);
            bit12Transition.fromXProperty().bind(src5.translateXProperty()
                    .add(src5.getLayoutBounds().getWidth() - 60)
                    .add((src5.getLayoutBounds().getWidth() - textBit13.getLayoutBounds().getWidth()) / 2));
            bit12Transition.fromYProperty().bind(src5.translateYProperty()
                    .add(src5.getLayoutBounds().getHeight() / 3));
            bit12Transition.toXProperty().bind(bit12Transition.fromXProperty());
            bit12Transition.toYProperty().bind(bit12Transition.fromYProperty());

            bit13Transition.setNode(textBit14);
            bit13Transition.fromXProperty().bind(src6.translateXProperty()
                    .add(src6.getLayoutBounds().getWidth() - 60)
                    .add((src6.getLayoutBounds().getWidth() - textBit14.getLayoutBounds().getWidth()) / 2));
            bit13Transition.fromYProperty().bind(src6.translateYProperty()
                    .add(src6.getLayoutBounds().getHeight() / 3));
            bit13Transition.toXProperty().bind(bit13Transition.fromXProperty());
            bit13Transition.toYProperty().bind(bit13Transition.fromYProperty());

            bit14Transition.setNode(textBit15);
            bit14Transition.fromXProperty().bind(src7.translateXProperty()
                    .add(src7.getLayoutBounds().getWidth() - 60)
                    .add((src7.getLayoutBounds().getWidth() - textBit15.getLayoutBounds().getWidth()) / 2));
            bit14Transition.fromYProperty().bind(src7.translateYProperty()
                    .add(src7.getLayoutBounds().getHeight() / 3));
            bit14Transition.toXProperty().bind(bit14Transition.fromXProperty());
            bit14Transition.toYProperty().bind(bit14Transition.fromYProperty());

            bit15Transition.setNode(textBit16);
            bit15Transition.fromXProperty().bind(src8.translateXProperty()
                    .add(src8.getLayoutBounds().getWidth() - 60)
                    .add((src8.getLayoutBounds().getWidth() - textBit16.getLayoutBounds().getWidth()) / 2));
            bit15Transition.fromYProperty().bind(src8.translateYProperty()
                    .add(src8.getLayoutBounds().getHeight() / 3));
            bit15Transition.toXProperty().bind(bit15Transition.fromXProperty());
            bit15Transition.toYProperty().bind(bit15Transition.fromYProperty());




            bit16Transition.setNode(textBit17);
            bit16Transition.fromXProperty().bind(res1.translateXProperty()
                    .add(res1.getLayoutBounds().getWidth() - 60)
                    .add((res1.getLayoutBounds().getWidth() - textBit17.getLayoutBounds().getWidth()) / 2));
            bit16Transition.fromYProperty().bind(res1.translateYProperty()
                    .add(res1.getLayoutBounds().getHeight() / 3));
            bit16Transition.toXProperty().bind(bit16Transition.fromXProperty());
            bit16Transition.toYProperty().bind(bit16Transition.fromYProperty());

            bit17Transition.setNode(textBit18);
            bit17Transition.fromXProperty().bind(res2.translateXProperty()
                    .add(res2.getLayoutBounds().getWidth() - 60)
                    .add((res2.getLayoutBounds().getWidth() - textBit18.getLayoutBounds().getWidth()) / 2));
            bit17Transition.fromYProperty().bind(res2.translateYProperty()
                    .add(res2.getLayoutBounds().getHeight() / 3));
            bit17Transition.toXProperty().bind(bit17Transition.fromXProperty());
            bit17Transition.toYProperty().bind(bit17Transition.fromYProperty());

            bit18Transition.setNode(textBit19);
            bit18Transition.fromXProperty().bind(res3.translateXProperty()
                    .add(res3.getLayoutBounds().getWidth() - 60)
                    .add((res3.getLayoutBounds().getWidth() - textBit19.getLayoutBounds().getWidth()) / 2));
            bit18Transition.fromYProperty().bind(res3.translateYProperty()
                    .add(res3.getLayoutBounds().getHeight() / 3));
            bit18Transition.toXProperty().bind(bit18Transition.fromXProperty());
            bit18Transition.toYProperty().bind(bit18Transition.fromYProperty());

            bit19Transition.setNode(textBit20);
            bit19Transition.fromXProperty().bind(res4.translateXProperty()
                    .add(res4.getLayoutBounds().getWidth() - 60)
                    .add((res4.getLayoutBounds().getWidth() - textBit20.getLayoutBounds().getWidth()) / 2));
            bit19Transition.fromYProperty().bind(res4.translateYProperty()
                    .add(res4.getLayoutBounds().getHeight() / 3));
            bit19Transition.toXProperty().bind(bit19Transition.fromXProperty());
            bit19Transition.toYProperty().bind(bit19Transition.fromYProperty());

            bit20Transition.setNode(textBit21);
            bit20Transition.fromXProperty().bind(res5.translateXProperty()
                    .add(res5.getLayoutBounds().getWidth() - 60)
                    .add((res5.getLayoutBounds().getWidth() - textBit21.getLayoutBounds().getWidth()) / 2));
            bit20Transition.fromYProperty().bind(res5.translateYProperty()
                    .add(res5.getLayoutBounds().getHeight() / 3));
            bit20Transition.toXProperty().bind(bit20Transition.fromXProperty());
            bit20Transition.toYProperty().bind(bit20Transition.fromYProperty());

            bit21Transition.setNode(textBit22);
            bit21Transition.fromXProperty().bind(res6.translateXProperty()
                    .add(res6.getLayoutBounds().getWidth() - 60)
                    .add((res6.getLayoutBounds().getWidth() - textBit22.getLayoutBounds().getWidth()) / 2));
            bit21Transition.fromYProperty().bind(res6.translateYProperty()
                    .add(res6.getLayoutBounds().getHeight() / 3));
            bit21Transition.toXProperty().bind(bit21Transition.fromXProperty());
            bit21Transition.toYProperty().bind(bit21Transition.fromYProperty());

            bit22Transition.setNode(textBit23);
            bit22Transition.fromXProperty().bind(res7.translateXProperty()
                    .add(res7.getLayoutBounds().getWidth() - 60)
                    .add((res7.getLayoutBounds().getWidth() - textBit23.getLayoutBounds().getWidth()) / 2));
            bit22Transition.fromYProperty().bind(res7.translateYProperty()
                    .add(res7.getLayoutBounds().getHeight() / 3));
            bit22Transition.toXProperty().bind(bit22Transition.fromXProperty());
            bit22Transition.toYProperty().bind(bit22Transition.fromYProperty());

            bit23Transition.setNode(textBit24);
            bit23Transition.fromXProperty().bind(res8.translateXProperty()
                    .add(res8.getLayoutBounds().getWidth() - 60)
                    .add((res8.getLayoutBounds().getWidth() - textBit24.getLayoutBounds().getWidth()) / 2));
            bit23Transition.fromYProperty().bind(res8.translateYProperty()
                    .add(res8.getLayoutBounds().getHeight() / 3));
            bit23Transition.toXProperty().bind(bit23Transition.fromXProperty());
            bit23Transition.toYProperty().bind(bit23Transition.fromYProperty());


            bit23Transition.play();
            bit22Transition.play();
            bit21Transition.play();
            bit20Transition.play();
            bit19Transition.play();
            bit18Transition.play();
            bit17Transition.play();
            bit16Transition.play();

            bit15Transition.play();
            bit14Transition.play();
            bit13Transition.play();
            bit12Transition.play();
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
}
