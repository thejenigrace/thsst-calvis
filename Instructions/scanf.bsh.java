consoleExecute(registers, memory, console) {
	Platform.runLater(
		new Thread() {
            public void run() {
			        	console.scanf();
            }
        }
    );
}
