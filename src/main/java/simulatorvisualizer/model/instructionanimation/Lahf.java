package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.*;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Lahf extends CalvisAnimation {

    @Override
    public void animate(Tab tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        Calculator calculator = new Calculator(registers, memory);
        EFlags flags = registers.getEFlags();

        // ANIMATION ASSETS
        Token token = new Token(Token.REG, "AH");

        // CODE HERE
        Text description = new Text("The values of carry, parity, auxiliary, zero and sign flag, are copied into bit 0, 2, 4, 6 and 7 of AH, respectively. \n" +
                "Affected flags: none \n\n" +
                "Below is the binary representation of the new value of register AH:");
        description.setX(100);
        description.setY(100);

        String ah = calculator.hexToBinaryString(registers.get("AH"), token);

        int width = 50;
        int height = 52;
        Rectangle bit7 = new Rectangle(width, height, Color.web("#27ae60", 1.0));
        Rectangle bit6 = new Rectangle(width, height, Color.web("#27ae60", 1.0));
        Rectangle bit5 = new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
        Rectangle bit4 = new Rectangle(width, height, Color.web("#27ae60", 1.0));
        Rectangle bit3 = new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
        Rectangle bit2 = new Rectangle(width, height, Color.web("#27ae60", 1.0));
        Rectangle bit1 = new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
        Rectangle bit0 = new Rectangle(width, height, Color.web("#27ae60", 1.0));

        bit7.setX(150);
        bit7.setY(150);
        bit6.setX(205);
        bit6.setY(150);
        bit5.setX(260);
        bit5.setY(150);
        bit4.setX(315);
        bit4.setY(150);
        bit3.setX(370);
        bit3.setY(150);
        bit2.setX(425);
        bit2.setY(150);
        bit1.setX(480);
        bit1.setY(150);
        bit0.setX(535);
        bit0.setY(150);

        this.root.getChildren().addAll(description, bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0);

        Text textBit7 = new Text("Bit 7 \n\nSF: " + flags.getSignFlag());
        textBit7.setX(150);
        textBit7.setY(150);
        Text textBit6 = new Text("Bit 6 \n\nZF: " + flags.getZeroFlag());
        textBit6.setX(205);
        textBit6.setY(150);
        Text textBit5 = new Text("Bit 5 \n\n   0");
        textBit5.setX(260);
        textBit5.setY(150);
        Text textBit4 = new Text("Bit 4 \n\nAF: " + flags.getAuxiliaryFlag());
        textBit4.setX(315);
        textBit4.setY(150);
        Text textBit3 = new Text("Bit 3 \n\n   0");
        textBit3.setX(370);
        textBit3.setY(150);
        Text textBit2 = new Text("Bit 2 \n\nPF: " + flags.getParityFlag());
        textBit2.setX(425);
        textBit2.setY(150);
        Text textBit1 = new Text("Bit 1 \n\n   1");
        textBit1.setX(480);
        textBit1.setY(150);
        Text textBit0 = new Text("Bit 0 \n\nCF: " + flags.getCarryFlag());
        textBit0.setX(535);
        textBit0.setY(150);

        this.root.getChildren().addAll(textBit7, textBit6, textBit5, textBit4, textBit3, textBit2, textBit1, textBit0);

        TranslateTransition bit7Transition = new TranslateTransition();
        TranslateTransition bit6Transition = new TranslateTransition();
        TranslateTransition bit5Transition = new TranslateTransition();
        TranslateTransition bit4Transition = new TranslateTransition();
        TranslateTransition bit3Transition = new TranslateTransition();
        TranslateTransition bit2Transition = new TranslateTransition();
        TranslateTransition bit1Transition = new TranslateTransition();
        TranslateTransition bit0Transition = new TranslateTransition();

        bit7Transition.setNode(textBit7);
        bit7Transition.fromXProperty().bind(bit7.translateXProperty()
                .add(bit7.getLayoutBounds().getWidth() - 50)
                .add((bit7.getLayoutBounds().getWidth() - textBit7.getLayoutBounds().getWidth()) / 2));
        bit7Transition.fromYProperty().bind(bit7.translateYProperty()
                .add(bit7.getLayoutBounds().getHeight() / 3));
        bit7Transition.toXProperty().bind(bit7Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit7Transition.fromYProperty());

        bit6Transition.setNode(textBit6);
        bit6Transition.fromXProperty().bind(bit6.translateXProperty()
                .add(bit6.getLayoutBounds().getWidth() - 50)
                .add((bit6.getLayoutBounds().getWidth() - textBit6.getLayoutBounds().getWidth()) / 2));
        bit6Transition.fromYProperty().bind(bit6.translateYProperty()
                .add(bit6.getLayoutBounds().getHeight() / 3));
        bit7Transition.toXProperty().bind(bit6Transition.fromXProperty());
        bit7Transition.toYProperty().bind(bit6Transition.fromYProperty());

        bit5Transition.setNode(textBit5);
        bit5Transition.fromXProperty().bind(bit5.translateXProperty()
                .add(bit5.getLayoutBounds().getWidth() - 50)
                .add((bit5.getLayoutBounds().getWidth() - textBit5.getLayoutBounds().getWidth()) / 2));
        bit5Transition.fromYProperty().bind(bit5.translateYProperty()
                .add(bit5.getLayoutBounds().getHeight() / 3));
        bit5Transition.toXProperty().bind(bit5Transition.fromXProperty());
        bit5Transition.toYProperty().bind(bit5Transition.fromYProperty());

        bit4Transition.setNode(textBit4);
        bit4Transition.fromXProperty().bind(bit4.translateXProperty()
                .add(bit4.getLayoutBounds().getWidth() - 50)
                .add((bit4.getLayoutBounds().getWidth() - textBit4.getLayoutBounds().getWidth()) / 2));
        bit4Transition.fromYProperty().bind(bit4.translateYProperty()
                .add(bit4.getLayoutBounds().getHeight() / 3));
        bit4Transition.toXProperty().bind(bit4Transition.fromXProperty());
        bit4Transition.toYProperty().bind(bit4Transition.fromYProperty());

        bit3Transition.setNode(textBit3);
        bit3Transition.fromXProperty().bind(bit3.translateXProperty()
                .add(bit3.getLayoutBounds().getWidth() - 50)
                .add((bit3.getLayoutBounds().getWidth() - textBit3.getLayoutBounds().getWidth()) / 2));
        bit3Transition.fromYProperty().bind(bit3.translateYProperty()
                .add(bit3.getLayoutBounds().getHeight() / 3));
        bit3Transition.toXProperty().bind(bit3Transition.fromXProperty());
        bit3Transition.toYProperty().bind(bit3Transition.fromYProperty());

        bit2Transition.setNode(textBit2);
        bit2Transition.fromXProperty().bind(bit2.translateXProperty()
                .add(bit2.getLayoutBounds().getWidth() - 50)
                .add((bit2.getLayoutBounds().getWidth() - textBit2.getLayoutBounds().getWidth()) / 2));
        bit2Transition.fromYProperty().bind(bit2.translateYProperty()
                .add(bit2.getLayoutBounds().getHeight() / 3));
        bit2Transition.toXProperty().bind(bit2Transition.fromXProperty());
        bit2Transition.toYProperty().bind(bit2Transition.fromYProperty());

        bit1Transition.setNode(textBit1);
        bit1Transition.fromXProperty().bind(bit1.translateXProperty()
                .add(bit1.getLayoutBounds().getWidth() - 50)
                .add((bit1.getLayoutBounds().getWidth() - textBit1.getLayoutBounds().getWidth()) / 2));
        bit1Transition.fromYProperty().bind(bit1.translateYProperty()
                .add(bit1.getLayoutBounds().getHeight() / 3));
        bit1Transition.toXProperty().bind(bit1Transition.fromXProperty());
        bit1Transition.toYProperty().bind(bit1Transition.fromYProperty());

        bit0Transition.setNode(textBit0);
        bit0Transition.fromXProperty().bind(bit0.translateXProperty()
                .add(bit0.getLayoutBounds().getWidth() - 50)
                .add((bit0.getLayoutBounds().getWidth() - textBit0.getLayoutBounds().getWidth()) / 2));
        bit0Transition.fromYProperty().bind(bit0.translateYProperty()
                .add(bit0.getLayoutBounds().getHeight() / 3));
        bit0Transition.toXProperty().bind(bit0Transition.fromXProperty());
        bit0Transition.toYProperty().bind(bit0Transition.fromYProperty());

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
