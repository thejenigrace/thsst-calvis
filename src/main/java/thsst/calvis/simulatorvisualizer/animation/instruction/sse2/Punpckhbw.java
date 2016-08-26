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
public class Punpckhbw extends CalvisAnimation {

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
            Rectangle des1 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des3 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des5 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des6 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des7 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des8 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));

            Rectangle src1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src2 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src4 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src5 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src6 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src7 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src8 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));

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

//            String l = "";
//            if ( tokens[1].getType() == Token.REG ) {
//                l = tokens[1].getValue();
//            }
//            else if ( tokens[1].getType() == Token.MEM ) {
//                l = "[" + tokens[1].getValue() + "]";
//            }
            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, des3, des4, des5, des6, des7, des8, src1, src2, src3, src4, src5, src6, src7, src8, res1, res2, res3, res4, res5, res6, res7, res8, label1, label2, label3);

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

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 2));
            textBit2 = new Text(X + 70, Y + 10, "" + value0.substring(2, 4));
            textBit3 = new Text(X + 140, Y + 10, "" + value0.substring(4, 6));
            textBit4 = new Text(X + 210, Y + 10, "" + value0.substring(6, 8));
            textBit5 = new Text(X + 280, Y + 10, "" + value0.substring(8, 10));
            textBit6 = new Text(X + 350, Y + 10, "" + value0.substring(10, 12));
            textBit7 = new Text(X + 420, Y + 10, "" + value0.substring(12, 14));
            textBit8 = new Text(X + 490, Y + 10, "" + value0.substring(14));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 2));
            textBit10 = new Text(X + 70, Y + 60, "" + value1.substring(2, 4));
            textBit11 = new Text(X + 140, Y + 60, "" + value1.substring(4, 6));
            textBit12 = new Text(X + 210, Y + 60, "" + value1.substring(6, 8));
            textBit13 = new Text(X + 280, Y + 60, "" + value1.substring(8, 10));
            textBit14 = new Text(X + 350, Y + 60, "" + value1.substring(10, 12));
            textBit15 = new Text(X + 420, Y + 60, "" + value1.substring(12, 14));
            textBit16 = new Text(X + 490, Y + 60, "" + value1.substring(14));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 2));
            textBit18 = new Text(X + 70, Y + 110, "" + result.substring(2, 4));
            textBit19 = new Text(X + 140, Y + 110, "" + result.substring(4, 6));
            textBit20 = new Text(X + 210, Y + 110, "" + result.substring(6, 8));
            textBit21 = new Text(X + 280, Y + 110, "" + result.substring(8, 10));
            textBit22 = new Text(X + 350, Y + 110, "" + result.substring(10, 12));
            textBit23 = new Text(X + 420, Y + 110, "" + result.substring(12, 14));
            textBit24 = new Text(X + 490, Y + 110, "" + result.substring(14));

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
        else {
            int width = 60;
            int height = 30;
            Rectangle des1 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des3 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des5 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des6 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des7 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des8 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle des9 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des10 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des11 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des12 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des13 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des14 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des15 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle des16 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));

            Rectangle src1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src2 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src4 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src5 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src6 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src7 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src8 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle src9 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src10 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src11 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src12 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src13 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src14 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src15 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));
            Rectangle src16 = new Rectangle(width, height, Color.web("#7f8c8d", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res3 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res4 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res5 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res6 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res7 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res8 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res9 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res10 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res11 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res12 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res13 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res14 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));
            Rectangle res15 = new Rectangle(width, height, Color.web("#3498db", 1.0));
            Rectangle res16 = new Rectangle(width, height, Color.web("#1abc9c", 1.0));

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
            des9.setX(X + 560);
            des9.setY(Y);
            des10.setX(X + 630);
            des10.setY(Y);
            des11.setX(X + 700);
            des11.setY(Y);
            des12.setX(X + 770);
            des12.setY(Y);
            des13.setX(X + 840);
            des13.setY(Y);
            des14.setX(X + 910);
            des14.setY(Y);
            des15.setX(X + 980);
            des15.setY(Y);
            des16.setX(X + 1050);
            des16.setY(Y);

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
            src9.setX(X + 560);
            src9.setY(Y + 50);
            src10.setX(X + 630);
            src10.setY(Y + 50);
            src11.setX(X + 700);
            src11.setY(Y + 50);
            src12.setX(X + 770);
            src12.setY(Y + 50);
            src13.setX(X + 840);
            src13.setY(Y + 50);
            src14.setX(X + 910);
            src14.setY(Y + 50);
            src15.setX(X + 980);
            src15.setY(Y + 50);
            src16.setX(X + 1050);
            src16.setY(Y + 50);

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
            res9.setX(X + 560);
            res9.setY(Y + 100);
            res10.setX(X + 630);
            res10.setY(Y + 100);
            res11.setX(X + 700);
            res11.setY(Y + 100);
            res12.setX(X + 770);
            res12.setY(Y + 100);
            res13.setX(X + 840);
            res13.setY(Y + 100);
            res14.setX(X + 910);
            res14.setY(Y + 100);
            res15.setX(X + 980);
            res15.setY(Y + 100);
            res16.setX(X + 1050);
            res16.setY(Y + 100);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, des3, des4, des5, des6, des7, des8, des9, des10, des11, des12, des13, des14, des15, des16,
                    src1, src2, src3, src4, src5, src6, src7, src8, src9, src10, src11, src12, src13, src14, src15, src16,
                    res1, res2, res3, res4, res5, res6, res7, res8, res9, res10, res11, res12, res13, res14, res15, res16,
                    label1, label2, label3);

            Text textBit1;
            Text textBit2;
            Text textBit3;
            Text textBit4;
            Text textBit5;
            Text textBit6;
            Text textBit7;
            Text textBit8;
            Text textBit1a;
            Text textBit2b;
            Text textBit3c;
            Text textBit4d;
            Text textBit5e;
            Text textBit6f;
            Text textBit7g;
            Text textBit8h;

            Text textBit9;
            Text textBit10;
            Text textBit11;
            Text textBit12;
            Text textBit13;
            Text textBit14;
            Text textBit15;
            Text textBit16;
            Text textBit9a;
            Text textBit10b;
            Text textBit11c;
            Text textBit12d;
            Text textBit13e;
            Text textBit14f;
            Text textBit15g;
            Text textBit16h;

            Text textBit17;
            Text textBit18;
            Text textBit19;
            Text textBit20;
            Text textBit21;
            Text textBit22;
            Text textBit23;
            Text textBit24;
            Text textBit17a;
            Text textBit18b;
            Text textBit19c;
            Text textBit20d;
            Text textBit21e;
            Text textBit22f;
            Text textBit23g;
            Text textBit24h;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 2));
            textBit2 = new Text(X + 70, Y + 10, "" + value0.substring(2, 4));
            textBit3 = new Text(X + 140, Y + 10, "" + value0.substring(4, 6));
            textBit4 = new Text(X + 210, Y + 10, "" + value0.substring(6, 8));
            textBit5 = new Text(X + 280, Y + 10, "" + value0.substring(8, 10));
            textBit6 = new Text(X + 350, Y + 10, "" + value0.substring(10, 12));
            textBit7 = new Text(X + 420, Y + 10, "" + value0.substring(12, 14));
            textBit8 = new Text(X + 490, Y + 10, "" + value0.substring(14, 16));
            textBit1a = new Text(X + 560, Y + 10, "" + value0.substring(16, 18));
            textBit2b = new Text(X + 630, Y + 10, "" + value0.substring(18, 20));
            textBit3c = new Text(X + 700, Y + 10, "" + value0.substring(20, 22));
            textBit4d = new Text(X + 770, Y + 10, "" + value0.substring(22, 24));
            textBit5e = new Text(X + 840, Y + 10, "" + value0.substring(24, 26));
            textBit6f = new Text(X + 910, Y + 10, "" + value0.substring(26, 28));
            textBit7g = new Text(X + 980, Y + 10, "" + value0.substring(28, 30));
            textBit8h = new Text(X + 1050, Y + 10, "" + value0.substring(30));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 2));
            textBit10 = new Text(X + 70, Y + 60, "" + value1.substring(2, 4));
            textBit11 = new Text(X + 140, Y + 60, "" + value1.substring(4, 6));
            textBit12 = new Text(X + 210, Y + 60, "" + value1.substring(6, 8));
            textBit13 = new Text(X + 280, Y + 60, "" + value1.substring(8, 10));
            textBit14 = new Text(X + 350, Y + 60, "" + value1.substring(10, 12));
            textBit15 = new Text(X + 420, Y + 60, "" + value1.substring(12, 14));
            textBit16 = new Text(X + 490, Y + 60, "" + value1.substring(14, 16));
            textBit9a = new Text(X + 560, Y + 60, "" + value1.substring(16, 18));
            textBit10b = new Text(X + 630, Y + 60, "" + value1.substring(18, 20));
            textBit11c = new Text(X + 700, Y + 60, "" + value1.substring(20, 22));
            textBit12d = new Text(X + 770, Y + 60, "" + value1.substring(22, 24));
            textBit13e = new Text(X + 840, Y + 60, "" + value1.substring(24, 26));
            textBit14f = new Text(X + 910, Y + 60, "" + value1.substring(26, 28));
            textBit15g = new Text(X + 980, Y + 60, "" + value1.substring(28, 30));
            textBit16h = new Text(X + 1050, Y + 60, "" + value1.substring(30));

            textBit17 = new Text(X, Y + 110, "" + result.substring(0, 2));
            textBit18 = new Text(X + 70, Y + 110, "" + result.substring(2, 4));
            textBit19 = new Text(X + 140, Y + 110, "" + result.substring(4, 6));
            textBit20 = new Text(X + 210, Y + 110, "" + result.substring(6, 8));
            textBit21 = new Text(X + 280, Y + 110, "" + result.substring(8, 10));
            textBit22 = new Text(X + 350, Y + 110, "" + result.substring(10, 12));
            textBit23 = new Text(X + 420, Y + 110, "" + result.substring(12, 14));
            textBit24 = new Text(X + 490, Y + 110, "" + result.substring(14, 16));
            textBit17a = new Text(X + 560, Y + 110, "" + result.substring(16, 18));
            textBit18b = new Text(X + 630, Y + 110, "" + result.substring(18, 20));
            textBit19c = new Text(X + 700, Y + 110, "" + result.substring(20, 22));
            textBit20d = new Text(X + 770, Y + 110, "" + result.substring(22, 24));
            textBit21e = new Text(X + 840, Y + 110, "" + result.substring(24, 26));
            textBit22f = new Text(X + 910, Y + 110, "" + result.substring(26, 28));
            textBit23g = new Text(X + 980, Y + 110, "" + result.substring(28, 30));
            textBit24h = new Text(X + 1050, Y + 110, "" + result.substring(30));

            this.root.getChildren().addAll(textBit1, textBit2, textBit3, textBit4, textBit5, textBit6, textBit7, textBit8,
                    textBit1a, textBit2b, textBit3c, textBit4d, textBit5e, textBit6f, textBit7g, textBit8h,
                    textBit9, textBit10, textBit11, textBit12, textBit13, textBit14, textBit15, textBit16,
                    textBit9a, textBit10b, textBit11c, textBit12d, textBit13e, textBit14f, textBit15g, textBit16h,
                    textBit17, textBit18, textBit19, textBit20, textBit21, textBit22, textBit23, textBit24,
                    textBit17a, textBit18b, textBit19c, textBit20d, textBit21e, textBit22f, textBit23g, textBit24h);

            TranslateTransition bit7Transition = new TranslateTransition();
            TranslateTransition bit6Transition = new TranslateTransition();
            TranslateTransition bit5Transition = new TranslateTransition();
            TranslateTransition bit4Transition = new TranslateTransition();
            TranslateTransition bit3Transition = new TranslateTransition();
            TranslateTransition bit2Transition = new TranslateTransition();
            TranslateTransition bit1Transition = new TranslateTransition();
            TranslateTransition bit0Transition = new TranslateTransition();
            TranslateTransition bit7aTransition = new TranslateTransition();
            TranslateTransition bit6bTransition = new TranslateTransition();
            TranslateTransition bit5cTransition = new TranslateTransition();
            TranslateTransition bit4dTransition = new TranslateTransition();
            TranslateTransition bit3eTransition = new TranslateTransition();
            TranslateTransition bit2fTransition = new TranslateTransition();
            TranslateTransition bit1gTransition = new TranslateTransition();
            TranslateTransition bit0hTransition = new TranslateTransition();

            TranslateTransition bit8Transition = new TranslateTransition();
            TranslateTransition bit9Transition = new TranslateTransition();
            TranslateTransition bit10Transition = new TranslateTransition();
            TranslateTransition bit11Transition = new TranslateTransition();
            TranslateTransition bit12Transition = new TranslateTransition();
            TranslateTransition bit13Transition = new TranslateTransition();
            TranslateTransition bit14Transition = new TranslateTransition();
            TranslateTransition bit15Transition = new TranslateTransition();
            TranslateTransition bit8aTransition = new TranslateTransition();
            TranslateTransition bit9bTransition = new TranslateTransition();
            TranslateTransition bit10cTransition = new TranslateTransition();
            TranslateTransition bit11dTransition = new TranslateTransition();
            TranslateTransition bit12eTransition = new TranslateTransition();
            TranslateTransition bit13fTransition = new TranslateTransition();
            TranslateTransition bit14gTransition = new TranslateTransition();
            TranslateTransition bit15hTransition = new TranslateTransition();

            TranslateTransition bit16Transition = new TranslateTransition();
            TranslateTransition bit17Transition = new TranslateTransition();
            TranslateTransition bit18Transition = new TranslateTransition();
            TranslateTransition bit19Transition = new TranslateTransition();
            TranslateTransition bit20Transition = new TranslateTransition();
            TranslateTransition bit21Transition = new TranslateTransition();
            TranslateTransition bit22Transition = new TranslateTransition();
            TranslateTransition bit23Transition = new TranslateTransition();
            TranslateTransition bit16aTransition = new TranslateTransition();
            TranslateTransition bit17bTransition = new TranslateTransition();
            TranslateTransition bit18cTransition = new TranslateTransition();
            TranslateTransition bit19dTransition = new TranslateTransition();
            TranslateTransition bit20eTransition = new TranslateTransition();
            TranslateTransition bit21fTransition = new TranslateTransition();
            TranslateTransition bit22gTransition = new TranslateTransition();
            TranslateTransition bit23hTransition = new TranslateTransition();

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

            bit7aTransition.setNode(textBit1a);
            bit7aTransition.fromXProperty().bind(des8.translateXProperty()
                    .add(des8.getLayoutBounds().getWidth() - 60)
                    .add((des8.getLayoutBounds().getWidth() - textBit1a.getLayoutBounds().getWidth()) / 2));
            bit7aTransition.fromYProperty().bind(des8.translateYProperty()
                    .add(des8.getLayoutBounds().getHeight() / 3));
            bit7aTransition.toXProperty().bind(bit7aTransition.fromXProperty());
            bit7aTransition.toYProperty().bind(bit7aTransition.fromYProperty());

            bit6bTransition.setNode(textBit2b);
            bit6bTransition.fromXProperty().bind(des9.translateXProperty()
                    .add(des9.getLayoutBounds().getWidth() - 60)
                    .add((des9.getLayoutBounds().getWidth() - textBit2b.getLayoutBounds().getWidth()) / 2));
            bit6bTransition.fromYProperty().bind(des9.translateYProperty()
                    .add(des9.getLayoutBounds().getHeight() / 3));
            bit6bTransition.toXProperty().bind(bit6bTransition.fromXProperty());
            bit6bTransition.toYProperty().bind(bit6bTransition.fromYProperty());

            bit5cTransition.setNode(textBit3c);
            bit5cTransition.fromXProperty().bind(des10.translateXProperty()
                    .add(des10.getLayoutBounds().getWidth() - 60)
                    .add((des10.getLayoutBounds().getWidth() - textBit3c.getLayoutBounds().getWidth()) / 2));
            bit5cTransition.fromYProperty().bind(des10.translateYProperty()
                    .add(des10.getLayoutBounds().getHeight() / 3));
            bit5cTransition.toXProperty().bind(bit5cTransition.fromXProperty());
            bit5cTransition.toYProperty().bind(bit5cTransition.fromYProperty());

            bit4dTransition.setNode(textBit4d);
            bit4dTransition.fromXProperty().bind(des11.translateXProperty()
                    .add(des11.getLayoutBounds().getWidth() - 60)
                    .add((des11.getLayoutBounds().getWidth() - textBit4d.getLayoutBounds().getWidth()) / 2));
            bit4dTransition.fromYProperty().bind(des11.translateYProperty()
                    .add(des11.getLayoutBounds().getHeight() / 3));
            bit4dTransition.toXProperty().bind(bit4dTransition.fromXProperty());
            bit4dTransition.toYProperty().bind(bit4dTransition.fromYProperty());

            bit3eTransition.setNode(textBit5e);
            bit3eTransition.fromXProperty().bind(des12.translateXProperty()
                    .add(des12.getLayoutBounds().getWidth() - 60)
                    .add((des12.getLayoutBounds().getWidth() - textBit5e.getLayoutBounds().getWidth()) / 2));
            bit3eTransition.fromYProperty().bind(des12.translateYProperty()
                    .add(des12.getLayoutBounds().getHeight() / 3));
            bit3eTransition.toXProperty().bind(bit3eTransition.fromXProperty());
            bit3eTransition.toYProperty().bind(bit3eTransition.fromYProperty());

            bit2fTransition.setNode(textBit6f);
            bit2fTransition.fromXProperty().bind(des13.translateXProperty()
                    .add(des13.getLayoutBounds().getWidth() - 60)
                    .add((des13.getLayoutBounds().getWidth() - textBit6f.getLayoutBounds().getWidth()) / 2));
            bit2fTransition.fromYProperty().bind(des13.translateYProperty()
                    .add(des13.getLayoutBounds().getHeight() / 3));
            bit2fTransition.toXProperty().bind(bit2fTransition.fromXProperty());
            bit2fTransition.toYProperty().bind(bit2fTransition.fromYProperty());

            bit1gTransition.setNode(textBit7g);
            bit1gTransition.fromXProperty().bind(des14.translateXProperty()
                    .add(des14.getLayoutBounds().getWidth() - 60)
                    .add((des14.getLayoutBounds().getWidth() - textBit7g.getLayoutBounds().getWidth()) / 2));
            bit1gTransition.fromYProperty().bind(des14.translateYProperty()
                    .add(des14.getLayoutBounds().getHeight() / 3));
            bit1gTransition.toXProperty().bind(bit1gTransition.fromXProperty());
            bit1gTransition.toYProperty().bind(bit1gTransition.fromYProperty());

            bit0hTransition.setNode(textBit8h);
            bit0hTransition.fromXProperty().bind(des15.translateXProperty()
                    .add(des15.getLayoutBounds().getWidth() - 60)
                    .add((des15.getLayoutBounds().getWidth() - textBit8h.getLayoutBounds().getWidth()) / 2));
            bit0hTransition.fromYProperty().bind(des15.translateYProperty()
                    .add(des15.getLayoutBounds().getHeight() / 3));
            bit0hTransition.toXProperty().bind(bit0hTransition.fromXProperty());
            bit0hTransition.toYProperty().bind(bit0hTransition.fromYProperty());




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

            bit8aTransition.setNode(textBit9a);
            bit8aTransition.fromXProperty().bind(src9.translateXProperty()
                    .add(src9.getLayoutBounds().getWidth() - 60)
                    .add((src9.getLayoutBounds().getWidth() - textBit9a.getLayoutBounds().getWidth()) / 2));
            bit8aTransition.fromYProperty().bind(src9.translateYProperty()
                    .add(src9.getLayoutBounds().getHeight() / 3));
            bit8aTransition.toXProperty().bind(bit8aTransition.fromXProperty());
            bit8aTransition.toYProperty().bind(bit8aTransition.fromYProperty());

            bit9bTransition.setNode(textBit10b);
            bit9bTransition.fromXProperty().bind(src10.translateXProperty()
                    .add(src10.getLayoutBounds().getWidth() - 60)
                    .add((src10.getLayoutBounds().getWidth() - textBit10b.getLayoutBounds().getWidth()) / 2));
            bit9bTransition.fromYProperty().bind(src10.translateYProperty()
                    .add(src10.getLayoutBounds().getHeight() / 3));
            bit9bTransition.toXProperty().bind(bit9bTransition.fromXProperty());
            bit9bTransition.toYProperty().bind(bit9bTransition.fromYProperty());

            bit10cTransition.setNode(textBit11c);
            bit10cTransition.fromXProperty().bind(src11.translateXProperty()
                    .add(src11.getLayoutBounds().getWidth() - 60)
                    .add((src11.getLayoutBounds().getWidth() - textBit11c.getLayoutBounds().getWidth()) / 2));
            bit10cTransition.fromYProperty().bind(src11.translateYProperty()
                    .add(src11.getLayoutBounds().getHeight() / 3));
            bit10cTransition.toXProperty().bind(bit10cTransition.fromXProperty());
            bit10cTransition.toYProperty().bind(bit10cTransition.fromYProperty());

            bit11dTransition.setNode(textBit12d);
            bit11dTransition.fromXProperty().bind(src12.translateXProperty()
                    .add(src12.getLayoutBounds().getWidth() - 60)
                    .add((src12.getLayoutBounds().getWidth() - textBit12d.getLayoutBounds().getWidth()) / 2));
            bit11dTransition.fromYProperty().bind(src12.translateYProperty()
                    .add(src12.getLayoutBounds().getHeight() / 3));
            bit11dTransition.toXProperty().bind(bit11dTransition.fromXProperty());
            bit11dTransition.toYProperty().bind(bit11dTransition.fromYProperty());

            bit12eTransition.setNode(textBit13e);
            bit12eTransition.fromXProperty().bind(src13.translateXProperty()
                    .add(src13.getLayoutBounds().getWidth() - 60)
                    .add((src13.getLayoutBounds().getWidth() - textBit13e.getLayoutBounds().getWidth()) / 2));
            bit12eTransition.fromYProperty().bind(src13.translateYProperty()
                    .add(src13.getLayoutBounds().getHeight() / 3));
            bit12eTransition.toXProperty().bind(bit12eTransition.fromXProperty());
            bit12eTransition.toYProperty().bind(bit12eTransition.fromYProperty());

            bit13fTransition.setNode(textBit14f);
            bit13fTransition.fromXProperty().bind(src14.translateXProperty()
                    .add(src14.getLayoutBounds().getWidth() - 60)
                    .add((src14.getLayoutBounds().getWidth() - textBit14f.getLayoutBounds().getWidth()) / 2));
            bit13fTransition.fromYProperty().bind(src14.translateYProperty()
                    .add(src14.getLayoutBounds().getHeight() / 3));
            bit13fTransition.toXProperty().bind(bit13fTransition.fromXProperty());
            bit13fTransition.toYProperty().bind(bit13fTransition.fromYProperty());

            bit14gTransition.setNode(textBit15g);
            bit14gTransition.fromXProperty().bind(src15.translateXProperty()
                    .add(src15.getLayoutBounds().getWidth() - 60)
                    .add((src15.getLayoutBounds().getWidth() - textBit15g.getLayoutBounds().getWidth()) / 2));
            bit14gTransition.fromYProperty().bind(src15.translateYProperty()
                    .add(src15.getLayoutBounds().getHeight() / 3));
            bit14gTransition.toXProperty().bind(bit14gTransition.fromXProperty());
            bit14gTransition.toYProperty().bind(bit14gTransition.fromYProperty());

            bit15hTransition.setNode(textBit16h);
            bit15hTransition.fromXProperty().bind(src16.translateXProperty()
                    .add(src16.getLayoutBounds().getWidth() - 60)
                    .add((src16.getLayoutBounds().getWidth() - textBit16h.getLayoutBounds().getWidth()) / 2));
            bit15hTransition.fromYProperty().bind(src16.translateYProperty()
                    .add(src16.getLayoutBounds().getHeight() / 3));
            bit15hTransition.toXProperty().bind(bit15hTransition.fromXProperty());
            bit15hTransition.toYProperty().bind(bit15hTransition.fromYProperty());




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

            bit16aTransition.setNode(textBit17a);
            bit16aTransition.fromXProperty().bind(res9.translateXProperty()
                    .add(res9.getLayoutBounds().getWidth() - 60)
                    .add((res9.getLayoutBounds().getWidth() - textBit17a.getLayoutBounds().getWidth()) / 2));
            bit16aTransition.fromYProperty().bind(res9.translateYProperty()
                    .add(res9.getLayoutBounds().getHeight() / 3));
            bit16aTransition.toXProperty().bind(bit16aTransition.fromXProperty());
            bit16aTransition.toYProperty().bind(bit16aTransition.fromYProperty());

            bit17bTransition.setNode(textBit18b);
            bit17bTransition.fromXProperty().bind(res10.translateXProperty()
                    .add(res10.getLayoutBounds().getWidth() - 60)
                    .add((res10.getLayoutBounds().getWidth() - textBit18b.getLayoutBounds().getWidth()) / 2));
            bit17bTransition.fromYProperty().bind(res10.translateYProperty()
                    .add(res10.getLayoutBounds().getHeight() / 3));
            bit17bTransition.toXProperty().bind(bit17bTransition.fromXProperty());
            bit17bTransition.toYProperty().bind(bit17bTransition.fromYProperty());

            bit18cTransition.setNode(textBit19c);
            bit18cTransition.fromXProperty().bind(res11.translateXProperty()
                    .add(res11.getLayoutBounds().getWidth() - 60)
                    .add((res11.getLayoutBounds().getWidth() - textBit19c.getLayoutBounds().getWidth()) / 2));
            bit18cTransition.fromYProperty().bind(res11.translateYProperty()
                    .add(res11.getLayoutBounds().getHeight() / 3));
            bit18cTransition.toXProperty().bind(bit18cTransition.fromXProperty());
            bit18cTransition.toYProperty().bind(bit18cTransition.fromYProperty());

            bit19dTransition.setNode(textBit20d);
            bit19dTransition.fromXProperty().bind(res12.translateXProperty()
                    .add(res12.getLayoutBounds().getWidth() - 60)
                    .add((res12.getLayoutBounds().getWidth() - textBit20d.getLayoutBounds().getWidth()) / 2));
            bit19dTransition.fromYProperty().bind(res12.translateYProperty()
                    .add(res12.getLayoutBounds().getHeight() / 3));
            bit19dTransition.toXProperty().bind(bit19dTransition.fromXProperty());
            bit19dTransition.toYProperty().bind(bit19dTransition.fromYProperty());

            bit20eTransition.setNode(textBit21e);
            bit20eTransition.fromXProperty().bind(res13.translateXProperty()
                    .add(res13.getLayoutBounds().getWidth() - 60)
                    .add((res13.getLayoutBounds().getWidth() - textBit21e.getLayoutBounds().getWidth()) / 2));
            bit20eTransition.fromYProperty().bind(res13.translateYProperty()
                    .add(res13.getLayoutBounds().getHeight() / 3));
            bit20eTransition.toXProperty().bind(bit20eTransition.fromXProperty());
            bit20eTransition.toYProperty().bind(bit20eTransition.fromYProperty());

            bit21fTransition.setNode(textBit22f);
            bit21fTransition.fromXProperty().bind(res14.translateXProperty()
                    .add(res14.getLayoutBounds().getWidth() - 60)
                    .add((res14.getLayoutBounds().getWidth() - textBit22f.getLayoutBounds().getWidth()) / 2));
            bit21fTransition.fromYProperty().bind(res14.translateYProperty()
                    .add(res14.getLayoutBounds().getHeight() / 3));
            bit21fTransition.toXProperty().bind(bit21fTransition.fromXProperty());
            bit21fTransition.toYProperty().bind(bit21fTransition.fromYProperty());

            bit22gTransition.setNode(textBit23g);
            bit22gTransition.fromXProperty().bind(res15.translateXProperty()
                    .add(res15.getLayoutBounds().getWidth() - 60)
                    .add((res15.getLayoutBounds().getWidth() - textBit23g.getLayoutBounds().getWidth()) / 2));
            bit22gTransition.fromYProperty().bind(res15.translateYProperty()
                    .add(res15.getLayoutBounds().getHeight() / 3));
            bit22gTransition.toXProperty().bind(bit22gTransition.fromXProperty());
            bit22gTransition.toYProperty().bind(bit22gTransition.fromYProperty());

            bit23hTransition.setNode(textBit24h);
            bit23hTransition.fromXProperty().bind(res16.translateXProperty()
                    .add(res16.getLayoutBounds().getWidth() - 60)
                    .add((res16.getLayoutBounds().getWidth() - textBit24h.getLayoutBounds().getWidth()) / 2));
            bit23hTransition.fromYProperty().bind(res16.translateYProperty()
                    .add(res16.getLayoutBounds().getHeight() / 3));
            bit23hTransition.toXProperty().bind(bit23hTransition.fromXProperty());
            bit23hTransition.toYProperty().bind(bit23hTransition.fromYProperty());

            bit23Transition.play();
            bit22Transition.play();
            bit21Transition.play();
            bit20Transition.play();
            bit19Transition.play();
            bit18Transition.play();
            bit17Transition.play();
            bit16Transition.play();
            bit23hTransition.play();
            bit22gTransition.play();
            bit21fTransition.play();
            bit20eTransition.play();
            bit19dTransition.play();
            bit18cTransition.play();
            bit17bTransition.play();
            bit16aTransition.play();

            bit15Transition.play();
            bit14Transition.play();
            bit13Transition.play();
            bit12Transition.play();
            bit11Transition.play();
            bit10Transition.play();
            bit9Transition.play();
            bit8Transition.play();
            bit15hTransition.play();
            bit14gTransition.play();
            bit13fTransition.play();
            bit12eTransition.play();
            bit11dTransition.play();
            bit10cTransition.play();
            bit9bTransition.play();
            bit8aTransition.play();

            bit7Transition.play();
            bit6Transition.play();
            bit5Transition.play();
            bit4Transition.play();
            bit3Transition.play();
            bit2Transition.play();
            bit1Transition.play();
            bit0Transition.play();
            bit7aTransition.play();
            bit6bTransition.play();
            bit5cTransition.play();
            bit4dTransition.play();
            bit3eTransition.play();
            bit2fTransition.play();
            bit1gTransition.play();
            bit0hTransition.play();
        }

    }
}
