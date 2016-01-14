package EnvironmentConfiguration.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class RegisterList {
	
	private HashMap<String, Register> map;
	private ArrayList<String[]> lookup;
	static int MAX_SIZE = 32; //default is 32 bit registers

	public RegisterList(String csvFile) {
		
		this.map = new HashMap<String, Register>();
		this.lookup = new ArrayList<String[]>();
		
		BufferedReader br = null;
		String line = "";
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
					regSize = Integer.parseInt(reg[2]);
					if ( MAX_SIZE < regSize )
						MAX_SIZE = regSize;
					
					int startIndex = Integer.parseInt(reg[4]);
					int endIndex = Integer.parseInt(reg[5]);
					
					// System.out.println(source.getValue().substring(startIndex, endIndex + 1));
					
					// need to convert start and end indices into hex equivalents
					endIndex = (( endIndex + 1 ) / 4 ) - 1;
					startIndex = startIndex / 4;
					
					// reverse order of indices
					endIndex = (( RegisterList.MAX_SIZE / 4 ) - 1) - endIndex;
					startIndex = (( RegisterList.MAX_SIZE / 4) - 1) - startIndex;
					reg[4] = String.valueOf(endIndex);
					reg[5] = String.valueOf(startIndex);
					
					reg[0] = reg[0].toUpperCase();
					// add csv row to lookup table
					this.lookup.add(reg);
				}
				
				
				// if a register is the source register itself
				if ( reg[0].equals(reg[1]) ){
					// populate register value with hex value of 0s depending on register size
					String regInitialValue = "";
					for ( int zCount = 0; zCount < regSize / 4; zCount++ ){
						regInitialValue += "0";
					}
					switch(reg[3]){
						case "1":
						case "2":	Register g = new Register(regInitialValue, reg[1]);
									this.map.put(reg[1], g);
									break;
						case "4": 	EFlags e = new EFlags(regInitialValue.substring(1) + "2", reg[1]);
									this.map.put(reg[1], e);
									break;
//						default:	System.out.println("Invalid EnvironmentConfiguration.model.Register Type: " + reg[3]
//										+ " at row " + (lineCounter + 1));
					}
				}
				//apply logic that if
				
				lineCounter++;
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	
	public Iterator<String[]> getRegisterList(){
		return this.lookup.iterator();
	}
	
	public EFlags getEFlags(){
		return (EFlags) this.map.get("EFLAGS");
	}
	
	public boolean isExisting(String regname){
		for (String[] x : this.lookup){
			if ( x[0].equals(regname) ){
				return true;
			}
		}
		return false;
	}
	
	public int getSize(String regname){
		return getSize(new Token(Token.reg, regname));
	}
	
	public int getSize(Token a){
		String key = a.getValue();
		String size = "0";
		for (String[] x : this.lookup){
			//System.out.println(x[0] + "=" + key);
			if ( x[0].equals(key) ){
				size = x[2];
				break;
			}
		}
		return Integer.parseInt(size);
	}
	
	public String get(String regname){
		return get(new Token(Token.reg, regname));
	}
	
	public String get(Token a){
		
		// find the mother/source register
		String key = a.getValue();
		String[] register = null;
		for (String[] x : this.lookup){
			//System.out.println(x[0] + "=" + key);
			if ( x[0].equals(key) ){
				register = x;
				break;
			}
		}
		
		// get the mother register
		if ( isExisting(key) ) {
			Register mother = this.map.get(register[1]);
			int startIndex = Integer.parseInt(register[4]);
			int endIndex = Integer.parseInt(register[5]);
			
			// System.out.println(source.getValue().substring(startIndex, endIndex + 1));
			
			// return the indicated child register value
			return mother.getValue().substring(startIndex, endIndex + 1);
		} else {
			System.out.println("ERROR: EnvironmentConfiguration.model.Register " + key + " does not exist.");
		}
		return null;
		
	}
	
	public void set(String regname, String hexString){
		set(new Token(Token.reg, regname), hexString);
	}
	
	public void set(Token a, String hexString){
		String key = a.getValue();
		String[] register = null;
		
		for (String[] x : this.lookup){
			if ( x[0].equals(key) ){
				register = x;
				break;
			}
		}
		
		// get the mother register
		
		Register mother = this.map.get(register[1]);
		int startIndex = Integer.parseInt(register[4]);
		int endIndex = Integer.parseInt(register[5]);
		
		//check for mismatch between parameter hexString and child register value		
		String child = mother.getValue().substring(startIndex, endIndex + 1);
		
		if ( child.length() > hexString.length() ){
			int differenceInLength = child.length() - hexString.length();
			for (int i = 0; i < differenceInLength; i++){
				hexString = "0" + hexString;
			}
		}

		if ( child.length() == hexString.length() ){
			String newValue = mother.getValue();
			char[] val = newValue.toCharArray();
			for( int i = startIndex; i <= endIndex; i++){
				val[i] = hexString.charAt(i-startIndex);
			}
			newValue = new String(val);
//			System.out.println("new: " + newValue);
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
		Iterator<String> keys = this.map.keySet().iterator();
		while (keys.hasNext()){
			this.map.get(keys.next()).setValue("");
		}
	}
	
	public void print(){
		Map<String, Register> sortedMap = new TreeMap<String, Register>(map);
		Iterator<Map.Entry<String, Register>> ite = sortedMap.entrySet().iterator();
		while (ite.hasNext()){
			Map.Entry<String, Register> x = ite.next();
				System.out.println(x);
		}
	}
}
