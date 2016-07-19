package thsst.calvis.simulatorvisualizer.animation.instruction.sse2;

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

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Movmskpd extends CalvisAnimation {

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
        String desStr = timeFunc.getValue(des);
        String srcStr = timeFunc.getPreviousValue(src, size);
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 8;
        Text sign = new Text("Transfer To Destination Value As Hex ^");

        Rectangle desRec = this.createRectangle(des, 230, 80);
        Rectangle srcRec = this.createRectangle(src, 230, 150);
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

        String srcBits = c.convertHexToBits(srcStr, timeFunc.getBitSize(src));
        System.out.println("srcBITS " + srcBits);
        Text desFirstBit = new Text(this.createLabelText(des).getText() + "[0] <- " + this.createLabelText(src).getText() + "[31]:" + srcBits.charAt(127 - 31));
        Text desSecondBit = new Text(this.createLabelText(des).getText() + "[1] <- " + this.createLabelText(src).getText() + "[63]:" + srcBits.charAt(127 - 127));
        Text desThirdBit = new Text(this.createLabelText(des).getText() + "[2 to 31] <- Zero Extend");
//        Text desFourthBit = new Text(this.createLabelText(des).getText() + "[3] <- " + this.createLabelText(src).getText() + "[127]:" + srcBits.charAt(0));
//        Text desRest = new Text(this.createLabelText(des).getText() + "[4:31] <- " + this.createLabelText(src).getText() + "Pad Zeroes");

        parent.addAll(desFirstBit, desSecondBit, desThirdBit);

        desFirstBit.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desFirstBit.getLayoutBounds().getWidth() * 0.75));
        desFirstBit.setY(posY + 60 + desRec.getHeight() + 45);

        desSecondBit.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desSecondBit.getLayoutBounds().getWidth() * 0.75));
        desSecondBit.setY(posY + 80 + desRec.getHeight() + 45);

        desThirdBit.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desThirdBit.getLayoutBounds().getWidth() * 0.75));
        desThirdBit.setY(posY + 100 + desRec.getHeight() + 45);

//        desFourthBit.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desFourthBit.getLayoutBounds().getWidth() * 0.75));
//        desFourthBit.setY(posY + 120 + desRec.getHeight() + 45);
//
//        desRest.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desRest.getLayoutBounds().getWidth() / 2));
//        desRest.setY(posY + 140 + desRec.getHeight() + 45);

    }
}

