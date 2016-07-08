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
public class Popf extends CalvisAnimation {

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
        Text description = new Text("The word pointed to by SP is transferred to the lower 16 bits of EFLAGS, thereby altering the value of all status flags. SP is incremented by 2." + "\n" +
                "Affected flags: CF, OF, SF, PF, ZF, AF");
        description.setX(100);
        description.setY(100);
        this.root.getChildren().addAll(description);
    }
}