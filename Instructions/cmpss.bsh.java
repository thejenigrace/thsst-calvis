execute(des,src,op3,registers,memory) {
  int DWORD = 32;
  String desValue = registers.get(des);
  String srcValue;
  Calculator calculator = new Calculator(registers,memory);
  if(src.isRegister()) {
    srcValue = registers.get(src);
    srcValue = calculator.cutToCertainHexSize("getLower",srcValue,DWORD/4);
  } else if(src.isMemory()) {
    srcValue = memory.read(src,DWORD);
  }

  String desNewValue = calculator.cutToCertainHexSize("getUpper",desValue,DWORD*3/4);

  // Get the desValue from 0 to 31 bit
  desValue = calculator.cutToCertainHexSize("getLower",desValue,DWORD/4);

  float floatDes = calculator.convertHexToSinglePrecision(desValue);
  float floatSrc = calculator.convertHexToSinglePrecision(srcValue);

  System.out.println("d: " + floatDes + " _ s: " + floatSrc);

  String operand = op3.getValue();
  int intOperand = Integer.parseInt(operand,16);
  System.out.println("intOperand = " + intOperand);

  int retval = Float.compare(floatDes, floatSrc);
  System.out.println("retval = " + retval);
  switch(intOperand) {
    case 0:
      // des == src
      if(retval == 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 1:
      // des < src
      if(retval < 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 2:
      // des <= src
      if(retval < 0 || retval == 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 3:
      if(floatDes.isNaN() || floatSrc.isNaN())
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 4:
      // des != src
      if(retval != 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 5:
      // des >= src
      if(retval > 0 || retval == 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 6:
      // des > src
      if(retval > 0)
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    case 7:
      if(!floatDes.isNaN() && !floatSrc.isNaN())
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    default: System.out.println("NONE");
  }
}
