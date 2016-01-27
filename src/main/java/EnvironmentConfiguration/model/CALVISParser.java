package EnvironmentConfiguration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Exe;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.OrSubRule;

public class CALVISParser {
	
	private Exe exe;
	private HashMap<String, ArrayList<Element>> registerTokenMap;
	private InstructionList instructions;
	private RegisterList registers;
	private Memory memory;
	private Calculator calculator;
	private HashMap<String, CALVISInstruction> mappedInstruction;
	private Lang lang;
	private int lineNumber;

	public CALVISParser(InstructionList instructions, RegisterList registers, Memory memory){
		this.instructions = instructions;
		this.registers = registers;
		this.memory = memory;
		this.calculator = new Calculator(registers, memory);
		this.mappedInstruction = new HashMap<String, CALVISInstruction>();
		this.lineNumber = 0;		
		this.lang = new Lang("CALVIS");
		

		Grule assembly = lang.newGrule();
		Grule variableDeclarations = lang.newGrule();	
		Grule variable = lang.newGrule();
		Grule label = lang.newGrule();
		Grule dx = lang.newGrule();
		Grule value = lang.newGrule();
		Grule mainProgram = lang.newGrule();
		Grule instruction = lang.newGrule();
		Grule memoryAddressingMode = lang.newGrule();
		Grule memoryExpression = lang.newGrule();
		Grule memoryBase = lang.newGrule();
		Grule memoryIndex = lang.newGrule();
		Grule memoryDisplacement = lang.newGrule();

//		Grule memoryAddExpr = lang.newGrule();
//		Grule memoryTimesExpr = lang.newGrule();
		
		TokenDef dbToken = lang.newToken("DB");
		TokenDef dwToken = lang.newToken("DW");
		TokenDef ddToken = lang.newToken("DD");
		TokenDef colon = lang.newToken(":");
		TokenDef comma = lang.newToken(",");
		TokenDef lsb = lang.newToken("\\[");
		TokenDef rsb = lang.newToken("\\]");
		TokenDef plus = lang.newToken("\\+");
		TokenDef minus = lang.newToken("\\-");
		TokenDef times = lang.newToken("\\*");
		TokenDef hex = lang.newToken("0x[0-9a-fA-F]{1,8}");
		
		// Prepare <List of Registers>
		prepareRegisterTokenMap();
		
		// start ::= assembly $
		lang.defineGrule(assembly, CC.EOF);

		// assembly ::= variableDeclarations? mainProgram
		assembly.define(CC.op(variableDeclarations), mainProgram);

		// memory addressing mode constructs
		// memory ::= [ memoryExpr ]
		memoryAddressingMode.define(lsb, memoryExpression, rsb)
		.action((Action<Object[]>) matched -> {
			for (Object obj : matched){
				System.out.println("memory addressing mode rule: " + obj);
			}
            Token mem = (Token) matched[1];
            mem.setType(Token.mem);
            return mem;
        });

		//memoryExpression ::= base + index * scale + displacement
		memoryExpression.define(memoryBase, CC.op(memoryIndex), CC.op(memoryDisplacement))
		.action((Action<Object[]>) matched -> {
			for (Object obj : matched){
				System.out.println("memory expression rule: " + obj);
			}
			return null;
		});

		memoryBase.define(getAllMemoryAddressableElements())
				.alt(hex)
					.action((Action<Object>) matched -> {
							System.out.println("memory base rule: " + matched);
						return null;
					});

		memoryIndex.define( plus, getMemoryIndexScalableElements(), CC.op(times, "[1248]"))
				.alt(CC.op("[1248]", times), getMemoryIndexScalableElements())
					.action((Action<Object[]>) matched -> {
						for (Object obj : matched){
							System.out.println("memory index rule: " +obj);
						}
                        return null;
                    });

		memoryDisplacement.define(plus.or(minus), hex)
				.action((Action<Object[]>) matched -> {
                    //no + or - yet
					for (Object obj : matched){
						System.out.println("memory displacement rule: " +obj);
					}
					return null;
                });
		
//		memoryExpr.define(memoryAddExpr, CC.ks(plus, memoryAddExpr))
//		.action(new Action<Object[]>() {
//		    public Token act(Object[] matched) {
//		    	Object[] ms = (Object[]) matched;
//		        Token a = (Token) matched[0];
//		        Object[] aPairs = (Object[]) ms[1];
//		        	for (Object p : aPairs) {
//		        		String op = (String) ((Object[]) p)[0];
//		                Token b = new Token(Token.hex, ((Object[]) p)[1].toString());
//		                a = calculator.compute(new Token[]{a,b}, op);
//		            }
//		        return (Token) a;
//		    }
//		});
//
//		memoryAddExpr.define(getMemoryIndexScalableElements(), CC.kc(times, "[1248]"))
//		.action(new Action<Object[]>() {
//		    public Token act(Object[] matched) {
//		    	Object[] ms = (Object[]) matched;
//		        Token a = new Token(Token.reg, matched[0].toString());
//		        Object[] aPairs = (Object[]) ms[1];
//		        	for (Object p : aPairs) {
//		        		String op = (String) ((Object[]) p)[0];
//		                Token b = new Token(Token.hex, ((Object[]) p)[1].toString());
//		                a = calculator.compute(new Token[]{a,b}, op);
//		            }
//		        return (Token) a;
//		    }
//		}).alt(memoryTimesExpr).action(new Action<Object>() {
//		    public Token act(Object matched) {
//		        return (Token) matched;
//		    }
//		});
//
//		memoryTimesExpr.define(getAllMemoryAddressableElements())
//			.action(new Action<Object>() {
//				public Token act(Object matched) {
//					return calculator.compute(new Token[]{(Token) matched}, "nop");
//				}
//		}).alt(hex).action(new Action<Object>() {
//		    public Token act(Object matched) {
//		        return new Token(Token.hex, matched.toString());
//		    }
//		});
		
		// Prepare <List of Instructions>
		Iterator<String[]> instructionProductionRules = this.instructions.getInstructionProductionRules();
		ArrayList<Alternative> altList = new ArrayList<Alternative>();
		while (instructionProductionRules.hasNext()){
			String[] prodRule = instructionProductionRules.next();
			String insName = "\\b" + prodRule[0] + "\\b";
			TokenDef instructionName = lang.newToken(insName);

			// add comma count to numParameters. * 2 includes the actual instruction name
			int numParameters = Integer.parseInt(prodRule[2]) * 2;
			if ( numParameters == 0){
				numParameters += 1;
			}

			Element[] elements = new Element[numParameters];
			elements[0] = instructionName.or(lang.newToken(prodRule[0].toLowerCase()));

			int prodRuleCounter = 3;
			for (int i = 1; i < numParameters; i++){
				if ( i % 2 == 0){
					elements[i] = comma;
				}
				else {
					// get specific operand requirements
					String[] allowed = prodRule[prodRuleCounter].split("/");

					ArrayList<Element> orSubRuleList = new ArrayList<Element>();

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
							orSubRuleList.add(memoryAddressingMode);
						}
						if (anAllowed.contains("i")) {
							orSubRuleList.add(hex);
						}
						if (anAllowed.contains("label")) { // not sure what labels should look like

						}
						elements[i] = concatenateOrSubRules(orSubRuleList);
					}
					prodRuleCounter++;
				}
			}

			// bind action from BSH files
			Alternative instructionAlternative = new Alternative(elements);
			instructionAlternative.setAction((Action<Object[]>) args -> {
				int numParameters1 = args.length / 2;
				Instruction someInstruction = instructions.getInstruction(args[0].toString());
				ArrayList<Token> tokenArr = new ArrayList<>();

				for (int i = 0; i < numParameters1; i++) {
					tokenArr.add((Token) args[i*2+1]);
				}

				CALVISInstruction calvisInstruction = new CALVISInstruction(someInstruction,
						tokenArr.toArray(), registers, memory);

				String instructionAdd = Integer.toHexString(lineNumber);
				mappedInstruction.put(calculator.reformatAddress(instructionAdd), calvisInstruction);
				lineNumber++;
				return null;
			});
			altList.add(instructionAlternative);
		}
		
		// instruction ::= <List of Instructions>
		instruction.setAlts(altList);
				
		// variableDeclarations ::=  (variable)*
		variableDeclarations.define(CC.ks(variable));
		
		// variable ::= <identifier> dx value
		variable.define("[a-zA-Z_][a-zA-Z\\d_]*", dx, value);
		
		// label ::= identifier ":"
		label.define("[a-zA-Z_][a-zA-Z\\d_]*", colon);
			
		// dx ::= 'db' | 'dw' | 'dd'
		dx.define(dbToken).alt(dwToken).alt(ddToken);
			
		// value ::= string | char | int | double
		value.define("\\d+(\\.\\d+)?").alt("\\d*\\.\\d*").alt("\"([^\\\"]+|\\.)*\"");
		//value.define("5");
		
		// mainProgram ::= ( label? instruction)*
		mainProgram.define(CC.ks(CC.op(label), instruction));		
		
		// produce instruction rules
		exe = lang.compile();
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
                        if ( arg0.toString().contains("0x")){
                            return new Token(Token.hex, (String) arg0);
                        }
                        return new Token(Token.reg, (String) arg0);
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
	private Element getAllMemoryAddressableElements(){
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