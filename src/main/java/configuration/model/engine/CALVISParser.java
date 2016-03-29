package configuration.model.engine;

import com.github.pfmiles.dropincc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CalvisParser {

	private InstructionList instructions;
	private RegisterList registers;
	private Memory memory;

	private Exe exe;
	private Lang lang;

	private ElementConcatenator elementConcatenator;

	private HashMap<String, CalvisInstruction> mappedInstruction;
	private ArrayList<Exception> exceptions;
	private int lineNumber;

	private TokenBag tokenBag;
	private Element allRegisters;
	private Grule memoryAddressingMode;
	private Element allMemorySizeDirectives;

	public CalvisParser(InstructionList instructions, RegisterList registers, Memory memory) {
		this.instructions = instructions;
		this.registers = registers;
		this.memory = memory;

		this.lang = new Lang("CALVIS");

		this.elementConcatenator = new ElementConcatenator(registers, memory);

		this.mappedInstruction = new HashMap<>();
		this.exceptions = new ArrayList<>();
		this.lineNumber = 0;

		Grule assembly = lang.newGrule();
		Grule mainProgram = lang.newGrule();
		Grule sectionDataRule = lang.newGrule();
		Grule justLabel = lang.newGrule();
		Grule label = lang.newGrule();

		TokenDef colon = lang.newToken(":");
		TokenDef sectionData = lang.newToken("SECTION .DATA");
		TokenDef hex = lang.newToken(PatternList.hexPattern);
		TokenDef floatingPoint = lang.newToken(PatternList.floatingPointPattern);
		TokenDef dec = lang.newToken(PatternList.decPattern);

		justLabel.define(PatternList.labelPattern)
				.action((Action<Object>) args -> new Token(Token.LABEL, args.toString()));

		label.define(justLabel, colon);

		this.tokenBag = new TokenBag(hex, dec, justLabel, floatingPoint);

		ParserRegisterFactory parserRegisterFactory = new ParserRegisterFactory(registers, lang, elementConcatenator);

		ParserMemoryFactory parserMemoryFactory = new ParserMemoryFactory(memory, lang, elementConcatenator, tokenBag,
				parserRegisterFactory.getAllRegisterElements(),
				parserRegisterFactory.getMemoryAddressableRegisters(),
				parserRegisterFactory.getMemoryIndexScalableElements());

		ParserVariableFactory parserVariableFactory = new ParserVariableFactory(memory, lang, elementConcatenator,
				tokenBag, exceptions, parserMemoryFactory.getVariableDeclarationTokenMap());
		this.exceptions.addAll(parserVariableFactory.getExceptions());

		this.allRegisters = parserRegisterFactory.getAllRegisterElements();
		this.memoryAddressingMode = parserMemoryFactory.getMemoryAddressingMode();
		this.allMemorySizeDirectives = parserMemoryFactory.getAllMemorySizeDirectiveElements();

		// start ::= assembly $ ( $ is end of file )
		lang.defineGrule(assembly, CC.EOF);

		// assembly ::= mainProgram variableDeclarations?
		assembly.define(CC.ks(PatternList.commentPattern), mainProgram,
				CC.ks(PatternList.commentPattern), CC.op(sectionDataRule));

		Grule instruction = createInstructionGrules();

		// mainProgram ::= ( LABEL? instruction)*
		ParserLabelFactory parserLabelFactory = new ParserLabelFactory(memory, exceptions);
		mainProgram.define(CC.ks(CC.ks(PatternList.commentPattern), CC.op(label), instruction))
				.action((Action<Object[]>) matched -> {
					this.exceptions.addAll(parserLabelFactory.createLabel(matched));
					return null;
				});

		sectionDataRule.define(sectionData, CC.ks(parserVariableFactory.getVariableDeclarationsGrule()));

		exe = lang.compile();
	}

	private Grule createInstructionGrules() {
		// Prepare different types of instruction tokens
		ArrayList<Element> noParameterTokens = new ArrayList<>();
		ArrayList<Element> oneParameterTokens = new ArrayList<>();
		ArrayList<Element> twoParameterTokens = new ArrayList<>();
		ArrayList<Element> threeParameterTokens = new ArrayList<>();

		Iterator<String[]> instructionProductionRules = this.instructions.getInstructionProductionRules();
		while ( instructionProductionRules.hasNext() ) {
			String[] prodRule = instructionProductionRules.next();

			for ( int i = 0; i < prodRule.length; i++ ) {
				System.out.print(prodRule[i] + " ");
			}
			System.out.println("");

			String insName = "(\\b" + prodRule[0] + "\\b)|(\\b" + prodRule[0].toLowerCase() + "\\b)";

			String appendType = prodRule[3];

			if ( !appendType.equals("0") ) {
				insName = "(\\b((" + prodRule[0] + "|" + prodRule[0].toLowerCase()
						+ ")(" + instructions.getConditionsRegEx(appendType) + "))\\b)";
			}

			TokenDef instructionName = lang.newToken(insName);
			int parameterCount = Integer.parseInt(prodRule[4]);

			switch ( parameterCount ) {
				case 0:
					noParameterTokens.add(instructionName);
					break;
				case 1:
					oneParameterTokens.add(instructionName);
					break;
				case 2:
					twoParameterTokens.add(instructionName);
					break;
				case 3:
					threeParameterTokens.add(instructionName);
					break;
			}
		}
		
		TokenDef comma = lang.newToken(",");

		Grule instruction = lang.newGrule();
		Grule noParameterInstruction = lang.newGrule();
		Grule oneParameterInstruction = lang.newGrule();
		Grule twoParameterInstruction = lang.newGrule();
		Grule threeParameterInstruction = lang.newGrule();

		noParameterInstruction.define(elementConcatenator.concatenateOrSubRules(noParameterTokens))
				.action((Action<Object>) args -> {
					String anInstruction = ((Token) args).getValue();
					Instruction someInstruction = instructions.getInstruction(anInstruction);
					CalvisInstruction calvisInstruction
							= new CalvisInstruction(someInstruction, anInstruction, registers, memory);
					String instructionAdd = Integer.toHexString(lineNumber);
					mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
							RegisterList.instructionPointerSize, "0"), calvisInstruction);
					lineNumber++;
					return calvisInstruction;
				});

		oneParameterInstruction.define(elementConcatenator.concatenateOrSubRules(oneParameterTokens), getAllParameters(true))
				.action((Action<Object[]>) args -> prepareCalvisInstruction(args));
		twoParameterInstruction.define(elementConcatenator.concatenateOrSubRules(twoParameterTokens), getAllParameters(false),
				comma, getAllParameters(false))
				.action((Action<Object[]>) args -> prepareCalvisInstruction(args));
		threeParameterInstruction.define(elementConcatenator.concatenateOrSubRules(threeParameterTokens), getAllParameters(false),
				comma, getAllParameters(false), comma, getAllParameters(true))
				.action((Action<Object[]>) args -> prepareCalvisInstruction(args));

		instruction.define(noParameterInstruction)
				.alt(oneParameterInstruction)
				.alt(twoParameterInstruction)
				.alt(threeParameterInstruction);

		return instruction;
	}

	private CalvisInstruction prepareCalvisInstruction(Object[] args) {
		int numParameters = args.length / 2;
		String anInstruction;
		if ( args[0] instanceof Token ) {
			anInstruction = ((Token) args[0]).getValue();
		} else {
			anInstruction = (String) args[0];
		}

//		String anInstruction = ((Token) args[0]).getValue();

		ArrayList<Object> tokenArr = new ArrayList<>();

		boolean isAppended = false;
		String base = instructions.getBaseConditionalInstruction("1", anInstruction);

		if ( !anInstruction.equals(base) ){
			isAppended = true;
		} else {
			base = instructions.getBaseConditionalInstruction("2", anInstruction);
			if ( !anInstruction.equals(base) ) {
				isAppended = true;
			}
		}
		if ( isAppended ) {
			String replaced = anInstruction.replaceAll(base, "");
			replaced = replaced.replaceAll(base.toUpperCase(), "");
			tokenArr.add(replaced);
			anInstruction = base;
		}

		String[] prodRule = instructions.find(anInstruction, numParameters);

//		String baseConditionalInstruction = instructions.getBaseConditionalInstruction(anInstruction);
//
//		if ( !anInstruction.equals(baseConditionalInstruction) ) {
//			String replaced = anInstruction.replaceAll(baseConditionalInstruction, "");
//			replaced = replaced.replaceAll(baseConditionalInstruction.toUpperCase(), "");
//			tokenArr.add(replaced);
//			anInstruction = baseConditionalInstruction;
//			isConditionalInstruction = true;
//		}

		Instruction someInstruction = instructions.getInstruction(anInstruction);
		for ( int i = 0; i < numParameters; i++ ) {
			tokenArr.add(args[i * 2 + 1]);
		}
		Object[] tokens = new Object[tokenArr.size()];
		tokens = tokenArr.toArray(tokens);

		ArrayList<String> result = new ArrayList<>();

		if ( numParameters == 1 ) {
			String[] firstParameter = prodRule[5].split("/");
			ArrayList<String[]> firstParameterList = new ArrayList<>();
			for ( int i = 0; i < firstParameter.length; i++ ) {
				String[] reformatted = expandInstructionParameter(firstParameter[i]);
				firstParameterList.add(reformatted);
			}
			for ( String[] entry : firstParameterList ) {
				for ( int i = 0; i < entry.length; i++ ) {
					result.add(entry[i]);
				}
			}
		}

		if ( numParameters >= 2 ) {
			result.clear();
			String[] firstParameter = prodRule[5].split("/");
			ArrayList<String[]> firstParameterList = new ArrayList<>();
			for ( int i = 0; i < firstParameter.length; i++ ) {
				String[] reformatted = expandInstructionParameter(firstParameter[i]);
				firstParameterList.add(reformatted);
			}
			ArrayList<String> specifications1 = new ArrayList<>();
			for ( String[] entry : firstParameterList ) {
				for ( int i = 0; i < entry.length; i++ ) {
					specifications1.add(entry[i]);
				}
			}
//			System.out.println(specifications1);

			String[] secondParameter = prodRule[6].split("/");
			ArrayList<String[]> secondParameterList = new ArrayList<>();
			for ( int i = 0; i < secondParameter.length; i++ ) {
				String[] reformatted = expandInstructionParameter(secondParameter[i]);
				secondParameterList.add(reformatted);
			}

			String[] thirdParameter = null;
			if ( numParameters == 3 ) {
				thirdParameter = prodRule[7].split("/");
			}
			ArrayList<String> specifications2 = new ArrayList<>();
			for ( String[] entry : secondParameterList ) {
				for ( int i = 0; i < entry.length; i++ ) {
					if ( thirdParameter != null ) {
						for ( String third : thirdParameter ) {
							specifications2.add(entry[i] + "/" + third);
						}
					} else {
						specifications2.add(entry[i]);
					}
				}
			}
//			System.out.println(specifications2);

			for ( String first : specifications1 ) {
				for ( String second : specifications2 ) {
					if ( !first.matches("[ci]") ) {
						String resultInstance = first + "/" + second;
						result.add(resultInstance);
					}
				}
			}
//				System.out.println(result);
		}

		CalvisInstruction calvisInstruction
				= new CalvisInstruction(someInstruction, anInstruction,
				tokens, registers, memory, Integer.parseInt(prodRule[3]), result);

		// Insert special check if instruction should be verified
		calvisInstruction.setVerifiable(instructions.isVerifiable(anInstruction));

		String instructionAdd = Integer.toHexString(lineNumber);
		mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
				RegisterList.instructionPointerSize, "0"), calvisInstruction);
		lineNumber++;
		return calvisInstruction;
	}

	/**
	 * This function expands the "r" or "m" notation to cover all sizes e.g. "r"
	 * would be expanded to {r8, r16, r32,...}
	 *
	 * @param parameterSpecification
	 * @return String[] of the reformatted parameter specifications
	 */
	private String[] expandInstructionParameter(String parameterSpecification) {
		ArrayList<String> parameterList = new ArrayList<>();
		int maxSize = RegisterList.MAX_SIZE / 8;
		if ( parameterSpecification.matches("[rm]") ) {
			if ( parameterSpecification.equals("m") ) {
				maxSize = Memory.MAX_ADDRESS_SIZE / 8;
			}
			for ( int i = 1; i < maxSize; i++ ) {
				Double size = Math.pow(2, 2 + i);
				String sizeInString = String.valueOf(size.intValue());
				parameterList.add(parameterSpecification + sizeInString);
			}
		} else {
			parameterList.add(parameterSpecification);
		}
		String[] result = new String[parameterList.size()];
		result = parameterList.toArray(result);
		return result;
	}

	private Element getAllParameters(boolean isOneParameter) {
		ArrayList<Element> orSubRuleList = new ArrayList<>();
		orSubRuleList.add(allRegisters);
		if ( isOneParameter ) {
			orSubRuleList.add(allMemorySizeDirectives);
		} else {
			orSubRuleList.add(memoryAddressingMode);
			orSubRuleList.add(allMemorySizeDirectives);
		}
		orSubRuleList.add(tokenBag.hex());
		orSubRuleList.add(tokenBag.dec());
		orSubRuleList.add(tokenBag.justLabel());

		return elementConcatenator.concatenateOrSubRules(orSubRuleList);
	}

	public HashMap<String, CalvisInstruction> parse(String code) throws Exception {
		this.mappedInstruction.clear();
		this.exceptions.clear();
		this.lineNumber = 0;
		this.exe.eval(code);
		if ( !exceptions.isEmpty() ) {
			throw exceptions.get(0);
		}
		return this.mappedInstruction;
	}

}
