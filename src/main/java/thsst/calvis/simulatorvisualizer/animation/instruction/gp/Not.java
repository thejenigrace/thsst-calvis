package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

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
public class Not extends CalvisAnimation {

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
        String result = "";

        if( tokens[0].getType() == Token.REG ) {
            value0 = finder.getRegister(tokens[0].getValue());
            result = registers.get(tokens[0].getValue());
        }
        else if( tokens[0].getType() == Token.MEM ) {
            try {
                value0 = finder.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }

            try {
                result = memory.read(tokens[0].getValue(), tokens[0]);
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }
        }

        Text description = new Text("!" + value0 + " = " +  result + "\n" +
                "Affected flags: none");
        description.setX(100);
        description.setY(100);
        this.root.getChildren().addAll(description);
    }
}