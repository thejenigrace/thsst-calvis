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
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.PushModel;
import simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Popad extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
//        Token des = tokens[0];
//        int operandSize = timeFunc.getBitSize(des);
//        String desVal = timeFunc.getValue(des, operandSize);
        ObservableList<Node> parent = this.root.getChildren();
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);

        ArrayList<String> registerList = new ArrayList<>();
        registerList.add("EAX");
        registerList.add("ECX");
        registerList.add("EDX");
        registerList.add("EBX");
        registerList.add("ESP");
        registerList.add("EBP");
        registerList.add("ESI");
        registerList.add("EDI");

        ArrayList<String> spList = new ArrayList<>();
        String currentSp = registers.get("ESP");
        for(int x = 0; x < registerList.size(); x++){
            String multiplier = Integer.toString(4 * x);
            BigInteger result = new BigInteger(currentSp, 16).subtract(new BigInteger(multiplier, 16));
            spList.add(c.hexZeroExtend(result.toString(16).toUpperCase(), 8));
        }

        ArrayList<PushModel> pushModels = new ArrayList<>();
        for(int x = 0; x < registerList.size(); x++){
            pushModels.add(new PushModel(registerList.get(x), spList.get(x)));
        }

        ArrayList<Rectangle> desRecArr = new ArrayList<>();
        ArrayList<Text> desLabelArr = new ArrayList<>();
        ArrayList<Text> desValueArr = new ArrayList<>();
        ArrayList<Text> desValueMovingArr = new ArrayList<>();
        ArrayList<Rectangle> stackRecArr = new ArrayList<>();
        ArrayList<Text> stackLabelArr = new ArrayList<>();
        ArrayList<Text> espLabelArr = new ArrayList<>();
        ArrayList<Text> pushLabelArr = new ArrayList<>();
        ArrayList<Text> espValArr = new ArrayList<>();
        ArrayList<Text> insLabelArr = new ArrayList<>();

        parent.add(fake);
        for(int x = 0; x < pushModels.size(); x++){
            desRecArr.add(timeFunc.createRectangle(140, 90, Color.web("#e9d66b")));
            desLabelArr.add(timeFunc.generateText(new Text(pushModels.get(x).getResult()), 15, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            desValueArr.add(timeFunc.generateText(new Text(registers.get(pushModels.get(x).getResult())), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            desValueMovingArr.add(timeFunc.generateText(new Text(registers.get(pushModels.get(x).getResult())), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));

            stackRecArr.add(timeFunc.createRectangle(140, 90, Color.web("#6e7f80")));
            stackLabelArr.add(timeFunc.generateText(new Text("STACK"), 18, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            espLabelArr.add(timeFunc.generateText(new Text("ESP"), 15, "#b31b1b", FontWeight.BOLD, "Elephant"));
            pushLabelArr.add(timeFunc.generateText(new Text("POPPED VALUE"), 14, "#b31b1b", FontWeight.BOLD, "Elephant"));
            espValArr.add(timeFunc.generateText(new Text(pushModels.get(x).getSP()), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            insLabelArr.add(timeFunc.generateText(new Text("<= Value will be popped to"), 15, "#b31b1b", FontWeight.BOLD, "Elephant"));
            parent.addAll(insLabelArr.get(x), desRecArr.get(x), desLabelArr.get(x), stackRecArr.get(x), espValArr.get(x), stackLabelArr.get(x), desValueMovingArr.get(x), espLabelArr.get(x), pushLabelArr.get(x), desValueArr.get(x));
            //        Timeline animations
            desValueArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - desValueMovingArr.get(x).getLayoutBounds().getWidth() / 2 + 400);
            desValueArr.get(x).setY(100 + (140 * x));

            desValueMovingArr.get(x).setVisible(false);

            timeFunc.addTimeline(desRecArr.get(x).getLayoutBounds().getWidth() / 2 - desValueMovingArr.get(x).getLayoutBounds().getWidth() / 2 + 20, 80 + (140 * x), 3000, desValueMovingArr.get(x));
            timeFunc.addTimeline(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - desValueMovingArr.get(x).getLayoutBounds().getWidth() / 2 + 400, 100 + (140 * x), 0, desValueMovingArr.get(x));

            desLabelArr.get(x).setX(desRecArr.get(x).getLayoutBounds().getWidth() / 2 - desLabelArr.get(x).getLayoutBounds().getWidth() / 2 + 20);
            desLabelArr.get(x).setY(50 + (140 * x));

            stackRecArr.get(x).setX(400);
            stackRecArr.get(x).setY(20 + (140 * x));

            espValArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - espValArr.get(x).getLayoutBounds().getWidth() / 2 + 400);
            espValArr.get(x).setY(55 + (140 * x));

            stackLabelArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - stackLabelArr.get(x).getLayoutBounds().getWidth() / 2 + 400);
            stackLabelArr.get(x).setY(15 + (140 * x));

            desRecArr.get(x).setX(20);
            desRecArr.get(x).setY(20 + (140 * x));

            espLabelArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - espLabelArr.get(x).getLayoutBounds().getWidth() / 2 + 400);
            espLabelArr.get(x).setY(35 + (140 * x));

            pushLabelArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 - pushLabelArr.get(x).getLayoutBounds().getWidth() / 2 + 400);
            pushLabelArr.get(x).setY(80 + (140 * x));

            insLabelArr.get(x).setX(stackRecArr.get(x).getLayoutBounds().getWidth() / 2 + 100 + 5);
            insLabelArr.get(x).setY(70 + (140 * x));

        timeFunc.hideshowElement(desValueMovingArr.get(x), 1000 / 5, true);
        timeFunc.hideshowElement(desValueArr.get(x), 1000 / 5 + 3000 - 250, false);
        timeFunc.hideshowElement(pushLabelArr.get(x), 1000 / 5 + 3000 - 250, false);
        }

        desValueArr.get(4).setText(finder.getRegister("ESP"));
        desValueMovingArr.get(4).setText(finder.getRegister("ESP"));
        timeline.setDelay(Duration.millis(2000));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
