package thsst.calvis.simulatorvisualizer.animation.instruction.mmx;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);

        desRectangle.setX(100);
        desRectangle.setY(100);
        desRectangle.setArcWidth(10);
        desRectangle.setArcHeight(10);

        srcRectangle.setX(100);
        srcRectangle.setY(150);
        srcRectangle.setArcWidth(10);
        srcRectangle.setArcHeight(10);

        root.getChildren().addAll(desRectangle, srcRectangle);

        // Destination is always a Register: XMM or MM
        int desBitSize = registers.getBitSize(tokens[0]);

        // Cut per byte
        String desValue = this.finder.getRegister(tokens[0].getValue());
        System.out.println("desValue = " + desValue);
        String desValueWithDesign = "";
        for ( int i = 0; i < desValue.length(); i++ ) {
            if ( i % 2 != 0)
                desValueWithDesign += desValue.charAt(i) + "   ";
            else
                desValueWithDesign += desValue.charAt(i);
        }



        System.out.println(desValueWithDesign);

        Text desLabelText = this.createLabelText(X, Y, tokens[0]);
        Text desValueText = new Text(X, Y, desValueWithDesign);

        root.getChildren().addAll(desLabelText, desValueText);

        // ANIMATION LOGIC
        TranslateTransition desLabelTransition = new TranslateTransition();
        TranslateTransition desValueTransition = new TranslateTransition();

        // DES LABEL static
        desLabelTransition.setNode(desLabelText);
        desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
        desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 3));
        desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
        desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

        // DES VALUE static
        desValueTransition.setNode(desValueText);
        desValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
        desValueTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
        desValueTransition.toXProperty().bind(desValueTransition.fromXProperty());
        desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

        desLabelTransition.play();
        desValueTransition.play();
    }
}
