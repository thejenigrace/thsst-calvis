package configuration.model.engine;

public interface Instruction {

    void execute(RegisterList r, Memory m);

    void execute(Token a, RegisterList r, Memory m);

    void execute(Token a, Token b, RegisterList r, Memory m);

    void execute(Token a, Token b, Token c, RegisterList r, Memory m);

}
