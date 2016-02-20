package EnvironmentConfiguration.model.engine;

import com.github.pfmiles.dropincc.*;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.OrSubRule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CALVISParser {

	private InstructionList instructions;
	private RegisterList registers;
	private Memory memory;

	private Exe exe;
	private Lang lang;

	private HashMap<String, ArrayList<Element>> registerTokenMap;
	private HashMap<String, Element> memoryTokenMap;
	private HashMap<String, Element> variableDeclarationTokenMap;
	private HashMap<String, Grule> memorySizeDirectives;

	private HashMap<String, CALVISInstruction> mappedInstruction;
	private ArrayList<Exception> exceptions;
	private int lineNumber;

	private final String hexPattern = "\\b(0[xX][0-9a-fA-F]{1," + RegisterList.MAX_SIZE / 4 + "})\\b";
	private final String decPattern = "\\b(\\d+)\\b";
	private final String commentPattern = "(;.*)";
	private final String labelPattern = "[a-zA-Z_][a-zA-Z\\d_]*";

	private TokenDef hex;
	private TokenDef dec;

	private TokenDef cl;

	private Grule justLabel;
	private Grule label;

//	private Grule hexOrDecimal;

	public CALVISParser(InstructionList instructions, RegisterList registers, Memory memory){
		this.instructions = instructions;
		this.registers = registers;
		this.memory = memory;
		this.mappedInstruction = new HashMap<>();
		this.exceptions = new ArrayList<>();
		this.lineNumber = 0;		
		this.lang = new Lang("CALVIS");

		Grule assembly = lang.newGrule();
		Grule variableDeclarations = lang.newGrule();
		Grule sectionDataRule = lang.newGrule();
		Grule mainProgram = lang.newGrule();
		Grule instruction = lang.newGrule();
		Grule memoryAddressingMode = lang.newGrule();
		Grule memoryExpression = lang.newGrule();
		Grule memoryBase = lang.newGrule();
		Grule memoryIndex = lang.newGrule();
		Grule memoryDisplacement = lang.newGrule();

		label = lang.newGrule();
		justLabel = lang.newGrule();
//		hexOrDecimal = lang.newGrule();

		TokenDef colon = lang.newToken(":");
		TokenDef comma = lang.newToken(",");
		TokenDef lsb = lang.newToken("\\[");
		TokenDef rsb = lang.newToken("\\]");
		TokenDef plus = lang.newToken("\\+");
		TokenDef minus = lang.newToken("\\-");
		TokenDef times = lang.newToken("\\*");
		TokenDef sectionData = lang.newToken("SECTION .DATA");
		TokenDef stringLiteral = lang.newToken("\"([^\"\\\\]|\\\\.)*\"");

		/** The succeeding code now focuses on building the parser
		  * by connecting the grammar rules (Grule) and tokens (TokenDef)
		 */

		// 1. Prepare <List of Registers>
		prepareRegisterTokenMap();
		// 2. Prepare Memory Size Directives
		prepareMemorySizeDirectives();

		hex = lang.newToken(hexPattern);
		dec = lang.newToken(decPattern);

		// start ::= assembly $
		lang.defineGrule(assembly, CC.EOF);

		justLabel.define(labelPattern)
			.action((Action<Object>) args -> new Token(Token.LABEL, args.toString()));

//		// LABEL ::= identifier ":"
		label.define(justLabel, colon);

		// memory addressing mode constructs
		// memory ::= [ memoryExpr ]
		memoryAddressingMode.define(lsb, memoryExpression, rsb)
				.action((Action<Object[]>) matched -> {
					for (Object obj : matched){
						System.out.println("memory addressing mode rule: " + obj);
					}
					Token[] mem = (Token[]) matched[1];
					return mem;
				});

		//memoryExpression ::= base + index * scale + displacement
		memoryExpression.define(memoryBase, CC.op(plus, memoryIndex), CC.op(plus.or(minus), memoryDisplacement))
			.action((Action<Object[]>) matched -> {
				ArrayList<Token> tokenArrayList = new ArrayList<>();
				for (Object obj : matched){
					if ( obj != null){
						System.out.println("memory expression rule: " + obj);
						if ( obj instanceof Token ) {
							tokenArrayList.add((Token) obj);
						}
						else if (obj instanceof Object[]){
							Object[] objects = (Object[]) obj;
							/** This object[] is always:
							 * object[0] = "+" or "-"
							 * object[1] = Token
							*/
							Token t = (Token) objects[1];
							t.setValue(objects[0].toString() + " " + t.getValue());
							tokenArrayList.add(t);
						}
					}
				}
				// Perform address computations within each element of the object[] matched
				Token[] tokens = new Token[tokenArrayList.size()];
				tokens = tokenArrayList.toArray(tokens);
				return tokens;
			});

		memoryBase.define(getAllMemoryAddressableRegisters())
			.action((Action<Object>) matched -> {
				Token t = (Token) matched;
				System.out.println("memory base rule: " + t.getValue() + " :: " + t.getType());
				return t;
			})
			.alt(hex)
				.action((Action<Object>) matched -> {
					System.out.println("memory base rule: " + matched);
					return new Token(Token.HEX, matched.toString());
				})
			.alt(dec)
				.action((Action<Object>) matched -> {
					System.out.println("memory base rule: " + matched);
					return new Token(Token.DEC, matched.toString());
				})
			.alt(justLabel)
				.action((Action<Object>) matched -> {
					System.out.println("memory base rule: " + matched);
					return new Token(Token.DEC, matched.toString());
				});

		memoryIndex.define(getMemoryIndexScalableElements(), CC.op(times, dec))
			.action((Action<Object[]>) matched -> {
				String result = "";
				for (Object obj : matched){
					if (obj != null){
						System.out.println("memory index rule: " +obj);
						if ( obj instanceof Token ){
							result += ((Token) obj).getValue() + " ";
						}
						else if ( obj instanceof  Object[] ){
							Object[] objects = (Object []) obj;
							if ( !objects[1].toString().matches("[1248]") ){
								throw new DropinccException("Invalid scale value");
							}
							String optionalTimes = objects[0] + " " + objects[1];
								result += optionalTimes;
						}
					}
				}
				return new Token(Token.REG, result);
			})
			.alt(CC.op(dec, times), getMemoryIndexScalableElements())
				.action((Action<Object[]>) matched -> {
					String result = "";
					for (Object obj : matched){
						if (obj != null){
							System.out.println("memory index rule: " +obj);
							if ( obj instanceof Token ){
								result += ((Token) obj).getValue();
							}
							else if ( obj instanceof  Object[] ){
								Object[] objects = (Object []) obj;
								if ( !objects[0].toString().matches("[1248]") ){
									throw new DropinccException("Invalid scale value");
								}
								String optionalTimes = objects[0] + " " + objects[1] + " ";
								result += optionalTimes;
							}
						}
					}
					return new Token(Token.REG, result);
                });

		memoryDisplacement.define(hex)
			.action((Action<Object>) matched -> {
				System.out.println("memory displacement rule: " + matched);
				return new Token(Token.HEX, matched.toString());
			})
			.alt(dec)
				.action((Action<Object>) matched -> {
					System.out.println("memory displacement rule: " + matched);
					return new Token(Token.DEC, matched.toString());
				});

		/**
		 *  Prepare memory size directive addressing mode
		 *  and variable declaration types
		 */
		this.memorySizeDirectives = new HashMap<>();

//		hexOrDecimal.define(hexPattern)
//			.action((Action<Object>) matched -> new Token(Token.HEX, matched.toString()))
//			.alt(decPattern)
//			.action((Action<Object>) matched -> new Token(Token.DEC, matched.toString()));

		ArrayList<Element> hexOrDecimalList = new ArrayList<>();
		hexOrDecimalList.add(hex);
		hexOrDecimalList.add(dec);
		Element hexOrDecimal = concatenateOrSubRules(hexOrDecimalList);

		for(int i = 1; i < Memory.MAX_ADDRESS_SIZE / 8; i++){
			Double size = Math.pow(2, 2 + i);
			String sizeInString = String.valueOf(size.intValue());
			Grule sizeDirectiveInstance = lang.newGrule();
			sizeDirectiveInstance.define(getMemoryElement(sizeInString), memoryAddressingMode)
				.action((Action<Object[]>) matched -> {
					ArrayList<Token> tokenArrayList = new ArrayList<>();
					for ( Object obj : matched ){
						if ( obj instanceof String ){
							String sizeDirectiveName = (String) obj;
							Token sizeDirective = new Token(Token.MEM, sizeDirectiveName);
							tokenArrayList.add(sizeDirective);
						}
						else if ( obj instanceof Token[] ){
							Token[] tokens = (Token[]) obj;
							for (int k = 0; k < tokens.length; k++) {
								tokenArrayList.add(tokens[k]);
							}
						}
					}
					Token[] appendedTokens = new Token[tokenArrayList.size()];
					appendedTokens = tokenArrayList.toArray(appendedTokens);
					return appendedTokens;
				});

			this.memorySizeDirectives.put(sizeInString, sizeDirectiveInstance);
		}

		/**
		 * variable declaration per variable type
		 * db, dw, dd, ...
		 */
//		variableDeclarations ::=  (variable)*
		variableDeclarations.define(justLabel, getAllVariableElements(), hexOrDecimal, CC.ks(comma, hexOrDecimal))
			.action((Action<Object[]>) matched -> {
				Token labelName = (Token) matched[0];
				Token dataType = (Token) matched[1];
				Token value = (Token) matched[2];
				Object[] moreValues = (Object[]) matched[3];
//				System.out.println(labelName.getValue() + " " + dataType.getValue() + " " + value.getValue());
//				System.out.println("with: " + moreValues.length);
				ArrayList<Token> valuesList = new ArrayList<>();
				valuesList.add(value);

				for ( Object obj : moreValues ) {
					Object[] objectGroup = (Object[]) obj;
					/**
					 * objectGroup[0] = comma
					 * objectGroup[1] = actual value
					 */
					Token objectGroupValue = (Token) objectGroup[1];
//					System.out.println(objectGroup[0] + " " + objectGroupValue.getValue());
					valuesList.add(objectGroupValue);
				}

				for( Token token : valuesList ) {
					if ( token.isHex() ) {
						int declaredSize = memory.getPrefixHexSize(dataType);
						String tokenValue = token.getValue();
						if ( tokenValue.length() > declaredSize ) {
							exceptions.add(new DataTypeMismatchException(labelName.getValue(),
									dataType.getValue(), tokenValue));
						} else {
							try {
								int prefixSize = memory.getPrefixBitSize(dataType);
								tokenValue = MemoryAddressCalculator.extend(tokenValue, prefixSize, "0");
								memory.write(memory.getVariablePointer(), tokenValue, prefixSize);
								memory.incrementVariablePointer(prefixSize);
							} catch (MemoryWriteException e) {
								exceptions.add(e);
							}
						}
					}
				}
				try {
					memory.putToVariableMap(labelName.getValue(), dataType.getValue());
				} catch (DuplicateVariableException e) {
					exceptions.add(e);
				}

				return null;
			});

		sectionDataRule.define(sectionData, CC.ks(variableDeclarations));

		// assembly ::= mainProgram variableDeclarations?
		assembly.define(CC.ks(commentPattern), mainProgram, CC.ks(commentPattern), CC.op(sectionDataRule));

		/**
		 * START of static definition of 2 parameter rules
		 */
		HashMap<String, Element[]> parameterSpecifications = new HashMap<>();

		for(int i = 1; i < RegisterList.MAX_SIZE / 8; i++){
			Double size = Math.pow(2, 2 + i);
			String sizeInString = String.valueOf(size.intValue());
//			System.out.println(sizeInString);

			// Register to Register
			Element[] registerToRegister = new Element[4];
			//registerToRegister[0] = instructionName;
			registerToRegister[1] = getRegisterElements(sizeInString);
			registerToRegister[2] = comma;
			registerToRegister[3] = getRegisterElements(sizeInString);
			parameterSpecifications.put("r" + sizeInString + "r" + sizeInString, registerToRegister);

			// Memory to Register
			Element[] memoryToRegister = new Element[4];

			ArrayList<Element> optionalSizeDirectiveList = new ArrayList<>();
			optionalSizeDirectiveList.add(memorySizeDirectives.get(sizeInString));
			optionalSizeDirectiveList.add(memoryAddressingMode);
			Element optionalSizeDirective = concatenateOrSubRules(optionalSizeDirectiveList);

			memoryToRegister[1] = getRegisterElements(sizeInString);
			memoryToRegister[2] = comma;
			memoryToRegister[3] = optionalSizeDirective;
			parameterSpecifications.put("r" + sizeInString + "m" + sizeInString, memoryToRegister);

			// Immediate to Register
			Element[] immediateToRegister = new Element[4];
			immediateToRegister[1] = getRegisterElements(sizeInString);
			immediateToRegister[2] = comma;
			immediateToRegister[3] = hexOrDecimal;
			parameterSpecifications.put("r" + sizeInString + "i", immediateToRegister);

			// CL to Register
			Element[] clToRegister = new Element[4];
			clToRegister[1] = getRegisterElements(sizeInString);
			clToRegister[2] = comma;
			clToRegister[3] = cl;
			parameterSpecifications.put("r" + sizeInString + "c", clToRegister);

			// Register to Memory
			Element[] registerToMemory = new Element[4];
			registerToMemory[1] = optionalSizeDirective;
			registerToMemory[2] = comma;
			registerToMemory[3] = getRegisterElements(sizeInString);
			parameterSpecifications.put("m" + sizeInString + "r" + sizeInString, registerToMemory);

			// Immediate to Memory
			Element[] immediateToMemory = new Element[4];
			immediateToMemory[1] = memorySizeDirectives.get(sizeInString);
			immediateToMemory[2] = comma;
			immediateToMemory[3] = hexOrDecimal;
			parameterSpecifications.put("m" + sizeInString + "i", immediateToMemory);

			// CL to Memory
			Element[] clToMemory = new Element[4];
			clToMemory[1] = memorySizeDirectives.get(sizeInString);
			clToMemory[2] = comma;
			clToMemory[3] = cl;
			parameterSpecifications.put("m" + sizeInString + "c", clToMemory);
		}

		/**
		 * END
		 */
//		System.out.println("PARAMETER SPECIFICATIONS HAVE: " + parameterSpecifications.size());
//		System.out.println(parameterSpecifications);

		// Prepare <List of Instructions>
		Iterator<String[]> instructionProductionRules = this.instructions.getInstructionProductionRules();
		ArrayList<Alternative> altList = new ArrayList<>();

		while (instructionProductionRules.hasNext()){
			ArrayList<Element[]> instructionAlternatives = new ArrayList<>();
			String[] prodRule = instructionProductionRules.next();

			for (int i = 0; i < prodRule.length; i++) {
				System.out.print(prodRule[i] + " ");
			}
			System.out.println("");

			String insName = "(\\b" + prodRule[0] + "\\b)|(\\b" + prodRule[0].toLowerCase() + "\\b)";

			if ( instructions.isConditionalInstruction(prodRule[0]) ) {
				insName = "(\\b((" + prodRule[0] + "|" + prodRule[0].toLowerCase()
						+ ")(" + instructions.getConditionsRegEx() + "))\\b)";
			}

			TokenDef instructionName = lang.newToken(insName);

			//System.out.println(insName);
			// prodRule[2] * 2 includes the actual instruction name and the commas
			int parameterCount = Integer.parseInt(prodRule[2]);
			int numParameters = parameterCount * 2;
			if ( numParameters == 0){
				numParameters += 1;
			}

//			System.out.println("actual count from csv: " + parameterCount);
//			System.out.println("number of parameters: " + numParameters);

			switch (parameterCount){
				case 0:
					Element[] elements0 = new Element[numParameters];
					elements0[0] = instructionName;
					instructionAlternatives.add(elements0);
					break;
				case 1:
					Element[] elements1 = new Element[numParameters];
					elements1[0] = instructionName;
					String[] justOne = prodRule[3].split("/");
					elements1[1] = parseOneParameter(justOne);
					instructionAlternatives.add(elements1);
					break;
				case 2: // fall through
				case 3:
					String[] firstParameter = prodRule[3].split("/");
					ArrayList<String[]> firstParameterList = new ArrayList<>();
					for (int i = 0; i < firstParameter.length; i++) {
						String[] reformatted = expandInstructionParameter(firstParameter[i]);
						firstParameterList.add(reformatted);
					}
					ArrayList<String> specifications1 = new ArrayList<>();
					for ( String[] entry : firstParameterList ){
						for (int i = 0; i < entry.length; i++) {
							specifications1.add(entry[i]);
						}
					}
//					System.out.println(specifications1);

					String[] secondParameter = prodRule[4].split("/");
					ArrayList<String[]> secondParameterList = new ArrayList<>();
					for (int i = 0; i < secondParameter.length; i++) {
						String[] reformatted = expandInstructionParameter(secondParameter[i]);
						secondParameterList.add(reformatted);
					}
					ArrayList<String> specifications2 = new ArrayList<>();
					for ( String[] entry : secondParameterList ){
						for (int i = 0; i < entry.length; i++) {
							specifications2.add(entry[i]);
						}
					}
//					System.out.println(specifications2);

					ArrayList<String> result = new ArrayList<>();
					for ( String first : specifications1 ){
						for ( String second : specifications2 ){
							if ( isPermissible(first, second) ) {
								String resultInstance = first + second;
								result.add(resultInstance);
							}
						}
					}
					System.out.println(result);

					for ( String resultInstance : result ){
						Element[] elements2 = new Element[numParameters];
						Element[] content = parameterSpecifications.get(resultInstance);
						for (int i = 0; i < elements2.length; i++) {
							if ( i < content.length ){
								elements2[i] = content[i];
							}
						}
						elements2[0] = instructionName;
						if ( parameterCount == 3 ) {
							elements2[4] = comma;
							String[] thirdParameter = prodRule[5].split("/");
							elements2[5] = parseOneParameter(thirdParameter);
						}
						instructionAlternatives.add(elements2);
					}
					break;
			}

			for ( Element[] elements: instructionAlternatives) {
				// bind action from BSH files
				Alternative instructionAlternative = new Alternative(elements);
				if (numParameters == 1) {
					instructionAlternative.setAction((Action<Object>) args -> {
						String anInstruction = (String) args;
						//System.out.println(anInstruction);
						//////////////////////
						Instruction someInstruction = instructions.getInstruction(anInstruction);
						CALVISInstruction calvisInstruction =
								new CALVISInstruction(someInstruction, anInstruction, null, registers, memory);
						String instructionAdd = Integer.toHexString(lineNumber);
						mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
								RegisterList.instructionPointerSize, "0"), calvisInstruction);
						lineNumber++;
						return calvisInstruction;
					});
				} else {
					instructionAlternative.setAction((Action<Object[]>) args -> {
						int numParameters1 = args.length / 2;
						String anInstruction = args[0].toString();
						ArrayList<Object> tokenArr = new ArrayList<>();
//						System.out.println(anInstruction);
						String baseConditionalInstruction = instructions.getBaseConditionalInstruction(anInstruction);
						if ( !anInstruction.equals(baseConditionalInstruction) ){
							String replaced = anInstruction.replaceAll(baseConditionalInstruction,"");
							tokenArr.add(replaced);
							anInstruction = baseConditionalInstruction;
						}
						//////////////////////
						Instruction someInstruction = instructions.getInstruction(anInstruction);

						for (int i = 0; i < numParameters1; i++) {
							tokenArr.add(args[i * 2 + 1]);
						}

						Object[] tokens = new Object[tokenArr.size()];
						tokens = tokenArr.toArray(tokens);
						CALVISInstruction calvisInstruction =
								new CALVISInstruction(someInstruction, anInstruction, tokens, registers, memory);
						String instructionAdd = Integer.toHexString(lineNumber);
						mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
								RegisterList.instructionPointerSize, "0"), calvisInstruction);
						lineNumber++;
						return calvisInstruction;
					});
				}
				altList.add(instructionAlternative);
			}
		}

		// instruction ::= <List of Instructions>
		instruction.setAlts(altList);
//
//		// value ::= string | char | int | double
//		value.define("\\d+(\\.\\d+)?").alt("\\d*\\.\\d*").alt("\"([^\\\"]+|\\.)*\"");


		// mainProgram ::= ( LABEL? instruction)*
		mainProgram.define(CC.ks(CC.ks(commentPattern), CC.op(label), instruction ))
			.action((Action<Object[]>) matched -> {
				int labelValue = 0;
				for (Object obj : matched){
//					System.out.println("LINE: " + labelValue);
					Object[] objects = (Object[]) obj;
					for (int i = 0; i < objects.length ; i++) {
//						System.out.println(i + " " + objects[i]);
						if ( objects[i] != null ){
							if ( i == 1 && objects[i] instanceof Object[] ){
//								System.out.println("we found an object array");
								Object[] objects1 = (Object[]) objects[i];
								/**
								 * objects1[0] = contains label
								 * objects1[1] = contains the colon
								 */
								String instructionAddress = Integer.toHexString(labelValue);
								instructionAddress = Memory.reformatAddress(instructionAddress);
								String label = ((Token) objects1[0]).getValue();
								if ( !memory.containsLabel(label) ) {
									memory.putToLabelMap(((Token) objects1[0]).getValue(), instructionAddress);
								}
								else {
									try {
										throw new DuplicateLabelException(label);
									} catch (DuplicateLabelException e) {
										exceptions.add(e);
									}
								}
//								System.out.println(((Token) objects1[0]).getValue() + " -> " + instructionAddress );
//								for (int j = 0; j < objects1.length; j++) {
//									System.out.println(i + " " + j + " " + objects1[j]);
//								}
							}
						}
					}
					labelValue++;
				}
				return null;
			});

		// produce instruction rules
		System.out.println("PARSER IS BEING COMPILED");
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		System.out.println("STARTED AT: " + df.format(dateobj));

		exe = lang.compile();
		dateobj = new Date();
		System.out.println("PARSER IS BUILT");
		System.out.println("ENDED AT: " + df.format(dateobj));

	}

	/**
	 * This function expands the "r" or "m" notation to cover all sizes
	 * e.g. "r" would be expanded to {r8, r16, r32,...}
	 * @param parameterSpecification
	 * @return String[] of the reformatted parameter specifications
	 */
	private String[] expandInstructionParameter(String parameterSpecification){
		ArrayList<String> parameterList = new ArrayList<>();
		int maxSize = RegisterList.MAX_SIZE / 8;
		if ( parameterSpecification.matches("[rm]") ) {
			if (parameterSpecification.equals("m")) {
				maxSize = Memory.MAX_ADDRESS_SIZE / 8;
			}
			for (int i = 1; i < maxSize; i++) {
				Double size = Math.pow(2, 2 + i);
				String sizeInString = String.valueOf(size.intValue());
				parameterList.add(parameterSpecification + sizeInString);
			}
		}
		else {
			parameterList.add(parameterSpecification);
		}
		String[] result = new String[parameterList.size()];
		result = parameterList.toArray(result);
		return result;
	}

	private boolean isPermissible(String first, String second){
		if ( first.contains("m") && second.contains("m") ){
			return false;
		}
		if ( first.substring(1).equals(second.substring(1)) ){
			return true;
		}
		if ( second.matches("[ci]") ){
			return true;
		}
		return false;
	}

	private Element parseOneParameter(String[] specification){
		// get specific operand requirements
		String[] allowed = specification;
		ArrayList<Element> orSubRuleList = new ArrayList<>();
		for ( String anAllowed : allowed ) {
			// instructions asks for a register as an operand
			if ( anAllowed.contains("r") ) {
				// get specific register sizes
				String size = anAllowed.substring(1);
				if (size.length() > 1) {
					orSubRuleList.add(getRegisterElements(size));
				} else {
					orSubRuleList.add(getAllRegisterElements());
				}
			}
			if ( anAllowed.contains("m") ) {
				String size = anAllowed.substring(1);
				if (size.length() > 1) {
					orSubRuleList.add(memorySizeDirectives.get(size));
				} else {
					orSubRuleList.add(getAllMemorySizeDirectiveElements());
				}
			}
			if ( anAllowed.contains("i") ) {
				orSubRuleList.add(hex);
				orSubRuleList.add(dec);
//				orSubRuleList.add(hexOrDecimal);
			}
			if ( anAllowed.contains("c") ) {
				orSubRuleList.add(cl);
			}
			if ( anAllowed.contains("l") ) {
				orSubRuleList.add(justLabel);
			}
		}
		return concatenateOrSubRules(orSubRuleList);
	}

	private void prepareMemorySizeDirectives() {
		this.memoryTokenMap = new HashMap<>();
		this.variableDeclarationTokenMap = new HashMap<>();

		Iterator<String[]> memoryTokens = this.memory.getLookup();

		while(memoryTokens.hasNext()){
			String[] memoryToken = memoryTokens.next();
			String sizeDirectiveName = memoryToken[Memory.SIZE_DIRECTIVE_NAME];
			String sizeDirectiveSize = memoryToken[Memory.SIZE_DIRECTIVE_SIZE];
			String sizeDirectivePrefix = memoryToken[Memory.SIZE_DIRECTIVE_PREFIX];

			String sizeDirectivePattern = "(\\b" + sizeDirectiveName + "\\b)" +
				"|(\\b" + sizeDirectiveName.toUpperCase() + "\\b)";
			TokenDef memorySizeDirective = lang.newToken(sizeDirectivePattern);
			this.memoryTokenMap.put(sizeDirectiveSize, memorySizeDirective);

			String variableTypePattern = "\\b(" + sizeDirectivePrefix + "|" +
				sizeDirectivePrefix.toUpperCase() + ")\\b";
			System.out.println(variableTypePattern);
			TokenDef variableType = lang.newToken(variableTypePattern);
			this.variableDeclarationTokenMap.put(sizeDirectiveSize, variableType);
		}

//		System.out.println(memoryTokenMap);
	}
	
	private void prepareRegisterTokenMap() {
		this.registerTokenMap = new HashMap<>();
		Iterator<String[]> registerTokens = this.registers.getRegisterList();
		boolean flag = false;
		while(registerTokens.hasNext()){
			String[] registerToken = registerTokens.next();
			String regName = "(\\b" + registerToken[RegisterList.NAME] + "\\b)"
					+ "|(\\b" + registerToken[RegisterList.NAME].toLowerCase() + "\\b)";
			String regSize = registerToken[RegisterList.SIZE];
			String regType = registerToken[RegisterList.TYPE];

			if ( !flag && registerToken[RegisterList.NAME].equalsIgnoreCase("cl")) {
//				System.out.println("CL was declared");
				cl = lang.newToken(regName);
				flag = true;
			}

			TokenDef registerName = lang.newToken(regName);
			/*
				 regType 1 = memory addressable
				 regType 2 = not memory addressable
				 regType 3 = instruction pointer
			 */
			if ( registerTokenMap.containsKey(regType) ){
				ArrayList<Element> altExist = registerTokenMap.get(regType);
				altExist.add(registerName);
			} else {
				ArrayList<Element> altNotExist = new ArrayList<>();
				altNotExist.add(registerName);
				registerTokenMap.put(regType, altNotExist);
			}

			// regSize = in bits, like 8, 16, 32
			if ( registerTokenMap.containsKey(regSize) ){
				ArrayList<Element> altExist = registerTokenMap.get(regSize);
				altExist.add(registerName);
			} else {
				ArrayList<Element> altNotExist = new ArrayList<>();
				altNotExist.add(registerName);
				registerTokenMap.put(regSize, altNotExist);
			}
		}
	}

	private Element getAllMemorySizeDirectiveElements(){
		Iterator<String> keys = memorySizeDirectives.keySet().iterator();
		ArrayList<Element> list = new ArrayList<>();
		while (keys.hasNext()){
			String key = keys.next();
			Element temp = memorySizeDirectives.get(key);
			list.add(temp);
		}
		return concatenateOrSubRules(list);
	}

	private Element getAllRegisterElements(){
		Iterator<String> keys = registerTokenMap.keySet().iterator();
		ArrayList<Element> list = new ArrayList<>();
		while (keys.hasNext()){
			String key = keys.next();
			// we don't want duplicates, so just get registers depending on size.
			if ( Integer.parseInt(key) > 4){ 
				Element temp = getRegisterElements(key);
				list.add(temp);
			}
		}
		return concatenateOrSubRules(list);
	}
		
	private Element concatenateOrSubRules(ArrayList<Element> list){
		Element result = null;
		OrSubRule osb = null;
		if (!list.isEmpty() ){
			result = list.get(0);
			if ( list.size() >= 2){
				if (list.get(1).getClass().equals(OrSubRule.class)){
					osb = ((OrSubRule) list.get(1)).or(result);
				}
				else if (list.get(1).getClass().equals(TokenDef.class)){
					osb = ((TokenDef) list.get(1)).or(result);
				}
				else if (list.get(1).getClass().equals(Grule.class)){
					osb = ((Grule) list.get(1)).or(result);
				}
				for(int i = 2; i < list.size(); i++ ){
					osb.or(list.get(i));
				}
				
				List<Alternative> producedList = osb.getAlts();
				for (int i = 0; i < producedList.size(); i++){
					producedList.get(i).setAction((Action<Object>) arg0 -> {
//						System.out.println(arg0 + " : " + arg0.getClass());
						if ( arg0 instanceof Token ){
							return (Token) arg0;
                        }
						if ( registers.isExisting(arg0.toString().toUpperCase()) ){
							return new Token(Token.REG, (String) arg0);
						}
						if ( memory.isExisting(arg0.toString()) ){
							return new Token(Token.MEM, (String) arg0);
						}
                        if ( arg0.toString().matches(hexPattern) ){
							return new Token(Token.HEX, (String) arg0);
						}
						if ( arg0.toString().matches(decPattern) ) {
							return new Token(Token.DEC, (String) arg0);
						}
						if ( arg0 instanceof Token[] ){
							return arg0;
						}
						if ( arg0.toString().matches("[a-zA-Z_][a-zA-Z\\d_]*") ){
							return new Token(Token.LABEL, (String) arg0);
						}

                        return new Token(Token.REG, (String) arg0);
                    });
				}
				return osb;
			}
		}
		return result;
	}
	
	private Element getMemoryIndexScalableElements(){
		ArrayList<Element> list = registerTokenMap.get("1");
		ArrayList<Element> result = new ArrayList<>();
		for(Element x: list){
			TokenDef y = (TokenDef) x;
			// we remove ESP and SP as index scalable registers for memory addressing mode
			if ( !y.getRegexp().contains("SP") ){
				result.add(x);
			}
			//System.out.println(y.getRegexp());
		}
		return concatenateOrSubRules(result);
	}

	private Element getAllMemoryAddressableRegisters(){
		return getRegisterElements("1");
	}
	
	private Element getRegisterElements(String string) {
		ArrayList<Element> list = registerTokenMap.get(string);
		return concatenateOrSubRules(list);
	}

	private Element getMemoryElement(String size) {
		return memoryTokenMap.get(size);
	}

	private Element getAllMemoryElements() {
		ArrayList<Element> memoryElementsList = new ArrayList<>();
		Iterator<Element> iterator = memoryTokenMap.values().iterator();
		while ( iterator.hasNext() ){
			memoryElementsList.add(iterator.next());
		}
		return concatenateOrSubRules(memoryElementsList);
	}

	private Element getAllVariableElements(){
		Iterator<Element> keys = variableDeclarationTokenMap.values().iterator();
		ArrayList<Element> list = new ArrayList<>();
		while (keys.hasNext()){
			list.add(keys.next());
		}
		return concatenateOrSubRules(list);
	}

	public HashMap<String, CALVISInstruction> parse(String code) throws Exception {
		this.mappedInstruction.clear();
		this.exceptions.clear();
		this.lineNumber = 0;
		this.exe.eval(code);
		if ( !exceptions.isEmpty() ){
			throw exceptions.get(0);
		}
		return this.mappedInstruction;
	}

}