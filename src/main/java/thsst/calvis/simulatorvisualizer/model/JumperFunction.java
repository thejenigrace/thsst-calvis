package thsst.calvis.simulatorvisualizer.model;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Ivan on 7/11/2016.
 */
public class JumperFunction{
	private RegisterList registers;
	private Memory memory;

	public JumperFunction(RegisterList registers, Memory memory){
		this.registers = registers;
		this.memory = memory;
	}

	public boolean computeJump(String cc, String[] flagValues) {
		String ccConvert = cc.toUpperCase();
		switch(ccConvert){
			case "JO":
			case "JS":
			case "JE":
			case "JZ":
			case "JP":
			case "JPE":
			case "JB":
			case "JNAE":
			case "JC":
				if(flagValues[0].equals("1"))
					return true;
				break;
			case "JNO":
			case "JNS":
			case "JNE":
			case "JNZ":
			case "JNB":
			case "JAE":
			case "JNC":
			case "JNP":
			case "JPO":
			case "JCXZ":
			case "JECXZ":
				return flagValues[0].equals("0");
			case "JG":
			case "JNLE":
				return flagValues[0].equals("0") && (flagValues[1].equals(flagValues[2]));
			case "JBE":
			case "JNA":
				return flagValues[0].equals("1") || (flagValues[1].equals("1"));
			case "JA":
			case "JNBE":
				return flagValues[0].equals("0") && (flagValues[1].equals("0"));
			case "JL":
			case "JNGE":
				return ! flagValues[0].equals(flagValues[1]);
			case "LOOPE":
			case "LOOPZ":
				return new BigInteger(registers.get("ECX"), 16).compareTo(new BigInteger("0", 16)) == 1 && registers.getEFlags().getZeroFlag().equals("1");
			case "LOOPNE":
			case "LOOPNZ":
				return new BigInteger(registers.get("ECX"), 16).compareTo(new BigInteger("0", 16)) == 1 && registers.getEFlags().getZeroFlag().equals("0");
			case "LOOP":
				return new BigInteger(registers.get("ECX"), 16).compareTo(new BigInteger("0", 16)) == 1;
		}
		return false;
	}

	public ArrayList<String> getFlags(String cc){
		String ccConvert = cc.toUpperCase();
		ArrayList<String> returnables = new ArrayList<>();
		switch(ccConvert){
			case "JO":
				returnables.add(registers.getEFlags().getOverflowFlag());
				break;
			case "JS":
				returnables.add(registers.getEFlags().getSignFlag());
				break;
			case "JE":
			case "JZ":
				returnables.add(registers.getEFlags().getZeroFlag());
				break;
			case "JP":
			case "JPE":
				returnables.add(registers.getEFlags().getParityFlag());
				break;
			case "JB":
			case "JNAE":
			case "JC":
				returnables.add(registers.getEFlags().getCarryFlag());
				break;
			case "JNO":
				returnables.add(registers.getEFlags().getOverflowFlag());
				break;
			case "JNS":
				returnables.add(registers.getEFlags().getSignFlag());
				break;
			case "JNE":
			case "JNZ":
				returnables.add(registers.getEFlags().getZeroFlag());
				break;
			case "JNB":
			case "JAE":
			case "JNC":
				returnables.add(registers.getEFlags().getCarryFlag());
				break;
			case "JNP":
			case "JPO":
				returnables.add(registers.getEFlags().getParityFlag());
				break;
			case "JCXZ":
			case "JECXZ":
				if(registers.get("ECX").equals("00000000") && registers.get("CX").equals("0000"))
					returnables.add("0");
				break;
			case "JG":
			case "JNLE":
				returnables.add(registers.getEFlags().getZeroFlag());
				returnables.add(registers.getEFlags().getSignFlag());
				returnables.add(registers.getEFlags().getOverflowFlag());
				break;
			case "JBE ":
			case "JNA":
				returnables.add(registers.getEFlags().getCarryFlag());
				returnables.add(registers.getEFlags().getZeroFlag());
				break;
			case "JA":
			case "JNBE":
				returnables.add(registers.getEFlags().getCarryFlag());
				returnables.add(registers.getEFlags().getZeroFlag());
				break;
			case "JL":
			case "JNGE":
				returnables.add(registers.getEFlags().getSignFlag());
				returnables.add(registers.getEFlags().getOverflowFlag());
				break;

		}
		return returnables;
	}

	public String computeJumpText(boolean isTrue, String cc) {
		String ccConvert = cc.toUpperCase();
		switch(ccConvert){
			case "JO":
				if(isTrue){
					return "OF == 1";
				}
				else{
					return "OF != 1";
				}

			case "JS":
				if(isTrue){
					return "SF == 1";
				}
				else{
					return "SF != 1";
				}

			case "JE":
			case "JZ":
				if(isTrue){
					return "ZF == 1";
				}
				else{
					return "ZF != 1";
				}

			case "JP":
			case "JPE":
				if(isTrue){
					return "PF == 1";
				}
				else{
					return "PF != 1";
				}

			case "JB":
			case "JNAE":
			case "JC":
				if(isTrue){
					return "CF == 1";
				}
				else{
					return "CF != 1";
				}

			case "JNO":
				if(isTrue){
					return "OF == 0";
				}
				else{
					return "OF != 0";
				}

			case "JNS":
				if(isTrue){
					return "SF == 0";
				}
				else{
					return "SF != 0";
				}

			case "JNE":
			case "JNZ":
				if(isTrue){
					return "ZF == 0";
				}
				else{
					return "ZF != 0";
				}

			case "JNB":
			case "JAE":
			case "JNC":
				if(isTrue){
					return "CF == 0";
				}
				else{
					return "CF != 0";
				}

			case "JNP":
			case "JPO":
				if(isTrue){
					return "PF == 0";
				}
				else{
					return "PF != 0";
				}

			case "JCXZ":
				if(isTrue){
					return "JCXZ == 0x0000";
				}
				else{
					return "JCXZ != 0x0000";
				}

			case "JECXZ":
				if(isTrue){
					return "JECXZ == 0x00000000";
				}
				else{
					return "JECXZ != 0x00000000";
				}

			case "JG":
			case "JNLE":
				if(isTrue){
					return "ZF == 0 && SF == OF";
				}
				else{
					return "ZF == 1 || SF != OF";
				}

			case "JBE ":
			case "JNA":
				if(isTrue){
					return "CF == 1 || ZF == 1";
				}
				else{
					return "CF == 0 && ZF == 0";
				}

			case "JA":
			case "JNBE":
				if(isTrue){
					return "CF == 0 && ZF == 0";
				}
				else{
					return "CF == 1 || ZF == 1";
				}

			case "JL":
			case "JNGE":
				if(isTrue){
					return "SF != OF";
				}
				else{
					return "SF == OF";
				}
			case "LOOPE":
			case "LOOPZ":
				if(isTrue){
					return "ECX > 1 && ZF == 0";
				}
				else{
					return "ECX < 1 || ZF == 1";
				}
			case "LOOPNE":
			case "LOOPNZ":
				if(isTrue){
					return "ECX > 1 && ZF == 1";
				}
				else{
					return "ECX < 1 || ZF == 0";
				}
			case "LOOP":
				if(isTrue){
					return "ECX > 1";
				}
				else{
					return "ECX < 1";
				}

		}
		return null;
	}
}
