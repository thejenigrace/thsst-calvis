consoleExecute(registers, memory, console) {
	String stackPointer = registers.get("ESP");

    // pointingTo is the first "pop" from stack.
	String pointingTo = memory.read(stackPointer, 32);
	BigInteger pointer = new BigInteger(pointingTo, 16);

	String printed = "";

	while ( !memory.read(pointer.toString(16), 8).equals("00") ) {
		// get one byte
	    String first = memory.read(pointer.toString(16), 8); 
		char ascii = (char) Integer.parseInt(first, 16);
		if ( ascii == '\\' ) {
			// increment one more
			pointer = pointer.add(new BigInteger("1"));
			// read character after escape char
			String second = memory.read(pointer.toString(16), 8);
			char ascii = (char) Integer.parseInt(second, 16);
			switch ( ascii ) {
				case 'n': // fall through
				case 'N':
                    printed += '\n';
                    break;
                case 't':
                case 'T':
                    printed += '\t';
                    break;
			}
			pointer = pointer.add(new BigInteger("1"));
		} else {
	        pointer = pointer.add(new BigInteger("1"));
	        printed += ascii;
	    }
	}

    Pattern pattern = Pattern.compile("(?<FIND>%[0-9]*([.][0-9]*)?(d|ld|u|lu|x|lx|f|lf|c|s))");
    Matcher matcher = pattern.matcher(printed);
    HashMap map = new HashMap();
    // c represents matched
    int c = 0;
    while (matcher.find()) {
        int[] arrRange = new int[2];
        arrRange[0] = matcher.start();
        arrRange[1] = matcher.end();
        map.put(c, arrRange);
        c++;
    }

    Object[] printfArgs = new Object[c];

    // print hashmap of print formatting expressions
    Iterator it = map.entrySet().iterator();
    int initialOffset = 32;
    int iteratorCounter = 0;
    int shift = 0;

    String stackPointer = registers.get("ESP");
    BigInteger stackAddress = new BigInteger(stackPointer, 16);

    while (it.hasNext()) {
        Map.Entry m = (Map.Entry) it.next();
        int key = (int) m.getKey();
        int[] value = (int[]) m.getValue();

        String format = printed.substring(value[0] - shift, value[1] - shift); 

        BigInteger offset = new BigInteger(initialOffset + "");
	    offset = offset.divide(new BigInteger("8"));
	    stackAddress = stackAddress.add(offset);

        if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
			throw new StackPopException(offset.intValue());
		} else {  
	        if ( format.matches("%[0-9]*d") ) {
	            // 16 bit signed int
				String bits  = memory.read(stackAddress.toString(16), 16);
				short s = (short) Integer.parseInt(bits, 16);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = s;
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 16;
			} else if ( format.matches("%[0-9]*ld") ) {
                // 32 bit signed int
                String format1 = format.replace("l", "");
                shift++;
                printed = printed.replaceFirst(format, format1);
				String bits  = memory.read(stackAddress.toString(16), 32);
				BigInteger s = new BigInteger(bits, 16);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = s.intValue();
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 32;

			} else if ( format.matches("%[0-9]*u") ) {
			    // 16 bit unsigned int
			    String format1 = format.replace("u", "d");
                printed = printed.replaceFirst(format, format1);
				String bits  = memory.read(stackAddress.toString(16), 16);
				BigInteger b = new BigInteger(bits, 16);
				String unsigned = getUnsigned(16, b.toString(2));
				
				BigInteger b2 = new BigInteger(unsigned);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = b2.intValueExact();
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 16;

			} else if ( format.matches("%[0-9]*lu") ) {
			    // 32 bit unsigned int
				String format1 = format.replace("lu", "d");
				shift++;
                printed = printed.replaceFirst(format, format1);
				String bits  = memory.read(stackAddress.toString(16), 32);

				BigInteger b = new BigInteger(bits, 16);
				String unsigned = getUnsigned(32, b.toString(2));
				BigInteger b2 = new BigInteger(unsigned);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = b2.longValue();
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 32;

			} else if ( format.matches("%[0-9]*x") ) {
			    // 16 bit hex
			    String bits  = memory.read(stackAddress.toString(16), 16);
				short s = (short) Integer.parseInt(bits, 16);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = s;
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 16;

			} else if ( format.matches("%[0-9]*lx") ) {
			    // 32 bit hex
			    String format1 = format.replace("l", "");
				shift++;
                printed = printed.replaceFirst(format, format1);
			    String bits  = memory.read(stackAddress.toString(16), 32);
				BigInteger s = new BigInteger(bits, 16);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = s.intValue();
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 32;

			} else if ( format.matches("%[0-9]*([.][0-9]*)?f") ) {
			    // 32 bit IEEE Single Precision
			    String bits  = memory.read(stackAddress.toString(16), 32);
				BigInteger s = new BigInteger(bits, 16);
				int hex = s.intValue();
				float f = Float.intBitsToFloat(hex);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = f;
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 32;

			} else if ( format.matches("%[0-9]*([.][0-9]*)?lf") ) {
			    // 64 bit IEEE Double Precision
			    String format1 = format.replace("l", "");
				shift++;
                printed = printed.replaceFirst(format, format1);

			    String bits  = memory.read(stackAddress.toString(16), 64);
				long longHex = parseUnsignedHex(bits);
		        double d = Double.longBitsToDouble(longHex);

				// add object to object array for printf args
				printfArgs[iteratorCounter] = d;
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 64;

			} else if ( format.matches("%[0-9]*c") ) {
			    // 16 bit character
				String bits  = memory.read(stackAddress.toString(16), 16);
				char s = (char) Integer.parseInt(bits, 16);
				// add object to object array for printf args
				printfArgs[iteratorCounter] = s;
				registers.set("ESP", stackAddress.toString(16));
				initialOffset = 16;
			} else if ( format.matches("%[0-9]*s") ) {
			    // String
				String bits  = memory.read(stackAddress.toString(16), 32);
				BigInteger stringPointer = new BigInteger(bits, 16);
				String stringVariable = "";
				while ( !memory.read(stringPointer.toString(16), 8).equals("00") ) {
					// get one byte
				    String first = memory.read(stringPointer.toString(16), 8); 
					char ascii = (char) Integer.parseInt(first, 16);
					if ( ascii == '\\' ) {
						// increment one more because of escape key
						stringPointer = stringPointer.add(new BigInteger("1"));
						// read character after escape char
						String second = memory.read(stringPointer.toString(16), 8);
						char ascii = (char) Integer.parseInt(second, 16);
						switch ( ascii ) {
							case 'n': // fall through
							case 'N':
			                    stringVariable += '\n';
			                    break;
			                case 't':
			                case 'T':
			                    stringVariable += '\t';
			                    break;
						}
						stringPointer = stringPointer.add(new BigInteger("1"));
					} else {
				        stringPointer = stringPointer.add(new BigInteger("1"));
				        stringVariable += ascii;
				    }
				}
				// add object to object array for printf args
				printfArgs[iteratorCounter] = stringVariable;
				initialOffset = 32;
			}

	        iteratorCounter++;
        }
    }

	Platform.runLater(
		new Thread() {
            public void run() {
                console.printf(String.format(printed, printfArgs));
            }
        }
    );
    
}

String getUnsigned(size, stringBits){
	String temp = stringBits;
	// zero extend
	while ( temp.length() < 16 ) {
		temp = "0" + temp;
	}

	StringBuilder tempBit = new StringBuilder(temp);
	String returnable = "";
	
	if (tempBit.charAt(0) == '1') {
		tempBit.setCharAt(0, '0');
		tempBit.insert(1, "1");
		BigInteger bi = new BigInteger(tempBit.toString(), 2);
		returnable = bi.toString(10);
	} else {
		BigInteger bi = new BigInteger(tempBit.toString(), 2);
		returnable = bi.toString(10);
	}
	return returnable;	
}

long parseUnsignedHex(String text) {
	if (text.length() == 16) {
		return (parseUnsignedHex(text.substring(0, 1)) << 60)
			| parseUnsignedHex(text.substring(1));
	}
	return Long.parseLong(text, 16);
}