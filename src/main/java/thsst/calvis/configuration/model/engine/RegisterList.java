package thsst.calvis.configuration.model.engine;

import thsst.calvis.configuration.controller.HandleConfigFunctions;
import thsst.calvis.configuration.model.errorlogging.ErrorLogger;
import thsst.calvis.configuration.model.errorlogging.ErrorMessage;
import thsst.calvis.configuration.model.errorlogging.ErrorMessageList;
import thsst.calvis.configuration.model.errorlogging.Types;
import thsst.calvis.configuration.model.exceptions.DataTypeMismatchException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RegisterList {

    public static final int NAME = 0;
    public static final int SOURCE = 1;
    public static final int SIZE = 2;
    public static final int TYPE = 3;
    public static final int START = 4;
    public static final int END = 5;

    public static String instructionPointerName = "EIP";
    public static int instructionPointerSize = 32;

    public static String stackPointerName = "ESP";
    public static int stackPointerSize = 32;

    public static int MAX_SIZE = 32; //default is 32 bit registers

    private TreeMap<String, Register> map;
    private ArrayList<String[]> lookup;
    private EFlags flags;
    private Mxscr mxscr;
    private X87Handler x87;

    private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<>());
    private TreeMap<String, TreeMap<String, Register>> childMap;

    public RegisterList(String csvFile, int maxSize) {
        Comparator<String> orderedComparator = (s1, s2) -> Integer.compare(indexOf(s1), indexOf(s2));

        this.map = new TreeMap<>(orderedComparator);
        this.lookup = new ArrayList<>();
        MAX_SIZE = maxSize;
        this.childMap = new TreeMap<>(orderedComparator);

        this.flags = new EFlags();
        this.mxscr = new Mxscr();
        this.x87 = new X87Handler(this);

        BufferedReader br = null;
        String line = "";
        int lineCounter = 0;
        ArrayList<String[]> lookUpCopy = new ArrayList<>();
        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ( (line = br.readLine()) != null ) {
                boolean isSkipped = false;
                String[] reg = HandleConfigFunctions.split(line, ',');
                for ( int i = 0; i < reg.length; i++ ) {
                    reg[i] = reg[i].trim();
                }
                lookUpCopy.add(reg);

                ArrayList<String> missingParametersRegister = HandleConfigFunctions.checkIfMissing(reg);
                ArrayList<String> invalidParametersRegister = HandleConfigFunctions.checkForInvalidInput(reg);
                if ( missingParametersRegister.size() > 0 ) {
                    isSkipped = true;
                    errorMessages.add(new ErrorMessage(
                            Types.registerShouldNotBeEmpty,
                            missingParametersRegister,
                            Integer.toString(lineCounter)));
                }

                if ( invalidParametersRegister.size() > 0 && lineCounter != 0 ) {
                    isSkipped = true;
                    errorMessages.add(new ErrorMessage(
                            Types.registerShouldNotBeInvalid,
                            invalidParametersRegister,
                            Integer.toString(lineCounter)));
                }

                // we need to get the max register size listed in the csv file
                if ( !isSkipped ) {
                    int regSize = 0;
                    // don't get the table headers
                    if ( lineCounter != 0 ) {

                        regSize = Integer.parseInt(reg[SIZE]);

                        int startIndex = Integer.parseInt(reg[START]);
                        int endIndex = Integer.parseInt(reg[END]);

                        if ( (startIndex == 0 && endIndex + 1 != regSize)
                                || (startIndex > 0 && endIndex - startIndex + 1 != regSize) ) {
                            errorMessages.add(new ErrorMessage(
                                    Types.registerInvalidSizeFormat,
                                    HandleConfigFunctions.generateArrayListString(reg[NAME]),
                                    Integer.toString(lineCounter)));
                            //						reg = HandleConfigFunctions.adjustAnArray(reg, 1);
                            //						reg[reg.length - 1] = "0";
                        }

                        endIndex = ((endIndex + 1) / 4) - 1;
                        startIndex = startIndex / 4;

                        // reverse order of indices
                        int dividedBy = 0; //RegisterList.MAX_SIZE / 4;
                        if ( reg[NAME].equals(reg[SOURCE]) ) {
                            dividedBy = Integer.parseInt(reg[SIZE]) / 4;
                        } else {
                            dividedBy = getBitSize(reg[SOURCE]) / 4;
                        }
                        endIndex = (dividedBy - 1) - endIndex;
                        startIndex = (dividedBy - 1) - startIndex;

                        reg[START] = String.valueOf(endIndex);
                        reg[END] = String.valueOf(startIndex);
                        reg[NAME] = reg[NAME].toUpperCase();
                        // add csv row to lookup table
//						System.out.println(reg[0] + " " +reg[1] + " " +reg[2]
//						 + " " + reg[3] + " "   + reg[4] + " " + reg[5]);
                        this.lookup.add(reg);
                    }

                    // if a register is the source register itself
                    if ( reg[NAME].equals(reg[SOURCE]) ) {
                        switch ( reg[TYPE] ) {
                            case "1": // fall through
                            case "2": // fall through
                            case "4": // fall through
                            case "6": // fall through
                                Register g = new Register(reg[NAME], regSize);
                                this.map.put(reg[NAME], g);
                                break;
                            case "5":
                                FpuRegister st0 = new FpuRegister(reg[NAME], 0);
                                this.map.put(reg[NAME], st0);
                                break;
                            case "7":
                                X87Register fpu = null;
                                if ( reg[NAME].equals("STATUS") ) {
                                    fpu = new X87StatusRegister();
                                    this.x87.setStatus((X87StatusRegister) fpu);
                                } else if ( reg[NAME].equals("TAG") ) {
                                    fpu = new X87TagRegister();
                                    this.x87.setTag((X87TagRegister) fpu);
                                } else if ( reg[NAME].equals("CONTROL") ) {
                                    fpu = new X87ControlRegister();
                                    this.x87.setControl((X87ControlRegister) fpu);
                                }
                                this.map.put(reg[NAME], fpu);
                                break;
                            case "3": // Instruction Pointer
                                Register h = new Register(reg[NAME], regSize);
                                instructionPointerName = reg[NAME];
                                instructionPointerSize = regSize;
                                this.map.put(reg[NAME], h);
                                break;
                            case "8": // Stack Pointer
                                StackPointerRegister spr = new StackPointerRegister(reg[NAME], regSize);
                                stackPointerName = reg[NAME];
                                stackPointerSize = regSize;
                                this.map.put(reg[NAME], spr);
                                break;
                            default:
                                errorMessages.add(
                                        new ErrorMessage(Types.invalidRegister,
                                                HandleConfigFunctions.generateArrayListString(reg[TYPE]),
                                                Integer.toString(lineCounter + 1)));
                                break;
                        }
                    } else {
                        Register baby;
                        switch ( reg[TYPE] ) {
                            case "3": // Instruction Pointer
                                baby = new Register(reg[NAME], regSize);
                                break;
                            case "8": // Stack Pointer
                                baby = new StackPointerRegister(reg[NAME], regSize);
                                break;
                            default:
                                baby = new Register(reg[NAME], regSize);
                        }

                        if ( childMap.get(reg[SOURCE]) == null ) {
                            TreeMap<String, Register> group = new TreeMap<>(orderedComparator);
//                            System.out.println("Create 1st Child-Type 2: " + reg[NAME]);
                            group.put(reg[NAME], baby);
                            this.childMap.put(reg[SOURCE], group);
                        } else {
                            TreeMap<String, Register> group = childMap.get(reg[SOURCE]);
//                            System.out.println("Sibling-Type 2: " + reg[NAME]);
                            group.put(reg[NAME], baby);
                            this.childMap.replace(reg[SOURCE], group);
                        }
                    }
                }
                lineCounter++;
            }
            lookUpCopy.remove(0);

            // should check for register thsst.calvis.configuration errors...
            int index = 0;
            for ( String[] lookupEntry : lookUpCopy ) {
                // if all source (mother) registers are existent
                //System.out.println(lookupEntry[1]);
                if ( !this.map.containsKey(lookupEntry[1]) ) {
//					int lineNumber = index;
//					if(isEmptyError)
//						lineNumber = index + 1;
                    errorMessages.add(new ErrorMessage(
                            Types.doesNotExist, HandleConfigFunctions.generateArrayListString(lookupEntry[1]),
                            Integer.toString(index + 1)));
                }
                index++;
            }

//            this.setRegisterContent(); // put value into the Register Map

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            if ( br != null ) {
                try {
                    br.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
        ErrorMessageList registerErrorList = new ErrorMessageList(Types.registerFile, errorMessages);
        errorLogger.add(registerErrorList);

    }

    public Iterator<String[]> getRegisterList() {
        return this.lookup.iterator();
    }

    public ArrayList<String[]> getRegisterLookup() {
        return this.lookup;
    }

    public Iterator<String> getRegisterKeys() {
        List registerKeys = new ArrayList<>();
        Iterator<String[]> iterator = getRegisterList();
        while ( iterator.hasNext() ) {
            String regName = iterator.next()[0];
            registerKeys.add(regName);
        }
        return registerKeys.iterator();
    }

    public EFlags getEFlags() {
        return this.flags;
    }

    public Mxscr getMxscr() {
        return this.mxscr;
    }

    public boolean isExisting(String registerName) {
        return (find(registerName) != null);
    }

    public int getBitSize(String registerName) {
        return getBitSize(new Token(Token.REG, registerName));
    }

    public int getBitSize(Token a) {
        String key = a.getValue();
        String size;
        size = find(key)[SIZE];
        return Integer.parseInt(size);
    }

    public int getHexSize(String registerName) {
        return getBitSize(registerName) / 4;
    }

    public int getHexSize(Token a) {
        return getBitSize(a) / 4;
    }

    public String get(String registerName) {
        return get(new Token(Token.REG, registerName));
    }

    public String get(Token a) {
        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
        // find the mother/source register
        String key = a.getValue();
        String[] register = find(key);

        // get the mother register
        if ( isExisting(key) ) {
            Register mother = this.map.get(register[SOURCE]);
            int startIndex = Integer.parseInt(register[START]);
            int endIndex = Integer.parseInt(register[END]);
            // return the indicated child register value
            if ( mother.getName().matches("ST[0-7]") ) {
                return mother.getValue();
            } else {
                return mother.getValue().substring(startIndex, endIndex + 1);
            }
        } else {
            System.out.println("ERROR: Register " + key + " does not exist.");
            errorMessages.add(new ErrorMessage(Types.doesNotExist,
                    HandleConfigFunctions.generateArrayListString(key), ""));
            errorLogger.get(0).add(errorMessages);
        }
        return null;
    }

    public void set(Token registerToken, String hexString) throws DataTypeMismatchException {
        set(registerToken.getValue(), hexString);
    }

    public void set(String key, String hexString) throws DataTypeMismatchException {
        String[] register = find(key);
        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
        // get the mother register

        Register mother = this.map.get(register[SOURCE]);
        int startIndex = Integer.parseInt(register[START]);
        int endIndex = Integer.parseInt(register[END]);

        String sourceName = mother.getName();

        if ( sourceName.matches("ST[0-7]") ) {
            mother.setValue(hexString);
            this.x87().setStackValue(sourceName.charAt(2) + "", hexString);
        } else {
            //check for mismatch between parameter hexString and child register value
            String child = mother.getValue().substring(startIndex, endIndex + 1);

            if ( child.length() >= hexString.length() ) {
                int differenceInLength = child.length() - hexString.length();
                for ( int i = 0; i < differenceInLength; i++ ) {
                    hexString = "0" + hexString;
                }
                String newValue = mother.getValue();
                char[] val = newValue.toCharArray();
                for ( int i = startIndex; i <= endIndex; i++ ) {
                    val[i] = hexString.charAt(i - startIndex);
                }
                newValue = new String(val);
                newValue = newValue.toUpperCase();
                mother.setValue(newValue);
                
                TreeMap<String, Register> treeMap = childMap.get(register[SOURCE]);

                if ( treeMap != null ) {
                    for( Map.Entry<String, Register> entry : treeMap.entrySet() ) {
                        String childRegisterName = entry.getKey();
                        Register value = entry.getValue();

                        String[] childRegister = find(childRegisterName);
                        int childStartIndex = Integer.parseInt(childRegister[START]);
                        int childEndIndex = Integer.parseInt(childRegister[END]);

                        String newChildValue = mother.getValue().substring(childStartIndex, childEndIndex + 1);
                        value.setValue(newChildValue);
                    }
                }
            } else {
                if ( hexString.equals("") ) {
                    System.out.println("Writing to register failed.");
                    errorMessages.add(new ErrorMessage(Types.writeRegisterFailed,
                            new ArrayList<>(), ""));
                } else {
                    System.out.println("Size mismatch between "
                            + register[0] + ":" + child + " and " + hexString);
                    errorMessages.add(new ErrorMessage(Types.dataTypeMismatch,
                            HandleConfigFunctions.generateArrayListString(register[0], child, hexString), ""));
                    throw new DataTypeMismatchException(register[0] + ":" + child, hexString);
                }
                errorLogger.get(0).add(errorMessages);
            }
        }
    }

    public String getInstructionPointer() {
        return get(instructionPointerName);
    }

    public void setInstructionPointer(String value) throws DataTypeMismatchException {
        set(instructionPointerName, value);
    }

    public String getStackPointer() {
        String registerStackValue = get(stackPointerName);
        if(registerStackValue.length() > 8){
            return registerStackValue.substring(8);
        }
        else{
            return registerStackValue;
        }
    }

    public void setStackPointer(String value) throws DataTypeMismatchException {
        String officialValue = value;

        set(stackPointerName, officialValue);
    }

    public void clear() {
        for ( String s : this.map.keySet() ) {
            this.map.get(s).initializeValue();
        }
        flags.initializeValue();
        mxscr.initializeValue();
        x87.clear();

        // initialize childMap
        for ( String s : this.childMap.keySet() ) {
            for ( String t : this.childMap.get(s).keySet() ) {
                this.childMap.get(s).get(t).initializeValue();
            }
        }

    }

    public void print() {
        map.entrySet().forEach(System.out::println);
    }

    public String[] find(String registerName) {
        for ( String[] x : this.lookup ) {
            if ( x[0].equalsIgnoreCase(registerName) ) {
                return x;
            }
        }
        return null;
    }

    public int indexOf(String registerName) {
        for ( int i = 0; i < this.lookup.size(); i++ ) {
            if ( registerName.equals(this.lookup.get(i)[RegisterList.NAME]) ) {
                return i;
            }
        }
        return -1;
    }

    public Map getRegisterMap() {
        return this.map;
    }

    public Map getChildRegisterMap(String regName) {
        return this.childMap.get(regName);
    }

    public ErrorLogger getErrorLogger() {
        if ( errorLogger.get(0).getSizeofErrorMessages() == 0 ) {
            return new ErrorLogger(new ArrayList<>());
        } else {
            return errorLogger;
        }
    }

    public Integer[] getAvailableSizes() {
        Set<Integer> available = new HashSet<>();
        Iterator iterator = getRegisterKeys();
        while ( iterator.hasNext() ) {
            String registerName = (String) iterator.next();
            available.add(getBitSize(registerName));
        }
        Integer[] list = new Integer[available.size()];

        return available.toArray(list);
    }

    public X87Handler x87() {
        return this.x87;
    }

    public void setRegisterContent() {
        try {
            set("EAX", "DDBBCCAA");
            set("EBX", "FFFF1111");
            set("ECX", "00000005");
            set("EDX", "56F38E84");
            set("ESP", "FF006655");
            set("ESI", "00000001");
            set("EDI", "88774433");
            set("EBP", "00000008");

            set("MM0", "FFFF901256781234");
            set("MM1", "1234567856781234");
            set("MM2", "FFCA90BB58926789");
            set("MM3", "9934566711111286");
            set("MM4", "0055006600770088");
            set("MM5", "111122228888FFFF");
            set("MM6", "0000000000000001");
            set("MM7", "0000000000000002");

            set("XMM0", "ABCEDF123456789000000000123AF43");
            set("XMM1", "1234567890ABCDEF123EBDCA0123AF43");
            set("XMM2", "11111111111111110000000000000000");
            set("XMM3", "1234567890AB90931FFF45210123ABCD");
            set("XMM4", "1FFF45210123ABCD1234567890AB9094");
            set("XMM5", "1234567845210123123EBDCA0123AF43");
            set("XMM6", "ABCDEF01452101230000000045210123");
            set("XMM7", "CDEF123E1234567890AB9093CDEF123E");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Loaded Register Content");
    }
}
