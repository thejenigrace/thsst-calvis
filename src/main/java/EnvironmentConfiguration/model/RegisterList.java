package EnvironmentConfiguration.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class RegisterList {

	static int MAX_SIZE = 32; //default is 32 bit registers
	static final int NAME = 0;
	static final int SOURCE = 1;
	static final int SIZE = 2;
	static final int TYPE = 3;
	static final int START = 4;
	static final int END = 5;

	private TreeMap<String, Register> map;
	private ArrayList<String[]> lookup;
	private ArrayList<ErrorMessage> errorMessages = new ArrayList<>();

	public RegisterList(String csvFile) {
		Comparator<String> orderedComparator = (s1, s2) -> Integer.compare(indexOf(s1), indexOf(s2));
		this.map = new TreeMap<>(orderedComparator);
		this.lookup = new ArrayList<>();
		
		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int lineCounter = 0;
			
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] reg = line.split(cvsSplitBy);
				// trim every row just in case
				for (int i = 0; i < reg.length; i++){
					reg[i] = reg[i].trim();
				}
//				System.out.println("EnvironmentConfiguration.model.Register [name= " + reg[0]
//	                                 + " , source=" + reg[1] 
//	                                 + " , size=" + reg[2]
//	                                 + " , type=" + reg[3]		 
//	                                 + " , start=" + reg[4]
//	                                 + " , end=" + reg[5]+ "]");

				// we need to get the max register size listed in the csv file
				int regSize = 0;
				// don't get the table headers
				if ( lineCounter != 0 ){
					regSize = Integer.parseInt(reg[SIZE]);
					if ( MAX_SIZE < regSize )
						MAX_SIZE = regSize;

					int startIndex = Integer.parseInt(reg[START]);
					int endIndex = Integer.parseInt(reg[END]);
					// System.out.println(source.getValue().substring(startIndex, endIndex + 1));
					
					// need to convert start and end indices into hex equivalents
					endIndex = (( endIndex + 1 ) / 4 ) - 1;
					startIndex = startIndex / 4;
					
					// reverse order of indices
					endIndex = (( RegisterList.MAX_SIZE / 4 ) - 1) - endIndex;
					startIndex = (( RegisterList.MAX_SIZE / 4) - 1) - startIndex;
					reg[START] = String.valueOf(endIndex);
					reg[END] = String.valueOf(startIndex);
					reg[NAME] = reg[NAME].toUpperCase();
					// add csv row to lookup table
					this.lookup.add(reg);
				}

				// if a register is the source register itself
				if ( reg[NAME].equals(reg[SOURCE]) ){
					/*
				 		regType 1 = memory addressable
				 		regType 2 = not memory addressable
				 		regType 4 = flag
			 		*/
					switch(reg[TYPE]){
						case "1":
						case "2":	Register g = new Register(reg[NAME], regSize);
									this.map.put(reg[NAME], g);
									break;
						case "4": 	EFlags e = new EFlags(reg[NAME], regSize);
									this.map.put(reg[NAME], e);
									break;
						default:	errorMessages.add(
										new ErrorMessage("Register List Error",
											"Invalid Register Type: " + reg[TYPE] + " at row " + (lineCounter + 1)));
					}
				}
				lineCounter++;
			}
			// should check for register configuration errors...
			int index = 1;
			boolean isFoundError = true;
			for (String[] lookupEntry : this.lookup){
				// if all source (mother) registers are existent
				if ( !this.map.containsKey(lookupEntry[1]) ){
					if(isFoundError) {
						errorMessages.add(new ErrorMessage("Register List Error", "Register configuration file error"));
						isFoundError = false;
					}
					errorMessages.add(
							new ErrorMessage("Register List Error", "Register Configuration Error: "
									+ lookupEntry[1] + "does not exist at line " + index + "\n"));
				}
				index++;
			}
		} catch (Exception e) {
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
	}

	public int getBitSize(String registerName){
		return getBitSize(new Token(Token.reg, registerName));
	}
	
	public int getBitSize(Token a){
		String key = a.getValue();
		String size;
		size = find(key)[SIZE];
		return Integer.parseInt(size);
	}

	public int getHexSize(Token a){
		return getBitSize(a) / 4;
	}
	
	public String get(String registerName){
		return get(new Token(Token.reg, registerName));
	}
	
	public String get(Token a){
		// find the mother/source register
		String key = a.getValue();
		String[] register = find(key);
		
		// get the mother register
		if ( isExisting(key) ) {
			Register mother = this.map.get(register[SOURCE]);
			int startIndex = Integer.parseInt(register[START]);
			int endIndex = Integer.parseInt(register[END]);
			// System.out.println(source.getValue().substring(startIndex, endIndex + 1));
			// return the indicated child register value
			return mother.getValue().substring(startIndex, endIndex + 1);
		} else {
			System.out.println("ERROR: EnvironmentConfiguration.model.Register " + key + " does not exist.");
		}
		return null;
	}
	
	public void set(String registerName, String hexString){
		set(new Token(Token.reg, registerName), hexString);
	}
	
	public void set(Token a, String hexString){
		String key = a.getValue();
		String[] register = find(key);
		
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
		}
		else {
			if ( hexString.equals("") )
				System.out.println("Writing to register failed.");
			else
				System.out.println("Data type mismatch between "
					+ register[0] + ":" + child + " and " + hexString);
		}
	}
	
	public void clear(){
		for (String s : this.map.keySet()) {
			this.map.get(s).initializeValue();
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

	public Iterator<String[]> getRegisterList(){
		return this.lookup.iterator();
	}

	// Retrieves all register names, and returns an iterator
	public Iterator getRegisterKeys(){
		List<String> registerKeys = new ArrayList<>();
		Iterator<String[]> iterator = getRegisterList();
		while(iterator.hasNext()){
			String regName = iterator.next()[0];
			registerKeys.add(regName);
		}
		return registerKeys.iterator();
	}

	public EFlags getEFlags(){
		return (EFlags) this.map.get("EFLAGS");
	}

	public boolean isExisting(String registerName){
		return (find(registerName) != null);
	}


	public ArrayList<ErrorMessage> getErrorMessages(){
		return errorMessages;
	}

	public Integer[] getAvailableSizes() {
		Set<Integer> available = new HashSet<>();
		Iterator iterator = getRegisterKeys();
		while(iterator.hasNext()) {
			String registerName = (String) iterator.next();
			available.add(getBitSize(registerName));
		}
		Integer[] list = new Integer[available.size()];
		return available.toArray(list);
	}
}
