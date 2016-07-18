package thsst.calvis.simulatorvisualizer.model;

import thsst.calvis.configuration.model.engine.*;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 10 Jul 2016.
 */
public class EnvironmentBagFinder {

    private EnvironmentBag bag;
    private ArrayList<String[]> memoryLookup;
    private ArrayList<String[]> registerLookup;

    public void setEnvironmentBag(EnvironmentBag bag) {
        this.bag = bag;
    }

    public void setMemoryLookup(ArrayList<String[]> lookup) {
        this.memoryLookup = lookup;
    }

    public void setRegisterLookup(ArrayList<String[]> lookup) {
        this.registerLookup = lookup;
    }

    public String getRegister(String registerName) {
        String key = registerName.toUpperCase(); // just in case
        String[][] registerMap = bag.getRegisterStringArray();
        String[] registerArray = find(registerName);
        String sourceRegister = registerArray[RegisterList.SOURCE];
        int startIndex = Integer.parseInt(registerArray[RegisterList.START]);
        int endIndex = Integer.parseInt(registerArray[RegisterList.END]);

        for ( int i = 0; i < registerMap.length; i++ ) {
            if ( registerMap[i][0].equals(sourceRegister) ) {
                String value = registerMap[i][1];
                if ( sourceRegister.matches("ST[0-7]") ) {
                    return value;
                } else {
                    value = value.substring(startIndex, endIndex + 1);

                    return value;
                }
            }
        }

        return "Register does not exist";
    }

    public EFlags getEflags() {
        EFlags oldFlags = new EFlags();
        String flagValues = bag.getFlagsValue();
        oldFlags.setValue(flagValues);
        return oldFlags;
    }

    public Mxscr getMxscr() {
        Mxscr oldFlags = new Mxscr();
        String flagValues = bag.getMxscrValue();
        oldFlags.setValue(flagValues);
        return oldFlags;
    }

    private String[] find(String registerName) {
        for ( String[] x : this.registerLookup ) {
            if ( x[0].equalsIgnoreCase(registerName) ) {
                return x;
            }
        }
        return null;
    }

    private String read(String address) {
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
        System.out.println("value eto:" + baseAddressToken.getValue());
        return read(baseAddressToken.getValue(), offset);
    }

    public String read(String wholeMemoryString, Token src) throws MemoryReadException {
        String entireMemoryToken = src.getValue();
        String[] memoryArray = entireMemoryToken.split("/");

        String sizeDirective = memoryArray[0];
        String baseAddress = memoryArray[1];

        int offset = 0; //default offset = 0;
        for ( String[] x : this.memoryLookup ) {
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
