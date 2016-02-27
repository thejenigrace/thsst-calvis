package EnvironmentConfiguration.model.engine;

import EnvironmentConfiguration.controller.FileHandlerController;
import EnvironmentConfiguration.controller.HandleConfigFunctions;
import EnvironmentConfiguration.model.error_logging.*;
import bsh.EvalError;
import bsh.Interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class InstructionList {

    private String conditionsRegEx;
    private HashMap<String, Instruction> map;
    private ArrayList<String[]> grammarDefinition;
    private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<>());
    private ArrayList<String> conditionalInstructions;
    private final String[] conditionsArray = {
        "A", "NBE", "AE", "NB", "B", "NAE",
        "BE", "NA", "G", "NLE", "GE", "NL",
        "L", "NGE", "LE", "NG", "E", "Z",
        "NE", "NZ", "P", "PE", "NP", "PO",
        "O", "NO", "C", "NC", "S", "NS"};

    public InstructionList(String csvFile) {
        this.conditionsRegEx = String.join("|", conditionsArray);
        this.conditionsRegEx += "|" + String.join("|", conditionsArray).toLowerCase();

        this.conditionalInstructions = new ArrayList<>();

        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();
        BufferedReader br = null;
        String line;
        FileHandlerController fileHandlerController = new FileHandlerController();
        this.map = new HashMap<>();
        this.grammarDefinition = new ArrayList<>();
        int lineCounter = 1;
        boolean containsError = false;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {

                String[] inst = HandleConfigFunctions.split(line, ',');
                for (int i = 0; i < inst.length; i++) {
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
                if (!inst[1].equals("Location")) { // do not get first row

                    //check here
                    ArrayList<String> missingParametersInstruction = HandleConfigFunctions.checkifMissing(inst);

                    ArrayList<String> instructionErrorCollection = new ArrayList<>();
                    ArrayList<String> instructionMissingCollection = new ArrayList<>();
                    boolean isInteger = HandleConfigFunctions.isInteger(inst[4], 10);
                    boolean hasNoParameter = Integer.parseInt(inst[4]) > 0;

//						if(missingParametersInstruction.size() > 0){
//	//						isSkipped = true;
//							errorMessages.add(new ErrorMessage(
//									Types.instructionShouldNotBeEmpty,
//									missingParametersInstruction,
//									Integer.toString(lineCounter)));
//						}
                    if (inst[0].isEmpty()) {
                        instructionMissingCollection.add(
                                new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionName).
                                generateMessage()
                        );
                    }
                    if (inst[1].isEmpty()) {
                        instructionMissingCollection.add(
                                new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionFileName).
                                generateMessage()
                        );
                    }

                    if (inst[4].isEmpty()) {
                        instructionMissingCollection.add(
                                new InstructionFileErrorMissingMessage(InstructionMissing.missingInstructionParameterSize).
                                generateMessage()
                        );
                    }

                    if (!isInteger) {
                        instructionErrorCollection.add(
                                new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileParamterCount).
                                generateMessage(HandleConfigFunctions.generateArrayListString(inst[4]))
                        );
                    } else if (isInteger) {
                        int parameterSize = Integer.parseInt(inst[4]);

                        //check if negative
                        if (Math.signum(parameterSize) == -1.0) {
                            instructionErrorCollection.add(
                                    new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileNegativeCount).
                                    generateMessage(HandleConfigFunctions.generateArrayListString(inst[4]))
                            );
                        } else {
                            //check if parameter size is equals to size of recieved parameters and if size > 0
                            int parameterReceivedCount = line.split(",").length - 5;
                            if (parameterSize != parameterReceivedCount && parameterSize > 0) {
                                instructionErrorCollection.add(
                                        new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileLackingParameterCount).
                                        generateMessage(HandleConfigFunctions.generateArrayListString(inst[4],
                                                Integer.toString(parameterReceivedCount)))
                                );
                            } //check if contains more than 0 parameters
                            else if (hasNoParameter) {
                                instructionErrorCollection = doParameterChecking(inst);
                            }
                        }
//							for(int x = 3; x < inst.length - 1; x++) {
//								int countOfVar = inst[x].split("/").length;
//								int countOfSep = StringUtils.countMatches(inst[x], "/");
//								boolean[] errorParameters = new boolean[inst.length - 3];
//								boolean containsOtherThan = Pattern.matches("[rmi|0-9/]+", inst[x]);
//								if((countOfVar != countOfSep + 1 && parameterSize > 0 )|| !containsOtherThan) {
//									instructionErrorCollection.add(
//											new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFileNegativeCount).
//													generateMessage(HandleConfigFunctions.generateArrayListString(inst[2]))
//									);
//									errorParameters[x - 3] = true;
//								}
//								else
//									errorParameters[x - 3] = false;
//							}
                    }

                    //set missing path Error
                    String filePathInvalid = fileHandlerController.checkIfFileExists(inst[1]);
                    if (filePathInvalid.length() > 0) {
                        instructionErrorCollection.add(filePathInvalid);
                    }
//							System.out.println(filePathInvalid.length());
                    //check for duplicates

                    if (instructionErrorCollection.size() > 0) {
                        errorMessages.add(new ErrorMessage(Types.instructionShouldNotBeInvalid,
                                instructionErrorCollection,
                                Integer.toString(lineCounter)));
                        containsError = true;
                    }

                    if (instructionMissingCollection.size() > 0) {
                        errorMessages.add(new ErrorMessage(Types.instructionShouldNotBeEmpty,
                                instructionMissingCollection,
                                Integer.toString(lineCounter)));
                        containsError = true;
                    }

                    if (!containsError) {
                        Interpreter scanner = new Interpreter();
                        scanner.source(inst[1]);
                        Instruction com = (Instruction) scanner.eval(prepareImportStatements());
                        this.map.put(inst[0].toUpperCase(), com);
                        this.grammarDefinition.add(inst);
                        if ( inst[3].equals("1") ) {
                            this.conditionalInstructions.add(inst[0].toLowerCase());
                        }
//							System.out.println("Loaded: " + inst[0]);
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

    private String prepareImportStatements() {
        StringBuilder sb = new StringBuilder();
        sb.append("import EnvironmentConfiguration.model.engine.Token;");
        sb.append("import EnvironmentConfiguration.model.engine.RegisterList;");
        sb.append("import EnvironmentConfiguration.model.engine.Memory;");
        sb.append("import EnvironmentConfiguration.model.engine.EFlags;");
        sb.append("import EnvironmentConfiguration.model.engine.Calculator;");
        sb.append("import EnvironmentConfiguration.model.engine.JumpOutOfBoundsException;");
        sb.append("import java.math.BigInteger;");
        sb.append("\n return (EnvironmentConfiguration.model.engine.Instruction) this");
        return sb.toString();
    }

    public Instruction getInstruction(String instructionName) throws NullPointerException {
        String key = instructionName.toUpperCase();
        if (this.map.containsKey(key)) {
            return this.map.get(key);
        } else {
            throw new NullPointerException("Instruction does not exist: " + instructionName);
        }
    }

    public Iterator<String[]> getInstructionProductionRules() {
        return this.grammarDefinition.iterator();
    }

    public Iterator<String> getInstructionKeys() {
        ArrayList<String> highlightedWords = new ArrayList<>();
        Iterator<String> iterator = this.map.keySet().iterator();
        while (iterator.hasNext()) {
            String instruction = iterator.next();
            if (isConditionalInstruction(instruction)) {
                for (int i = 0; i < conditionsArray.length; i++) {
                    highlightedWords.add(instruction + conditionsArray[i]);
                }
            } else {
                highlightedWords.add(instruction);
            }
        }
        return highlightedWords.iterator();
    }

    public ErrorLogger getErrorLogger() {
        if (errorLogger.get(0).getSizeofErrorMessages() == 0) {
            return new ErrorLogger(new ArrayList<>());
        } else {
            return errorLogger;
        }
    }

    private ArrayList<String> doParameterChecking(String[] inst) {
        ArrayList<String> instructionErrorCollection = new ArrayList<>();
        String[] acceptableInputs = {"r", "m", "i", "c", "l"};
        int i = 5;
        for (int x = 0; x < Integer.parseInt(inst[4]); x++) {
            String addressingArray[] = HandleConfigFunctions.split(inst[i], '/');
            String[] tempContainers = new String[addressingArray.length];
            ArrayList<String> parameterNonExistent = new ArrayList<>();
            ArrayList<String> parameterDuplicate = new ArrayList<>();
            int z = 0;
            for (int y = 0; y < addressingArray.length; y++) {
                if (HandleConfigFunctions.StringSearchContains(acceptableInputs, addressingArray[y])) {
                    if (!HandleConfigFunctions.StringSearchInstruction(tempContainers, new String(addressingArray[y]))) {
                        tempContainers[z] = addressingArray[y];
                        z++;
                    } else {
                        parameterDuplicate.add(addressingArray[y]);
                    }
                } else {
                    parameterNonExistent.add(addressingArray[y]);
                }
            }
            if (parameterDuplicate.size() > 0) {
                parameterDuplicate.add(0, Integer.toString(i + 1));
                instructionErrorCollection.add(new InstructionFileErrorInvalidMessage
                        (InstructionInvalid.invalidDuplicateFileRegisterDestinationParameter)
                        .generateMessage(parameterDuplicate));
            }
            if (parameterNonExistent.size() > 0) {
                parameterNonExistent.add(0, Integer.toString(i + 1));
                instructionErrorCollection.add(new InstructionFileErrorInvalidMessage
                        (InstructionInvalid.invalidParameterFormat).generateMessage(parameterNonExistent));
            }
            i++;
        }
        return instructionErrorCollection;
    }

    public boolean isConditionalInstruction(String instruction) {
        for (String element : conditionalInstructions) {
            if (element.equalsIgnoreCase(instruction)) {
                return true;
            }
        }
        return false;
    }

    public String getBaseConditionalInstruction(String instruction) {
        for (String element : conditionalInstructions) {
            String pattern = "(" + element + "|" + element.toUpperCase() + ")(" + conditionsRegEx + ")";
            if (instruction.matches(pattern)) {
                return element;
            }
        }
        return instruction;
    }

    public String getConditionsRegEx() {
        return conditionsRegEx;
    }

}
