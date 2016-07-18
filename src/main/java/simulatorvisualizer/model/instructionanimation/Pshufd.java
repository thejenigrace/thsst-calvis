package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Pshufd extends CalvisAnimation {

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
        Token ctr = tokens[2];
        int size = timeFunc.getBitSize(des);
        String desStr = timeFunc.getValue(des);
        String srcStr = timeFunc.getPreviousValue(src, size);
        String ctrStr = timeFunc.getValue(ctr, size);
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 4;
        Text sign = new Text("Transfer To Destination Value As Hex ^");

        Rectangle desRec = this.createRectangle(des, 245, 80);
        Rectangle srcRec = this.createRectangle(src, 245, 150);
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
        ArrayList<String> numList = new ArrayList<>();
        String binaryRes = c.binaryZeroExtend(new BigInteger(ctrStr, 16).toString(2), 8);
        for(int x = 0; x < operationSize; x++){

            numList.add(new BigInteger(binaryRes.substring(x * 2, x * 2 + 2), 2).toString(10));
        }
        String textResultNum = "";
        for(int x  = 0; x < numList.size(); x++){
            textResultNum += "    " + numList.get(x);
        }

        Text extractList = new Text("Word Indexes: " + textResultNum);

        Text desFirstWord = new Text(this.createLabelText(des).getText() + "[0 to 7] <- " + desStr.substring(24, 32));
        Text desSecondWord = new Text(this.createLabelText(des).getText() + "[8 to 15] <- " + desStr.substring(16, 24));
        Text desThirdWord = new Text(this.createLabelText(des).getText() + "[16 to 23] <- " + desStr.substring(8, 16));
        Text desFourthWord = new Text(this.createLabelText(des).getText() + "[24 to 31] <- " + desStr.substring(0, 8));

        parent.addAll(desFirstWord, desSecondWord, desThirdWord, desFourthWord, extractList);

        extractList.setX(posX + (srcRec.getLayoutBounds().getWidth() * 0.75 - extractList.getLayoutBounds().getWidth() * 0.75));
        extractList.setY(posY + 60 + desRec.getHeight() + 45);

        desFirstWord.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desFirstWord.getLayoutBounds().getWidth() * 0.75) + 25);
        desFirstWord.setY(posY + 80 + desRec.getHeight() + 45);

        desSecondWord.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desSecondWord.getLayoutBounds().getWidth() * 0.75) + 25);
        desSecondWord.setY(posY + 100 + desRec.getHeight() + 45);

        desThirdWord.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desThirdWord.getLayoutBounds().getWidth() * 0.75) + 25);
        desThirdWord.setY(posY + 120 + desRec.getHeight() + 45);

        desFourthWord.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desFourthWord.getLayoutBounds().getWidth() * 0.75) + 25);
        desFourthWord.setY(posY + 140 + desRec.getHeight() + 45);

    }
}

