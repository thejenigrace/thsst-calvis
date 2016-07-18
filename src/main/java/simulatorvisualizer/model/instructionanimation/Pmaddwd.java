package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Pmaddwd extends CalvisAnimation {

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
        String desStr = timeFunc.getPreviousValue(des, size);
        String srcStr = timeFunc.getPreviousValue(src, size);
        String resultStr = timeFunc.getValue(des, size);

        int operationSize = 4;
        Text sign = timeFunc.generateText(new Text("*"), 30, "#98777b");
        Text arrow = timeFunc.generateText(new Text("V"), 30, "#98777b");
        Text plusSign = timeFunc.generateText(new Text("+"), 30, "#98777b");
        Text equal = timeFunc.generateText(new Text("="), 30, "#98777b");
        String dividedStrDes = "";
        String dividedStrSrc = "";
        String dividedStrRes = "";
        String dividedStrTempOne = "";
        String dividedStrTempTwo = "";
        ArrayList<String> resultingMultiply = new ArrayList<>();


        for(int x = 0; x < (size/4) / 2; x = x + operationSize){
            String strDes = desStr.substring(0 + x, operationSize + x);
            String strSrc = (srcStr.substring(0 + x, operationSize + x));
            dividedStrDes += strDes + "     ";
            dividedStrSrc += strSrc + "     ";
            resultingMultiply.add(c.hexZeroExtend(new BigInteger(strDes, 16).multiply(new BigInteger(strSrc, 16)).toString(16), operationSize * 2));

        }

        for(int x = 0; x < (size/4) / 2; x = x + operationSize){
           
           dividedStrRes += (resultStr.substring(x, x + operationSize * 2)) + "      ";
        }

        for(int x  = 0; x < resultingMultiply.size(); x += 2){
            dividedStrTempOne += resultingMultiply.get(x) + "      ";
        }

        for(int x  = 1; x < resultingMultiply.size(); x += 2){
            dividedStrTempTwo += resultingMultiply.get(x) + "      ";
        }

//        String x = dividedStrDes.contains("sadasdad");
        Rectangle fake = new Rectangle(0,0, Color.web("#4b5320"));

        fake.setX(0);
        fake.setY(0);

        double cordX = 200;
        double cordY = 25;

        Rectangle desRec = this.createRectangle(des, 500, 60);
        Rectangle srcRec = this.createRectangle(src, 500, 60);
        Rectangle resRec = this.createRectangle(des, 500, 60);
        Rectangle tempMulRecOne = this.createRectangle(des, 500, 60);
        Rectangle tempMulRecTwo = this.createRectangle(des, 500, 60);
        parent.addAll(fake, desRec, srcRec, resRec, sign, tempMulRecOne, tempMulRecTwo, plusSign, arrow);

        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text resLabel = this.createLabelText(des);
        Text tempLabelOne = timeFunc.generateText(new Text("Addition of Products that are next to each other"), 13, "#000000");

        Text desVals = new Text();
        Text srcVals = new Text();
        Text resVals = new Text();
        Text tempOneVals = new Text();
        Text tempTwoVals = new Text();

        desRec.setX(cordX);
        desRec.setY(cordY);

        srcRec.setX(cordX);
        srcRec.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight());

        tempMulRecOne.setX(cordX);
        tempMulRecOne.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight());

        tempMulRecTwo.setX(cordX);
        tempMulRecTwo.setY(tempMulRecOne.getY() + 50 + tempMulRecOne.getLayoutBounds().getHeight());

        resRec.setX(cordX);
        resRec.setY(tempMulRecTwo.getY() + 50 + tempMulRecTwo.getLayoutBounds().getHeight());

        //end of rectangle

        desLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2 + desRec.getX() );
        desLabel.setY(cordY + 20);

        srcLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
        srcLabel.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 20);

        tempLabelOne.setX(tempMulRecOne.getLayoutBounds().getWidth() / 2 - tempLabelOne.getLayoutBounds().getWidth() / 2 + srcRec.getX());
        tempLabelOne.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight() + 20);

        resLabel.setX(tempMulRecTwo.getLayoutBounds().getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
        resLabel.setY(tempMulRecTwo.getY() + 50 + tempMulRecTwo.getLayoutBounds().getHeight() + 20);


        desVals.setText(dividedStrDes);
        srcVals.setText(dividedStrSrc);
        tempOneVals.setText(dividedStrTempOne);
        tempTwoVals.setText(dividedStrTempTwo);
        resVals.setText(dividedStrRes);

        parent.addAll(desVals, srcVals, tempOneVals, tempTwoVals, equal, resVals);


        parent.addAll(resLabel, srcLabel, desLabel, tempLabelOne);

        double desComp = desRec.getLayoutBounds().getWidth() / 2 - desVals.getLayoutBounds().getWidth() / 2 + desRec.getX();
        desVals.setX( desComp);
        desVals.setY(cordY + 40);

        double srcComp = srcRec.getLayoutBounds().getWidth() / 2 - srcVals.getLayoutBounds().getWidth() / 2 + srcRec.getX();
        srcVals.setX(srcComp);
        srcVals.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 40);

        double tempOneComp = tempMulRecOne.getLayoutBounds().getWidth() / 2 - tempOneVals.getLayoutBounds().getWidth() / 2 + srcRec.getX();
        tempOneVals.setX(tempOneComp);
        tempOneVals.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight() + 40);

        double tempTwoComp = tempMulRecTwo.getLayoutBounds().getWidth() / 2 - tempTwoVals.getLayoutBounds().getWidth() / 2 + tempMulRecOne.getX();
        tempTwoVals.setX(tempTwoComp);
        tempTwoVals.setY(tempMulRecOne.getY() + 50 + tempMulRecOne.getLayoutBounds().getHeight() + 40);

        double resComp = resRec.getLayoutBounds().getWidth() / 2 - resVals.getLayoutBounds().getWidth() / 2 + tempMulRecTwo.getX();
        resVals.setX(resComp);
        resVals.setY(tempMulRecTwo.getY() + 50 + tempMulRecTwo.getLayoutBounds().getHeight() + 40);

        plusSign.setX(tempMulRecTwo.getLayoutBounds().getWidth() / 2 - plusSign.getLayoutBounds().getWidth() / 2 + + desRec.getX());
        plusSign.setY(tempMulRecTwo.getY() - 20);

        sign.setX(desRec.getLayoutBounds().getWidth() / 2 - sign.getLayoutBounds().getWidth() / 2 + desRec.getX());
        sign.setY(cordY + 100);

        arrow.setX(desRec.getLayoutBounds().getWidth() / 2 - arrow.getLayoutBounds().getWidth() / 2 + desRec.getX());
        arrow.setY(cordY + 200);

        equal.setX(desRec.getLayoutBounds().getWidth() / 2 - equal.getLayoutBounds().getWidth() / 2 + desRec.getX());
        equal.setY(cordY + 420);


    }
}

