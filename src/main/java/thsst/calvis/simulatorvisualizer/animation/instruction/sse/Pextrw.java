package thsst.calvis.simulatorvisualizer.animation.instruction.sse;


import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Pextrw extends CalvisAnimation {

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
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 8;
        int count = Integer.parseInt(new BigInteger(timeFunc.getValue(ctr, size), 16).toString(10)) % 8;
        Text sign = new Text("Transfer Word To^");

        Rectangle desRec = this.createRectangle(des, 230, 80);
        Rectangle srcRec = this.createRectangle(src, 320, 150);
        Rectangle fake = new Rectangle(0,0);
        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text srcValue = new Text(srcStr);
        Text desValue = new Text(desStr);

        parent.addAll(fake, desRec, srcRec, desLabel, srcLabel, srcValue, desValue, sign);


        //

        double posX = 200;
        double posY = 20;

        desRec.setX(posX + 50);
        desRec.setY(posY);

        desLabel.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2) + 50);
        desLabel.setY(posY + 15);

        desValue.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desValue.getLayoutBounds().getWidth() / 2) + 50);
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
        
        Text counterText = new Text(this.createLabelText(src).getText() + "Word Index : " + Integer.toString(count));
        Group textGroup = new Group();
        Text extractedWord = new Text(" " + srcStr.substring(4 * count,4 * count + 4 ) + "");
        extractedWord.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Text firstHalf = new Text("");
        if(count > 0){
            firstHalf.setText(srcStr.substring((0), 4 * count));
        }
        Text secondHalf = new Text("");
        
        if(count + 1 < timeFunc.getBitSize(src) / 16){
            secondHalf.setText(srcStr.substring((4 * count) + 4));
        }

        TextFlow combinedText = new TextFlow(new Text("Word:"), firstHalf, extractedWord, secondHalf);
        combinedText.setLayoutX(posX + (srcRec.getLayoutBounds().getWidth()/ 9 - combinedText.getLayoutBounds().getWidth()/ 2));
        combinedText.setLayoutY(posY + 80 + desRec.getHeight() + 45);

        parent.addAll(counterText, combinedText);

        counterText.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - counterText.getLayoutBounds().getWidth() * 0.75));
        counterText.setY(posY + 60 + desRec.getHeight() + 45);



    }
}

