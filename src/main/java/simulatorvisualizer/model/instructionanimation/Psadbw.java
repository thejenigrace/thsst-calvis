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
public class Psadbw extends CalvisAnimation {

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
        String srcPrevStr = timeFunc.getPreviousValue(src, size);
        String desPrevStr = timeFunc.getPreviousValue(des, size);
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 8;
        Text sign = new Text("Transfer To Destination Value As Hex ^");

        Rectangle desRec = this.createRectangle(des, 230, 80);
        Rectangle srcRec = this.createRectangle(src, 370, 485);
        Rectangle fake = new Rectangle(0,0);
        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text srcValue = new Text(srcPrevStr);
        Text desValue = new Text(desStr);

        parent.addAll(fake, desRec, srcRec, desLabel, srcLabel, srcValue, desValue, sign);


        //

        double posX = 200;
        double posY = 20;
        double posAdded = - 60;
        desRec.setX(posX);
        desRec.setY(posY);

        desLabel.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2));
        desLabel.setY(posY + 15);

        desValue.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desValue.getLayoutBounds().getWidth() / 2));
        desValue.setY(posY + 40);

        srcRec.setX(posX + posAdded);
        srcRec.setY(posY + desRec.getHeight() + 50);

        sign.setX(srcRec.getWidth() / 2 - sign.getLayoutBounds().getWidth()/2 + posX + posAdded);
        sign.setY(posY + desRec.getHeight() + 25);

        srcLabel.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2) + posAdded);
        srcLabel.setY(posY + 30 + desRec.getHeight() + 40);

        srcValue.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcValue.getLayoutBounds().getWidth() / 2) + posAdded);
        srcValue.setY(posY + 40 + desRec.getHeight() + 45);

        String srcBits = c.convertHexToBits(srcPrevStr, sizeSrc);

        ArrayList<Text> arrIndexesBit = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> desArrWord = new ArrayList<>();
        ArrayList<String> srcArrWord = new ArrayList<>();
        ArrayList<String> resArrWord = new ArrayList<>();
        int valIndex = ((size / 4 ));
        String addedResultFirst = "00";
        String addedResultSecond = "00";
        for(int x = 0; x < (valIndex); x += 2){

            String desX = desPrevStr.substring(valIndex - x - 2, valIndex - x);
            String srcX = srcPrevStr.substring(valIndex - x - 2, valIndex - x);
            String resX = c.hexZeroExtend(new BigInteger(desX, 16).subtract(new BigInteger(srcX, 16)).toString(16).toUpperCase(), 2);
            if(x < 16){
                addedResultFirst = c.hexZeroExtend(new BigInteger(addedResultFirst, 16).add(new BigInteger(resX, 16)).toString(16).toUpperCase(), 2);
            }
            else{
                addedResultSecond = c.hexZeroExtend(new BigInteger(addedResultSecond, 16).add(new BigInteger(resX, 16)).toString(16).toUpperCase(), 2);
            }
            desArrWord.add(desX);
            srcArrWord.add(srcX);
            resArrWord.add(resX);


        }
        for(int x = 0; x < (size / 4 ) / 2; x++){

            temp.add(Integer.toString(x * 7) + ":" + Integer.toString(x * 7 + 7));
            String tempIndexes = temp.get(x);
            arrIndexesBit.add( new Text("TEMP[" + x + "] <- ABS("+ desArrWord.get(x) + " - " + srcArrWord.get(x)  + ")"));

            parent.add(arrIndexesBit.get(x));
            arrIndexesBit.get(x).setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - arrIndexesBit.get(x).getLayoutBounds().getWidth() /2) + posAdded);
            arrIndexesBit.get(x).setY(posY + 60 + 20 * arrIndexesBit.size() + desRec.getHeight() + 45);
        }

        if(size == 64){
            Text desRest = new Text(this.createLabelText(des).getText() + "[" + 0 + ":15] <- SUM(TEMP[0] to TEMP[7]: ");
            Text zeroedFirst = new Text(this.createLabelText(des).getText() + "[" + 16 + ":63] <- Fill Zeroes");
            parent.addAll(desRest, zeroedFirst);



            desRest.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desRest.getLayoutBounds().getWidth() / 2) + posAdded);
            desRest.setY(posY + 60 + 20 * arrIndexesBit.size() + 20 + desRec.getHeight() + 45);
        }
        else{
            Text desFirst = new Text(this.createLabelText(des).getText() + "[" + 0 + ":15] <- SUM(TEMP[0] to TEMP[7]: " );
            Text desSecond = new Text(this.createLabelText(des).getText() + "[" + 64 + ":79] <- SUM(TEMP[8] to TEMP[15]: ");
            Text firstZero = new Text(this.createLabelText(des).getText() + "[" + 16 + ":63] <- Fill With Zeroes");
            Text secondZero = new Text(this.createLabelText(des).getText() + "[" + 80 + ":127] <- Fill With Zeroes");
            parent.addAll(desFirst, desSecond, firstZero, secondZero);

            desFirst.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desFirst.getLayoutBounds().getWidth() / 2) + posAdded);
            desFirst.setY(posY + 60 + 20 * arrIndexesBit.size() + 20 + desRec.getHeight() + 45);

            desSecond.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - desSecond.getLayoutBounds().getWidth() / 2) + posAdded);
            desSecond.setY(posY + 60 + 20 * arrIndexesBit.size() + 60 + desRec.getHeight() + 45);

            firstZero.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - firstZero.getLayoutBounds().getWidth() / 2) + posAdded);
            firstZero.setY(posY + 60 + 20 * arrIndexesBit.size() + 40 + desRec.getHeight() + 45);

            secondZero.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - secondZero.getLayoutBounds().getWidth() / 2) + posAdded);
            secondZero.setY(posY + 60 + 20 * arrIndexesBit.size() + 80 + desRec.getHeight() + 45);
        }
    }
}

