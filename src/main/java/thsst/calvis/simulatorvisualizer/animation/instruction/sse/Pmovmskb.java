package thsst.calvis.simulatorvisualizer.animation.instruction.sse;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Pmovmskb extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        ObservableList<Node> parent = this.root.getChildren();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[0];
        Token src = tokens[1];
        int size = timeFunc.getBitSize(des);
        int sizeSrc = timeFunc.getBitSize(src);
        String desStr = timeFunc.getValue(des);
        String srcStr = timeFunc.getPreviousValue(src, size);
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 8;
        Text sign = new Text("Transfer To Destination Value As Hex ^");

        Rectangle desRec = this.createRectangle(des, 230, 80);
        Rectangle srcRec = this.createRectangle(src, 230, 400);
        Rectangle fake = new Rectangle(0,0);
        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text srcValue = new Text(srcStr);
        Text desValue = new Text(desStr);

        parent.addAll(fake, desRec, srcRec, desLabel, srcLabel, srcValue, desValue, sign);


        //

        double posX = 200;
        double posY = 20;

        desRec.setX(posX);
        desRec.setY(posY);

        desLabel.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2));
        desLabel.setY(posY + 15);

        desValue.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desValue.getLayoutBounds().getWidth() / 2));
        desValue.setY(posY + 40);

        srcRec.setX(posX);
        srcRec.setY(posY + desRec.getHeight() + 50);

        sign.setX(srcRec.getWidth() / 2 - sign.getLayoutBounds().getWidth()/2 + posX);
        sign.setY(posY + desRec.getHeight() + 25);

        srcLabel.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2));
        srcLabel.setY(posY + 30 + desRec.getHeight() + 40);

        srcValue.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcValue.getLayoutBounds().getWidth() / 2));
        srcValue.setY(posY + 40 + desRec.getHeight() + 45);

        String srcBits = c.convertHexToBits(srcStr, sizeSrc);
        
        ArrayList<Text> arrIndexesBit = new ArrayList<>();
        for(int x = 0; x < 8; x++){
            arrIndexesBit.add( new Text(this.createLabelText(des).getText() + "[" + x + "] <- " + this.createLabelText(src).getText() + "[" + (7 + (8 * x)) + "]:" + srcBits.charAt(sizeSrc - 1 - (7 + (8 * x)))));
            
            parent.add(arrIndexesBit.get(x));
            arrIndexesBit.get(x).setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - arrIndexesBit.get(x).getLayoutBounds().getWidth() * 0.75));
            arrIndexesBit.get(x).setY(posY + 60 + 20 * arrIndexesBit.size() + desRec.getHeight() + 45);
        }


        Text desRest = new Text(this.createLabelText(des).getText() + "["+ arrIndexesBit.size() +":31] <- " + this.createLabelText(src).getText() + " Fill Zeroes");

        parent.addAll(desRest);



        desRest.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desRest.getLayoutBounds().getWidth() / 2));
        desRest.setY(posY + 60 + 20 * arrIndexesBit.size() + 20 + desRec.getHeight() + 45);

    }
}

