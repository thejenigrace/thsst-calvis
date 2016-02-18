package EnvironmentConfiguration.model.engine;

import EnvironmentConfiguration.controller.HandleConfigFunctions;
import EnvironmentConfiguration.model.error_logging.ErrorLogger;
import EnvironmentConfiguration.model.error_logging.ErrorMessage;
import EnvironmentConfiguration.model.error_logging.ErrorMessageList;
import EnvironmentConfiguration.model.error_logging.Types;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RegisterList {

	static final int NAME = 0;
	static final int SOURCE = 1;
	static final int SIZE = 2;
	static final int TYPE = 3;
	static final int START = 4;
	static final int END = 5;

	private TreeMap<String, Register> map;
	private ArrayList<String[]> lookup;
	private EFlags flags;
	private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<>());

    private TreeMap<String, TreeMap<String, Register>> childMap;

	public static String instructionPointerName = "EIP";
	public static int instructionPointerSize = 32;
	public static int MAX_SIZE = 32; //default is 32 bit registers

	public RegisterList(String csvFile, int maxSize) {
		Comparator<String> orderedComparator = (s1, s2) -> Integer.compare(indexOf(s1), indexOf(s2));
		this.flags = new EFlags("EFLAGS", 32);
		this.map = new TreeMap<>(orderedComparator);
		this.lookup = new ArrayList<>();
		this.MAX_SIZE = maxSize;

        this.childMap = new TreeMap<>(orderedComparator);

		BufferedReader br = null;
		String line = "";
		int lineCounter = 0;
		ArrayList<String[]> lookUpCopy = new ArrayList<>();
		ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				boolean isSkipped = false;
				String[] reg = HandleConfigFunctions.split(line, ',');
				for (int i = 0; i < reg.length; i++) {
					reg[i] = reg[i].trim();
				}
				lookUpCopy.add(reg);

				ArrayList<String> missingParametersRegister = HandleConfigFunctions.checkifMissing(reg);
				ArrayList<String> invalidParametersRegister = HandleConfigFunctions.checkForInvalidInput(reg);
				if(missingParametersRegister.size() > 0){
					isSkipped = true;
					errorMessages.add(new ErrorMessage(
							Types.registerShouldNotBeEmpty,
							missingParametersRegister,
							Integer.toString(lineCounter)));
				}

				if(invalidParametersRegister.size() > 0 && lineCounter != 0){
					isSkipped = true;
					errorMessages.add(new ErrorMessage(
							Types.registerShouldNotBeInvalid,
							invalidParametersRegister,
							Integer.toString(lineCounter)));
				}

//				System.out.println("EnvironmentConfiguration.model.engine.Register [name= " + reg[0]
//	                                 + " , source=" + reg[1]
//	                                 + " , size=" + reg[2]
//	                                 + " , type=" + reg[3]
//	                                 + " , start=" + reg[4]
//	                                 + " , end=" + reg[5]+ "]");

				// we need to get the max register size listed in the csv file
				if(!isSkipped) {
					int regSize = 0;
					// don't get the table headers
					if (lineCounter != 0) {

						regSize = Integer.parseInt(reg[SIZE]);

						int startIndex = Integer.parseInt(reg[START]);
						int endIndex = Integer.parseInt(reg[END]);

						if ((startIndex == 0 && endIndex + 1 != regSize) ||
								(startIndex > 0 && endIndex - startIndex + 1 != regSize)) {
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
						int dividedBy = RegisterList.MAX_SIZE / 4;
						if ( reg[NAME].equals(reg[SOURCE]) ){
							dividedBy = Integer.parseInt(reg[SIZE]) / 4;
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
					if (reg[NAME].equals(reg[SOURCE])) {
						switch (reg[TYPE]) {
							case "1":
							case "2":
								Register g = new Register(reg[NAME], regSize);
//								System.out.println("Case 1 & 2: " + reg[NAME]);
								this.map.put(reg[NAME], g);
								break;
							case "3": // Instruction Pointer
								Register h = new Register(reg[NAME], regSize);
								instructionPointerName = reg[NAME];
								instructionPointerSize = regSize;
								this.map.put(reg[NAME], h);
								break;
							default:
								errorMessages.add(
										new ErrorMessage(Types.invalidRegister,
												HandleConfigFunctions.generateArrayListString(reg[TYPE]),
												Integer.toString(lineCounter + 1)));
								break;
						}
					} else if(reg[TYPE].equals("2")) {
                        Register g = new Register(reg[NAME], regSize);
                        if(childMap.get(reg[SOURCE]) == null) {
                            TreeMap<String, Register> group = new TreeMap<>(orderedComparator);

//                            System.out.println("Create 1st Child-Type 2: " + reg[NAME]);
                            group.put(reg[NAME], g);

                            this.childMap.put(reg[SOURCE], group);
                        } else {
                            TreeMap<String, Register> group = childMap.get(reg[SOURCE]);
//                            System.out.println("Sibling-Type 2: " + reg[NAME]);
                            group.put(reg[NAME], g);

                            this.childMap.replace(reg[SOURCE], group);
                        }
                    }
				}
				lineCounter++;
			}
			lookUpCopy.remove(0);
			// should check for register configuration errors...
			int index = 0;
			for (String[] lookupEntry : lookUpCopy){

				// if all source (mother) registers are existent
				//System.out.println(lookupEntry[1]);
				if ( !this.map.containsKey(lookupEntry[1]) ){
//					int lineNumber = index;
//					if(isEmptyError)
//						lineNumber = index + 1;
					errorMessages.add(new ErrorMessage(
							Types.doesNotExist, HandleConfigFunctions.generateArrayListString(lookupEntry[1]),
							Integer.toString(index + 1)));
				}
				index++;
			}

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ErrorMessageList registerErrorList = new ErrorMessageList(Types.registerFile, errorMessages);
		errorLogger.add(registerErrorList);
	}

	public Iterator<String[]> getRegisterList(){
		return this.lookup.iterator();
	}

	/*
		getRegisterKeys() is used for getting all register names to be highlighted
	 */
	public Iterator<String> getRegisterKeys(){
		List registerKeys = new ArrayList<>();
		Iterator<String[]> iterator = getRegisterList();
		while(iterator.hasNext()){
			String regName = iterator.next()[0];
			registerKeys.add(regName);
		}
		return registerKeys.iterator();
	}

	public EFlags getEFlags(){
		return this.flags;
	}

	public boolean isExisting(String registerName){
		return (find(registerName) != null) ;
	}

	public int getBitSize(String registerName){
		return getBitSize(new Token(Token.REG, registerName));
	}

	public int getBitSize(Token a){
		String key = a.getValue();
		String size;
		size = find(key)[SIZE];
		return Integer.parseInt(size);
	}

	public int getHexSize(String registerName) {
		return getBitSize(registerName) / 4;
	}

	public int getHexSize(Token a){
		return getBitSize(a) / 4;
	}

	public String get(String registerName){
		return get(new Token(Token.REG, registerName));
	}

	public String get(Token a){
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
			return mother.getValue().substring(startIndex, endIndex + 1);
		} else {
			System.out.println("ERROR: EnvironmentConfiguration.model.engine.Register " + key + " does not exist.");
			errorMessages.add(new ErrorMessage(Types.doesNotExist,
					HandleConfigFunctions.generateArrayListString(key), ""));
			errorLogger.get(0).add(errorMessages);
		}
		return null;
	}

	public void set(String registerName, String hexString) throws DataTypeMismatchException {
		set(new Token(Token.REG, registerName), hexString);
	}

	public void set(Token a, String hexString) throws DataTypeMismatchException {
		String key = a.getValue();
		String[] register = find(key);
		ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		// get the mother register

		Register mother = this.map.get(register[SOURCE]);
		int startIndex = Integer.parseInt(register[START]);
		int endIndex = Integer.parseInt(register[END]);

		//check for mismatch between parameter hexString and child register value		
		String child = mother.getValue().substring(startIndex, endIndex + 1);

		if ( child.length() >= hexString.length() ){
			int differenceInLength = child.length() - hexString.length();
			for (int i = 0; i < differenceInLength; i++){
				hexString = "0" + hexString;
			}
			String newValue = mother.getValue();
			char[] val = newValue.toCharArray();
			for( int i = startIndex; i <= endIndex; i++){
				val[i] = hexString.charAt(i-startIndex);
			}
			newValue = new String(val);
			mother.setValue(newValue.toUpperCase());

//            System.out.println("register[SOURCE] = " + register[SOURCE]);
            if ( childMap.get(register[SOURCE]) != null ) {
//                System.out.println("key = " + key);
//				System.out.println("newValue = " + newValue.toUpperCase());

                TreeMap<String, Register> temp = childMap.get(register[SOURCE]);
                for(int i = 0; i < temp.size(); i++) {
                    switch (register[SOURCE]) {
                        case "EAX": childMap.get(register[SOURCE]).get("AX").setValue(get16BitHexString(newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("AH").setValue(get8BitHexString('H', newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("AL").setValue(get8BitHexString('L', newValue.toUpperCase()));
                            break;
                        case "EBX": childMap.get(register[SOURCE]).get("BX").setValue(get16BitHexString(newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("BH").setValue(get8BitHexString('H', newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("BL").setValue(get8BitHexString('L', newValue.toUpperCase()));
                            break;
                        case "ECX": childMap.get(register[SOURCE]).get("CX").setValue(get16BitHexString(newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("CH").setValue(get8BitHexString('H', newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("CL").setValue(get8BitHexString('L', newValue.toUpperCase()));
                            break;
                        case "EDX": childMap.get(register[SOURCE]).get("DX").setValue(get16BitHexString(newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("DH").setValue(get8BitHexString('H', newValue.toUpperCase()));
                            childMap.get(register[SOURCE]).get("DL").setValue(get8BitHexString('L', newValue.toUpperCase()));
                            break;
                        default: System.out.println("NONE");
                    }
                }
            }
		}
		else {
			if ( hexString.equals("") ) {
				System.out.println("Writing to register failed.");
				errorMessages.add(new ErrorMessage(Types.writeRegisterFailed,
						new ArrayList<String>(), ""));
			}
			else {
				System.out.println("Size mismatch between "
						+ register[0] + ":" + child + " and " + hexString);
				errorMessages.add(new ErrorMessage(Types.dataTypeMismatch,
						HandleConfigFunctions.generateArrayListString(register[0], child, hexString), ""));
				throw new DataTypeMismatchException(register[0] + ":" + child, hexString);
			}
			errorLogger.get(0).add(errorMessages);
		}
	}

    public String get16BitHexString (String hexString) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = hexString.length()/2; i < hexString.length(); i++)
            stringBuilder.append(hexString.charAt(i));

        return  stringBuilder.toString();
    }

    public String get8BitHexString (char type, String hexString) {
        StringBuilder stringBuilder = new StringBuilder();
        if(type == 'H') {
            stringBuilder.append(hexString.charAt(4));
            stringBuilder.append(hexString.charAt(5));
        } else if(type == 'L') {
            stringBuilder.append(hexString.charAt(6));
            stringBuilder.append(hexString.charAt(7));
        }

        return  stringBuilder.toString();
    }

	public String getInstructionPointer(){
		return get(instructionPointerName);
	}

	public void setInstructionPointer(String value) throws DataTypeMismatchException {
		set(instructionPointerName, value);
	}

	public void clear(){
		for (String s : this.map.keySet()) {
			this.map.get(s).initializeValue();
		}
		flags.initializeValue();

        // initialize childMap
		for(String s: this.childMap.keySet()) {
            for(String t: this.childMap.get(s).keySet()) {
                this.childMap.get(s).get(t).initializeValue();
            }
        }
	}

	public void print(){
		map.entrySet().forEach(System.out::println);
	}

	public String[] find(String registerName){
		for (String[] x : this.lookup){
			if ( x[0].equals(registerName) ){
				return x;
			}
		}
		return null;
	}

	public int indexOf(String registerName){
		for (int i = 0; i < this.lookup.size(); i++) {
			if ( registerName.equals(this.lookup.get(i)[RegisterList.NAME])) {
				return i;
			}
		}
		return -1;
	}

	public Map getRegisterMap(){
		return this.map;
	}

    /**
     *
     * @param regName
     * @return
     */
    public Map getChildRegisterMap(String regName){
        return this.childMap.get(regName);
    }

	public ErrorLogger getErrorLogger(){
		if(errorLogger.get(0).getSizeofErrorMessages() == 0)
			return new ErrorLogger(new ArrayList<ErrorMessageList>());
		else
			return errorLogger;
	}

	public Integer[] getAvailableSizes(){
		Set<Integer> available = new HashSet<>();
		Iterator iterator = getRegisterKeys();
		while(iterator.hasNext()){
			String registerName = (String) iterator.next();
			available.add(getBitSize(registerName));
		}
		Integer[] list = new Integer[available.size()];

		return available.toArray(list);
	}
}
