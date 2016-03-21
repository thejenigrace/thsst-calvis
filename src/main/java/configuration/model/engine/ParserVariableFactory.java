package configuration.model.engine;

import com.github.pfmiles.dropincc.*;

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
		TokenDef floatingPoint = lang.newToken(PatternList.floatingPointPattern);
		TokenDef comma = lang.newToken(",");

		ArrayList<Element> possibleValues = new ArrayList<>();
		possibleValues.add(tokenBag.hex());
		possibleValues.add(tokenBag.dec());
		possibleValues.add(stringLiteral);
		possibleValues.add(floatingPoint);
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
//                            System.out.println(token.getValue() + " " + token.getType());
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

						}
					}

					return null;
				});
	}

	private Element getAllVariableElements() {
		Iterator<Element> keys = variableDeclarationTokenMap.values().iterator();
		ArrayList<Element> list = new ArrayList<>();
		while ( keys.hasNext() ) {
			list.add(keys.next());
		}
		return elementConcatenator.concatenateOrSubRules(list);
	}

	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}

	public Grule getVariableDeclarationsGrule() {
		return this.variableDeclarations;
	}
}
