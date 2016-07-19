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
public class Packssdw extends CalvisAnimation {

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
            int width = 60;
            int height = 30;
            Rectangle des1 = new Rectangle(80, height, Color.web("#4CAF50", 1.0));
            Rectangle des2 = new Rectangle(80, height, Color.web("#81C784", 1.0));

            Rectangle src1 = new Rectangle(80, height, Color.web("#03A9F4", 1.0));
            Rectangle src2 = new Rectangle(80, height, Color.web("#4FC3F7", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#03A9F4", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#4FC3F7", 1.0));
            Rectangle res3 = new Rectangle(width, height, Color.web("#4CAF50", 1.0));
            Rectangle res4 = new Rectangle(width, height, Color.web("#81C784", 1.0));

            des1.setX(X);
            des1.setY(Y);
            des2.setX(X + 90);
            des2.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 90);
            src2.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 123);
            res2.setX(X + 70);
            res2.setY(Y + 123);
            res3.setX(X + 140);
            res3.setY(Y + 123);
            res4.setX(X + 210);
            res4.setY(Y + 123);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text(X, Y, "Result: (stored in " + tokens[0].getValue() + ")\n" +
                    "Check per double word if the value is > 0x7FFF for positive integer, then replace the integer as 0x7FFF or < 0x8000 for negative integer, then replace the integer as 0x8000, else copy the original value.");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, src1, src2, res1, res2, res3, res4, label1, label2, label3);

            Text textBit1;
            Text textBit2;

            Text textBit9;
            Text textBit10;

            Text textBit17;
            Text textBit18;
            Text textBit19;
            Text textBit20;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 8));
            textBit2 = new Text(X + 90, Y + 10, "" + value0.substring(8));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 8));
            textBit10 = new Text(X + 90, Y + 60, "" + value1.substring(8));

            textBit17 = new Text(X, Y + 135, "" + result.substring(0, 4));
            textBit18 = new Text(X + 70, Y + 135, "" + result.substring(4, 8));
            textBit19 = new Text(X + 140, Y + 135, "" + result.substring(8, 12));
            textBit20 = new Text(X + 210, Y + 135, "" + result.substring(12));

            this.root.getChildren().addAll(textBit1, textBit2,
                    textBit9, textBit10,
                    textBit17, textBit18, textBit19, textBit20);

            TranslateTransition bit7Transition = new TranslateTransition();
            TranslateTransition bit6Transition = new TranslateTransition();

            TranslateTransition bit8Transition = new TranslateTransition();
            TranslateTransition bit9Transition = new TranslateTransition();

            TranslateTransition bit16Transition = new TranslateTransition();
            TranslateTransition bit17Transition = new TranslateTransition();
            TranslateTransition bit18Transition = new TranslateTransition();
            TranslateTransition bit19Transition = new TranslateTransition();

            bit7Transition.setNode(textBit1);
            bit7Transition.fromXProperty().bind(des1.translateXProperty()
                    .add(des1.getLayoutBounds().getWidth() - 80 )
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() - 80 )
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());

            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 80 )
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 80 )
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

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
            bit9Transition.play();
            bit8Transition.play();
            bit7Transition.play();
            bit6Transition.play();
        }
        else {
            int width = 60;
            int height = 30;
            Rectangle des1 = new Rectangle(80, height, Color.web("#4CAF50", 1.0));
            Rectangle des2 = new Rectangle(80, height, Color.web("#66BB6A", 1.0));
            Rectangle des3 = new Rectangle(80, height, Color.web("#81C784", 1.0));
            Rectangle des4 = new Rectangle(80, height, Color.web("#A5D6A7", 1.0));

            Rectangle src1 = new Rectangle(80, height, Color.web("#03A9F4", 1.0));
            Rectangle src2 = new Rectangle(80, height, Color.web("#29B6F6", 1.0));
            Rectangle src3 = new Rectangle(80, height, Color.web("#4FC3F7", 1.0));
            Rectangle src4 = new Rectangle(80, height, Color.web("#81D4FA", 1.0));

            Rectangle res1 = new Rectangle(width, height, Color.web("#03A9F4", 1.0));
            Rectangle res2 = new Rectangle(width, height, Color.web("#29B6F6", 1.0));
            Rectangle res3 = new Rectangle(width, height, Color.web("#4FC3F7", 1.0));
            Rectangle res4 = new Rectangle(width, height, Color.web("#81D4FA", 1.0));
            Rectangle res5 = new Rectangle(width, height, Color.web("#4CAF50", 1.0));
            Rectangle res6 = new Rectangle(width, height, Color.web("#66BB6A", 1.0));
            Rectangle res7 = new Rectangle(width, height, Color.web("#81C784", 1.0));
            Rectangle res8 = new Rectangle(width, height, Color.web("#A5D6A7", 1.0));

            des1.setX(X);
            des1.setY(Y);
            des2.setX(X + 90);
            des2.setY(Y);
            des3.setX(X + 180);
            des3.setY(Y);
            des4.setX(X + 270);
            des4.setY(Y);

            src1.setX(X);
            src1.setY(Y + 50);
            src2.setX(X + 90);
            src2.setY(Y + 50);
            src3.setX(X + 180);
            src3.setY(Y + 50);
            src4.setX(X + 270);
            src4.setY(Y + 50);

            res1.setX(X);
            res1.setY(Y + 123);
            res2.setX(X + 70);
            res2.setY(Y + 123);
            res3.setX(X + 140);
            res3.setY(Y + 123);
            res4.setX(X + 210);
            res4.setY(Y + 123);
            res5.setX(X + 280);
            res5.setY(Y + 123);
            res6.setX(X + 350);
            res6.setY(Y + 123);
            res7.setX(X + 420);
            res7.setY(Y + 123);
            res8.setX(X + 490);
            res8.setY(Y + 123);

            Text label1 = new Text(tokens[0].getValue());
            Text label2 = new Text(l);

            Text label3 = new Text("Result: (stored in " + tokens[0].getValue() + ")\n" +
                    "Check per double word if the value is > 0x7FFF for positive integer, then replace the integer as 0x7FFF or < 0x8000 for negative integer, then replace the integer as 0x8000, else copy the original value.");

            label1.setX(X);
            label1.setY(Y);
            label2.setX(X);
            label2.setY(Y + 50);
            label3.setX(X);
            label3.setY(Y + 100);
            this.root.getChildren().addAll(des1, des2, des3, des4,
                    src1, src2, src3, src4,
                    res1, res2, res3, res4, res5, res6, res7, res8,
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
            Text textBit21;
            Text textBit22;
            Text textBit23;
            Text textBit24;

            textBit1 = new Text(X, Y + 10, "" + value0.substring(0, 8));
            textBit2 = new Text(X + 90, Y + 10, "" + value0.substring(8, 16));
            textBit3 = new Text(X + 180, Y + 10, "" + value0.substring(16, 24));
            textBit4 = new Text(X + 270, Y + 10, "" + value0.substring(24));

            textBit9 = new Text(X, Y + 60, "" + value1.substring(0, 8));
            textBit10 = new Text(X + 90, Y + 60, "" + value1.substring(8, 16));
            textBit11 = new Text(X + 180, Y + 60, "" + value1.substring(16, 24));
            textBit12 = new Text(X + 270, Y + 60, "" + value1.substring(24));

            textBit17 = new Text(X, Y + 135, "" + result.substring(0, 4));
            textBit18 = new Text(X + 70, Y + 135, "" + result.substring(4, 8));
            textBit19 = new Text(X + 140, Y + 135, "" + result.substring(8, 12));
            textBit20 = new Text(X + 210, Y + 135, "" + result.substring(12, 16));
            textBit21 = new Text(X + 280, Y + 135, "" + result.substring(16, 20));
            textBit22 = new Text(X + 350, Y + 135, "" + result.substring(20, 24));
            textBit23 = new Text(X + 420, Y + 135, "" + result.substring(24, 28));
            textBit24 = new Text(X + 490, Y + 135, "" + result.substring(28));

            this.root.getChildren().addAll(textBit1, textBit2, textBit3, textBit4,
                    textBit9, textBit10, textBit11, textBit12,
                    textBit17, textBit18, textBit19, textBit20, textBit21, textBit22, textBit23, textBit24);

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
            TranslateTransition bit20Transition = new TranslateTransition();
            TranslateTransition bit21Transition = new TranslateTransition();
            TranslateTransition bit22Transition = new TranslateTransition();
            TranslateTransition bit23Transition = new TranslateTransition();

            bit7Transition.setNode(textBit1);
            bit7Transition.fromXProperty().bind(des1.translateXProperty()
                    .add(des1.getLayoutBounds().getWidth() - 80)
                    .add((des1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
            bit7Transition.fromYProperty().bind(des1.translateYProperty()
                    .add(des1.getLayoutBounds().getHeight() / 3));
            bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
            bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

            bit6Transition.setNode(textBit2);
            bit6Transition.fromXProperty().bind(des2.translateXProperty()
                    .add(des2.getLayoutBounds().getWidth() - 80)
                    .add((des2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
            bit6Transition.fromYProperty().bind(des2.translateYProperty()
                    .add(des2.getLayoutBounds().getHeight() / 3));
            bit6Transition.toXProperty().bind(bit6Transition.fromXProperty());
            bit6Transition.toYProperty().bind(bit6Transition.fromYProperty());

            bit5Transition.setNode(textBit3);
            bit5Transition.fromXProperty().bind(des3.translateXProperty()
                    .add(des3.getLayoutBounds().getWidth() - 80)
                    .add((des3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
            bit5Transition.fromYProperty().bind(des3.translateYProperty()
                    .add(des3.getLayoutBounds().getHeight() / 3));
            bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
            bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

            bit4Transition.setNode(textBit4);
            bit4Transition.fromXProperty().bind(des4.translateXProperty()
                    .add(des4.getLayoutBounds().getWidth() - 80)
                    .add((des4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
            bit4Transition.fromYProperty().bind(des4.translateYProperty()
                    .add(des4.getLayoutBounds().getHeight() / 3));
            bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
            bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());

            bit8Transition.setNode(textBit9);
            bit8Transition.fromXProperty().bind(src1.translateXProperty()
                    .add(src1.getLayoutBounds().getWidth() - 80)
                    .add((src1.getLayoutBounds().getWidth() - textBit9.getLayoutBounds().getWidth()) / 2));
            bit8Transition.fromYProperty().bind(src1.translateYProperty()
                    .add(src1.getLayoutBounds().getHeight() / 3));
            bit8Transition.toXProperty().bind(bit8Transition.fromXProperty());
            bit8Transition.toYProperty().bind(bit8Transition.fromYProperty());

            bit9Transition.setNode(textBit10);
            bit9Transition.fromXProperty().bind(src2.translateXProperty()
                    .add(src2.getLayoutBounds().getWidth() - 80)
                    .add((src2.getLayoutBounds().getWidth() - textBit10.getLayoutBounds().getWidth()) / 2));
            bit9Transition.fromYProperty().bind(src2.translateYProperty()
                    .add(src2.getLayoutBounds().getHeight() / 3));
            bit9Transition.toXProperty().bind(bit9Transition.fromXProperty());
            bit9Transition.toYProperty().bind(bit9Transition.fromYProperty());

            bit10Transition.setNode(textBit11);
            bit10Transition.fromXProperty().bind(src3.translateXProperty()
                    .add(src3.getLayoutBounds().getWidth() - 80)
                    .add((src3.getLayoutBounds().getWidth() - textBit11.getLayoutBounds().getWidth()) / 2));
            bit10Transition.fromYProperty().bind(src3.translateYProperty()
                    .add(src3.getLayoutBounds().getHeight() / 3));
            bit10Transition.toXProperty().bind(bit10Transition.fromXProperty());
            bit10Transition.toYProperty().bind(bit10Transition.fromYProperty());

            bit11Transition.setNode(textBit12);
            bit11Transition.fromXProperty().bind(src4.translateXProperty()
                    .add(src4.getLayoutBounds().getWidth() - 80)
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
