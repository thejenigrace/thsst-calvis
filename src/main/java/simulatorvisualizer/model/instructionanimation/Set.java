package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Set extends CalvisAnimation {

    @Override
    public void animate(Tab tab) {
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
        String text = "";
        switch ( tokens[0].getValue().toUpperCase() ) {
            case "A":
            case "NBE": // fall through
                text = "Condition tested: CF = 0 or ZF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "AE":
            case "NB": // fall through
                text = "Condition tested: CF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "B":
            case "NAE": // fall through
                text = "Condition tested: CF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "BE":
            case "NA": // fall through
                text = "Condition tested: CF = 1 or ZF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "G":
            case "NLE": // fall through
                text = "Condition tested: SF = OF or ZF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "GE":
            case "NL": // fall through
                text = "Condition tested: SF = OF \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
            case "L":
            case "NGE": // fall through
                text = "Condition tested: SF != OF\n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "LE":
            case "NG": // fall through
                text = "Condition tested: SF != OF or ZF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "E":
            case "Z": // fall through
                text = "Condition tested: ZF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "NE":
            case "NZ": // fall through
                text = "Condition tested: ZF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "P":
            case "PE": // fall through
                text = "Condition tested: PF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "NP":
            case "PO": // fall through
                text = "Condition tested: PF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "O":
                text = "Condition tested: OF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "NO":
                text = "Condition tested: OF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "C":
                text = "Condition tested: CF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "NC":
                text = "Condition tested: CF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "S":
                text = "Condition tested: SF = 1 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;
            case "NS":
                text = "Condition tested: SF = 0 \n" +
                        "If condition is true, " + tokens[1].getValue() + " is set to 1. Else, 0. \n" +
                        "Affected flags: none";
                break;

            default:
                System.out.println("Condition not found");
                text = "Condition not found";
                break;


        }

        Text description = new Text(text);
        description.setX(100);
        description.setY(100);
        this.root.getChildren().addAll(description);
    }
}