package EnvironmentConfiguration.model.engine;

import com.github.pfmiles.dropincc.*;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.OrSubRule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    private final String stringLiteralPattern = "\"([^\"\\\\]|\\\\.)*\"";

    private TokenDef hex;
    private TokenDef dec;
    private TokenDef cl;

    private Grule justLabel;
    private Grule label;
    private Grule memoryAddressingMode;

    public CALVISParser(InstructionList instructions, RegisterList registers, Memory memory) {
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
        Grule memoryExpression = lang.newGrule();
        Grule memoryBase = lang.newGrule();
        Grule memoryIndex = lang.newGrule();
        Grule memoryDisplacement = lang.newGrule();

        justLabel = lang.newGrule();
        label = lang.newGrule();
        memoryAddressingMode = lang.newGrule();

        TokenDef colon = lang.newToken(":");
        TokenDef comma = lang.newToken(",");
        TokenDef lsb = lang.newToken("\\[");
        TokenDef rsb = lang.newToken("\\]");
        TokenDef plus = lang.newToken("\\+");
        TokenDef minus = lang.newToken("\\-");
        TokenDef times = lang.newToken("\\*");
        TokenDef sectionData = lang.newToken("SECTION .DATA");
        TokenDef stringLiteral = lang.newToken(stringLiteralPattern);

        /**
         * The succeeding code now focuses on building the parser by connecting
         * the grammar rules (Grule) and tokens (TokenDef)
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
                    Token[] mem = (Token[]) matched[1];
                    return mem;
                });

        //memoryExpression ::= base + index * scale + displacement
        memoryExpression.define(memoryBase, CC.op(plus, memoryIndex), CC.op(plus.or(minus), memoryDisplacement))
                .action((Action<Object[]>) matched -> {
                    ArrayList<Token> tokenArrayList = new ArrayList<>();
                    for (Object obj : matched) {
                        if (obj != null) {
                            if (obj instanceof Token) {
                                tokenArrayList.add((Token) obj);
                            } else if (obj instanceof Object[]) {
                                Object[] objects = (Object[]) obj;
                                /**
                                 * This object[] is always: object[0] = "+" or
                                 * "-" object[1] = Token
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
                    return t;
                })
                .alt(hex)
                .action((Action<Object>) matched -> new Token(Token.HEX, matched.toString()))
                .alt(dec)
                .action((Action<Object>) matched -> new Token(Token.DEC, matched.toString()))
                .alt(justLabel)
                .action((Action<Object>) matched -> (Token) matched);

        memoryIndex.define(getMemoryIndexScalableElements(), CC.op(times, dec))
                .action((Action<Object[]>) matched -> {
                    String result = "";
                    for (Object obj : matched) {
                        if (obj != null) {
                            if (obj instanceof Token) {
                                result += ((Token) obj).getValue() + " ";
                            } else if (obj instanceof Object[]) {
                                Object[] objects = (Object[]) obj;
                                if (!objects[1].toString().matches("[1248]")) {
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
                    for (Object obj : matched) {
                        if (obj != null) {
                            if (obj instanceof Token) {
                                result += ((Token) obj).getValue();
                            } else if (obj instanceof Object[]) {
                                Object[] objects = (Object[]) obj;
                                if (!objects[0].toString().matches("[1248]")) {
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
                .action((Action<Object>) matched -> new Token(Token.HEX, matched.toString()))
                .alt(dec)
                .action((Action<Object>) matched -> new Token(Token.DEC, matched.toString()));

        /**
         * Prepare memory size directive addressing mode and variable
         * declaration types
         */
        this.memorySizeDirectives = new HashMap<>();
        int maxMemorySize = memory.getlookupPower() + 1;

        for (int i = 1; i < maxMemorySize; i++) {
            Double size = Math.pow(2, 2 + i);
            String sizeInString = String.valueOf(size.intValue());
            Grule sizeDirectiveInstance = lang.newGrule();
            sizeDirectiveInstance.define(getMemoryElement(sizeInString), memoryAddressingMode)
                    .action((Action<Object[]>) matched -> {
                        ArrayList<Token> tokenArrayList = new ArrayList<>();
                        for (Object obj : matched) {
                            if (obj instanceof String) {
                                String sizeDirectiveName = (String) obj;
                                Token sizeDirective = new Token(Token.MEM, sizeDirectiveName);
                                tokenArrayList.add(sizeDirective);
                            } else if (obj instanceof Token[]) {
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
         * variable declaration per variable type db, dw, dd, ...
         */

        ArrayList<Element> possibleValues = new ArrayList<>();
        possibleValues.add(hex);
        possibleValues.add(dec);
        possibleValues.add(stringLiteral);
        Element valueElement = concatenateOrSubRules(possibleValues);

//		variableDeclarations ::=  (variable)*
        variableDeclarations.define(justLabel, getAllVariableElements(), valueElement, CC.ks(comma, valueElement))
                .action((Action<Object[]>) matched -> {
                    Token labelName = (Token) matched[0];
                    Token dataType = (Token) matched[1];
                    Token value = (Token) matched[2];
                    Object[] moreValues = (Object[]) matched[3];
                    ArrayList<Token> valuesList = new ArrayList<>();
                    valuesList.add(value);

                    for (Object obj : moreValues) {
                        Object[] objectGroup = (Object[]) obj;
                        /**
                         * objectGroup[0] = comma objectGroup[1] = actual value
                         */
                        Token objectGroupValue = (Token) objectGroup[1];
                        valuesList.add(objectGroupValue);
                    }
                    try {
                        memory.putToVariableMap(labelName.getValue(), dataType.getValue());
                    } catch (DuplicateVariableException e) {
                        exceptions.add(e);
                    }

                    for (Token token : valuesList) {
                        int declaredSize = memory.getPrefixHexSize(dataType);
                        int prefixSize = memory.getPrefixBitSize(dataType);
                        String tokenValue = token.getValue();
                        if (token.isHex()) {
                            if (tokenValue.length() > declaredSize) {
                                exceptions.add(new DataTypeMismatchException(labelName.getValue(),
                                        dataType.getValue(), tokenValue));
                            } else {
                                try {
                                    tokenValue = MemoryAddressCalculator.extend(tokenValue, prefixSize, "0");
                                    memory.write(memory.getVariablePointer(), tokenValue, prefixSize);
                                    memory.incrementVariablePointer(prefixSize);
                                } catch (MemoryWriteException e) {
                                    exceptions.add(e);
                                }
                            }
                        } else if (token.isStringLiteral()) {
//                            System.out.println(token.getValue() + " " + token.getType());
                            try {
                                byte[] bytes = tokenValue.getBytes("US-ASCII");
                                for (int i = 0; i < bytes.length; i++) {
                                    String asciiHexValue = String.format("%0" + declaredSize + "X", bytes[i]);
//                                    System.out.println(tokenValue.charAt(i) + " " + asciiHexValue);
                                    memory.write(memory.getVariablePointer(), asciiHexValue, prefixSize);
                                    memory.incrementVariablePointer(prefixSize);
                                }
                            } catch (Exception e) {
                                exceptions.add(e);
                            }

                        }
                    }

                    return null;
                });

        sectionDataRule.define(sectionData, CC.ks(variableDeclarations));

        // assembly ::= mainProgram variableDeclarations?
        assembly.define(CC.ks(commentPattern), mainProgram, CC.ks(commentPattern), CC.op(sectionDataRule));

        // Prepare <List of Instructions>
        Iterator<String[]> instructionProductionRules = this.instructions.getInstructionProductionRules();
        ArrayList<Alternative> altList = new ArrayList<>();

        while (instructionProductionRules.hasNext()) {
            String[] prodRule = instructionProductionRules.next();

            for (int i = 0; i < prodRule.length; i++) {
                System.out.print(prodRule[i] + " ");
            }
            System.out.println("");

            String insName = "(\\b" + prodRule[0] + "\\b)|(\\b" + prodRule[0].toLowerCase() + "\\b)";

            if (instructions.isConditionalInstruction(prodRule[0])) {
                insName = "(\\b((" + prodRule[0] + "|" + prodRule[0].toLowerCase()
                        + ")(" + instructions.getConditionsRegEx() + "))\\b)";
            }

            TokenDef instructionName = lang.newToken(insName);

            //System.out.println(insName);
            // prodRule[2] * 2 includes the actual instruction name and the commas
            int parameterCount = Integer.parseInt(prodRule[4]);
            int numParameters = parameterCount * 2;
            if (numParameters == 0) {
                numParameters += 1;
            }

//			System.out.println("actual count from csv: " + parameterCount);
//			System.out.println("number of parameters: " + numParameters);
            ArrayList<String> result = new ArrayList<>();

            Element[] elements = new Element[numParameters];
            elements[0] = instructionName;
            int specificationsCounter = 5; // csv index starts at 5
            for (int k = 1; k < numParameters; k++) {
                if (k % 2 == 0) {
                    elements[k] = comma;
                } else {
                    String[] parameterSpecifications = prodRule[specificationsCounter].split("/");
                    boolean enforceSizeDirectives = true;
                    if (parameterCount >= 2) {
                        enforceSizeDirectives = false;
                        if (parameterCount == 3 && k == numParameters - 1) {
                            enforceSizeDirectives = true;
                        }
//                        else if (prodRule[2].equals("0")) { // for movsx / movzx
//                            enforceSizeDirectives = true;
//                        }
                    }
                    elements[k] = parseOneParameter(parameterSpecifications, enforceSizeDirectives);
                    specificationsCounter++;
                }
            }

            if (parameterCount >= 2) {
                String[] firstParameter = prodRule[5].split("/");
                ArrayList<String[]> firstParameterList = new ArrayList<>();
                for (int i = 0; i < firstParameter.length; i++) {
                    String[] reformatted = expandInstructionParameter(firstParameter[i]);
                    firstParameterList.add(reformatted);
                }
                ArrayList<String> specifications1 = new ArrayList<>();
                for (String[] entry : firstParameterList) {
                    for (int i = 0; i < entry.length; i++) {
                        specifications1.add(entry[i]);
                    }
                }
//				System.out.println(specifications1);

                String[] secondParameter = prodRule[6].split("/");
                ArrayList<String[]> secondParameterList = new ArrayList<>();
                for (int i = 0; i < secondParameter.length; i++) {
                    String[] reformatted = expandInstructionParameter(secondParameter[i]);
                    secondParameterList.add(reformatted);
                }
                ArrayList<String> specifications2 = new ArrayList<>();
                for (String[] entry : secondParameterList) {
                    for (int i = 0; i < entry.length; i++) {
                        specifications2.add(entry[i]);
                    }
                }
//				System.out.println(specifications2);

                for (String first : specifications1) {
                    for (String second : specifications2) {
                        if (isPermissible(first, second)) {
                            String resultInstance = first + "/" + second;
                            result.add(resultInstance);
                        }
                    }
                }
//				System.out.println(result);
            }

            // bind action from BSH files
            Alternative instructionAlternative = new Alternative(elements);
            if (numParameters == 1) {
                instructionAlternative.setAction((Action<Object>) args -> {
                    String anInstruction = (String) args;
                    //////////////////////
                    Instruction someInstruction = instructions.getInstruction(anInstruction);
                    CALVISInstruction calvisInstruction
                            = new CALVISInstruction(someInstruction, anInstruction, registers, memory);
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
                    boolean isConditionalInstruction = false;
                    String baseConditionalInstruction = instructions.getBaseConditionalInstruction(anInstruction);

                    if (!anInstruction.equals(baseConditionalInstruction)) {
                        String replaced = anInstruction.replaceAll(baseConditionalInstruction, "");
                        replaced = replaced.replaceAll(baseConditionalInstruction.toUpperCase(), "");
                        tokenArr.add(replaced);
                        anInstruction = baseConditionalInstruction;
                        isConditionalInstruction = true;
                    }
                    //////////////////////
                    Instruction someInstruction = instructions.getInstruction(anInstruction);
                    for (int i = 0; i < numParameters1; i++) {
                        tokenArr.add(args[i * 2 + 1]);
                    }
                    Object[] tokens = new Object[tokenArr.size()];
                    tokens = tokenArr.toArray(tokens);
                    CALVISInstruction calvisInstruction
                                = new CALVISInstruction(someInstruction, anInstruction,
                                        tokens, registers, memory, isConditionalInstruction, result);

                    // Insert special check if instruction should be verified
                    if (prodRule[2].equals("1")) {
                        calvisInstruction.setVerifiable(true);
                    } else {
                        calvisInstruction.setVerifiable(false);
                    }
                    String instructionAdd = Integer.toHexString(lineNumber);
                    mappedInstruction.put(MemoryAddressCalculator.extend(instructionAdd,
                            RegisterList.instructionPointerSize, "0"), calvisInstruction);
                    lineNumber++;
                    return calvisInstruction;
                });
            }
            altList.add(instructionAlternative);
        }

        // instruction ::= <List of Instructions>
        System.out.println("Total number of instructions loaded: " + altList.size());
        instruction.setAlts(altList);

        // mainProgram ::= ( LABEL? instruction)*
        mainProgram.define(CC.ks(CC.ks(commentPattern), CC.op(label), instruction))
                .action((Action<Object[]>) matched -> {
                    int labelValue = 0;
                    for (Object obj : matched) {
//					System.out.println("LINE: " + labelValue);
                        Object[] objects = (Object[]) obj;
                        for (int i = 0; i < objects.length; i++) {
//						System.out.println(i + " " + objects[i]);
                            if (objects[i] != null) {
                                if (i == 1 && objects[i] instanceof Object[]) {
//								System.out.println("we found an object array");
                                    Object[] objects1 = (Object[]) objects[i];
                                    /**
                                     * objects1[0] = contains label objects1[1]
                                     * = contains the colon
                                     */
                                    String instructionAddress = Integer.toHexString(labelValue);
                                    instructionAddress = Memory.reformatAddress(instructionAddress);
                                    String label = ((Token) objects1[0]).getValue();
                                    if (!memory.containsLabel(label)) {
                                        memory.putToLabelMap(((Token) objects1[0]).getValue(), instructionAddress);
                                    } else {
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
//        System.out.println("PARSER IS BEING COMPILED");
//        LocalDateTime timePoint = LocalDateTime.now();     // The current date and time
//        System.out.println(timePoint);
        exe = lang.compile();

//        LocalDateTime endPoint = LocalDateTime.now();     // The current date and time
//        System.out.println(endPoint);

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
        if (parameterSpecification.matches("[rm]")) {
            if (parameterSpecification.equals("m")) {
                maxSize = Memory.MAX_ADDRESS_SIZE / 8;
            }
            for (int i = 1; i < maxSize; i++) {
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

    private boolean isPermissible(String first, String second) {
        if (first.contains("m") && second.contains("m")) {
            return false;
        }
        if (first.substring(1).equals(second.substring(1))) {
            return true;
        }
        if (second.matches("[ci]")) {
            return true;
        }
        return false;
    }

    private Element parseOneParameter(String[] specification, boolean isOneParameter) {
        // get specific operand requirements
        String[] allowed = specification;
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
                if (isOneParameter) {
                    String size = anAllowed.substring(1);
                    if (size.length() > 1) {
                        orSubRuleList.add(memorySizeDirectives.get(size));
                    } else {
                        orSubRuleList.add(getAllMemorySizeDirectiveElements());
                    }
                } else {
                    orSubRuleList.add(memoryAddressingMode);
                    orSubRuleList.add(getAllMemorySizeDirectiveElements());
                }
            }
            if (anAllowed.contains("i")) {
                orSubRuleList.add(hex);
                orSubRuleList.add(dec);
//				orSubRuleList.add(hexOrDecimal);
            }
            if (anAllowed.contains("c")) {
                orSubRuleList.add(cl);
            }
            if (anAllowed.contains("l")) {
                orSubRuleList.add(justLabel);
            }
        }
        return concatenateOrSubRules(orSubRuleList);
    }

    private void prepareMemorySizeDirectives() {
        this.memoryTokenMap = new HashMap<>();
        this.variableDeclarationTokenMap = new HashMap<>();

        Iterator<String[]> memoryTokens = this.memory.getLookup();

        while (memoryTokens.hasNext()) {
            String[] memoryToken = memoryTokens.next();
            String sizeDirectiveName = memoryToken[Memory.SIZE_DIRECTIVE_NAME];
            String sizeDirectiveSize = memoryToken[Memory.SIZE_DIRECTIVE_SIZE];
            String sizeDirectivePrefix = memoryToken[Memory.SIZE_DIRECTIVE_PREFIX];

            String sizeDirectivePattern = "(\\b" + sizeDirectiveName + "\\b)"
                    + "|(\\b" + sizeDirectiveName.toUpperCase() + "\\b)";
            TokenDef memorySizeDirective = lang.newToken(sizeDirectivePattern);
            this.memoryTokenMap.put(sizeDirectiveSize, memorySizeDirective);

            String variableTypePattern = "\\b(" + sizeDirectivePrefix + "|"
                    + sizeDirectivePrefix.toUpperCase() + ")\\b";
            TokenDef variableType = lang.newToken(variableTypePattern);
            this.variableDeclarationTokenMap.put(sizeDirectiveSize, variableType);
        }

//		System.out.println(memoryTokenMap);
    }

    private void prepareRegisterTokenMap() {
        this.registerTokenMap = new HashMap<>();
        Iterator<String[]> registerTokens = this.registers.getRegisterList();
        boolean flag = false;
        while (registerTokens.hasNext()) {
            String[] registerToken = registerTokens.next();
            String regName = "(\\b" + registerToken[RegisterList.NAME] + "\\b)"
                    + "|(\\b" + registerToken[RegisterList.NAME].toLowerCase() + "\\b)";
            String regSize = registerToken[RegisterList.SIZE];
            String regType = registerToken[RegisterList.TYPE];

            if (!flag && registerToken[RegisterList.NAME].equalsIgnoreCase("cl")) {
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
            if (registerTokenMap.containsKey(regType)) {
                ArrayList<Element> altExist = registerTokenMap.get(regType);
                altExist.add(registerName);
            } else {
                ArrayList<Element> altNotExist = new ArrayList<>();
                altNotExist.add(registerName);
                registerTokenMap.put(regType, altNotExist);
            }

            // regSize = in bits, like 8, 16, 32
            if (registerTokenMap.containsKey(regSize)) {
                ArrayList<Element> altExist = registerTokenMap.get(regSize);
                altExist.add(registerName);
            } else {
                ArrayList<Element> altNotExist = new ArrayList<>();
                altNotExist.add(registerName);
                registerTokenMap.put(regSize, altNotExist);
            }
        }
    }

    private Element getAllMemorySizeDirectiveElements() {
        Iterator<String> keys = memorySizeDirectives.keySet().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while (keys.hasNext()) {
            String key = keys.next();
            Element temp = memorySizeDirectives.get(key);
            list.add(temp);
        }
        return concatenateOrSubRules(list);
    }

    private Element getAllRegisterElements() {
        Iterator<String> keys = registerTokenMap.keySet().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while (keys.hasNext()) {
            String key = keys.next();
            // we don't want duplicates, so just get registers depending on size.
            if (Integer.parseInt(key) > 4) {
                Element temp = getRegisterElements(key);
                list.add(temp);
            }
        }
        return concatenateOrSubRules(list);
    }

    private Element concatenateOrSubRules(ArrayList<Element> list) {
        Element result = null;
        OrSubRule osb = null;
        if (!list.isEmpty()) {
            result = list.get(0);
            if (list.size() >= 2) {
                if (list.get(1).getClass().equals(OrSubRule.class)) {
                    osb = ((OrSubRule) list.get(1)).or(result);
                } else if (list.get(1).getClass().equals(TokenDef.class)) {
                    osb = ((TokenDef) list.get(1)).or(result);
                } else if (list.get(1).getClass().equals(Grule.class)) {
                    osb = ((Grule) list.get(1)).or(result);
                }
                for (int i = 2; i < list.size(); i++) {
                    osb.or(list.get(i));
                }

                List<Alternative> producedList = osb.getAlts();
                for (int i = 0; i < producedList.size(); i++) {
                    producedList.get(i).setAction((Action<Object>) arg0 -> {
//						System.out.println(arg0 + " : " + arg0.getClass());
                        if (arg0 instanceof Token) {
                            return (Token) arg0;
                        }
                        if (registers.isExisting(arg0.toString().toUpperCase())) {
                            return new Token(Token.REG, (String) arg0);
                        }
                        if (memory.isExisting(arg0.toString())) {
                            return new Token(Token.MEM, (String) arg0);
                        }
                        if (arg0.toString().matches(hexPattern)) {
                            return new Token(Token.HEX, (String) arg0);
                        }
                        if (arg0.toString().matches(decPattern)) {
                            return new Token(Token.DEC, (String) arg0);
                        }
                        if (arg0 instanceof Token[]) {
                            return arg0;
                        }
                        if (arg0.toString().matches("[a-zA-Z_][a-zA-Z\\d_]*")) {
                            return new Token(Token.LABEL, (String) arg0);
                        }
                        if (arg0.toString().matches(stringLiteralPattern)) {
                            return new Token(Token.STRING, (String) arg0);
                        }

                        return new Token(Token.REG, (String) arg0);
                    });
                }
                return osb;
            }
        }
        return result;
    }

    private Element getMemoryIndexScalableElements() {
        ArrayList<Element> list = registerTokenMap.get("1");
        ArrayList<Element> result = new ArrayList<>();
        for (Element x : list) {
            TokenDef y = (TokenDef) x;
            // we remove ESP and SP as index scalable registers for memory addressing mode
            if (!y.getRegexp().contains("SP")) {
                result.add(x);
            }
            //System.out.println(y.getRegexp());
        }
        return concatenateOrSubRules(result);
    }

    private Element getAllMemoryAddressableRegisters() {
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
        while (iterator.hasNext()) {
            memoryElementsList.add(iterator.next());
        }
        return concatenateOrSubRules(memoryElementsList);
    }

    private Element getAllVariableElements() {
        Iterator<Element> keys = variableDeclarationTokenMap.values().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while (keys.hasNext()) {
            list.add(keys.next());
        }
        return concatenateOrSubRules(list);
    }

    public HashMap<String, CALVISInstruction> parse(String code) throws Exception {
        this.mappedInstruction.clear();
        this.exceptions.clear();
        this.lineNumber = 0;
        this.exe.eval(code);
        if (!exceptions.isEmpty()) {
            throw exceptions.get(0);
        }
        return this.mappedInstruction;
    }

}
