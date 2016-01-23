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
	private int lineNumber;

	public CALVISParser(InstructionList instructions, RegisterList registers,
						Memory memory){

		this.instructions = instructions;
		this.registers = registers;
		this.memory = memory;
		this.calculator = new Calculator(registers, memory);
		this.mappedInstruction = new HashMap<String, CALVISInstruction>();
		this.lineNumber = 0;
		
		Iterator<String[]> instructionProductionRules = 
				this.instructions.getInstructionProductionRules();
		Iterator<String[]> registerTokens = this.registers.getRegisterList();
		
		Lang lang = new Lang("CALVIS");
		Grule assembly = lang.newGrule();
		Grule variableDeclarations = lang.newGrule();	
		Grule variable = lang.newGrule();
		Grule label = lang.newGrule();
		Grule dx = lang.newGrule();
		Grule value = lang.newGrule();
		Grule mainProgram = lang.newGrule();
		Grule instruction = lang.newGrule();
		
		Grule memoryAddrMode = lang.newGrule();
		Grule memoryExpr = lang.newGrule();
		Grule memoryAddExpr = lang.newGrule();
		Grule memoryTimesExpr = lang.newGrule();
		
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
		this.registerTokenMap = new HashMap<String, ArrayList<Element>>();
			
		while(registerTokens.hasNext()){
			String[] registerToken = registerTokens.next();
			String regName = "(\\b" + registerToken[0] + "\\b)"
					+ "|(\\b" + registerToken[0].toLowerCase() + "\\b)";
			String regSize = registerToken[2];
			String regType = registerToken[3];
			
			TokenDef registerName = lang.newToken(regName);

			// regType 1 = memory addressable, regType 2 = not memory addresable.
			if ( registerTokenMap.containsKey(regType) ){
				ArrayList<Element> altExist = registerTokenMap.get(regType);
				altExist.add(registerName);
			} else {
				ArrayList<Element> altNotExist = new ArrayList<Element>();
				altNotExist.add(registerName);
				registerTokenMap.put(regType, altNotExist);
			}

			// regSize = in bits, like 8, 16, 32
			if ( registerTokenMap.containsKey(regSize) ){
				ArrayList<Element> altExist = registerTokenMap.get(regSize);
				altExist.add(registerName);
			} else {
				ArrayList<Element> altNotExist = new ArrayList<Element>();
				altNotExist.add(registerName);
				registerTokenMap.put(regSize, altNotExist);
			}	
		}

		// start ::= assembly $
		lang.defineGrule(assembly, CC.EOF);

		// assembly ::= variableDeclarations? mainProgram
		assembly.define(CC.op(variableDeclarations), mainProgram);

		// memory addressing mode constructs
		// memory ::= [ memoryExpr ]
		memoryAddrMode.define(lsb, memoryExpr, rsb)
		.action(new Action<Object[]>() {
		    public Token act(Object[] matched) {
		    	Token mem = (Token) matched[1];
		    	mem.setType(Token.mem);
		    	return mem;
		    }
		});

//		memoryExpr ::= base + index * scale + displacement
//		memoryExpr.define(memoryBase).alt(hex);
//
//		memoryBase.define(getAllMemoryAddressableElements(), memoryScale)
//				.alt(getAllMemoryAddressableElements(), plus,  memoryScale);
//
//		memoryScale.define(getMemoryIndexScalabaleElements(), CC.op(times, "[1248]"), memoryDisplacement);
//
//		memoryDisplacement.define(displace, hex);
//
//		displace.define(plus).alt(minus);


		memoryExpr.define(memoryAddExpr, CC.ks(plus, memoryAddExpr))
		.action(new Action<Object[]>() {
		    public Token act(Object[] matched) {
		    	Object[] ms = (Object[]) matched;
		        Token a = (Token) matched[0];
		        Object[] aPairs = (Object[]) ms[1];
		        	for (Object p : aPairs) {
		        		String op = (String) ((Object[]) p)[0];
		                Token b = new Token(Token.hex, ((Object[]) p)[1].toString());
		                a = calculator.compute(new Token[]{a,b}, op);
		            }
		        return (Token) a;
		    }
		});		
		
		memoryAddExpr.define(getMemoryIndexScalabaleElements(), CC.kc(times, "[1248]"))
		.action(new Action<Object[]>() {
		    public Token act(Object[] matched) {
		    	Object[] ms = (Object[]) matched;
		        Token a = new Token(Token.reg, matched[0].toString());
		        Object[] aPairs = (Object[]) ms[1];
		        	for (Object p : aPairs) {
		        		String op = (String) ((Object[]) p)[0];
		                Token b = new Token(Token.hex, ((Object[]) p)[1].toString());
		                a = calculator.compute(new Token[]{a,b}, op);
		            }
		        return (Token) a;
		    }
		}).alt(memoryTimesExpr).action(new Action<Object>() {
		    public Token act(Object matched) {
		        return (Token) matched;
		    }
		});
		
		memoryTimesExpr.define(getAllMemoryAddressableElements())
			.action(new Action<Object>() {
				public Token act(Object matched) {
					return calculator.compute(new Token[]{(Token) matched}, "nop");
				}
		}).alt(hex).action(new Action<Object>() {
		    public Token act(Object matched) {
		        return new Token(Token.hex, matched.toString());
		    }
		});
		
		// Prepare <List of Instructions>
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
					
					for ( int q = 0; q < allowed.length; q++){
						//System.out.println(prodRule[0] + allowed[q]);
						
						// instructions asks for a register as an operand
						if ( allowed[q].contains("r")){
							// get specific register sizes
							String size = allowed[q].substring(1);
							if ( size.length() > 1){
								orSubRuleList.add(getRegisterElements(size));
							} else {
								orSubRuleList.add(getAllRegisterElements());
							}
						}
						if (allowed[q].contains("m")){
							orSubRuleList.add(memoryAddrMode);
						}
						if (allowed[q].contains("i")){
							orSubRuleList.add(hex);
						}
						if (allowed[q].contains("label")){ // not sure what labels should look like
							
						}
						elements[i] = concatenateOrSubRules(orSubRuleList);
					}
					prodRuleCounter++;					
				}
			}
			
			// bind action from BSH files
			Alternative instructionAlternative = new Alternative(elements);
			instructionAlternative.setAction(new Action<Object[]>(){

				@Override
				public Object act(Object[] args) {
					int numParameters = args.length / 2;
					Instruction someInstruction = instructions.getInstruction(args[0].toString());
					ArrayList<Token> tokenArr = new ArrayList<Token>();

					for (int i = 0; i < numParameters; i++) {
						tokenArr.add((Token) args[i*2+1]);
					}

					CALVISInstruction calvis = new CALVISInstruction(someInstruction,
							tokenArr.toArray(), registers, memory);

					String instructionAdd = Integer.toHexString(lineNumber);
					mappedInstruction.put(reformatAddress(instructionAdd), calvis);
					lineNumber++;

					return null;
				}
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

	private Element getAllRegisterElements(){
		Iterator<String> keys = registerTokenMap.keySet().iterator();
		ArrayList<Element> list = new ArrayList<Element>();
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
			result = (Element) list.get(0);
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
	
	private Element getMemoryIndexScalabaleElements(){
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

	private String reformatAddress(String add){
		String newAdd = add.toUpperCase();
		for (int i = 0; i < (this.memory.MAX_ADDRESS_SIZE / 2) - add.length(); i++){
			newAdd = "0" + newAdd;
		}
		return newAdd;
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