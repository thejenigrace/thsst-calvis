package EnvironmentConfiguration.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Memory {

	static int MAX_ADDRESS_SIZE;

	private TreeMap<String, String> mem;
    private ArrayList<String[]> lookup;

	public Memory(int bitSize, String csvFile){
        MAX_ADDRESS_SIZE = bitSize;
        mem = new TreeMap<>();
        int maxMemoryAddress = (int) Math.pow(2, bitSize);

		for ( int i = 0; i <= maxMemoryAddress; i++){
			String address = Memory.reformatAddress(Integer.toHexString(i));
			mem.put(address, "00");
			//mem.put(address, address.substring(3) + address.substring(3));
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
		String newAdd = add.toUpperCase();
		for (int i = 0; i < (MAX_ADDRESS_SIZE / 4) - add.length(); i++){
			newAdd = "0" + newAdd;
		}
		return newAdd;
	}

	public void write(Token baseAddrToken, String value, Token des){
		write(baseAddrToken.getValue(), value, des);
	}
	
	public void write(String baseAddr, String value, Token des){
		// des contains our offset.
		String desValue = des.getValue();
		int offset = 0; //default offset = 0;

        for (String[] x : this.lookup){
            if ( desValue.contains(x[0]) ){
                offset = Integer.valueOf(x[1]) / 4;
                break;
            }
        }

		write(baseAddr, value, offset); 
		
	}

	public void write(Token baseAddrToken, String value, int offset){
		write(baseAddrToken.getValue(), value, offset);
	}
	
	public void write(String baseAddr, String value, int offset){
		if ( this.mem.containsKey(baseAddr)){
			Integer inc;
			inc = Integer.parseInt(baseAddr, 16);
			for ( int i = 0; i < offset / 2; i++ ){
				this.mem.put(Memory.reformatAddress(Integer.toHexString(inc)),
						value.substring((value.length() - 2) - (i*2) , value.length() - (i * 2) ));
				inc++;
			}
			System.out.println("EnvironmentConfiguration.model.Memory read in little endian starting at: " + baseAddr);
		}
		else {
			System.out.println("EnvironmentConfiguration.model.Memory access invalid on: " + baseAddr);
		}
	}
	
	public String read(String address){
		return this.mem.get(address);
	}
	
	public String read(Token baseAddrToken, Token src){
		return read(baseAddrToken.getValue(), src);
	}
	
	public String read(String baseAddr, Token src){
		String srcVal = src.getValue();
		int offset = 0;
        for (String[] x : this.lookup){
            if ( srcVal.contains(x[0]) ){
                offset = Integer.valueOf(x[1]) / 4;
                break;
            }
        }
		return read(baseAddr, offset); 
	}
	
	public String read(Token baseAddrToken, int offset){
		return read(baseAddrToken.getValue(), offset);
	}
	
	public String read(String baseAddr, int offset){
		String result = "";
		Integer inc;
		
		System.out.println("BASE ADDRESS = " + reformatAddress(baseAddr));
		inc = Integer.parseInt(baseAddr, 16);
		int offsetHex = offset/4;
		for ( int i = 0; i < offsetHex / 2; i++ ){
			result = read(reformatAddress(Integer.toHexString(inc))) + result;
			inc++;
		}
		System.out.println("EnvironmentConfiguration.model.Memory read in little endian: " + result);
		return result;
	}
	
	public void print(String start, String end){
		Map<String, String> map = new TreeMap<String, String>(mem).descendingMap();
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
			this.mem.put((keys.next()), "00" );
		}
	}
	
}
