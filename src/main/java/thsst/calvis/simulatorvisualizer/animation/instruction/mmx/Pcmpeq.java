package thsst.calvis.simulatorvisualizer.animation.instruction.mmx;

import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Jennica on 19/07/2016.
 */
public class Pcmpeq extends CalvisAnimation {
    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ )
            System.out.println(tokens[i] + " : " + tokens[i].getClass());

        // CODE HERE
        int width = 280;
        int height = 70;

        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
//        Rectangle srcRectangle =

        desRectangle.setX(100);
        desRectangle.setY(100);
        desRectangle.setArcWidth(10);
        desRectangle.setArcHeight(10);

        root.getChildren().addAll(desRectangle);

        // Destination is always a Register: XMM or MM
        int desBitSize = registers.getBitSize(tokens[0]);

        // Cut per byte
        String desValue = this.finder.getRegister(tokens[0].getValue());
        System.out.println("desValue = " + desValue);
        String desValueDesign = "";
        for ( int i = 0; i <= desValue.length(); i++ ) {
            if ( i % 2 != 0)
                desValueDesign += "" + desValue.charAt(i) + "   ";
            else
                desValueDesign += "" + desValue.charAt(i);
        }

        System.out.println(desValueDesign);
    }
}
