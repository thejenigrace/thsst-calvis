package EnvironmentConfiguration.model;

import bsh.EvalError;
import bsh.Interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import bsh.EvalError;
import bsh.Interpreter;


public class InstructionList {
	
	private HashMap<String, Instruction> map;
	private ArrayList<String[]> grammarDefinition;
	
	public InstructionList(String csvFile){
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		this.map = new HashMap<String, Instruction>();
		this.grammarDefinition = new ArrayList<String[]>();
		
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			    // use comma as separator
				String[] inst = line.split(cvsSplitBy);
				
				// trim every row just in case.
				for (int i = 0; i < inst.length; i++){
					inst[i] = inst[i].trim();
				}

				inst[0] = inst[0].toUpperCase();
				
				// debug printing
//				for (int i = 0; i < inst.length; i++){
//					inst[i] = inst[i].trim();
//					System.out.print(inst[i] + " ");
//				}
//				System.out.println("");
			
				// inst[0] contains instruction name
				// inst[1] contains .bsh file location
				
				if ( !inst[1].equals("Location") ){
					Interpreter scanner = new Interpreter();
					scanner.source(inst[1]);
					Instruction com =  (Instruction) scanner.eval("import EnvironmentConfiguration.model.Calculator;" +
							"import EnvironmentConfiguration.model.EFlags;" +
							"\n return (EnvironmentConfiguration.model.Instruction) this");
					this.map.put(inst[0].toUpperCase(), com);
					this.grammarDefinition.add(inst);
				}			
				
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EvalError e) {
			// TODO Auto-generated catch block
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
	
	public Instruction getInstruction(Token t){
		return getInstruction(t.getValue());
	}
	
	public Instruction getInstruction(String instructionName){
		String key = instructionName.toUpperCase();
		if ( this.map.containsKey(key) )
			return this.map.get(key);
		return null;
	}
	
	public Iterator<String[]> getInstructionProductionRules(){
		return this.grammarDefinition.iterator();
	}

	public Iterator<String> getInstructionKeys(){
		return this.map.keySet().iterator();
	}
}
