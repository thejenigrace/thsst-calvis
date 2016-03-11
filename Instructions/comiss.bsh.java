execute(s,t,registers,memory) {
  int DWORD = 32;
  Calculator calculator = new Calculator(registers,memory);
  EFlags eFlags = registers.getEFlags();
  String sValue = registers.get(s);
  String tValue;
  if(t.isRegister()) {
    tValue = registers.get(t);
    tValue = calculator.cutToCertainHexSize("getLower",tValue,DWORD/4);
  } else if(t.isMemory()) {
    tValue = memory.read(t,DWORD);
  }

  // Get the sValue from 0 to 31 bit
  sValue = calculator.cutToCertainHexSize("getLower",sValue,DWORD/4);

  float floatS = calculator.convertToSinglePrecisionFloatingPoint(sValue);
  float floatT = calculator.convertToSinglePrecisionFloatingPoint(tValue);

  if(Float.isNaN(floatS) || Float.isNaN(floatT)) {
    eFlags.setZeroFlag("1");
    eFlags.setParityFlag("1");
    eFlags.setCarryFlag("1");
  } else if(floatS > floatT) {
    eFlags.setZeroFlag("0");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("0");
  } else if(floatS < floatT) {
    eFlags.setZeroFlag("0");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("1");
  } else if(floatS == floatT) {
    eFlags.setZeroFlag("1");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("0");
  }

  eFlags.setOverflowFlag("0");
  eFlags.setAuxiliaryFlag("0");
  eFlags.setSignFlag("0");
}
