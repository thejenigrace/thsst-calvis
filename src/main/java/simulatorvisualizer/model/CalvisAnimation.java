package simulatorvisualizer.model;

import configuration.model.engine.CalvisFormattedInstruction;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public abstract class CalvisAnimation {

    protected Timeline timeline;
    protected Group root;
    protected CalvisFormattedInstruction currentInstruction;

    public CalvisAnimation() {
        this.timeline = new Timeline();
        this.root = new Group();
    }

    public abstract void animate(Tab tab);

    public void setCurrentInstruction(CalvisFormattedInstruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }

    protected Rectangle createRectangle(Token token, int width, int height) {
        // Check token type
        switch ( token.getType() ) {
            case Token.REG:
                System.out.println("REG");
                return new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
            case Token.MEM:
                System.out.println("MEM");
                return new Rectangle(width, height, Color.web("#79CFCE", 1.0));
            case Token.HEX:
                System.out.println("HEX");
                return new Rectangle(width, height, Color.web("#7BB88C", 1.0));
            default:
                return null;
        }
    }

    protected Text createLabelText(Token token) {
        switch ( token.getType() ) {
            case Token.REG:
                return new Text(token.getValue());
            case Token.MEM:
                return new Text("[" + token.getValue() + "]");
            case Token.HEX:
                return new Text("HEX");
            default:
                return null;
        }
    }

    protected Text createValueText(Token token, RegisterList registers, Memory memory, int size) {
        try {
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG");
                    return new Text(registers.get(token));
                case Token.MEM:
                    System.out.println("MEM");

                    return new Text(memory.read(token, size));
                case Token.HEX:
                    System.out.println("HEX");
                    return new Text("0x" + token.getValue());
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
}
