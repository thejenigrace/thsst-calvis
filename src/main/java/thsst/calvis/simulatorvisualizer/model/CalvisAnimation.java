package thsst.calvis.simulatorvisualizer.model;

import thsst.calvis.configuration.model.engine.CalvisFormattedInstruction;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
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
    protected EnvironmentBag oldEnvironment;
    protected EnvironmentBagFinder finder;

    protected static final double X = 100;
    protected static final double Y = 100;

    public CalvisAnimation() {
        this.timeline = new Timeline();
        this.root = new Group();
        this.finder = new EnvironmentBagFinder();
    }

    public abstract void animate(ScrollPane scrollPane);

    public void setCurrentInstruction(CalvisFormattedInstruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }

    public void setOldEnvironment(EnvironmentBag environment) {
        this.oldEnvironment = environment;
        this.finder.setEnvironmentBag(environment);
        this.finder.setMemoryLookup(currentInstruction.getMemory().getLookupTable());
        this.finder.setRegisterLookup(currentInstruction.getRegisters().getRegisterLookup());
    }

    protected Rectangle createRectangle(Token token, int width, int height) {
        // Check token type
        switch ( token.getType() ) {
            case Token.REG:
//                System.out.println("REG");
                return new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
            case Token.MEM:
//                System.out.println("MEM");
                return new Rectangle(width, height, Color.web("#79CFCE", 1.0));
            case Token.HEX:
//                System.out.println("IMMEDIATE");
                return new Rectangle(width, height, Color.web("#7BB88C", 1.0));
            default:
                return new Rectangle(width, height, Color.web("#D1CAAE", 1.0));
        }
    }

    protected Rectangle createRectangle(String token, int width, int height) {
        // Check token type
        switch ( token ) {
            case Token.REG:
//                System.out.println("REG");
                return new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
            case Token.MEM:
//                System.out.println("MEM");
                return new Rectangle(width, height, Color.web("#79CFCE", 1.0));
            case Token.HEX:
//                System.out.println("IMMEDIATE");
                return new Rectangle(width, height, Color.web("#7BB88C", 1.0));
            default:
                return new Rectangle(width, height, Color.web("#D1CAAE", 1.0));
        }
    }

    protected Rectangle createRectangle(int width, int height) {
        return new Rectangle(width, height, Color.web("#FCBD6D", 1.0));
    }

    protected Text createLabelText(String name) {
                return new Text(name);
    }

    protected Text createLabelText(Token token) {
        switch ( token.getType() ) {
            case Token.REG:
                return new Text(token.getValue());
            case Token.MEM:
                return new Text("[" + token.getValue() + "]");
            case Token.HEX:
                return new Text("IMMEDIATE");
            default:
                return null;
        }
    }

    protected Text createLabelText(double x, double y, Token token) {
        switch ( token.getType() ) {
            case Token.REG:
                return new Text(x, y, token.getValue());
            case Token.MEM:
                return new Text(x, y, "[" + token.getValue() + "]");
            case Token.HEX:
                return new Text(x, y, "IMMEDIATE");
            default:
                return null;
        }
    }

    protected Text createValueText(Token token, RegisterList registers, Memory memory, int bitSize) {
        try {
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG");
                    return new Text("0x" + registers.get(token));
                case Token.MEM:
                    System.out.println("MEM");

                    return new Text(memory.read(token, bitSize));
                case Token.HEX:
                    System.out.println("IMMEDIATE");
                    return new Text("0x" + token.getValue());
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected Text createValueText(double x, double y, Token token, RegisterList registers, Memory memory, int bitSize) {
        try {
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG");
                    return new Text(x, y, "0x" + registers.get(token));
                case Token.MEM:
                    System.out.println("MEM");
                    return new Text(x, y, memory.read(token, bitSize));
                case Token.HEX:
                    System.out.println("IMMEDIATE");
                    return new Text(x, y, "0x" + token.getValue());
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected Text createValueTextUsingFinder(double x, double y, Token token, int bitSize) {
        try {
            System.out.println("Using Finder!");
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG");
                    return new Text(x, y, "0x" + this.finder.getRegister(token.getValue()));
                case Token.MEM:
                    System.out.println("MEM");
                    return new Text(x, y, this.finder.read(token, bitSize));
                case Token.HEX:
                    System.out.println("IMMEDIATE");
                    return new Text(x, y, "0x" + token.getValue());
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected Text createValueTextUsingFinderNotHex(double x, double y, Token token, int bitSize) {
        try {
            System.out.println("Using Finder Not Hex!");
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG");
                    return new Text(x, y, this.finder.getRegister(token.getValue()));
                case Token.MEM:
                    System.out.println("MEM");
                    return new Text(x, y, this.finder.read(token, bitSize));
                case Token.HEX:
                    System.out.println("IMMEDIATE");
                    return new Text(x, y, token.getValue());
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getValueString(Token token, RegisterList registers, Memory memory, int bitSize) {
        try {
            switch ( token.getType() ) {
                case Token.REG:
                    System.out.println("REG -- VALUE STRING");
                    return registers.get(token);
                case Token.MEM:
                    System.out.println("MEM -- VALUE STRING");
                    return memory.read(token, bitSize);
                case Token.HEX:
                    System.out.println("IMMEDIATE -- VALUE STRING");
                    return token.getValue();
                default:
                    return null;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getSubHigherHexValueString(Token token, RegisterList registers, Memory memory, int bitSize, int packedSize) {
        String hexValue = this.getValueString(token, registers, memory, bitSize);

        System.out.println("hexValue = " + hexValue);
        System.out.println("bitSize = " + bitSize + "; packedSize = " + packedSize);

        if ( packedSize != hexValue.length() ) {
            System.out.println("subHigherHexValue = " + hexValue.substring(0, packedSize));
            return hexValue.substring(0, packedSize);
        }

        return hexValue;
    }

    protected String getSubLowerHexValueString(Token token, RegisterList registers, Memory memory, int bitSize, int packedSize) {
        String hexValue = this.getValueString(token, registers, memory, bitSize);

        System.out.println("hexValue = " + hexValue);
        System.out.println("bitSize = " + bitSize + "; packedSize = " + packedSize);

        if ( packedSize != hexValue.length() ) {
            System.out.println("subLowerHexValue = " + hexValue.substring(hexValue.length()-packedSize, hexValue.length()));
            return hexValue.substring(hexValue.length()-packedSize, hexValue.length());
        }

        return hexValue;
    }

    protected String chopHexValue(String hexValue, int packedSize) {
        // Chop hex value into certain given packed size (hex size)
        String desValueWithDesign = "";

        System.out.println(packedSize);

        if ( packedSize != hexValue.length() / 2 ) {
            System.out.println("IF");
            for ( int i = 1; i <= hexValue.length(); i++ ) {
                if ( i % packedSize == 0 )
                    desValueWithDesign += hexValue.charAt(i - 1) + "    ";
                else
                    desValueWithDesign += hexValue.charAt(i - 1);
            }
        } else {
            System.out.println("ELSE");
            int halfIndexPosition = hexValue.length() / 2 - 1;
            desValueWithDesign += hexValue.substring(0, halfIndexPosition + 1) +
                    "       " + hexValue.substring(halfIndexPosition + 1);
        }

        System.out.println("desValueWithDesign: " + desValueWithDesign);

        return desValueWithDesign;
    }

}
