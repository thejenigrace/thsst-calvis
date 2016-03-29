execute(des,src,op3,registers,memory) {
  int DWORD = 32;
  int DQWORD = 128;
  String desValue = registers.get(des);
  String desValue0 = desValue.substring(16,32);
  String desValue1 = desValue.substring(0,16);
  String srcValue;
  String srcValue0;
  String srcValue1;
  String srcValue2;
  String srcValue3;
  Calculator calculator = new Calculator(registers,memory);

  if(src.isRegister()) {
    srcValue = registers.get(src);
  } else if(src.isMemory()) {
    srcValue = memory.read(src,DQWORD);
  }

  srcValue0 = srcValue.substring(16,32);
  srcValue1 = srcValue.substring(0,16);

  String result0 = compare(calculator,desValue0,srcValue0,op3);
  String result1 = compare(calculator,desValue1,srcValue1,op3);

  System.out.println("desValue0 = " + desValue0);
  System.out.println("srcValue0 = " + srcValue0);
  System.out.println("result0 = " + result0);

  System.out.println("desValue1 = " + desValue1);
  System.out.println("srcValue1 = " + srcValue1);
  System.out.println("result1 = " + result1);

  registers.set(des,result1.concat(result0));
}

String compare(calculator,desValue,srcValue,op3) {
  System.out.println("desValue = " + desValue);
  System.out.println("srcValue = " + srcValue);

  Double doubleDes = calculator.convertHexToDoublePrecision(desValue);
  Double doubleSrc = calculator.convertHexToDoublePrecision(srcValue);

  String operand = op3.getValue();
  int intOperand = Integer.parseInt(operand);
  System.out.println("intOperand = " + intOperand);

  int retval = Double.compare(doubleDes, doubleSrc);
  switch(intOperand) {
    case 0:
      // des == src
      if(retval == 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 1:
      // des < src
      if(retval < 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 2:
      // des <= src
      if(retval < 0 || retval == 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 3:
      if(doubleDes.isNaN() || doubleSrc.isNaN())
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 4:
      // des != src
      if(retval != 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 5:
      // des >= src
      if(retval > 0 || retval == 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 6:
      // des > src
      if(retval > 0)
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    case 7:
      if(!doubleDes.isNaN() && !doubleSrc.isNaN())
        return "FFFFFFFFFFFFFFFF";
      else
        return "0000000000000000";
      break;
    default: System.out.println("NONE");
  }
}
