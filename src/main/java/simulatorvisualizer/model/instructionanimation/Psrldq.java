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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.RotateModel;
import simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Psrldq extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[0];
        Token count = tokens[1];
        int counter = timeFunc.parseHex(count);
        int operandSize = timeFunc.getBitSize(des);
        ObservableList<Node> parent = this.root.getChildren();

        ArrayList<RotateModel> rotateModels = new ArrayList<>();
        String destination = timeFunc.getPreviousValue(des, timeFunc.getBitSize(des));
        BigInteger biDes = new BigInteger(destination, 16);
        BigInteger biResult = biDes;
        boolean bitSet = false;

        String temp = "";
        String resultingHex = destination;
        for(int x = 0; x < (counter); x++){
            temp = resultingHex.substring(0, 30);
            System.out.println(32 - ((x + 1) * 2) + " num");
            temp = "00" + temp;
            resultingHex = temp;
            rotateModels.add(new RotateModel(resultingHex, "0"));
        }

        ArrayList<Text> rotateResults = new ArrayList<>();
        ArrayList<Text> zeroFlags = new ArrayList<>();
        ArrayList<Text> resultLabel = new ArrayList<>();
//        ArrayList<Text> cfLabel = new ArrayList<>();
        ArrayList<Text> numberLabel = new ArrayList<>();
        Text resultText = timeFunc.generateText(new Text("Final Result"), 20, "#3d2b1f", FontWeight.NORMAL, "System");
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        parent.add(fake);
        if(counter == 0){
            rotateModels.add(new RotateModel(timeFunc.getValue(des), registers.getEFlags().getCarryFlag()));
            resultLabel.add(timeFunc.generateText(new Text(des.getValue()), 15, "#3d2b1f", FontWeight.NORMAL, "System"));
            rotateResults.add(timeFunc.generateText(new Text(rotateModels.get(0).getResult()), 20, "#5d8aa8", FontWeight.NORMAL, "System"));
            zeroFlags.add(timeFunc.generateText(new Text(), 20, "#5d8aa8", FontWeight.NORMAL, "System"));
//            cfLabel.add(timeFunc.generateText(new Text("CF"), 15, "#3d2b1f", FontWeight.NORMAL, "System"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(0)), 20, "#a4c639", FontWeight.NORMAL, "System"));
            parent.addAll(rotateResults.get(0), zeroFlags.get(0), resultLabel.get(0), numberLabel.get(0));
            timeFunc.setTimelinePosition(60 + rotateResults.get(0).getLayoutBounds().getWidth(), 20 + 50 * 0, zeroFlags.get(0));
            timeFunc.setTimelinePosition(20 + 30, 20 + 50 * 0, rotateResults.get(0));
//            timeFunc.setTimelinePosition(60 + rotateResults.get(0).getLayoutBounds().getWidth() , 40 + 50 * 0, cfLabel.get(0));
            timeFunc.setTimelinePosition(20 + 30 , 40 + 50 * 0, resultLabel.get(0));
            timeFunc.setTimelinePosition(10 , 20 + 50 * 0, numberLabel.get(0));
        }
        for(int x = 0; x < counter; x++){
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 20, "#a4c639", FontWeight.NORMAL, "System"));
            rotateResults.add(timeFunc.generateText(new Text(rotateModels.get(x).getResult()), 20, "#5d8aa8", FontWeight.NORMAL, "System"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels.get(x).getFlag()), 20, "#5d8aa8", FontWeight.NORMAL, "System"));
            resultLabel.add(timeFunc.generateText(new Text(des.getValue()), 15, "#3d2b1f", FontWeight.NORMAL, "System"));
//            cfLabel.add(timeFunc.generateText(new Text("CF"), 15, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30, 20 + 50 * x, zeroFlags.get(x));
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30
                    + zeroFlags.get(x).getLayoutBounds().getWidth() + 30, 20 + 50 * x, rotateResults.get(x));
//            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30 , 40 + 50 * x, cfLabel.get(x));
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30
                    + zeroFlags.get(x).getLayoutBounds().getWidth() + 30 , 40 + 50 * x, resultLabel.get(x));
            timeFunc.setTimelinePosition(10 , 20 + 50 * x, numberLabel.get(x));
        }
        parent.add(resultText);
        timeFunc.setTimelinePosition(20 + 60 + zeroFlags.get(0).getLayoutBounds().getWidth() + 80 + resultLabel.get(0).getLayoutBounds().getWidth(), 40 + 50 * (counter  - 1), resultText);


        timeline.play();
    }
}
