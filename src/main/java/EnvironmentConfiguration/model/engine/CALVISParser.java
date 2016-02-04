package EnvironmentConfiguration.model.engine;

import com.github.pfmiles.dropincc.*;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.OrSubRule;

import java.util.*;
import java.util.stream.Collectors;

public class CALVISParser {

	private InstructionList instructions;
	private RegisterList registers;
	private Memory memory;

	private Exe exe;
	private Lang lang;

	private HashMap<String, ArrayList<Element>> registerTokenMap;
	private HashMap<String, Element> memoryTokenMap;

	private HashMap<String, CALVISInstruction> mappedInstruction;
	private int lineNumber;

	private final String hexPattern = "(0[xX][0-9a-fA-F]{1," + RegisterList.MAX_SIZE / 4 + "})" +
			"|([0-9a-fA-F]{1," + RegisterList.MAX_SIZE / 4 + "}[hH])";

	public CALVISParser(InstructionList instructions, RegisterList registers, Memory memory){
		this.instructions = instructions;
		this.registers = registers;
		this.memory = memory;
		this.mappedInstruction = new HashMap<>();
		this.lineNumber = 0;		
		this.lang = new Lang("CALVIS");

		Grule assembly = lang.newGrule();
		Grule variableDeclarations = lang.newGrule();
		Grule variable = lang.newGrule();
		Grule label = lang.newGrule();
//		Grule dx = lang.newGrule();
//		Grule value = lang.newGrule();

		Grule mainProgram = lang.newGrule();
		Grule instruction = lang.newGrule();

		Grule sizeDirectiveAddressingMode = lang.newGrule();
		Grule memoryAddressingMode = lang.newGrule();
		Grule memoryExpression = lang.newGrule();
		Grule memoryBase = lang.newGrule();
		Grule memoryIndex = lang.newGrule();
		Grule memoryDisplacement = lang.newGrule();

		TokenDef colon = lang.newToken(":");
		TokenDef comma = lang.newToken(",");
		TokenDef lsb = lang.newToken("\\[");
		TokenDef rsb = lang.newToken("\\]");
		TokenDef plus = lang.newToken("\\+");
		TokenDef minus = lang.newToken("\\-");
		TokenDef times = lang.newToken("\\*");

		TokenDef hex = lang.newToken(hexPattern);
		TokenDef dec = lang.newToken("\\b\\d+\\b");

		/** The succeeding code now focuses on building the parser
		  * by connecting the grammar rules (Grule) and tokens (TokenDef)
		 */

		// 1. Prepare <List of Registers>
		prepareRegisterTokenMap();

		// 2. Prepare Memory Size Directives
		prepareMemorySizeDirectives();
		
		// start ::= assembly $
		lang.defineGrule(assembly, CC.EOF);

		// assembly ::= variableDeclarations? mainProgram
		assembly.define(CC.op(variableDeclarations), mainProgram);

		sizeDirectiveAddressingMode.define(getSizeDirectives(), memoryAddressingMode)
				.action((Action<Object[]>) matched -> {
					ArrayList<Token> tokenArrayList = new ArrayList<>();
					for ( Object obj : matched ){
						if ( obj instanceof Token ){
							Token sizeDirective = (Token) obj;
							tokenArrayList.add(sizeDirective);
						}
						else if ( obj instanceof Token[] ){
							Token[] tokens = (Token[]) obj;
							for (int i = 0; i < tokens.length; i++) {
								tokenArrayList.add(tokens[i]);
							}
						}
					}
					Token[] appendedTokens = new Token[tokenArrayList.size()];
					appendedTokens = tokenArrayList.toArray(appendedTokens);
					return appendedTokens;
				});

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


		
		// Prepare <List of Instructions>
		Iterator<String[]> instructionProductionRules = this.instructions.getInstructionProductionRules();
		ArrayList<Alternative> altList = new ArrayList<>();
		while (instructionProductionRules.hasNext()){
			String[] prodRule = instructionProductionRules.next();

			for (int i = 0; i < prodRule.length; i++) {
				System.out.print(prodRule[i] + " ");
			}
			System.out.println("");

			String insName = "(\\b" + prodRule[0] + "\\b)|(\\b" + prodRule[0].toLowerCase() + "\\b)";
			TokenDef instructionName = lang.newToken(insName);

			//System.out.println(insName);
			// add comma count to numParameters. * 2 includes the actual instruction name
			int numParameters = Integer.parseInt(prodRule[2]) * 2;
			if ( numParameters == 0){
				numParameters += 1;
			}

			System.out.println("number of parameters: " + numParameters);

			Element[] elements = new Element[numParameters];

			// 1. elements[0] = instruction name
			elements[0] = instructionName;
			// prodRuleCounter starts at index 3 because that's where the parameter specifications are
			int prodRuleCounter = 3;
			for (int i = 1; i < numParameters; i++){
				if ( i % 2 == 0){
					elements[i] = comma;
				}
				else {
					// get specific operand requirements
					String[] allowed = prodRule[prodRuleCounter].split("/");

					ArrayList<Element> orSubRuleList = new ArrayList<>();

					for (String anAllowed : allowed) {
						// instructions asks for a register as an operand
						if (anAllowed.contains("r")) {
							// get specific register sizes
							String size = anAllowed.substring(1);
							if (size.length() > 1) {
								orSubRuleList.add(getRegisterElements(size));
							} else {
								orSubRuleList.add(getAllRegisterElements());
							}
						}
						if (anAllowed.contains("m")) {
							// Create condition for "word ptr" bla bla
							orSubRuleList.add(memoryAddressingMode);
							orSubRuleList.add(sizeDirectiveAddressingMode);
						}
						if (anAllowed.contains("i")) {
							orSubRuleList.add(hex);
							orSubRuleList.add(dec);
						}
						if (anAllowed.contains("LABEL")) { // not sure what labels should look like

						}
						elements[i] = concatenateOrSubRules(orSubRuleList);
					}
					prodRuleCounter++;
				}
			}

			// bind action from BSH files
			Alternative instructionAlternative = new Alternative(elements);

			if ( numParameters == 1) {
				instructionAlternative.setAction((Action<Object>) args -> {
					String anInstruction = (String) args;
					//System.out.println(anInstruction);

					Instruction someInstruction = instructions.getInstruction(anInstruction);
					CALVISInstruction calvisInstruction =
							new CALVISInstruction(someInstruction, anInstruction, null, registers, memory);

					String instructionAdd = Integer.toHexString(lineNumber);
					mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
							RegisterList.instructionPointerSize, "0" ), calvisInstruction);
					lineNumber++;
					return null;
				});
			}
			else {
				instructionAlternative.setAction((Action<Object[]>) args -> {
					int numParameters1 = args.length / 2;
					String anInstruction = args[0].toString();

					Instruction someInstruction = instructions.getInstruction(anInstruction);
					ArrayList<Object> tokenArr = new ArrayList<>();

					for (int i = 0; i < numParameters1; i++) {
						tokenArr.add(args[i * 2 + 1]);
					}
					Object[] tokens = new Object[tokenArr.size()];
					tokens = tokenArr.toArray(tokens);

					CALVISInstruction calvisInstruction =
							new CALVISInstruction(someInstruction, anInstruction, tokens, registers, memory);

					String instructionAdd = Integer.toHexString(lineNumber);
					mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
							Memory.MAX_ADDRESS_SIZE, "0"), calvisInstruction);
					lineNumber++;
					return null;
				});
			}
			altList.add(instructionAlternative);
		}


		// instruction ::= <List of Instructions>
		instruction.setAlts(altList);

		// variableDeclarations ::=  (variable)*
		variableDeclarations.define(CC.ks(variable));

		// variable ::= <identifier> dx value
		//variable.define("[a-zA-Z_][a-zA-Z\\d_]*", dx, value);
		variable.define("[a-zA-Z_][a-zA-Z\\d_]*");

//		// LABEL ::= identifier ":"
		label.define("[a-zA-Z_][a-zA-Z\\d_]*", colon);
//
//		// dx ::= 'db' | 'dw' | 'dd'
//		dx.define(dbToken).alt(dwToken).alt(ddToken);
//
//		// value ::= string | char | int | double
//		value.define("\\d+(\\.\\d+)?").alt("\\d*\\.\\d*").alt("\"([^\\\"]+|\\.)*\"");
//		//value.define("5");

		// mainProgram ::= ( LABEL? instruction)*
		mainProgram.define(CC.ks(CC.op(label), instruction));

		// produce instruction rules
		exe = lang.compile();
	}

	private void prepareMemorySizeDirectives() {
		this.memoryTokenMap = new HashMap<>();
		Iterator<String[]> memoryTokens = this.memory.getLookup();

		while(memoryTokens.hasNext()){
			String[] memoryToken = memoryTokens.next();
			String sizeDirectiveName = memoryToken[Memory.SIZE_DIRECTIVE_NAME];
			String sizeDirectiveSize = memoryToken[Memory.SIZE_DIRECTIVE_SIZE];
			String sizeDirectivePattern = "(\\b" + sizeDirectiveName + "\\b)"
					+ "|(\\b" + sizeDirectiveName.toUpperCase() + "\\b)";

			TokenDef memorySizeDirective = lang.newToken(sizeDirectivePattern);

			this.memoryTokenMap.put(sizeDirectiveSize, memorySizeDirective);
		}
		System.out.println(memoryTokenMap);
	}

	private Element getSizeDirectives() {
		ArrayList<Element> elementArrayList =
				this.memoryTokenMap.entrySet().stream()
						.map(Map.Entry::getValue)
						.collect(Collectors.toCollection(ArrayList::new));

		return concatenateOrSubRules(elementArrayList);
	}
	
	private void prepareRegisterTokenMap() {
		this.registerTokenMap = new HashMap<>();
		Iterator<String[]> registerTokens = this.registers.getRegisterList();
		
		while(registerTokens.hasNext()){
			String[] registerToken = registerTokens.next();
			String regName = "(\\b" + registerToken[RegisterList.NAME] + "\\b)"
					+ "|(\\b" + registerToken[RegisterList.NAME].toLowerCase() + "\\b)";
			String regSize = registerToken[RegisterList.SIZE];
			String regType = registerToken[RegisterList.TYPE];

			TokenDef registerName = lang.newToken(regName);
			/*
				 regType 1 = memory addressable
				 regType 2 = not memory addressable
				 regType 4 = flag
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
						if ( arg0 instanceof Token){
                            return (Token) arg0;
                        }
						if ( memory.isExisting(arg0.toString())){
							return new Token(Token.MEM, (String) arg0);
						}
                        if ( arg0.toString().matches(hexPattern)){
                            return new Token(Token.HEX, (String) arg0);
                        }
						if ( arg0.toString().matches("\\b\\d+\\b")) {
							return new Token(Token.DEC, (String) arg0);
						}
						if ( arg0 instanceof Token[] ){
							return arg0;
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
		ArrayList<Element> result = new ArrayList<Element>();
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
	
	public HashMap<String, CALVISInstruction> parse(String code){
		try {
			this.mappedInstruction.clear();
			this.lineNumber = 0;
			this.exe.eval(code);
		} catch(Exception e){
			System.out.println("CALVIS Parsing error message: " + e.getMessage());
			e.printStackTrace();
		}
		return this.mappedInstruction;
	}
}