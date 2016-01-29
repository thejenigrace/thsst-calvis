package EnvironmentConfiguration.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import EnvironmentConfiguration.controller.HandleConfigFunctions;
import bsh.EvalError;
import bsh.Interpreter;


public class InstructionList {
	
	private HashMap<String, Instruction> map;
	private ArrayList<String[]> grammarDefinition;
	private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<ErrorMessageList>());

	public InstructionList(String csvFile){
		ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		this.map = new HashMap<String, Instruction>();
		this.grammarDefinition = new ArrayList<String[]>();
		int lineCounter = 0;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			    // use comma as separator
				String[] inst = line.split(cvsSplitBy);
				// trim every row just in case.
				for (int i = 0; i < inst.length; i++){
					inst[i] = inst[i].trim();
				}

				line.replaceAll("\\s+","");
				//line.split();
				String[] inst = HandleConfigFunctions.split(line);
				inst[0] = inst[0].toUpperCase();
//				?System.out.println(inst[0] + " 0to " +inst[1] + " 1to " +inst[2] + " 2to "+inst[3] + " 3to "+inst[4] + " 4to ") ;
				// debug printing
//				for (int i = 0; i < inst.length; i++){
//					inst[i] = inst[i].trim();
//					System.out.print(inst[i] + " ");
//				}
//				System.out.println("");
			
				// inst[0] contains instruction name
				// inst[1] contains .bsh file location
				
				if ( !inst[1].equals("Location") ){ // do not get first row

					//check here
					ArrayList<String> missingParametersInstruction = HandleConfigFunctions.checkifMissing(inst);
					if(missingParametersInstruction.size() > 0){
//						isSkipped = true;
						errorMessages.add(new ErrorMessage(
								Types.instructionShouldNotBeEmpty,
								missingParametersInstruction,
								Integer.toString(lineCounter)));
					}

					Interpreter scanner = new Interpreter();
					scanner.source(inst[1]);
					Instruction com =  (Instruction) scanner.eval(prepareImportStatements());
					this.map.put(inst[0].toUpperCase(), com);
					this.grammarDefinition.add(inst);
				}
				}
				lineCounter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String prepareImportStatements(){
		StringBuilder sb = new StringBuilder();
		sb.append("import EnvironmentConfiguration.model.Token;");
		sb.append("import EnvironmentConfiguration.model.RegisterList;");
		sb.append("import EnvironmentConfiguration.model.Memory;");
		sb.append("import EnvironmentConfiguration.model.Calculator;");
		sb.append("\n return (EnvironmentConfiguration.model.Instruction) this");
		return sb.toString();
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
