package configuration.model.engine;

import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Lang;
import com.github.pfmiles.dropincc.TokenDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public class ParserRegisterFactory {

    private RegisterList registers;
    private Lang lang;
    private ElementConcatenator elementConcatenator;

    private HashMap<String, ArrayList<Element>> registerTokenMap;

    public ParserRegisterFactory(RegisterList registers, Lang lang, ElementConcatenator elementConcatenator) {
        this.registers = registers;
        this.lang = lang;
        this.elementConcatenator = elementConcatenator;

        createRegisterTokens();
    }

    public void createRegisterTokens() {
        this.registerTokenMap = new HashMap<>();
        Iterator<String[]> registerTokens = this.registers.getRegisterList();
        while ( registerTokens.hasNext() ) {
            String[] registerToken = registerTokens.next();
            String regName = "(\\b" + registerToken[RegisterList.NAME] + "\\b)"
                    + "|(\\b" + registerToken[RegisterList.NAME].toLowerCase() + "\\b)";
            String regType = registerToken[RegisterList.TYPE];

            TokenDef registerName = lang.newToken(regName);
            /*
                 regType 1 = memory addressable
				 regType 2 = not memory addressable
				 regType 3 = instruction pointer
             */
            if ( registerTokenMap.containsKey(regType) ) {
                ArrayList<Element> altExist = registerTokenMap.get(regType);
                altExist.add(registerName);
            } else {
                ArrayList<Element> altNotExist = new ArrayList<>();
                altNotExist.add(registerName);
                registerTokenMap.put(regType, altNotExist);
            }
        }
    }

    public Element getMemoryIndexScalableElements() {
        ArrayList<Element> list = registerTokenMap.get("1");
        ArrayList<Element> result = new ArrayList<>();
        for ( Element x : list ) {
            TokenDef y = (TokenDef) x;
            // we remove ESP and SP as index scalable registers for memory addressing mode
            if ( !y.getRegexp().contains("SP") ) {
                result.add(x);
            }
        }
        return elementConcatenator.concatenateOrSubRules(result);
    }

    public Element getMemoryAddressableRegisters() {
        return getRegisterElements("1");
    }

    public Element getAllRegisterElements() {
        Iterator<String> keys = registerTokenMap.keySet().iterator();
        ArrayList<Element> list = new ArrayList<>();
        while ( keys.hasNext() ) {
            String key = keys.next();
            Element temp = getRegisterElements(key);
            list.add(temp);
        }
        return elementConcatenator.concatenateOrSubRules(list);
    }

    private Element getRegisterElements(String string) {
        ArrayList<Element> list = registerTokenMap.get(string);
        return elementConcatenator.concatenateOrSubRules(list);
    }

}
