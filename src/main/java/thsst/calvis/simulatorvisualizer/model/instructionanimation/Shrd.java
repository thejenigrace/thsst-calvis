package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

import java.math.BigInteger;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Shrd extends CalvisAnimation {

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
        int width = 140;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);

        String value = "";
        String value0 = "";
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

        if ( tokens[2].getType() == Token.REG)
            value = registers.get(tokens[2].getValue() + "");
        else if ( tokens[2].getType() == Token.HEX)
            value = tokens[2].getValue();

        BigInteger biValue = new BigInteger(value, 16);

        Text text = new Text(value0 + " is shifted to the left by " + biValue.intValue() + " bit(s).\n" +
                tokens[1].getValue() + " provides bits to shift in from the left. \n" +
                "The result is stored to " + address +
                "\nAffected flags: CF, OF, SF, PF, ZF, AF");

        root.getChildren().addAll(text);

//        if ( desRectangle != null && srcRectangle != null ) {
//            desRectangle.setX(100);
//            desRectangle.setY(50);
//            desRectangle.setArcWidth(10);
//            desRectangle.setArcHeight(10);
//
//            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + 100);
//            srcRectangle.setY(50);
//            srcRectangle.setArcWidth(10);
//            srcRectangle.setArcHeight(10);
//
//            root.getChildren().addAll(text, desRectangle, srcRectangle);
//
//            int desSize = 0;
//            if ( tokens[0].getType() == Token.REG )
//                desSize = registers.getBitSize(tokens[0]);
//            else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
//                desSize = registers.getBitSize(tokens[1]);
//            else
//                desSize = memory.getBitSize(tokens[0]);
//
//            Text desLabelText = this.createLabelText(tokens[0]);
//            Text desValueText = this.createValueText(tokens[0], registers, memory, desSize);
//            Text srcLabelText = this.createLabelText(tokens[1]);
//            Text srcValueText = this.createValueText(tokens[1], registers, memory, desSize);
//
//            desLabelText.setX(90);
//            desLabelText.setY(50);
//
//            desValueText.setX(90);
//            desValueText.setY(50);
//
//            srcLabelText.setX(90);
//            srcLabelText.setY(50);
//
//            srcValueText.setX(90);
//            srcValueText.setY(50);
//
//            root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText);
//
//            // ANIMATION LOGIC
//            TranslateTransition desLabelTransition = new TranslateTransition();
//            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
//            TranslateTransition srcLabelTransition = new TranslateTransition();
//            TranslateTransition srcTransition = new TranslateTransition();
//
//            // Destination label static
//            desLabelTransition.setNode(desLabelText);
//            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
//                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
//            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
//                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
//            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
//            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());
//
//            // Destination value moving
//            desTransition.setInterpolator(Interpolator.LINEAR);
//            desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
//                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
//                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
//            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
//                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
//            desTransition.toXProperty().bind(desRectangle.translateXProperty()
//                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
//            desTransition.toYProperty().bind(desTransition.fromYProperty());
//
//            // Source label static
//            srcLabelTransition.setNode(srcLabelText);
//            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
//                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
//                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
//            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
//            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
//            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());
//
//            // Source value static
//            srcTransition.setNode(srcValueText);
//            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
//                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
//                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
//            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
//            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
//            srcTransition.toYProperty().bind(srcTransition.fromYProperty());
//
//            // Play 1000 milliseconds of animation
//            desLabelTransition.play();
//            srcLabelTransition.play();
//            desTransition.play();
//            srcTransition.play();
//        }
    }
}