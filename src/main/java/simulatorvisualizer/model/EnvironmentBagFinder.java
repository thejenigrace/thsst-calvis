package simulatorvisualizer.model;

import configuration.model.engine.EFlags;
import configuration.model.engine.Memory;
import configuration.model.engine.Mxscr;
import configuration.model.engine.Token;
import configuration.model.exceptions.MemoryReadException;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 10 Jul 2016.
 */
public class EnvironmentBagFinder {

    private EnvironmentBag bag;
    private ArrayList<String[]> lookup;

    public void setLookup(ArrayList<String[]> lookup){
        this.lookup = lookup;
    }

    public void setEnvironmentBag(EnvironmentBag bag) {
        this.bag = bag;
    }

    public String getRegister(String registerName) {
        String key = registerName.toUpperCase(); // just in case
        String[][] registerMap = bag.getRegisterStringArray();
        for ( int i = 0; i < registerMap.length; i++ ) {
            if ( registerMap[i][0].equals(key) ) {
                return registerMap[i][1];
            }
        }
        return "Register does not exist;";
    }

    public EFlags getEflags(String flagValues) {
        EFlags oldFlags = new EFlags();
        oldFlags.setValue(flagValues);
        return oldFlags;
    }

    public Mxscr getMxscr(String flagValues) {
        Mxscr oldFlags = new Mxscr();
        oldFlags.setValue(flagValues);
        return oldFlags;
    }

    public String read(String address) {
        String[][] memoryMap = bag.getMemoryArray();
        for ( int i = 0; i < memoryMap.length; i++ ) {
            if ( memoryMap[i][0].equals(address) ) {
                return memoryMap[i][1];
            }
        }
        return null;
    }

    public String read(Token baseAddressToken, Token src) throws MemoryReadException {
        return read(baseAddressToken.getValue(), src);
    }

    public String read(Token baseAddressToken, int offset) throws MemoryReadException {
        return read(baseAddressToken.getValue(), offset);
    }

    public String read(String wholeMemoryString, Token src) throws MemoryReadException {
        String entireMemoryToken = src.getValue();
        String[] memoryArray = entireMemoryToken.split("/");

        String sizeDirective = memoryArray[0];
        String baseAddress = memoryArray[1];

        int offset = 0; //default offset = 0;
        for ( String[] x : this.lookup ) {
            if ( sizeDirective.equalsIgnoreCase(x[Memory.SIZE_DIRECTIVE_NAME]) ) {
                offset = Integer.valueOf(x[Memory.SIZE_DIRECTIVE_SIZE]);
                break;
            }
        }
        return read(baseAddress, offset);
    }

    public String read(String baseAddress, int offset) throws MemoryReadException {
        String result = "";
        Integer inc;

        String checkSizeDirective = baseAddress;
        if ( checkSizeDirective.contains("/") ) {
            checkSizeDirective = baseAddress.split("/")[1];
        }
        //System.out.println("BASE ADDRESS = " + reformatAddress(baseAddr));
        inc = Integer.parseInt(checkSizeDirective, 16);
        int offsetHex = offset / 4;
        for ( int i = 0; i < offsetHex / 2; i++ ) {
            result = read(Memory.reformatAddress(Integer.toHexString(inc))) + result;
            inc++;
        }
//        System.out.println("Memory read in little endian: " + result);
        if ( result.contains("null") ) {
            throw new MemoryReadException(baseAddress, offset);
        }
        return result;
    }

}
