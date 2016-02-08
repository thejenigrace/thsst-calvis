package EnvironmentConfiguration.model.engine;

import SimulatorVisualizer.model.MemoryReadException;
import SimulatorVisualizer.model.MemoryWriteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

/**
 *  Memory  		- Protected Mode Flat Model
 *  address size  	- 32-bit
 *  starts from  	- 0x0000 0000
 *  ends at  		- 0x0000 FFFF
 */

public class Memory {

	static int MAX_ADDRESS_SIZE;

	static final int SIZE_DIRECTIVE_NAME = 0;
	static final int SIZE_DIRECTIVE_PREFIX = 1;
	static final int SIZE_DIRECTIVE_SIZE = 2;

	private TreeMap<String, String> mem;
	private ArrayList<String[]> lookup;

	public Memory(int bitSize, int bitEndLength, String csvFile){
		this.MAX_ADDRESS_SIZE = bitSize;
		this.mem = new TreeMap<>(Collections.reverseOrder());

		String lastAddress = MemoryAddressCalculator.extend("F", bitEndLength, "F");
		int end = Integer.parseInt(lastAddress, 16);
		for ( int i = 0x0; i <= end; i++){
			String address = MemoryAddressCalculator.extend(Integer.toHexString(i), bitSize, "0");
			//mem.put(address, "00");
			mem.put(address, "" + address.charAt(address.length()-1) + address.charAt(address.length()-1) );
		}

		System.out.println("Loaded: Memory");

		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int lineCounter = 0;
		this.lookup = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(new File(csvFile)));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] row = line.split(cvsSplitBy);
				// trim every row just in case
				for (int i = 0; i < row.length; i++) {
					row[i] = row[i].trim();
				}
				if ( lineCounter != 0 ) {
					this.lookup.add(row);
//                    System.out.println(" Name = [" + row[Memory.SIZE_DIRECTIVE_NAME] +
//		                    "], Prefix = [" + row[Memory.SIZE_DIRECTIVE_PREFIX] +
//		                    "], Size = [" + row[Memory.SIZE_DIRECTIVE_SIZE] + "]");
				}
				lineCounter++;
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

	public Map getMemoryMap(){
		return this.mem;
	}

	private static String reformatAddress(String add) {
		return MemoryAddressCalculator.extend(add, Memory.MAX_ADDRESS_SIZE, "0");
	}

	public void write(Token baseAddressToken, String value, Token des) throws MemoryWriteException{
		write(baseAddressToken.getValue(), value, des);
	}

	public void write(Token baseAddressToken, String value, int offset) throws MemoryWriteException{
		write(baseAddressToken.getValue(), value, offset);
	}

	public void write(String wholeMemoryString, String value, Token des) throws MemoryWriteException{
		// des contains our offset.
		String desValue = des.getValue();
		String[] memoryArray = desValue.split("/");

		String sizeDirective = memoryArray[0];
		String baseAddress = memoryArray[1];

		int offset = 0; //default offset = 0;
		for (String[] x : this.lookup){
            if ( sizeDirective.equalsIgnoreCase(x[Memory.SIZE_DIRECTIVE_NAME]) ){
                offset = Integer.valueOf(x[SIZE_DIRECTIVE_SIZE]);
                break;
            }
        }

		String reformattedValue = MemoryAddressCalculator.extend(value,offset,"0");
		write(baseAddress, reformattedValue, offset);
	}

	public void write(String baseAddress, String value, int offset) throws MemoryWriteException{
		if ( this.mem.containsKey(baseAddress)){
			Integer inc;
			inc = Integer.parseInt(baseAddress, 16);
			int offsetHex = offset/4;
			System.out.println("writing value" + value);
			for ( int i = 0; i < offsetHex / 2; i++ ){
				String succeedingAddress = Memory.reformatAddress(Integer.toHexString(inc));
				if (this.mem.containsKey(succeedingAddress)) {
					this.mem.put(succeedingAddress,
							value.substring((value.length() - 2) - (i * 2), value.length() - (i * 2)));
					inc++;
				}
				else {
					throw new MemoryWriteException(succeedingAddress);
				}
			}
			//System.out.println("Memory read in little endian starting at: " + baseAddr);
		}
		else {
			throw new MemoryWriteException(baseAddress);
		}
	}
	
	public String read(String address){
		return this.mem.get(address);
	}
	
	public String read(Token baseAddressToken, Token src) throws MemoryReadException{
		return read(baseAddressToken.getValue(), src);
	}

	public String read(Token baseAddressToken, int offset) throws MemoryReadException{
		return read(baseAddressToken.getValue(), offset);
	}

	public String read(String wholeMemoryString, Token src) throws MemoryReadException{
		String entireMemoryToken = src.getValue();
		String[] memoryArray = entireMemoryToken.split("/");

		String sizeDirective = memoryArray[0];
		String baseAddress = memoryArray[1];

		int offset = 0; //default offset = 0;
		for (String[] x : this.lookup){
			if ( sizeDirective.equalsIgnoreCase(x[Memory.SIZE_DIRECTIVE_NAME]) ){
				offset = Integer.valueOf(x[SIZE_DIRECTIVE_SIZE]);
				break;
			}
		}
		return read(baseAddress, offset);
	}

	public String read(String baseAddress, int offset) throws MemoryReadException{
		String result = "";
		Integer inc;

		String checkSizeDirective = baseAddress;
		if ( checkSizeDirective.contains("/") ){
			checkSizeDirective = baseAddress.split("/")[1];
		}
		//System.out.println("BASE ADDRESS = " + reformatAddress(baseAddr));
		inc = Integer.parseInt(checkSizeDirective, 16);
		int offsetHex = offset/4;
		for ( int i = 0; i < offsetHex / 2; i++ ){
			result = read(reformatAddress(Integer.toHexString(inc))) + result;
			inc++;
		}
		System.out.println("Memory read in little endian: " + result);
		if ( result.contains("null") ){
			throw new MemoryReadException(baseAddress, offset);
		}
		return result;
	}

	public boolean isExisting(String sizeDirective){
		for (String[] x : this.lookup){
			if ( x[Memory.SIZE_DIRECTIVE_NAME].equals(sizeDirective) ){
				return true;
			}
		}
		return false;
	}
	
	public void print(String start, String end){
		Map<String, String> map = new TreeMap<>(mem).descendingMap();
		Iterator<Map.Entry<String, String>> ite = map.entrySet().iterator();
		while (ite.hasNext()){
			Map.Entry<String, String> x = ite.next();
			if ( Integer.parseInt(x.getKey(), 16) >= Integer.parseInt(start, 16) 
					&& Integer.parseInt(x.getKey(), 16) <= Integer.parseInt(end, 16) )
				System.out.println(x);
		}
	}

	public void clear(){
		Iterator<String> keys = this.mem.keySet().iterator();
		while (keys.hasNext()){
			//this.mem.put((keys.next()), "00" );
			String address = keys.next();
			this.mem.put(address, "" + address.charAt(address.length()-1) + address.charAt(address.length()-1));
		}
	}

	public Iterator<String[]> getLookup() {
		return lookup.iterator();
	}

	/*
		getRegisterKeys() is used for getting all register names to be highlighted
	 */
	public Iterator<String> getMemoryKeys(){
		List memoryKeys = new ArrayList<>();
		Iterator<String[]> iterator = getLookup();
		while(iterator.hasNext()){
			String sizeDirective = iterator.next()[Memory.SIZE_DIRECTIVE_NAME];
			memoryKeys.add(sizeDirective);
		}
		return memoryKeys.iterator();
	}

	public int getBitSize(Token a){
		String key = a.getValue();

		String checkSizeDirective = key;
		if ( checkSizeDirective.contains("/") ){
			key = checkSizeDirective.split("/")[0];
		}

		String[] arr = find(key);
		if ( arr != null ){
			return Integer.parseInt(arr[Memory.SIZE_DIRECTIVE_SIZE]);
		}
		return 0;
	}

	public String[] find(String sizeDirective){
		for (String[] x : this.lookup){
			if ( x[Memory.SIZE_DIRECTIVE_NAME].equalsIgnoreCase(sizeDirective) ){
				return x;
			}
		}
		return null;
	}

	public int getHexSize(Token a){
		return getBitSize(a) / 4;
	}

}

	public String[] find(String sizeDirective){
		for (String[] x : this.lookup){
			if ( x[Memory.SIZE_DIRECTIVE_NAME].equalsIgnoreCase(sizeDirective) ){
				return x;
			}
		}
		return null;
	}
	public int getHexSize(Token a){
		return getBitSize(a) / 4;
	}
}