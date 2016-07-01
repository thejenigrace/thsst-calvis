package configuration.model.engine;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.CC;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;
import configuration.model.exceptions.DataTypeMismatchException;
import configuration.model.exceptions.DuplicateVariableException;
import configuration.model.exceptions.MemoryWriteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public class ParserVariableFactory {

    private Memory memory;
    private Lang lang;
    private ElementConcatenator elementConcatenator;
    private TokenBag tokenBag;
    private HashMap<String, Element> variableDeclarationTokenMap;

    private ArrayList<Exception> exceptions;

    private Grule variableDeclarations;

    public ParserVariableFactory(Memory memory, Lang lang, ElementConcatenator elementConcatenator, TokenBag tokenBag,
                                 ArrayList<Exception> exceptions, HashMap<String, Element> variableDeclarationTokenMap) {
        this.memory = memory;
        this.lang = lang;
        this.elementConcatenator = elementConcatenator;
        this.tokenBag = tokenBag;
        this.variableDeclarationTokenMap = variableDeclarationTokenMap;
        this.exceptions = exceptions;

        createVariableGrules();
    }

    private void createVariableGrules() {
        TokenDef stringLiteral = lang.newToken(PatternList.stringLiteralPattern);
        TokenDef comma = lang.newToken(",");

        Grule negateDecimal = lang.newGrule();
        negateDecimal.define(CC.op("\\-"), tokenBag.dec())
                .action((Action<Object[]>) matched -> new Token(Token.DEC, (String) matched[1]));

        ArrayList<Element> possibleValues = new ArrayList<>();
        possibleValues.add(tokenBag.hex());
        possibleValues.add(stringLiteral);
        possibleValues.add(tokenBag.floating());
        possibleValues.add(negateDecimal);
        Element valueElement = elementConcatenator.concatenateOrSubRules(possibleValues);

        this.variableDeclarations = lang.newGrule();

//		variableDeclarations ::=  (variable)*
        this.variableDeclarations.define(tokenBag.justLabel(), getAllVariableElements(),
                valueElement, CC.ks(comma, valueElement))
                .action((Action<Object[]>) matched -> {
                    Token labelName = (Token) matched[0];
                    Token dataType = (Token) matched[1];
                    Token value = (Token) matched[2];
                    Object[] moreValues = (Object[]) matched[3];
                    ArrayList<Token> valuesList = new ArrayList<>();
                    valuesList.add(value);

                    for ( Object obj : moreValues ) {
                        Object[] objectGroup = (Object[]) obj;
                        /**
                         * objectGroup[0] = comma objectGroup[1] = actual value
                         */
                        Token objectGroupValue = (Token) objectGroup[1];
                        valuesList.add(objectGroupValue);
                    }
                    try {
                        memory.putToVariableMap(labelName.getValue(), dataType.getValue());
                    } catch ( DuplicateVariableException e ) {
                        exceptions.add(e);
                    }

                    for ( Token token : valuesList ) {
                        int declaredSize = memory.getPrefixHexSize(dataType);
                        int prefixSize = memory.getPrefixBitSize(dataType);
                        String tokenValue = token.getValue();
                        if ( token.isHex() ) {
                            if ( tokenValue.length() > declaredSize ) {
                                exceptions.add(new DataTypeMismatchException(labelName.getValue(),
                                        dataType.getValue(), tokenValue));
                            } else {
                                try {
                                    tokenValue = MemoryAddressCalculator.extend(tokenValue, prefixSize, "0");
                                    memory.write(memory.getVariablePointer(), tokenValue, prefixSize);
                                    memory.incrementVariablePointer(prefixSize);
                                } catch ( MemoryWriteException e ) {
                                    exceptions.add(e);
                                }
                            }
                        } else if ( token.isStringLiteral() ) {
                            try {
                                byte[] bytes = tokenValue.getBytes("US-ASCII");
                                for ( int i = 0; i < bytes.length; i++ ) {
                                    String asciiHexValue = String.format("%0" + declaredSize + "X", bytes[i]);
//                                    System.out.println(tokenValue.charAt(i) + " " + asciiHexValue);
                                    memory.write(memory.getVariablePointer(), asciiHexValue, prefixSize);
                                    memory.incrementVariablePointer(prefixSize);
                                }
                            } catch ( Exception e ) {
                                exceptions.add(e);
                            }
                        } else if ( token.isFloatLiteral() ) {
                            if ( prefixSize >= 32 ) {
                                String representation = "";
                                switch ( prefixSize ) {
                                    case 32:
                                        // declare as 32 bit IEEE single precision
                                        float floatValue = Float.parseFloat(tokenValue);
                                        representation = Integer.toHexString(Float.floatToIntBits(floatValue));
                                        break;
                                    case 64:
                                        // declare as 64 bit IEEE double precision
                                        Double doubleValue = Double.parseDouble(tokenValue);
                                        representation = Long.toHexString(Double.doubleToLongBits(doubleValue));
                                        break;
                                    case 80:
                                        // declare as 80 bit extended precision

                                        // NOT YET IMPLEMENTED
                                        Double doubleValue2 = Double.parseDouble(tokenValue);
                                        representation = "0000" + Long.toHexString(Double.doubleToLongBits
                                                (doubleValue2));
                                        break;
                                    default:
                                        exceptions.add(new DataTypeMismatchException(labelName.getValue(),
                                                dataType.getValue(), tokenValue));
                                        break;
                                }

                                try {
                                    memory.write(memory.getVariablePointer(), representation, prefixSize);
                                    memory.incrementVariablePointer(prefixSize);
                                } catch ( MemoryWriteException e ) {
                                    exceptions.add(e);
                                }
                            } else {
                                exceptions.add(new DataTypeMismatchException(labelName.getValue(),
                                        dataType.getValue(), tokenValue));
                            }
                        }
                    }

                    return null;
                });
    }

    public ArrayList<Exception> getExceptions() {
        return exceptions;
    }

    public Grule getVariableDeclarationsGrule() {
        return this.variableDeclarations;
    }

    private Element getAllVariableElements() {
        Iterator<Element> keys = variableDeclarationTokenMap.values().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while ( keys.hasNext() ) {
            list.add(keys.next());
        }
        return elementConcatenator.concatenateOrSubRules(list);
    }
}
