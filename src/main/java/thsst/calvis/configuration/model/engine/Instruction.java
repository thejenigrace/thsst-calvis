package thsst.calvis.configuration.model.engine;

import thsst.calvis.editor.controller.ConsoleController;

public interface Instruction {

    void execute(RegisterList r, Memory m);

    void execute(Token a, RegisterList r, Memory m);

    void execute(Token a, Token b, RegisterList r, Memory m);

    void execute(Token a, Token b, Token c, RegisterList r, Memory m);

    void consoleExecute(RegisterList r, Memory m, ConsoleController c);

}
