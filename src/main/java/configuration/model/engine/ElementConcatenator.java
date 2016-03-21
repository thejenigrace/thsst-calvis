package configuration.model.engine;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.Element;
import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;
import com.github.pfmiles.dropincc.impl.Alternative;
import com.github.pfmiles.dropincc.impl.OrSubRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public class ElementConcatenator {

	private RegisterList registers;
	private Memory memory;

	public ElementConcatenator(RegisterList registers, Memory memory) {
		this.registers = registers;
		this.memory = memory;
	}

	public Element concatenateOrSubRules(ArrayList<Element> list) {
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
						if (arg0 instanceof Token[]) {
							return arg0;
						}
						if (registers.isExisting(arg0.toString())) {
							return new Token(Token.REG, (String) arg0);
						}
						if (memory.isExisting(arg0.toString())) {
							return new Token(Token.MEM, (String) arg0);
						}
						if (arg0.toString().matches(PatternList.hexPattern)) {
							return new Token(Token.HEX, (String) arg0);
						}
						if (arg0.toString().matches(PatternList.decPattern)) {
							return new Token(Token.DEC, (String) arg0);
						}
						if (arg0.toString().matches(PatternList.labelPattern)) {
							return new Token(Token.LABEL, (String) arg0);
						}
						if (arg0.toString().matches(PatternList.stringLiteralPattern)) {
							return new Token(Token.STRING, (String) arg0);
						}
						if (arg0.toString().matches(PatternList.floatingPointPattern)) {
							return new Token(Token.FLOAT, (String) arg0);
						}
						return new Token(Token.REG, (String) arg0);
					});
				}
				return osb;
			}
		}
		return result;
	}
}
