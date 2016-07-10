package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Pushfd extends CalvisAnimation {

    @Override
    public void animate(Tab tab) {
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
        Text description = new Text("SP is decremented by 4 and the lower 16 bits of the ELFAGS register is moved to the stop of stack pointed by SP (Look at memory address: " + registers.get("ESP") +").");
        description.setX(100);
        description.setY(100);
        this.root.getChildren().addAll(description);
    }
}