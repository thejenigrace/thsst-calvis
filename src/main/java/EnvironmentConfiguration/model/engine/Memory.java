package EnvironmentConfiguration.model.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Memory  		- Protected Mode Flat Model
 *  address size  	- 32-bit
 *  starts from  	- 0x0000 0000
 *  ends at  		- 0x0000 FFFF
 */

public class Memory {

	static int MAX_ADDRESS_SIZE;

	static final int SIZE_DIRECTIVE_NAME = 0;
	static final int SIZE_DIRECTIVE_SIZE = 1;

	private TreeMap<String, String> mem;
	private ArrayList<String[]> lookup;

	public Memory(int bitSize, int bitEndLength, String csvFile){
        this.MAX_ADDRESS_SIZE = bitSize;
        this.mem = new TreeMap<>();

		String lastAddress = MemoryAddressCalculator.extend("F", bitEndLength, "F");
		int end = Integer.parseInt(lastAddress, 16);
		for ( int i = 0x0; i <= end; i++){
			String address = MemoryAddressCalculator.extend(Integer.toHexString(i), bitSize, "0");
			//mem.put(address, "00");
			mem.put(address, "" + address.charAt(address.length()-1) + address.charAt(address.length()-1) );
		}

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
                   // System.out.println(" [Prefix = " + row[0] + ", Size = " + row[1] + "]");
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
	
	private static String reformatAddress(String add){
		return MemoryAddressCalculator.extend(add, Memory.MAX_ADDRESS_SIZE, "0");
	}

	public void write(Token baseAddressToken, String value, Token des){
		write(baseAddressToken.getValue(), value, des);
	}

	public void write(Token baseAddressToken, String value, int offset){
		write(baseAddressToken.getValue(), value, offset);
	}

	public void write(String baseAddress, String value, Token des){
		// des contains our offset.
		String desValue = des.getValue();
		int offset = 0; //default offset = 0;

        for (String[] x : this.lookup){
            if ( desValue.contains(x[0]) ){
                offset = Integer.valueOf(x[1]) / 4;
                break;
            }
        }

		write(baseAddress, value, offset);
	}

	public void write(String baseAddress, String value, int offset){
		if ( this.mem.containsKey(baseAddress)){
			Integer inc;
			inc = Integer.parseInt(baseAddress, 16);
			int offsetHex = offset/4;
			for ( int i = 0; i < offsetHex / 2; i++ ){
				this.mem.put(Memory.reformatAddress(Integer.toHexString(inc)),
						value.substring((value.length() - 2) - (i*2) , value.length() - (i * 2) ));
				inc++;
			}
			//System.out.println("Memory read in little endian starting at: " + baseAddr);
		}
		else {
			System.out.println("Memory access invalid on: " + baseAddress);
		}
	}
	
	public String read(String address){
		return this.mem.get(address);
	}
	
	public String read(Token baseAddressToken, Token src){
		return read(baseAddressToken.getValue(), src);
	}

	public String read(Token baseAddressToken, int offset){
		return read(baseAddressToken.getValue(), offset);
	}

	public String read(String baseAddress, Token src){
		String srcVal = src.getValue();
		int offset = 0;
        for (String[] x : this.lookup){
            if ( srcVal.contains(x[0]) ){
                offset = Integer.valueOf(x[1]) / 4;
                break;
            }
        }
		return read(baseAddress, offset);
	}

	public String read(String baseAddress, int offset){
		String result = "";
		Integer inc;
		
		//System.out.println("BASE ADDRESS = " + reformatAddress(baseAddr));
		inc = Integer.parseInt(baseAddress, 16);
		int offsetHex = offset/4;
		for ( int i = 0; i < offsetHex / 2; i++ ){
			result = read(reformatAddress(Integer.toHexString(inc))) + result;
			inc++;
		}
		System.out.println("Memory read in little endian: " + result);
		try{
			if ( result.contains("null") ){
				throw new NumberFormatException("Memory read failed at base address: " + baseAddress);
			}
			return result;
		} catch ( NumberFormatException e ){
			e.printStackTrace();
		}
		return null;
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
	
}
