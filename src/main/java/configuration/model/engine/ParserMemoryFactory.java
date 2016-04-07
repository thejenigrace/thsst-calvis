package configuration.model.engine;

import com.github.pfmiles.dropincc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public class ParserMemoryFactory {

    private Memory memory;
    private Lang lang;
    private ElementConcatenator elementConcatenator;

    private HashMap<String, Element> memoryTokenMap;
    private HashMap<String, Element> variableDeclarationTokenMap;
    private HashMap<String, Grule> memorySizeDirectives;

    private TokenBag tokenBag;
    private Element allRegisters;
    private Element memoryPermissibleRegisters;
    private Element memoryIndexScalableRegisters;

    private Grule memoryAddressingMode;

    public ParserMemoryFactory(Memory memory, Lang lang, ElementConcatenator elementConcatenator, TokenBag tokenBag,
                               Element allRegisters, Element memoryPermissibleRegisters,
                               Element memoryIndexScalableRegisters) {
        this.memory = memory;
        this.lang = lang;
        this.elementConcatenator = elementConcatenator;
        this.tokenBag = tokenBag;
        this.allRegisters = allRegisters;
        this.memoryPermissibleRegisters = memoryPermissibleRegisters;
        this.memoryIndexScalableRegisters = memoryIndexScalableRegisters;

        prepareMemorySizeDirectives();
        createMemoryGrules();
    }

    public Grule getMemoryAddressingMode() {
        return memoryAddressingMode;
    }

    public Element getAllMemorySizeDirectiveElements() {
        Iterator<String> keys = memorySizeDirectives.keySet().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while ( keys.hasNext() ) {
            String key = keys.next();
            Element temp = memorySizeDirectives.get(key);
            list.add(temp);
        }
        return elementConcatenator.concatenateOrSubRules(list);
    }

    public HashMap<String, Element> getVariableDeclarationTokenMap() {
        return this.variableDeclarationTokenMap;
    }

    private void prepareMemorySizeDirectives() {
        this.memoryTokenMap = new HashMap<>();
        this.variableDeclarationTokenMap = new HashMap<>();

        Iterator<String[]> memoryTokens = this.memory.getLookup();

        while ( memoryTokens.hasNext() ) {
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
    }

    private void createMemoryGrules() {
        this.memoryAddressingMode = lang.newGrule();

        Grule memoryExpression = lang.newGrule();
        Grule memoryBase = lang.newGrule();
        Grule memoryIndex = lang.newGrule();
        Grule memoryDisplacement = lang.newGrule();

        TokenDef lsb = lang.newToken("\\[");
        TokenDef rsb = lang.newToken("\\]");
        TokenDef plus = lang.newToken("\\+");
        TokenDef times = lang.newToken("\\*");

        // memory addressing mode constructs
        // memory ::= [ memoryExpr ]
        memoryAddressingMode.define(lsb, memoryExpression, rsb)
                .action((Action<Object[]>) matched -> {
                    Token[] mem = (Token[]) matched[1];
                    return mem;
                });

        //memoryExpression ::= base + index * scale + displacement
        memoryExpression.define(memoryBase, CC.op(plus, memoryIndex), CC.op(plus.or("\\-"), memoryDisplacement))
                .action((Action<Object[]>) matched -> {
                    ArrayList<Token> tokenArrayList = new ArrayList<>();
                    for ( Object obj : matched ) {
                        if ( obj != null ) {
                            if ( obj instanceof Token ) {
                                tokenArrayList.add((Token) obj);
                            } else if ( obj instanceof Object[] ) {
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

        memoryBase.define(memoryPermissibleRegisters)
                .action((Action<Object>) matched -> {
                    Token t = (Token) matched;
                    return t;
                })
                .alt(tokenBag.hex())
                .action((Action<Object>) matched -> new Token(Token.HEX, matched.toString()))
                .alt(tokenBag.dec())
                .action((Action<Object>) matched -> new Token(Token.DEC, matched.toString()))
                .alt(tokenBag.justLabel())
                .action((Action<Object>) matched -> (Token) matched);

        memoryIndex.define(memoryIndexScalableRegisters, CC.op(times, tokenBag.dec()))
                .action((Action<Object[]>) matched -> {
                    String result = "";
                    for ( Object obj : matched ) {
                        if ( obj != null ) {
                            if ( obj instanceof Token ) {
                                result += ((Token) obj).getValue() + " ";
                            } else if ( obj instanceof Object[] ) {
                                Object[] objects = (Object[]) obj;
                                if ( !objects[1].toString().matches("[1248]") ) {
                                    throw new DropinccException("Invalid scale value");
                                }
                                String optionalTimes = objects[0] + " " + objects[1];
                                result += optionalTimes;
                            }
                        }
                    }
                    return new Token(Token.REG, result);
                })
                .alt(CC.op(tokenBag.dec(), times), memoryIndexScalableRegisters)
                .action((Action<Object[]>) matched -> {
                    String result = "";
                    for ( Object obj : matched ) {
                        if ( obj != null ) {
                            if ( obj instanceof Token ) {
                                result += ((Token) obj).getValue();
                            } else if ( obj instanceof Object[] ) {
                                Object[] objects = (Object[]) obj;
                                if ( !objects[0].toString().matches("[1248]") ) {
                                    throw new DropinccException("Invalid scale value");
                                }
                                String optionalTimes = objects[0] + " " + objects[1] + " ";
                                result += optionalTimes;
                            }
                        }
                    }
                    return new Token(Token.REG, result);
                });

        memoryDisplacement.define(tokenBag.hex())
                .action((Action<Object>) matched -> new Token(Token.HEX, matched.toString()))
                .alt(tokenBag.dec())
                .action((Action<Object>) matched -> new Token(Token.DEC, matched.toString()));
        /**
         * Prepare memory size directive addressing mode
         */
        this.memorySizeDirectives = new HashMap<>();

        Iterator<String[]> memoryTokens = this.memory.getLookup();

        while ( memoryTokens.hasNext() ) {
            String[] memoryToken = memoryTokens.next();
            String sizeDirectiveSize = memoryToken[Memory.SIZE_DIRECTIVE_SIZE];
            Grule sizeDirectiveInstance = lang.newGrule();
            sizeDirectiveInstance.define(getMemoryElement(sizeDirectiveSize), memoryAddressingMode)
                    .action((Action<Object[]>) matched -> {
                        ArrayList<Token> tokenArrayList = new ArrayList<>();
                        for ( Object obj : matched ) {
                            if ( obj instanceof String ) {
                                String sizeDirectiveName = (String) obj;
                                Token sizeDirective = new Token(Token.MEM, sizeDirectiveName);
                                tokenArrayList.add(sizeDirective);
                            } else if ( obj instanceof Token[] ) {
                                Token[] tokens = (Token[]) obj;
                                for ( int k = 0; k < tokens.length; k++ ) {
                                    tokenArrayList.add(tokens[k]);
                                }
                            }
                        }
                        Token[] appendedTokens = new Token[tokenArrayList.size()];
                        appendedTokens = tokenArrayList.toArray(appendedTokens);
                        return appendedTokens;
                    });

            this.memorySizeDirectives.put(sizeDirectiveSize, sizeDirectiveInstance);
        }
    }

    private Element getMemoryElement(String size) {
        return memoryTokenMap.get(size);
    }

}
