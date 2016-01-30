package EnvironmentConfiguration.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import EnvironmentConfiguration.FileHandlerController;
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
		FileHandlerController fileHandlerController = new FileHandlerController();
		this.map = new HashMap<String, Instruction>();
		this.grammarDefinition = new ArrayList<String[]>();
		int lineCounter = 1;
		boolean containsError = false;
			try {

				br = new BufferedReader(new FileReader(csvFile));
				br.readLine();
				while ((line = br.readLine()) != null) {

					// use comma as separator
					line.replaceAll("\\s+","");
					//line.split();
					String[] inst = HandleConfigFunctions.split(line, ',');
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

						ArrayList<String> instructionErrorCollection = new ArrayList<String>();
						ArrayList<String> instructionMissingCollection = new ArrayList<String>();
						boolean isInteger = HandleConfigFunctions.isInteger(inst[2], 10);
						boolean hasNoParameter = Integer.parseInt(inst[2]) > 0;

//						if(missingParametersInstruction.size() > 0){
//	//						isSkipped = true;
//							errorMessages.add(new ErrorMessage(
//									Types.instructionShouldNotBeEmpty,
//									missingParametersInstruction,
//									Integer.toString(lineCounter)));
//						}
						if(inst[0].isEmpty())
							instructionMissingCollection.add(
									new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionName).
											generateMessage()
							);
						if(inst[1].isEmpty())
							instructionMissingCollection.add(
									new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionFileName).
											generateMessage()
							);

						if(inst[2].isEmpty())
							instructionMissingCollection.add(
									new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionParameterSize).
											generateMessage()
							);

						if(!isInteger){
							instructionErrorCollection.add(
									new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileParamterCount).
											generateMessage(HandleConfigFunctions.generateArrayListString(inst[2]))
							);
						}
						else if(isInteger){
							int parameterSize = Integer.parseInt(inst[2]);
							//check if negative
							if(Math.signum(parameterSize) == -1.0) {
								instructionErrorCollection.add(
										new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileNegativeCount).
												generateMessage(HandleConfigFunctions.generateArrayListString(inst[2]))
								);
							}
							else {
								//check if parameter size is equals to size of recieved parameters and if size > 0
								int parameterRecievedCount = line.split(",").length - 3;
								if(parameterSize != parameterRecievedCount && parameterSize > 0){
									instructionErrorCollection.add(
											new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileLackingParameterCount).
													generateMessage(HandleConfigFunctions.generateArrayListString(inst[2],
															Integer.toString(parameterRecievedCount)))
									);
								}
								//check if contains more than 0 parameters
								else if (hasNoParameter){
									instructionErrorCollection = doParameterChecking(inst);
								}
							}
						}

						//set missing path Error
						String filePathInvalid = fileHandlerController.checkIfFileExists(inst[1]);
						if(filePathInvalid.length() > 0){
							instructionErrorCollection.add(filePathInvalid);
						}
//							System.out.println(filePathInvalid.length());
						//check for duplicates


						if(instructionErrorCollection.size() > 0) {
							errorMessages.add(new ErrorMessage(Types.instructionShouldNotBeInvalid,
									instructionErrorCollection,
									Integer.toString(lineCounter)));
							containsError = true;
						}

						if(instructionMissingCollection.size() > 0) {
							errorMessages.add(new ErrorMessage(Types.instructionShouldNotBeEmpty,
									instructionMissingCollection,
									Integer.toString(lineCounter)));
							containsError = true;
						}

						if(!containsError) {
							Interpreter scanner = new Interpreter();
							scanner.source(inst[1]);
							Instruction com = (Instruction) scanner.eval(" return (EnvironmentConfiguration.model.Instruction) this");
							this.map.put(inst[0].toUpperCase(), com);
							this.grammarDefinition.add(inst);
						}
					}
					lineCounter++;
				}

				errorLogger.add(new ErrorMessageList(Types.instructionFile, errorMessages));
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

	public ErrorLogger getErrorLogger(){
		if(errorLogger.get(0).getSizeofErrorMessages() == 0)
			return new ErrorLogger(new ArrayList<ErrorMessageList>());
		else
			return errorLogger;
	}

	private ArrayList<String> doParameterChecking(String[] inst){
		ArrayList<String> instructionErrorCollection = new ArrayList<String>();
		String[] acceptableInputs = {"r", "m", "i", "r16", "r32"};
		for (int x = 0; x < inst.length - 1 - Integer.parseInt(inst[2]); x++) {
			String addressingArray[] = HandleConfigFunctions.split(inst[x + 3], '/');
			String[] tempContainers = new String[addressingArray.length];
			ArrayList<String> parameterErrors = new ArrayList<String>();
			int z = 0;
			for (int y = 0; y < addressingArray.length; y++) {
				if (HandleConfigFunctions.StringSearchInstruction(acceptableInputs, addressingArray[y]) &&
						!HandleConfigFunctions.StringSearchInstruction(tempContainers, new String(addressingArray[y]))) {
					tempContainers[z] = addressingArray[y];
					z++;
				} else
					parameterErrors.add(addressingArray[y]);
			}
			if(parameterErrors.size() > 0) {
				parameterErrors.add(0, Integer.toString(x + 1));
				instructionErrorCollection.add(new InstructionFileErrorInvalidMessage(InstructionInvalid.
						invalidDuplicateFileRegisterDestinationParameter).generateMessage(parameterErrors));
			}
		}
		return instructionErrorCollection;
	}
}
