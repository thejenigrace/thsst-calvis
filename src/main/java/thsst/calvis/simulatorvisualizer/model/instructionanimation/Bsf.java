package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

import java.math.BigInteger;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Bsf extends CalvisAnimation {

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

        // CODE HERE
        Text description = new Text("If a least significant 1 bit is found in the source operand, its bit index is stored in the destination operand. The bit index is an unsigned offset from bit 0 of the source operand. If the content of the source operand is 0, the content of the destination operand is undefined. \n" +
                "Affected flags: CF, OF, SF, PF, ZF, AF\n" +
                "");
        description.setX(X);
        description.setY(Y);
        this.root.getChildren().addAll(description);
    }
}