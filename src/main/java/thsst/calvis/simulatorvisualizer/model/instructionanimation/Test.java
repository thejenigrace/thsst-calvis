package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Test extends CalvisAnimation {

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
        String value0 = "";
        String value1 = "";
        String result = "";
        String address = "";

        if( tokens[0].getType() == Token.REG ) {
            value0 = finder.getRegister(tokens[0].getValue());
            address = tokens[0].getValue();
        }
        else if( tokens[0].getType() == Token.MEM ) {
            try {
                value0 = finder.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }
            address = "[" + tokens[0].getValue() + "]";
        }

        if( tokens[1].getType() == Token.REG )
            value1 = finder.getRegister(tokens[1].getValue());
        else if( tokens[1].getType() == Token.MEM )
            try {
                value1 = finder.read(tokens[1].getValue(), tokens[1]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }
        else
            value1 = tokens[1].getValue();

        if( tokens[0].getType() == Token.REG )
            result = registers.get(tokens[0].getValue());
        else if( tokens[0].getType() == Token.MEM )
            try {
                result = memory.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }

        Text description = new Text(value0 + " & " + value1 + " = " + result + ", but result is discarded." + "\n" +
                "Affected flags: CF, OF, SF, PF, ZF, AF");
        description.setX(100);
        description.setY(100);
        this.root.getChildren().addAll(description);
    }
}