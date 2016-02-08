execute(des, src, count, registers, memory) {
		Calculator calculator = new Calculator(registers, memory);
		EFlags eflags = registers.getEFlags();
		System.out.println("SHRD");

		if ( des.isRegister() && src.isRegister() ) {
		//get size of des, src
			int desSize = registers.getBitSize(des);
			int srcSize = registers.getBitSize(src);
			if( (desSize == 16 && srcSize == 16) && count.getValue().equals("CL") ) {
				System.out.println("SHLD register and CL");
				//get size of des
				String originalDes = calculator.hexToBinaryString(registers.get(des), des);
				String originalSign = originalDes.charAt(0) + "";
				System.out.println(originalDes + " value");
				System.out.println("sign " + originalSign);
				boolean checkSize = false;

				for (int a: registers.getAvailableSizes()) {
					if (a == desSize) {
						checkSize = true;
					}
				}

				int cnt = new BigInteger(registers.get(count), 16).intValue() % 31;

				int limit = cnt;
				System.out.println(limit + " limit");
				if (checkSize && (limit >= 0 && limit <= 32 - 1)) {
					String destination = calculator.hexToBinaryString(registers.get(des), des);
					String source = calculator.hexToBinaryString(registers.get(src), src);
					//
					//
					System.out.println(source + " SOURCE");
					BigInteger biSrc = new BigInteger(source, 2);
					BigInteger biDes = new BigInteger(destination, 2);
					BigInteger biResult = biDes;
					boolean bitSet = false;
					boolean srcBitSet = false;
					//BigInteger biResult = biDes.shiftLeft(count.intValue());
					String carryFlag = eflags.getCarryFlag();

					System.out.println(desSize + "pota");
					for (int x = 0; x < limit; x++) {
						bitSet = biDes.testBit(desSize - 1);
						biResult = biResult.shiftLeft(x + 1);

						if (bitSet) {
							carryFlagValue = "1";
						}
						else {
							carryFlagValue = "0";
						}
						//check sign bit of source then transfer to lsb of destination
					srcBitSet = biSrc.testBit(desSize - 1 - x);
					System.out.println("index ito:" + (desSize - 1 - x) + "boolean: " + srcBitSet);
						switch (srcBitSet) {
							case true:
								biResult = biResult.setBit(0);
								break;
							case false:
								biResult = biResult.clearBit(0);
								break;
						}
					}

					eflags.setCarryFlag(carryFlagValue);
					String result = calculator.binaryToHexString(biResult.toString(2), des);
					System.out.println("result " + result);
					if (result.length() > 8) {
						int cut = result.length() - 8;
						String t = result.substring(cut);
						registers.set(des, t);
					}
					else {
						registers.set(des, result);
					}

					if (limit == 0) {
						//eflags not affected
					}
					else {
						if (biDes.equals(BigInteger.ZERO)) {
							eflags.setZeroFlag("1");
						}
						else {
							eflags.setZeroFlag("0");
						}
						String r = calculator.hexToBinaryString(registers.get(des), des);
						String sign = "" + r.charAt(0);
						eflags.setSignFlag(sign);

						String parity = calculator.checkParity(r);
						eflags.setParityFlag(parity);
						if (limit == 1 && originalSign.equals(sign)) {
							eflags.setOverflowFlag("0");
						}
						else if (limit == 1 && !originalSign.equals(sign)) {
							eflags.setOverflowFlag("1");
						}
						else {
							// eflags.setOverflowFlag(undefined);
						}
						eflags.setCarryFlag(originalDes.charAt(limit - 1).toString());
						//eflags.setAuxiliaryFlag(undefined)
					}
				}
			}
			else if ( (desSize == 32 && srcSize == 32) && count.getValue().length() <= 2 ) {
				System.out.println("SHL register and i8");

				//get size of des
				int desSize = registers.getBitSize(des);
				String originalDes = calculator.hexToBinaryString(registers.get(des), des);
				String originalSign = originalDes.charAt(0) + "";

				boolean checkSize = false;
				for (int a: registers.getAvailableSizes()) {
					if (a == desSize) {
						checkSize = true;
					}
				}

				int cnt = new BigInteger(count.getValue(), 16).intValue() % 32;
				int limit = cnt.intValue();

				if (checkSize && (limit >= 0 && limit <= 32 - 1)) {
					String destination = calculator.hexToBinaryString(registers.get(des), des);
					String source = calculator.hexToBinaryString(registers.get(src), src);

					BigInteger biSrc = new BigInteger(source, 2);
					BigInteger biDes = new BigInteger(destination, 2);
					BigInteger biResult = biDes;

					boolean bitSet = false;
					boolean srcBitSet = false;
					String carryFlag = eflags.getCarryFlag();

					for (int x = 0; x < limit; x++) {
						bitSet = biDes.testBit(desSize - 1);
						biResult = biResult.shiftLeft(x + 1);

						if (bitSet) {
						carryFlagValue = "1";
						} else {
						carryFlagValue = "0";
						}
						//check sign bit of source then transfer to lsb of destination
						srcBitSet = biSrc.testBit(desSize - 1 - x);

						switch (srcBitSet) {
							case true:
								biResult = biResult.setBit(0);
								break;
							case false:
								biResult = biResult.clearBit(0);
							break;
						}
					}
					System.out.println(carryFlagValue + "carry flag shit");
					eflags.setCarryFlag(carryFlagValue);

					String result = calculator.binaryToHexString(biResult.toString(2), des);
					if (result.length() > 8) {
						int cut = result.length() - 8;
						String t = result.substring(cut);
						registers.set(des, t);
					}
					else {
						registers.set(des, result);
					}

					if (limit == 0) {
						//eflags not affected
					}
					else {
						if (biResult.equals(BigInteger.ZERO)) {
							eflags.setZeroFlag("1");
						}
						else {
							eflags.setZeroFlag("0");
						}

						String r = calculator.hexToBinaryString(registers.get(des), des);
						String sign = "" + r.charAt(0);
						eflags.setSignFlag(sign);

						String parity = calculator.checkParity(r);
						eflags.setParityFlag(parity);

						if (limit == 1 && originalSign.equals(sign)) {
							eflags.setOverflowFlag("0");
						}
						else if (limit == 1 && !originalSign.equals(sign)) {
							eflags.setOverflowFlag("1");
						}
						else {
							// eflags.setOverflowFlag(undefined);
						}

						eflags.setCarryFlag(originalDes.charAt(limit - 1).toString());
						//eflags.setAuxiliaryFlag(undefined)
					}
				}
			}
		}
		else if ( des.isMemory()) {
			System.out.println("woy puta");
			//get size of des, src
			int desSize = memory.getBitSize(des);
			int srcSize = registers.getBitSize(src);

			if( (desSize == 16 && srcSize == 16) && count.getValue().equals("CL") ) {
				System.out.println("woy puta");
				//get size of des
				String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
				String originalSign = originalDes.charAt(0) + "";
				System.out.println(originalDes + " value");
				System.out.println("sign " + originalSign);
				boolean checkSize = false;
				for (int a: registers.getAvailableSizes()) {
					if (a == desSize) {
						checkSize = true;
					}
				}

				int cnt = new BigInteger(registers.get(count), 16).intValue() % 32;
				System.out.println(cnt);
				int limit = cnt;
				System.out.println(limit + " limit");
				if (checkSize && (limit >= 0 && limit <= 32 - 1)) {
					System.out.println(memory.read(des, desSize));
					String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
					String source = calculator.hexToBinaryString(registers.get(src), src);

					System.out.println(source + " SOURCE");
					BigInteger biSrc = new BigInteger(source, 2);
					BigInteger biDes = new BigInteger(destination, 2);
					BigInteger biResult = biDes;
					boolean bitSet = false;
					boolean srcBitSet = false;
					String carryFlag = eflags.getCarryFlag();

					System.out.println(desSize + "pota");
					for (int x = 0; x < limit; x++) {
						bitSet = biDes.testBit(desSize - 1);
						biResult = biResult.shiftLeft(x + 1);

						if (bitSet) {
							carryFlagValue = "1";
						}
						else {
							carryFlagValue = "0";
						}
						//check sign bit of source then transfer to lsb of destination
						srcBitSet = biSrc.testBit(desSize - 1 - x);
						switch (srcBitSet) {
							case true:
								biResult = biResult.setBit(0);
								break;
							case false:
								biResult = biResult.clearBit(0);
								break;
						}
					}

					eflags.setCarryFlag(carryFlagValue);
					String result = calculator.binaryToHexString(biResult.toString(2), des);
					System.out.println("result " + result);
					memory.write(des, result, desSize);
					System.out.println("putangina");
					if (limit == 0) {
					//eflags not affected
					}
					else {
						if (biDes.equals(BigInteger.ZERO)) {
							eflags.setZeroFlag("1");
						}
						else {
							eflags.setZeroFlag("0");
						}
						String r = calculator.hexToBinaryString(registers.get(des), des);
						String sign = "" + r.charAt(0);
						eflags.setSignFlag(sign);

						String parity = calculator.checkParity(r);
						eflags.setParityFlag(parity);
						if (limit == 1 && originalSign.equals(sign)) {
							eflags.setOverflowFlag("0");
						}
						else if (limit == 1 && !originalSign.equals(sign)) {
							eflags.setOverflowFlag("1");
						}
						else {
							eflags.setOverflowFlag("0");
						}
						eflags.setCarryFlag(originalDes.charAt(limit - 1).toString());
						eflags.setAuxiliaryFlag("0");
					}
				}
			}
			else if ( (desSize == srcSize == 32) && count.getValue().length() <= 2 ) {

			}
		}
}