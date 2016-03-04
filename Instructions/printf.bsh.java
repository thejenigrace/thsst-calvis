consoleExecute(registers, memory, console) {
	String stackPointer = registers.get("ESP");

	String pointingTo = memory.read(stackPointer, 32);
	BigInteger pointer = new BigInteger(pointingTo, 16);

	System.out.println(memory.pop());

	String printed = "";
	System.out.println("start at" + memory.read(pointer.toString(16), 8));

	while ( !memory.read(pointer.toString(16), 8).equals("00") ) {
	    String first = memory.read(pointer.toString(16), 8); // get one byte
		System.out.println("["+first+"]");
	    pointer = pointer.add(new BigInteger("1"));
	    printed += (char) Integer.parseInt(first, 16);
	}
	
	Platform.runLater(
		new Thread() {
            public void run() {
                console.printf(printed);
            }
        }
    );
    
}
