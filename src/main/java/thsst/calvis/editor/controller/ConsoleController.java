package thsst.calvis.editor.controller;

import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import thsst.calvis.configuration.model.engine.*;
import thsst.calvis.configuration.model.exceptions.*;
import thsst.calvis.editor.view.AssemblyComponent;
import thsst.calvis.editor.view.ConsoleTextArea;

import java.math.BigInteger;

/**
 * Created by Jennica on 07/02/2016.
 */
public class ConsoleController extends AssemblyComponent {

    private ConsoleTextArea textArea;
    private Tab tab;
    private int lineBefore;
    private CalvisFormattedInstruction currentInstruction;

    public ConsoleController() {
        this.lineBefore = 0;
        this.textArea = new ConsoleTextArea(false);
        this.tab = new Tab();
        this.tab.setText("Console");
        this.tab.setContent(textArea);
    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        if ( currentInstruction != null ) {
            String name = currentInstruction.getName();
            if ( name.matches("PRINTF|SCANF|CLS") ) {
                attachCalvisInstruction(currentInstruction);
                currentInstruction.setConsole(this);
                currentInstruction.getInstruction().consoleExecute(sysCon.getRegisterState(),
                        sysCon.getMemoryState(), this);
            }
        }
    }

    @Override
    public void refresh() {
        cls();
    }

    @Override
    public void build() {
        this.textArea.setOnKeyPressed(keyEvent -> {
            if ( keyEvent.getCode() == KeyCode.ENTER ) {
                if ( currentInstruction != null && textArea.getState() == true ) {
                    try {
                        currentInstruction = null;
                        String retrieved = retrieveScanned();
                        executeScan(retrieved);
                        sysCon.resumeFromConsole();
                    } catch ( Exception e ) {
                        sysCon.reportError(e);
                    }
                }
            }
        });
    }

    public void printf(String text) {
        this.textArea.setState(true);
        this.textArea.appendText(text);
        this.textArea.setState(false);
    }

    public void scanf() {
        this.sysCon.pauseFromConsole();
        this.textArea.setState(true);
        lineBefore = textArea.getText().length();
    }

    public String retrieveScanned() {
        String text = textArea.getText();
        text = textArea.getText(lineBefore, text.length());
        this.textArea.setState(false);
        return text;
    }

    public void cls() {
        this.textArea.setState(true);
        this.textArea.clear();
        this.textArea.setState(false);
        this.lineBefore = 0;
    }

    public void stopConsole() {
        this.textArea.setState(false);
    }

    public void attachCalvisInstruction(CalvisFormattedInstruction CalvisInstruction) {
        this.currentInstruction = CalvisInstruction;
    }

    public void executeScan(String input) throws StackPopException, MemoryReadException,
            MemoryWriteException, DataTypeMismatchException, MemoryAlignmentException {

        RegisterList registers = this.sysCon.getRegisterState();
        Memory memory = this.sysCon.getMemoryState();

        String stackPointer = registers.getStackPointer();
        String pointingTo = memory.read(stackPointer, 32);
        BigInteger pointer = new BigInteger(pointingTo, 16);

        String scanFormat = "";
        // Read the format; assume it's only 1
        while ( !memory.read(pointer.toString(16), 8).equals("00") ) {
            // get one byte
            String first = memory.read(pointer.toString(16), 8);
            char ascii = (char) Integer.parseInt(first, 16);
            pointer = pointer.add(new BigInteger("1"));
            scanFormat += ascii;
        }
        // Get base address of where we store the scanned number
        // always add 4 to stack pointer since we're dealing with addresses
        BigInteger stackAddress = new BigInteger(stackPointer, 16);
        BigInteger offset = new BigInteger("4");
        stackAddress = stackAddress.add(offset);

        if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
            throw new StackPopException(offset.intValue());
        } else {
            String storeToAddress = memory.read(stackAddress.toString(16), 32);
            storeToMemory(memory, scanFormat, input, storeToAddress);
        }
    }

    private void storeToMemory(Memory memory, String format, String input, String baseAddress)
            throws MemoryWriteException, DataTypeMismatchException, MemoryReadException, MemoryAlignmentException {
        if ( format.matches("%d") ) {
            Short realShort = Short.parseShort(input);
            String shortToHex = Integer.toHexString(realShort.intValue());
            memory.write(baseAddress, shortToHex, 16);
        } else if ( format.matches("%ld") ) {
            BigInteger realInt = new BigInteger(input);
            String intToHex = Integer.toHexString(realInt.intValue());
            memory.write(baseAddress, intToHex, 32);
        } else if ( format.matches("%u") ) {
            BigInteger b = new BigInteger(input);
            String unsigned = getUnsigned(16, b.toString(2));
            BigInteger b2 = new BigInteger(unsigned);
            if ( b2.compareTo(new BigInteger("65535")) == 1 || b2.signum() == -1 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %u");
            }
            String unsigned16 = Integer.toHexString(b2.intValue());
            memory.write(baseAddress, unsigned16, 16);
        } else if ( format.matches("%lu") ) {
            BigInteger b = new BigInteger(input);
            String unsigned = getUnsigned(32, b.toString(2));
            BigInteger b2 = new BigInteger(unsigned);
            if ( b2.compareTo(new BigInteger("4,294,967,295")) == 1 || b2.signum() == -1 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %lu");
            } else {
                String unsigned32 = Long.toHexString(b2.longValue());
                memory.write(baseAddress, unsigned32, 32);
            }
        } else if ( format.matches("%x") ) {
            if ( !input.matches(PatternList.hexPattern) ) {
                throw new NumberFormatException("Value is not a hex Value:" + input + " Type: %x");
            } else if ( input.length() > 6 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %x");
            } else {
                String formatHex = input.substring(2);
                memory.write(baseAddress, formatHex, 16);
            }
        } else if ( format.matches("%lx") ) {
            if ( !input.matches(PatternList.hexPattern) ) {
                throw new NumberFormatException("Value is not a hex Value:" + input + " Type: %lx");
            } else if ( input.length() > 10 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %lx");
            } else {
                String formatHex = input.substring(2);
                memory.write(baseAddress, formatHex, 32);
            }
        } else if ( format.matches("%f") ) {
            float floatValue = Float.parseFloat(input);
            String representation = Integer.toHexString(Float.floatToIntBits(floatValue));
            memory.write(baseAddress, representation, 32);
        } else if ( format.matches("%lf") ) {
            Double doubleValue = Double.parseDouble(input);
            String representation = Long.toHexString(Double.doubleToLongBits(doubleValue));
            memory.write(baseAddress, representation, 64);
        } else if ( format.matches("%c") ) {
            if ( input.length() > 1 ) {
                throw new NumberFormatException("Not a character: " + input + " Type: %c");
            } else {
                byte[] realChar = input.getBytes();
                String asciiHexValue = String.format("%2X", realChar[0]);
                memory.write(baseAddress, asciiHexValue, 16);
            }
        } else if ( format.matches("%s") ) {
            byte[] bytes = input.getBytes();
            BigInteger stringPointer = new BigInteger(baseAddress, 16);
            for ( int i = 0; i < bytes.length; i++ ) {
                String asciiHexValue = String.format("%2X", bytes[i]);
                String address = stringPointer.toString(16);
                address = MemoryAddressCalculator.extend(address, 32, "0");
                memory.write(address, asciiHexValue, 8);
                stringPointer = stringPointer.add(new BigInteger("1"));
            }
            // add last 0 to end of scanned string
            String lastAddress = stringPointer.toString(16);
            lastAddress = MemoryAddressCalculator.extend(lastAddress, 32, "0");
            memory.write(lastAddress, "00", 8);
        }
    }

    private String getUnsigned(int size, String stringBits) {
        String temp = stringBits;
        // zero extend
        while ( temp.length() < 16 ) {
            temp = "0" + temp;
        }

        StringBuilder tempBit = new StringBuilder(temp);
        String returnable = "";

        if ( tempBit.charAt(0) == '1' ) {
            tempBit.setCharAt(0, '0');
            tempBit.insert(1, "1");
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        } else {
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        }
        return returnable;
    }

}
